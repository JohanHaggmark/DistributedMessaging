package Communication;

import se.his.drts.message.Envelope;

public class Sender extends Thread {
	
	Messages m_messages;
	RMConnection m_RMConnection;
	
	public Sender(RMConnection rmConnection, Messages messages) {
		this.m_RMConnection = rmConnection;
		this.m_messages = messages;
		
	}
	@Override
	public void run() {
		while(true) {
			try {
				Envelope msg = (Envelope) m_messages.getMessageQueue().take();
			//if not acknowledged:
				m_RMConnection.sendMessage(msg);
				//send to RTTHandler
				///
				/*
				 *ta reda på om meddelandet ska skickas om. Ska det inte det, så ska det inte skickas vidare till RTThandler
				 *
				 */
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


	}

}
