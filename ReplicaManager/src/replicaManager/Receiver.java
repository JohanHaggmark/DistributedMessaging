package replicaManager;

import java.util.List;
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
import se.his.drts.message.LocalMessage;
import se.his.drts.message.LocalMessages;

public class Receiver extends ReceiverAdapter {
	private Integer m_id;
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
		this.m_id = Integer.parseInt(split[split.length - 1]);
	}

	public void viewAccepted(View new_view) {
		JGroups.logger.debugLog("Receiver 42 " + m_channel.getAddress() + " ");
		if (!new_view.containsMember(JGroups.frontEnd)) {
			// Exponential backoff tills FrontEnd är uppe igen
		}
		//Election happens when primary left:
		if (JGroups.primaryRM != null && !new_view.containsMember(JGroups.primaryRM) && new_view.size() > 1) {
			JGroups.logger.debugLog("sending election");
			JGroups.electionQueue.add(new ElectionMessage(m_id));
		//Only the primary sends out to new replica managers about the coordinator
		} else if (m_channel.getAddress().equals(JGroups.primaryRM)) {
			List<Address> new_RM = View.newMembers(m_oldView, new_view);
			if (new_RM.isEmpty()) {
				JGroups.logger.debugLog("Member left");
			} else {
				for (Address newMember : new_RM) {
					try {
						m_channel.send(new Message(newMember, new CoordinatorMessage(this.m_id)));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} else if (new_view.size() == 1) {
			//sets the first replica manager to the coordinator:
			JGroups.isCoordinator = true;
			JGroups.primaryRM = m_channel.getAddress();
		}
		m_oldView = new_view;
	}

	private void sendAcknowledge(Integer id) {
		m_messages.addToMessageQueue(new LocalMessage(new AcknowledgeMessage(id)));

	}

	public void receive(Message msg) {
		if (JGroups.isCoordinator) {
			this.receiveAsCoordinator(msg);
		} else {
			this.receiveAsBackup(msg);
		}
	}

	private void receiveAsCoordinator(Message msg) {
		AbstractMessageTopClass msgTopClass = (AbstractMessageTopClass) msg.getObject();
		// AcknowledgeMessage
		if (msgTopClass.getUUID().equals(UUID.fromString("bb5eeb2c-fa66-4e70-891b-382d87b64814"))) {
			JGroups.logger.debugLog("AcknowledgeMessage - Receiver 84");
			m_messages.removeAcknowledgeFromMessage(msgTopClass.getId());
		}
		// draw
		else if (msgTopClass.getUUID().equals(UUID.fromString("54f642d7-eaf6-4d62-ad2d-316e4b821c03"))) {
			JGroups.logger.debugLog("DrawObjectsMessage - Receiver 88");
			sendAcknowledge(msgTopClass.getId());
		}
		// presentation
		else if (msgTopClass.getUUID().equals(UUID.fromString("8e69d7fb-4ca9-46de-b33d-cf1dc72377cd"))) {
			JGroups.logger.debugLog("Presentation - Receiver 92");
			String[] names = ((String) msgTopClass.executeInReplicaManager()).split("-");
			if (names[names.length - 1].equals("FrontEnd")) {
				JGroups.frontEnd = msg.src();
			}
			sendAcknowledge(msgTopClass.getId());
		}
		// ElectionMessage
		else if (msgTopClass.getUUID().equals(UUID.fromString("eceb2eb4-361c-425f-a760-a2cd434bbdff"))) {
			JGroups.logger.debugLog("ElectionMessage - Receiver 108");
			Integer id = (Integer) msgTopClass.executeInReplicaManager();
			if (!this.m_id.equals(id)) {
				if (this.m_id > id) {
					JGroups.logger.debugLog("My id: " + m_id + " other id: " + id);
					JGroups.isCoordinator = true;
					JGroups.electionQueue.add(new ElectionMessage(m_id));
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
			if (!m_id.equals((Integer) msgTopClass.executeInReplicaManager())) {
				JGroups.logger
						.criticalLog("My id: " + m_id + " other id: " + (Integer) msgTopClass.executeInReplicaManager()
								+ "\n" + "Received coordinator message from other dude while being coordinator myself");
			}
		} else {
			JGroups.logger.debugLog("Else - Receiver 118:  " + msgTopClass.getUUID());
			JGroups.logger.debugLog("Could not find the correct type");
		}
	}

	private void receiveAsBackup(Message msg) {
		AbstractMessageTopClass msgTopClass = (AbstractMessageTopClass) msg.getObject();
		// DrawObjects
		if (msgTopClass.getUUID().equals(UUID.fromString("54f642d7-eaf6-4d62-ad2d-316e4b821c03"))) {
			sendAcknowledge(msgTopClass.getId());
		}
		// ElectionMessage
		else if (msgTopClass.getUUID().equals(UUID.fromString("eceb2eb4-361c-425f-a760-a2cd434bbdff"))) {
			JGroups.logger.debugLog("ElectionMessage - Receiver 137");
			Integer id = (Integer) msgTopClass.executeInReplicaManager();
			if (!this.m_id.equals(id)) {
				if (this.m_id > id) {
					JGroups.logger.debugLog("My id: " + m_id + " other id: " + id);
					JGroups.electionQueue.add(new ElectionMessage(m_id));
				} else {
					JGroups.logger.debugLog("Receiver 143, I am not the coordinator");
					JGroups.isCoordinator = false;
				}
			}
		}
		// CoordinatorMessage
		else if (msgTopClass.getUUID().equals(UUID.fromString("88486f0c-1a3e-428e-a90c-3ceda5426f27"))) {
			JGroups.logger.debugLog("Coordinator - Receiver 147");
			JGroups.primaryRM = msg.getSrc();
			JGroups.logger.debugLog(JGroups.primaryRM + " is the address of the current coordinator");
		} else {
			JGroups.logger.debugLog("Could not find the correct type - Receiver 140  " + msgTopClass.getUUID());
		}
	}
}