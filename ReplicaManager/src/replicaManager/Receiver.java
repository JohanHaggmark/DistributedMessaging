package replicaManager;

import java.util.UUID;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;

import se.his.drts.message.AbstractMessageTopClass;
import se.his.drts.message.Acknowledge;
import se.his.drts.message.ElectionMessage;
import se.his.drts.message.LocalMessage;
import se.his.drts.message.LocalMessages;
import se.his.drts.message.Presentation;

public class Receiver extends ReceiverAdapter {
	private Integer m_id;
	private JChannel m_channel;
	private LocalMessages m_messages;
	private Thread m_TemporaryAsFkThread;

	public Receiver(JChannel channel, LocalMessages messages) {
		this.m_channel = channel;
		this.m_messages = messages;
		this.startSendTestThread();
		m_TemporaryAsFkThread.start();
	}

	public void start() throws Exception {
		m_channel.setReceiver(this);
		m_channel.connect("ChatCluster");
		String[] split = m_channel.getName().split("-");
		String id = split[split.length-1];
		this.m_id = Integer.parseInt(id);
	}

	public void viewAccepted(View new_view) {
		System.out.println("view " + new_view);
		if (!new_view.containsMember(JGroups.frontEnd)) {
			// Exponential backoff tills FrontEnd är uppe igen
		}
		if (!new_view.containsMember(JGroups.primaryRM)) {
			System.out.println(":O");
			if(JGroups.newestElection == null) {
				new Thread(new Election(this.m_id, this.m_messages)).start();
			}
		}
	}
	
	public void startSendTestThread() {
		m_TemporaryAsFkThread = new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						sleep(5000);
						System.out.println("TestMessage");
						m_messages.addNewMessage(Presentation.createReplicaManagerPresentation(m_channel.getName()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
	}

	private void sendAcknowledge(Integer id) {
		m_messages.addToMessageQueue(new LocalMessage(new Acknowledge(id)));
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
		// if message is an acknowledge message
		if (msgTopClass.getUUID().equals(UUID.fromString("bb5eeb2c-fa66-4e70-891b-382d87b64814"))) {
			m_messages.removeAcknowledgeFromMessage(msgTopClass.getId());
		}
		// draw
		else if (msgTopClass.getUUID().equals(UUID.fromString("54f642d7-eaf6-4d62-ad2d-316e4b821c03"))) {
			sendAcknowledge(msgTopClass.getId());
		}
		// presentation
		else if (msgTopClass.getUUID().equals(UUID.fromString("8e69d7fb-4ca9-46de-b33d-cf1dc72377cd"))) {
			String[] names = ((String) msgTopClass.executeInReplicaManager()).split("-");
			if (names[names.length - 1].equals("FrontEnd")) {
				JGroups.frontEnd = msg.src();
			}
			sendAcknowledge(msgTopClass.getId());
		}
		// election
		else if (msgTopClass.getUUID().equals(UUID.fromString("eceb2eb4-361c-425f-a760-a2cd434bbdff"))) {

		} else {
			System.out.println("Could not find the correct type");
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
			Integer id = (Integer) msgTopClass.executeInReplicaManager();
			if (this.m_id > id) {
				new Thread(new Election(this.m_id, this.m_messages)).start();
			} else {
				JGroups.isCoordinator = false;
			}
		} else {
			System.out.println("Could not find the correct type");
		}
	}
}
