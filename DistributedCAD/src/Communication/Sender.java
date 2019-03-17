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
				Cad.logger.debugLog("took a message from messageQueue ");
				if (RMConnection.hasFrontEnd && RMConnection.connectionName != null) {
					msg.getMsgTopClass().changeName(RMConnection.connectionName); //Make sure msg has the latest connectionName
					m_RMConnection.sendMessage(msg.getMsgTopClass());
					tryAddToRTT(msg);
				} 
				else {
					m_messages.addToMessagesToResender(msg);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void tryAddToRTT(LocalMessage msg) {
		if (msg.isAcknowledgeMessage()) {
			if ((msg.isAcknowledged() == false) && msg.getAttempt() < ATTEMPTS) {
				Cad.logger.debugLog("SENDER - sending message: " + msg.getMsgTopClass());
				msg.incrementAttempt();
				m_messages.addToRTTMessageQueue(msg);
			} else if(ATTEMPTS <= msg.getAttempt()){
				//the message should be removed from the map
				m_messages.removeAcknowledgeFromMessage(msg.getId());
			}
		}
	}
}
