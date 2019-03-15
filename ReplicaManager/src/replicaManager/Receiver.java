package replicaManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;

import se.his.drts.message.AbstractMessageTopClass;
import se.his.drts.message.AcknowledgeMessage;
import se.his.drts.message.CoordinatorMessage;
import se.his.drts.message.ElectionMessage;
import se.his.drts.message.LocalMessages;
import se.his.drts.message.MessagePayload;

public class Receiver extends ReceiverAdapter {

	private JChannel m_channel;
	private LocalMessages m_messages;
	private View m_oldView;

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
		JGroups.id = Integer.parseInt(split[split.length - 1]);
	}

	public void viewAccepted(View new_view) {
		JGroups.logger.debugLog("Receiver 42 " + m_channel.getAddress() + " ");
		if (!new_view.containsMember(JGroups.frontEnd)) {
			// Exponential backoff tills FrontEnd ‰r uppe igen
			JGroups.frontEnd = null;
		}
		// Election happens when primary left:
		if (JGroups.primaryRM != null && !new_view.containsMember(JGroups.primaryRM) && new_view.size() > 1) {
			JGroups.logger.debugLog("sending election");
			JGroups.isCoordinator = true;
			JGroups.electionQueue.add(new ElectionMessage(JGroups.id));
			// Only the primary sends out to new replica managers about the coordinator
		} 
		else if (m_channel.getAddress().equals(JGroups.primaryRM)) {
			List<Address> new_RM = View.newMembers(m_oldView, new_view);
			if (new_RM.isEmpty()) {
				JGroups.logger.debugLog("Member left");
			} else {
				for (Address newMember : new_RM) {
					try {
						JGroups.logger.debugLog("sending I am coordinator");
						byte[] bytes = new CoordinatorMessage().serialize();
						m_channel.send(new Message(newMember, new CoordinatorMessage()));
						JGroups.logger.debugLog("123sending bytes as coordinator");						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} 
		else if (new_view.size() == 1) {
			// sets the first replica manager to the coordinator:
			JGroups.logger.debugLog("set myself to coordinator");
			JGroups.isCoordinator = true;
			JGroups.primaryRM = m_channel.getAddress();
		}
		m_oldView = new_view;
	}

//	private void sendAcknowledge(Integer id, String address) {
//		JGroups.logger.debugLog("HALL≈≈??");
//		JGroups.logger.debugLog("To String address front end " + JGroups.frontEnd);
//		JGroups.logger.debugLog("To String address front end " + JGroups.frontEnd.toString());
//		m_messages.addToMessageQueue(new LocalMessage(new AcknowledgeMessage(id, address, JGroups.frontEnd.toString())));
//		try {
//			m_channel.send(new Message(JGroups.frontEnd, new AcknowledgeMessage(id, address, JGroups.frontEnd.toString())));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public void receive(Message msg) {
		byte[] bytes = msg.getBuffer();
		JGroups.logger.debugLog("Receiver() - Received a message: " + bytes.toString());
		
		Optional<MessagePayload> mpl = MessagePayload.createMessage(bytes);
		JGroups.logger.debugLog("Receiver() - Trying to unpack");
		AbstractMessageTopClass msgTopClass = (AbstractMessageTopClass) mpl.get();
		JGroups.logger.debugLog("Receiver() - Successfully unpacked!!");
		
		// AcknowledgeMessage
		if (msgTopClass.getUUID().equals(UUID.fromString("bb5eeb2c-fa66-4e70-891b-382d87b64814"))) {
			JGroups.logger.debugLog("AcknowledgeMessage - Receiver 84");
			m_messages.removeAcknowledgeFromMessage(msgTopClass.getMessageNumber());
		}
		// DrawObjectsMessage
		else if (msgTopClass.getUUID().equals(UUID.fromString("54f642d7-eaf6-4d62-ad2d-316e4b821c03"))) {
			JGroups.logger.debugLog("DrawObjectsMessage - Receiver 88");
			Object GObjectList = msgTopClass.executeInReplicaManager();
			String destination = msgTopClass.getDestination();			
			
			JGroups.logger.debugLog("DrawObjectsMessage - Sending ack");
			JGroups.logger.debugLog("DrawObjectsMessage - destination: " + destination);
			m_messages.addNewMessage(new AcknowledgeMessage(msgTopClass.getMessageNumber(), msgTopClass.getName()));
		}
		// PresentationMessage
		else if (msgTopClass.getUUID().equals(UUID.fromString("8e69d7fb-4ca9-46de-b33d-cf1dc72377cd"))) {
			JGroups.logger.debugLog("Presentation - Receiver");
			String type = msgTopClass.getType();
			if(type == null) {
				JGroups.logger.debugLog("Presentation null");
			}
			if (type.equals("FrontEnd")) {
				JGroups.logger.debugLog("Presentation --> Receiver --> found the front end");
				JGroups.frontEnd = msg.src();
			}
			else if (type.equals("Client")) {
				//send an acknowledge message for this message
				m_messages.addNewMessage(new AcknowledgeMessage(msgTopClass.getMessageNumber(), msgTopClass.getName()));
				String name = msgTopClass.getName();
				JGroups.logger.debugLog("Added new client with name " + name);
				JGroups.clients.add(name);
			}
			else {
				JGroups.logger.debugLog("Presentation - hittar fan ingen typ! :(");
			}
		}
		// ElectionMessage
		else if (msgTopClass.getUUID().equals(UUID.fromString("eceb2eb4-361c-425f-a760-a2cd434bbdff"))) {
			JGroups.logger.debugLog("ElectionMessage - Receiver 108");
			Integer id = (Integer) msgTopClass.executeInReplicaManager();
			if (!JGroups.id.equals(id)) {
				if (JGroups.id > id) {
					JGroups.logger.debugLog("My id: " + JGroups.id + " other id: " + id);
					JGroups.isCoordinator = true;
					JGroups.electionQueue.add(new ElectionMessage(JGroups.id));
				} else {
					JGroups.logger.debugLog("Receiver 115, Im not the coordinator");
					JGroups.isCoordinator = false;
				}
			}
		}
		// CoordinatorMessage
		else if (msgTopClass.getUUID().equals(UUID.fromString("88486f0c-1a3e-428e-a90c-3ceda5426f27"))) {
			JGroups.logger.debugLog("Coordinator - Receiver 109");
			JGroups.primaryRM = msg.getSrc();
			JGroups.logger.debugLog(JGroups.primaryRM + " is the address of the current coordinator");
		} else {
			JGroups.logger.criticalLog("Else - Receiver 118:  " + msgTopClass.getUUID());
			JGroups.logger.criticalLog("Could not find the correct type");
		}
	}
}