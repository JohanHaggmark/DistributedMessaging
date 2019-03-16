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
				// Cad har EXPONENTIAL BACKOFF
				//Front End lagrar en meddelanden som inte går att skicka
				if (FrontEnd.primaryRM != null) {
					FrontEnd.logger.debugLog("JGroupsSender() - Sending bytes from client" + bytes);
					m_channel.send(new Message(FrontEnd.primaryRM, bytes));
				}
			} catch (Exception e) {
				FrontEnd.logger.criticalLog("JGroupsSender() - Exception in JGroupsSender");
				e.printStackTrace();
			}
		}
	}

}
