package replicaManager;
import java.util.concurrent.LinkedBlockingQueue;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;

public class Receiver extends ReceiverAdapter {

	JChannel m_channel;
	LinkedBlockingQueue m_messageToSender;

	public Receiver(JChannel channel, LinkedBlockingQueue messageToSender) {
		this.m_channel = channel;
		this.m_messageToSender = messageToSender;
	}

	public void start() throws Exception {
		m_channel.setReceiver(this);
		m_channel.connect("ChatCluster");
	}

	public void viewAccepted(View new_view) {
		System.out.println("view " + new_view);
	}

	public void receive(Message msg) {
		System.out.println("receive viewclass");
		// receive message from frontend and send response
		System.out.println(msg.getObject().toString() + " i receiver");
		//System.out.println(msg.getBuffer().toString());
		System.out.println("Dra räva i grus");
	}
}
