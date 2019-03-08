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
		// L�GG TILL N�GON FORM AV LBQ H�R IST�LLET F�R EN STUB EFTERSOM DET BLIR CP ATT RECEIVER SKA HA KOLL P� DETTA OBJEKTET
		this.send(message);
	}

	private void send(String message) {
		Message msg = new Message(null, message.getBytes());
		try {
			m_channel.send(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while(true) {
			try {
				AbstractMessageTopClass msg = messagesToSender.take();
				msg.executeInReplicaManager();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}	
	}
}
