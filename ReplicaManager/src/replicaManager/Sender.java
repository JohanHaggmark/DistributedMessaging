package replicaManager;

import java.util.concurrent.LinkedBlockingQueue;

import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;

import DCAD.Cad;
import MessageHandling.LocalMessage;
import MessageHandling.LocalMessages;
import se.his.drts.message.AbstractMessageTopClass;

public class Sender implements Runnable {

	// Class should only send to FrontEnd. All communication between replica
	// managers does not happen here

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
				if (JGroups.frontEnd != null) { // Check if frontEnd exsists
					JGroups.logger.debugLog("Sender trying to send to:  " + msg.getMsgTopClass().getName());
					JGroups.logger.debugLog("message:  " + msg.getMsgTopClass().getUUID());
					m_channel.send(new Message(JGroups.frontEnd, msg.getMsgTopClass().serialize()));
					tryAddToRTT(msg); // Check if message should be acknowledged
				} else {
					m_messages.addToMessagesToResender(msg); // All messages gets stored when fronend is down
				}
			} catch (Exception e) {
				JGroups.logger.debugLog("exception e");
				e.printStackTrace();
			}

		}
	}

	private void tryAddToRTT(LocalMessage msg) {
		if (msg.isAcknowledgeMessage()) {
			if ((msg.isAcknowledged() == false) && msg.getAttempt() < ATTEMPTS) {
				JGroups.logger.debugLog("SENDER - sending message: " + msg.getMsgTopClass());
				msg.incrementAttempt();
				m_messages.addToRTTMessageQueue(msg);
			} else if (ATTEMPTS <= msg.getAttempt()) {
				// the message should be removed from the map
				m_messages.removeAcknowledgeFromMessage(msg.getId());
			}
		}
	}
}