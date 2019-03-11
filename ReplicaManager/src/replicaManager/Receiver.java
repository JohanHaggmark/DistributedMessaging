package replicaManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;

import se.his.drts.message.AbstractMessageTopClass;
import se.his.drts.message.Acknowledge;
import se.his.drts.message.LocalMessage;
import se.his.drts.message.LocalMessages;
import se.his.drts.message.Presentation;

public class Receiver extends ReceiverAdapter {
	JChannel m_channel;
	LocalMessages m_messages;
	Thread thread;
	LocalMessage localMessage;
	Map<String, String> listOfNames = new HashMap();

	public Receiver(JChannel channel, LocalMessages messages) {
		this.m_channel = channel;
		this.m_messages = messages;
		this.startThread();
		thread.start();
	}

	public void start() throws Exception {
		m_channel.setReceiver(this);
		m_channel.connect("ChatCluster");
	}

	public void viewAccepted(View new_view) {
		System.out.println("view " + new_view);
	}

	public void startThread() {
		thread = new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						sleep(5000);
						m_messages.addNewMessage(new Presentation(m_channel.getName()));
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
		AbstractMessageTopClass msgTopClass = (AbstractMessageTopClass) msg.getObject();
		// if messages is not acknowledge message
		// add make a new message and add it to senders message queue
		if (msgTopClass.getUUID().equals(UUID.fromString("54f642d7-eaf6-4d62-ad2d-316e4b821c03"))) {
			// update the state and add the message
			// gui.setObjectList((LinkedList<GObject>)
			// msgTopClass.executeInReplicaManager());
			sendAcknowledge(msgTopClass.getId());
		}

		// if message is an acknowledge message
		else if (msgTopClass.getUUID().equals(UUID.fromString("bb5eeb2c-fa66-4e70-891b-382d87b64814"))) {
			m_messages.removeAcknowledgeFromMessage(msgTopClass.getId());
			System.out.println("acknowledged Receiver 42");
		}
		// presentation
		else if (msgTopClass.getUUID().equals(UUID.fromString("8e69d7fb-4ca9-46de-b33d-cf1dc72377cd"))) {
			String name = (String) msgTopClass.executeInReplicaManager();
			System.out.println("pressss - received Name from RM: " + name);
			listOfNames.put(msgTopClass.getUUID().toString(), name.split("-")[1]);
			sendAcknowledge(msgTopClass.getId());
		} else {
			System.out.println(msgTopClass.getUUID().toString());
			msgTopClass.executeInReplicaManager();
//			Presentation amsg = (Presentation) msgTopClass;
			System.out.println("else");
//			System.out.println("received from RM: " + amsg.getName());
			sendAcknowledge(msgTopClass.getId());
		}
	}
}
