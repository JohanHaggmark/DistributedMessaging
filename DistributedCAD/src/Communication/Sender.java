package Communication;

import se.his.drts.message.Envelope;

public class Sender extends Thread {

	Messages m_messages;
	RMConnection m_RMConnection;
	private final int ATTEMPTS = 10;

	public Sender(RMConnection rmConnection, Messages messages) {
		this.m_RMConnection = rmConnection;
		this.m_messages = messages;

	}

	@Override
	public void run() {
		while (true) {
			try {
				Message msg = (Message) m_messages.getMessageQueue().take();
				if (!msg.isAcknowledge && msg.getAttempt() < ATTEMPTS) {
					m_RMConnection.sendMessage("hej ,Sender 23");
					msg.incrementAttempt();
					m_messages.addToRTTMessageQueue(msg);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
