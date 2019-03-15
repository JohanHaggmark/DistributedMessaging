package replicaManager;

import java.util.concurrent.LinkedBlockingQueue;

import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;

import MessageHandling.LocalMessage;
import MessageHandling.LocalMessages;
import se.his.drts.message.AbstractMessageTopClass;

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
				JGroups.logger.debugLog("Sender trying to send to: " + msg.getMsgTopClass().getName());
				JGroups.logger.debugLog("Sender found a message in m_messages");
				if (!msg.isAcknowledged() && msg.getAttempt() < ATTEMPTS) {
					JGroups.logger.debugLog("Sending - Sender 32");
					//JGroups.logger.debugLog("message address: " + ((Address)msg.getMsgTopClass().executeInReplicaManager()).toString());
					m_channel.send(new Message(null, msg.getMsgTopClass()));
					if (msg.getMsgTopClass().getUUID().toString().equals("54f642d7-eaf6-4d62-ad2d-316e4b821c03")) {
						msg.incrementAttempt();
						m_messages.addToRTTMessageQueue(msg);
					}
				}
			} catch (Exception e) {
				JGroups.logger.debugLog("exception e");
				e.printStackTrace();
			}
		}
	}
}