package replicaManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.util.Util;

import Communication.RMConnection;
import DCAD.Cad;
import DCAD.State;
import MessageHandling.GObject;
import MessageHandling.LocalMessages;
import MessageHandling.Resender;
import MessageHandling.SerializeObject;
import se.his.drts.message.AbstractMessageTopClass;
import se.his.drts.message.AcknowledgeMessage;
import se.his.drts.message.CoordinatorMessage;
import se.his.drts.message.DrawObjectsMessage;
import se.his.drts.message.ElectionMessage;
import se.his.drts.message.MessagePayload;

public class Receiver extends ReceiverAdapter {

	private JChannel m_channel;
	private LocalMessages m_messages;
	private View m_oldView;
	private replicaManager.State state;

	public Receiver(JChannel channel, LocalMessages messages) {
		this.m_channel = channel;
		this.m_messages = messages;
		state = new replicaManager.State();
	}

	public void start() throws Exception {
		m_channel.setReceiver(this);
		m_channel.connect("ChatCluster");
		setId();
	}

	private void setId() {
		String[] split = m_channel.getName().split("-");
		JGroups.id = Integer.parseInt(split[split.length - 1]);
	}

	public void viewAccepted(View new_view) {
		if (!new_view.containsMember(JGroups.frontEnd)) {
			// Exponential backoff tills FrontEnd är uppe igen
			JGroups.frontEnd = null;
		}
		// Election happens when primary left:
		if (JGroups.primaryRM != null && !new_view.containsMember(JGroups.primaryRM) && new_view.size() > 1) {
			JGroups.isCoordinator = true;
			JGroups.electionQueue.add(new ElectionMessage(JGroups.id));
			// Only the primary sends out to new replica managers about the coordinator
		} else if (m_channel.getAddress().equals(JGroups.primaryRM)) {
			List<Address> new_RM = View.newMembers(m_oldView, new_view);
			if (new_RM.isEmpty()) {
				JGroups.logger.debugLog("Member left");
			} else {
				for (Address newMember : new_RM) {
					try {
						JGroups.logger.debugLog("sending I am the coordinator!");
						m_channel.send(new Message(newMember, new CoordinatorMessage()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} else if (new_view.size() == 1) {
			// sets the first replica manager to the coordinator:
			JGroups.logger.debugLog("I am the coordinator!");
			JGroups.isCoordinator = true;
			JGroups.primaryRM = m_channel.getAddress();
		}
		m_oldView = new_view;
	}

	public void receive(Message msg) {
		JGroups.logger.debugLog("RECEIVE");
		byte[] bytes = msg.getBuffer();

		//Unpacking msg
		Optional<MessagePayload> mpl = MessagePayload.createMessage(bytes);
		AbstractMessageTopClass msgTopClass = (AbstractMessageTopClass) mpl.get();

		JGroups.logger.debugLog("UUID: " + msgTopClass.getUUID());

		// AcknowledgeMessage
		if (msgTopClass.getUUID().equals(UUID.fromString("bb5eeb2c-fa66-4e70-891b-382d87b64814"))) {
			m_messages.removeAcknowledgeFromMessage(msgTopClass.getMessageNumber());
		}
		// DrawObjectsMessage
		else if (msgTopClass.getUUID().equals(UUID.fromString("54f642d7-eaf6-4d62-ad2d-316e4b821c03"))) {
			m_messages.addNewMessage(new AcknowledgeMessage(msgTopClass.getackID(),msgTopClass.getName()));
			JGroups.logger.debugLog("DrawObjectsMessage - Sending ack to " + msgTopClass.getName());
			DrawObjectsMessage drawMsg = (DrawObjectsMessage)msgTopClass;
			//state.updateState((LinkedList<GObject>)msgTopClass.executeInReplicaManager(), msgTopClass.getName());
			JGroups.logger.debugLog("DrawObjectsMessage - Updated states ");
			//State is updated. Now send the new state to clients:		
			//m_messages.addNewMessageWithAcknowledge(new DrawObjectsMessage(state.getList(), msgTopClass.getName()));
			DCAD.State state = (DCAD.State) drawMsg.getM_state();
			System.out.println("state list size:");
			JGroups.logger.debugLog("was able to cast to State");
			m_messages.addNewMessageWithAcknowledge(new DrawObjectsMessage((String) drawMsg.executeInReplicaManager(),  drawMsg.getName()));
		}
		// PresentationMessage
		else if (msgTopClass.getUUID().equals(UUID.fromString("8e69d7fb-4ca9-46de-b33d-cf1dc72377cd"))) {
			String type = (String)msgTopClass.executeInReplicaManager();
			if (type == null) {
				JGroups.logger.debugLog("Presentation null");
			}
			if (type.equals("FrontEnd")) {
				JGroups.frontEnd = msg.src();
				JGroups.logger.debugLog("received from FrontEnd");
				startResender();
			} else if (type.equals("Client")) {
				JGroups.logger.debugLog("Added new client with name " + msgTopClass.getName());
				JGroups.clients.add(msgTopClass.getName());
			} else {
				JGroups.logger.debugLog("Presentation - hittar fan ingen typ! :(");
			}
		}
		// ElectionMessage
		else if (msgTopClass.getUUID().equals(UUID.fromString("eceb2eb4-361c-425f-a760-a2cd434bbdff"))) {
			Integer id = (Integer) msgTopClass.executeInReplicaManager();
			if (!JGroups.id.equals(id)) {
				if (JGroups.id > id) {
					JGroups.isCoordinator = true;
					JGroups.electionQueue.add(new ElectionMessage(JGroups.id));
				} else {
					JGroups.isCoordinator = false;
				}
			}
		}
		// CoordinatorMessage
		else if (msgTopClass.getUUID().equals(UUID.fromString("88486f0c-1a3e-428e-a90c-3ceda5426f27"))) {
			JGroups.primaryRM = msg.getSrc();
			getState();
			JGroups.logger.debugLog(JGroups.primaryRM + " is the address of the current coordinator");
		} else {
			JGroups.logger.criticalLog("UNKNOWN UUID: " + msgTopClass.getUUID());
		}
	}

	private void getState() {
		//Will get the state if there is a primary
		if (JGroups.primaryRM != null && JGroups.isCoordinator == false) {
			try {			
				m_channel.getState(JGroups.primaryRM, 1000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void getState(OutputStream output) throws Exception {
		JGroups.logger.debugLog("JGroups getState(OutputStream output)");
		synchronized (state.getList()) {
			Util.objectToStream(state.getList(), new DataOutputStream(output));
		}
	}

	public void setState(InputStream input) throws Exception {
		LinkedList<GObject> list;
		list = (LinkedList<GObject>) Util.objectFromStream(new DataInputStream(input));
		synchronized (state) {
			state.setState(list);
		}
	}
	
	private void startResender() {
		new Thread(new Resender(
				m_messages.getMessagesToResender(),
				m_messages.getMessageQueue(),
				RMConnection.hasFrontEnd)).start();
		JGroups.logger.debugLog("Started Resender successfully ");
	}
}