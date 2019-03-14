package Communication;

import DCAD.Cad;
import se.his.drts.message.LocalMessage;
import se.his.drts.message.LocalMessages;

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
				if (!msg.isAcknowledge && msg.getAttempt() < ATTEMPTS) {
					Cad.logger.debugLog("SENDER - sending message: " + msg.getMsgTopClass());
					m_RMConnection.sendMessage(msg.getMsgTopClass());
					msg.incrementAttempt();
					m_messages.addToRTTMessageQueue(msg);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
