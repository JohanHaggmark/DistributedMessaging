package Communication;

import DCAD.Cad;
import MessageHandling.LocalMessage;
import MessageHandling.LocalMessages;

public class Sender implements Runnable {

	LocalMessages m_messages;
	RMConnection m_RMConnection;
	private final int ATTEMPTS = 10;

	public Sender(RMConnection rmConnection, LocalMessages messages) {
		this.m_RMConnection = rmConnection;
		this.m_messages = messages;

	}

	@Override
	public void run() {
		while (true) {
			try {
				LocalMessage msg = (LocalMessage) m_messages.getMessageQueue().take();
				if (msg.isAcknowledgeMessage()) {
					if ((msg.isAcknowledged() == false) && msg.getAttempt() < ATTEMPTS) {
						Cad.logger.debugLog("SENDER - sending message: " + msg.getMsgTopClass());
						m_RMConnection.sendMessage(msg.getMsgTopClass());
						msg.incrementAttempt();
						m_messages.addToRTTMessageQueue(msg);
					}
				} 
				else {
					m_RMConnection.sendMessage(msg.getMsgTopClass());
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
