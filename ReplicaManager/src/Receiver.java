import java.util.concurrent.LinkedBlockingQueue;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;

public class Receiver extends ReceiverAdapter {

	JChannel m_channel;
	LinkedBlockingQueue messagesToSender;
	
	
	public Receiver(JChannel channel, LinkedBlockingQueue messagesToSender) {
		this.m_channel = channel;
		this.messagesToSender = messagesToSender;
	}
	
	public void start() throws Exception {
		m_channel.setReceiver(this);
	}
	
	public void receive(Message msg) {
		// receive message from frontend and send response
	    System.out.println(msg.getSrc() + ": " + msg.getObject());
	    this.send(msg.getObject());
	}
	
	private void send(String message) {
		// LÄGG TILL NÅGON FORM AV LBQ HÄR ISTÄLLET FÖR EN STUB EFTERSOM DET BLIR CP ATT RECEIVER SKA HA KOLL PÅ SENDER

	//	messagesToSender.add(e)
	}
}
