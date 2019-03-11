package replicaManager;
import java.util.concurrent.LinkedBlockingQueue;

import org.jgroups.JChannel;
import org.jgroups.Message;

import se.his.drts.message.AbstractMessageTopClass;

public class Sender implements Runnable{
	
	JChannel m_channel;
	LinkedBlockingQueue<AbstractMessageTopClass> messagesToSender;
	
	public Sender(JChannel channel, LinkedBlockingQueue messagesToSender) {
		this.m_channel = channel;
		this.messagesToSender = messagesToSender;

	}
	
	public void receiveFromJGroupsStub(String message) {
		// LÄGG TILL NÅGON FORM AV LBQ HÄR ISTÄLLET FÖR EN STUB EFTERSOM DET BLIR CP ATT RECEIVER SKA HA KOLL PÅ DETTA OBJEKTET
		this.send(message);
	}


	private void send(String message) {
		Message msg = new Message(null, message);
		try {
			m_channel.send(msg);
			System.out.println("sending");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendName() {
		send("hej");
	}
	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(1000);
				sendName();
				//AbstractMessageTopClass msg = messagesToSender.take();
				//msg.executeInReplicaManager();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}	
	}
}
