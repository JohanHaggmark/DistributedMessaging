package Communication;

import DCAD.Cad;
import MessageHandling.LocalMessage;
import MessageHandling.LocalMessages;

public class Sender implements Runnable {

	LocalMessages m_messages;
	RMConnection m_RMConnection;
	private boolean runThread = true;
	private final int ATTEMPTS = 10;

	public Sender(RMConnection rmConnection, LocalMessages messages) {
		this.m_RMConnection = rmConnection;
		this.m_messages = messages;

	}

	@Override
	public void run() {

		try {
			while (runThread) {
				LocalMessage msg = (LocalMessage) m_messages.getMessageQueue().take();

				Cad.logger.debugLog("took a message from messageQueue ");
				if (m_messages.isSenderHasConnection() && RMConnection.connectionName != null) { 
					msg.getMsgTopClass().changeName(RMConnection.connectionName); // Make sure msg has the latest
					Cad.logger.debugLog("sending over channel, atleast try");										// connectionName
					m_RMConnection.sendMessage(msg.getMsgTopClass());
					tryAddToRTT(msg);
				} else {//we dont send when Front end is down
					m_messages.setSenderHasConnection(false);
					Cad.logger.debugLog("fronend: " + m_messages.isSenderHasConnection() + "  RMConnection: "
							+ RMConnection.connectionName);
					m_messages.addToMessagesToResender(msg);
					runThread = false;
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			Cad.logger.debugLog("Sender got exception");
		}

	}

	private void tryAddToRTT(LocalMessage msg) {
		if (msg.isAcknowledgeMessage()) {
			if ((msg.isAcknowledged() == false) && msg.getAttempt() < ATTEMPTS) {
				Cad.logger.debugLog("SENDER - sending message: " + msg.getMsgTopClass());
				msg.incrementAttempt();
				m_messages.addToRTTMessageQueue(msg);
			} else if (ATTEMPTS <= msg.getAttempt()) {
				// the message should be removed from the map
				m_messages.removeAcknowledgeFromMessage(msg.getId());
			}
		}
	}
}
