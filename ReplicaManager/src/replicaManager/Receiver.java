package replicaManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.util.Util;

import MessageHandling.LocalMessages;
import MessageHandling.Resender;
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
	private Integer id;

	private State state = new State(); // this goes to JGroups state

	private int counter = 0; // only used for logging

	public Receiver(JChannel channel, LocalMessages messages) {
		this.m_channel = channel;
		this.m_messages = messages;

	}

	public void start() throws Exception {
		m_channel.setReceiver(this);
		m_channel.connect("ChatCluster");
		setId();
	}

	private void setId() {
		String[] split = m_channel.getName().split("-");
		this.id = Integer.parseInt(split[split.length - 1]);
	}

	public void viewAccepted(View new_view) {
		JGroups.logger.debugLog("View Changed! with size:" + new_view.size() + " primary is: " + JGroups.primaryRM);
		System.out.println("size of view: " + new_view.size());
		if (!new_view.containsMember(JGroups.frontEnd)) {
			// Exponential backoff tills FrontEnd är uppe igen
			JGroups.frontEnd = null;
		}
		// Election happens when primary left:
		if (JGroups.primaryRM != null && !new_view.containsMember(JGroups.primaryRM) && new_view.size() > 1) {
			JGroups.isCoordinator = true;
			JGroups.primaryRM = null;
			JGroups.logger.debugLog("starting new Election!");
			JGroups.electionQueue.add(new ElectionMessage(this.id));
			// Only the primary sends out to new replica managers and frontEnd about the
			// coordinator
		} else if (m_channel.getAddress().equals(JGroups.primaryRM)) {
			List<Address> new_RM = View.newMembers(m_oldView, new_view);
			if (new_RM.isEmpty()) {
				JGroups.logger.debugLog("Member left");
			} else {
				for (Address newMember : new_RM) {
					try {
						JGroups.logger.debugLog("sending I am the coordinator!");
						m_channel.send(new Message(newMember, new CoordinatorMessage().serialize()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} else if (new_view.size() == 2 && JGroups.primaryRM == null) {
			// sets the first replica manager to the coordinator:
			JGroups.logger.debugLog("starting election!");
			JGroups.isCoordinator = true;
			JGroups.electionQueue.add(new ElectionMessage(this.id));
		} else if(new_view.size() == 1) {
			JGroups.isCoordinator = true;
			JGroups.primaryRM = m_channel.getAddress();
		}
		m_oldView = new_view;
	}

	public void receive(Message msg) {
		if (!msg.getSrc().toString().equals(m_channel.getAddress().toString())) { // dont want to receive its own msg
			JGroups.logger.debugLog(msg.getSrc().toString() + "   != " + m_channel.getAddress().toString());
			counter++;
			JGroups.logger.debugLog(counter + " RECEIVE");
			byte[] bytes = msg.getBuffer();

			// Unpacking msg
			Optional<MessagePayload> mpl = MessagePayload.createMessage(bytes);
			AbstractMessageTopClass msgTopClass = (AbstractMessageTopClass) mpl.get();
			JGroups.logger.debugLog(counter + "UUID: " + msgTopClass.getUUID());

			// AcknowledgeMessage
			if (msgTopClass.getUUID().equals(UUID.fromString("bb5eeb2c-fa66-4e70-891b-382d87b64814"))) {
				m_messages.removeAcknowledgeFromMessage((Integer) msgTopClass.getackID());
			}
			// DrawObjectsMessage
			else if (msgTopClass.getUUID().equals(UUID.fromString("54f642d7-eaf6-4d62-ad2d-316e4b821c03"))) {
				m_messages.addNewMessage(new AcknowledgeMessage(msgTopClass.getackID(), msgTopClass.getName()));
				JGroups.logger.debugLog(counter + "DrawObjectsMessage - Sending ack to " + msgTopClass.getName());
				HashMap<String, String> map = msgTopClass.getObject();
				String key = map.keySet().iterator().next();
				JGroups.logger.debugLog(counter + "key of map: " + key);
				JGroups.logger.debugLog("current state: clients: " + state.getClients().size() + " drawobjects: "
						+ state.getObjectList().size());
				if (map.get(key).equals("add")) { // This is a new drawobject
					if (!state.getObjectList().contains(key)) {
						state.addObject(key);
						JGroups.logger.debugLog(
								counter + "sending the drawobject size of client list: " + state.getClients().size());
						for (String client : state.getClients()) {
							if (!msgTopClass.getName().equals(client)) {
								JGroups.logger.debugLog(counter + "send add draw: " + client);
								m_messages.addNewMessageWithAcknowledge(
										new DrawObjectsMessage(msgTopClass.getObject(), client));
								JGroups.logger.debugLog(counter + "send add drawafter");
							}
						}
					}
				} else { // remove old drawobject
					if (state.removeObject(key)) {
						JGroups.logger.debugLog(counter + "sending the drawobject");
						for (String client : state.getClients()) {
							if (!msgTopClass.getName().equals(client)) {
								m_messages.addNewMessageWithAcknowledge(
										new DrawObjectsMessage(msgTopClass.getObject(), client));
							}
						}
					}
				}
			}
			// PresentationMessage
			else if (msgTopClass.getUUID().equals(UUID.fromString("8e69d7fb-4ca9-46de-b33d-cf1dc72377cd"))) {
				String type = (String) msgTopClass.executeInReplicaManager();
				if (type == null) {
					JGroups.logger.debugLog(counter + "Presentation null");
				} else if (type.equals("FrontEnd")) {
					JGroups.frontEnd = msg.src();
					JGroups.logger.debugLog(counter + "received from FrontEnd");
					startResender();
				} else if (type.equals("Client")) {
					JGroups.logger.debugLog(counter + "Add new client with name " + msgTopClass.getName());
					m_messages.addNewMessage(new AcknowledgeMessage(msgTopClass.getackID(), msgTopClass.getName()));
					if (!state.getClients().contains(msgTopClass.getName())) {
						JGroups.logger.debugLog(counter + "Adds this new client with name " + msgTopClass.getName());
						state.getClients().add(msgTopClass.getName());
					}
					JGroups.logger.debugLog(counter + "Added and send state, name " + msgTopClass.getName());
					if (state.getObjectList().size() > 0) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						m_messages.addNewMessageWithAcknowledge(state.getStateMessage(msgTopClass.getName()));
						JGroups.logger.debugLog(counter + "sent state " + msgTopClass.getName());
					}

				} else {
					JGroups.logger.debugLog(counter + "Presentation - hittar inte rätt typ! :(" + type);
				}
			}
			// ElectionMessage
			else if (msgTopClass.getUUID().equals(UUID.fromString("eceb2eb4-361c-425f-a760-a2cd434bbdff"))) {
				Integer id = (Integer) msgTopClass.getPid();
				JGroups.logger.debugLog(counter + " election. my id: " + this.id + " msg.id: " + id);
				if (!this.id.equals(id)) {
					if (this.id > id) {
						JGroups.isCoordinator = true;
						JGroups.electionQueue.add(new ElectionMessage(this.id));
					} else {
						JGroups.isCoordinator = false;
					}
				}
			}
			// CoordinatorMessage
			else if (msgTopClass.getUUID().equals(UUID.fromString("88486f0c-1a3e-428e-a90c-3ceda5426f27"))) {
				JGroups.primaryRM = msg.getSrc();
				getState();
				JGroups.logger
						.debugLog(counter + " " + JGroups.primaryRM + " is the address of the current coordinator");
			} else {
				JGroups.logger.criticalLog(counter + "UNKNOWN UUID: " + msgTopClass.getUUID());
			}
		}
	}

	private void getState() {
		// Will get the state if there is a primary
		if (JGroups.primaryRM != null && !JGroups.primaryRM.equals(m_channel.getAddress())) {
			try {
				JGroups.logger.debugLog(counter + " Trying to get state");
				m_channel.getState(JGroups.primaryRM, 10000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void getState(OutputStream output) throws Exception {
		JGroups.logger.debugLog(counter + "JGroups getState(OutputStream output)");
		synchronized (state) {
			Util.objectToStream(state, new DataOutputStream(output));
		}
	}

	public void setState(InputStream input) throws Exception {
		State input_state;
		JGroups.logger.debugLog(counter + " set the state");
		input_state = (State) Util.objectFromStream(new DataInputStream(input));
		synchronized (state) {
			state = input_state;
		}
	}

	private void startResender() {
		new Thread(new Resender(m_messages.getMessagesToResender(), m_messages.getMessageQueue(), m_messages)).start();
		JGroups.logger.debugLog("Started Resender successfully ");
	}
}