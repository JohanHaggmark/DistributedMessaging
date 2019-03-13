package replicaManager;

import java.util.concurrent.LinkedBlockingQueue;

import org.jgroups.JChannel;
import org.jgroups.Message;

import se.his.drts.message.AbstractMessageTopClass;
import se.his.drts.message.LocalMessage;
import se.his.drts.message.LocalMessages;

public class Sender implements Runnable {

	JChannel m_channel;
	LinkedBlockingQueue<AbstractMessageTopClass> messagesToSender;
	LocalMessages m_messages;
	private static int ATTEMPTS = 10;

	public Sender(JChannel channel, LocalMessages messages) {
		this.m_channel = channel;
		this.m_messages = messages;

	}	

	@Override
	public void run() {
		while (true) {
			try {
				LocalMessage msg = (LocalMessage) m_messages.getMessageQueue().take();
				if (!msg.isAcknowledge && msg.getAttempt() < ATTEMPTS) {
					m_channel.send(new Message(null, msg.getMsgTopClass()));
					if (msg.getMsgTopClass().getUUID().toString().equals("54f642d7-eaf6-4d62-ad2d-316e4b821c03")) {
						msg.incrementAttempt();
						m_messages.addToRTTMessageQueue(msg);
					}
				}
			} catch (InterruptedException e1) {
				System.out.println("IE e1");
				e1.printStackTrace();
			} catch (Exception e) {
				System.out.println("exception e");
				e.printStackTrace();
			}
		}
	}
}