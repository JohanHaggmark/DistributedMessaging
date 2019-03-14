package frontEnd;

import java.util.concurrent.LinkedBlockingQueue;

import org.jgroups.JChannel;
import org.jgroups.Message;

public class JGroupsSender implements Runnable {

	JChannel m_channel;
	LinkedBlockingQueue<byte[]> m_messages;
	LinkedBlockingQueue<byte[]> m_resendMessages;
	private boolean m_hasPrimary = false;

	public JGroupsSender(JChannel channel, LinkedBlockingQueue<byte[]> messages) {
		this.m_messages = messages;
		this.m_channel = channel;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Object obj = m_messages.take();
				// OM PRIMARY INTE FINNS SKA VI DÅ LÄGGA TILL DENNA I LBQ??
				// KANSKE ÄR RIMLIGARE ATT CLIENT SKICKAR OM OCH HAR HAND OM EXPONENTIAL BACKOFF
				if (FrontEnd.primaryRM != null) {
					if(!m_hasPrimary) {
						m_hasPrimary = true;
						new ResendThread().start();
					}
					FrontEnd.logger.debugLog("Sending bytes from client" + obj);
					m_channel.send(new Message(FrontEnd.primaryRM, obj));
				} else {
					FrontEnd.logger.debugLog("No primary when received from client");
					m_resendMessages.put((byte[]) obj);
				}
			} catch (Exception e) {
				FrontEnd.logger.criticalLog("Exception in JGroupsSender");
				e.printStackTrace();
			}
		}
	}

	private class ResendThread extends Thread {		
		@Override
		public void run() {
			try {
				while(m_hasPrimary) {
					if(!m_hasPrimary) {
						FrontEnd.logger.criticalLog("HAS NO PRIMARY BUT THREAD IS RUNNING ANYWAY");
					}
					m_messages.put(m_resendMessages.take());
					if(!m_hasPrimary) {
						FrontEnd.logger.criticalLog("HAS NO PRIMARY BUT THREAD IS RUNNING ANYWAY");
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
