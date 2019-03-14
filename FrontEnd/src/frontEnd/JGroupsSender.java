package frontEnd;

import java.util.concurrent.LinkedBlockingQueue;

import org.jgroups.JChannel;
import org.jgroups.Message;

public class JGroupsSender implements Runnable {

	private JChannel m_channel;
	private LinkedBlockingQueue<byte[]> m_messages = new LinkedBlockingQueue();
	private LinkedBlockingQueue<byte[]> m_resendMessages = new LinkedBlockingQueue();
	private boolean m_hasPrimary = false;

	public JGroupsSender(JChannel channel, LinkedBlockingQueue<byte[]> messages) {
		this.m_messages = messages;
		this.m_channel = channel;
	}

	@Override
	public void run() {
		while (true) {
			try {
				byte[] bytes = m_messages.take();
				// OM PRIMARY INTE FINNS SKA VI DÅ LÄGGA TILL DENNA I LBQ??
				// KANSKE ÄR RIMLIGARE ATT CLIENT SKICKAR OM OCH HAR HAND OM EXPONENTIAL BACKOFF
				if (FrontEnd.primaryRM != null) {
					if(!m_hasPrimary) {
						m_hasPrimary = true;
						new ResendThread().start();
					}
					FrontEnd.logger.debugLog("JGroupsSender() - Sending bytes from client" + bytes);
//					FrontEnd.logger.debugLog("Trying to cast | 1 " + bytes);
//					Optional<MessagePayload> mpl = MessagePayload.createMessage(bytes);
//					AbstractMessageTopClass topClass = (AbstractMessageTopClass) mpl.get();
//					FrontEnd.logger.debugLog("Trying to cast | 2 ");
//					FrontEnd.logger.debugLog("Trying to cast | 3 " + topClass.getId());
					m_channel.send(new Message(FrontEnd.primaryRM, bytes));
				} else {
					FrontEnd.logger.debugLog("JGroupsSender() - No primary when received from client");
					m_resendMessages.put(bytes);
				}
			} catch (Exception e) {
				FrontEnd.logger.criticalLog("JGroupsSender() - Exception in JGroupsSender");
				e.printStackTrace();
			}
		}
	}

	private class ResendThread extends Thread {		
		@Override
		public void run() {
			FrontEnd.logger.debugLog("JGroupsSender() - resendThread started");
			try {
				while(m_hasPrimary) {
					if(!m_hasPrimary) {
						FrontEnd.logger.criticalLog("JGroupsSender() - HAS NO PRIMARY BUT THREAD IS RUNNING ANYWAY");
					}
					m_messages.put(m_resendMessages.take());
					if(!m_hasPrimary) {
						FrontEnd.logger.criticalLog("JGroupsSender() - HAS NO PRIMARY BUT THREAD IS RUNNING ANYWAY");
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
