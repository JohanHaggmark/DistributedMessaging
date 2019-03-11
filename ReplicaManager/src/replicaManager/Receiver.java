package replicaManager;
import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;

import se.his.drts.message.AbstractMessageTopClass;

public class Receiver extends ReceiverAdapter {

	JChannel m_channel;
	Messages m_messages;

	public Receiver(JChannel channel, Messages messages) {
		this.m_channel = channel;
		this.m_messages = messages;
	}

	public void start() throws Exception {
		m_channel.setReceiver(this);
		m_channel.connect("ChatCluster");
	}

	public void viewAccepted(View new_view) {
		System.out.println("view " + new_view);
	}

	public void receive(Message msg) {
		AbstractMessageTopClass msgTopClass = (AbstractMessageTopClass) msg.getObject();
			
		//if messages is not acknowledge message
		//add make a new message and add it to senders message queue
		if (msgTopClass.getUUID().equals(UUID.fromString("54f642d7-eaf6-4d62-ad2d-316e4b821c03"))) {
			//update the state and add the message
			//gui.setObjectList((LinkedList<GObject>) msgTopClass.executeInReplicaManager());
			m_messages.addNewMessage(msgTopClass.executeInReplicaManager());
		}
		
		//if message is an acknowledge message
		else if (msgTopClass.getUUID().equals(UUID.fromString("bb5eeb2c-fa66-4e70-891b-382d87b64814"))) {
			m_messages.acknowledgeMessage((Integer) msgTopClass.executeInReplicaManager());
			System.out.println("acknowledged Receiver 42");
		}
	}
}
