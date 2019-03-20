package MessageHandling;

import java.util.concurrent.LinkedBlockingQueue;

public class RTMessageRepeater implements Runnable {

	LinkedBlockingQueue<LocalMessage> m_RTTMessageQueue;
	LinkedBlockingQueue<LocalMessage> m_messageQueue;

	private int roundTime = 1024; //in this case it reprsent the time when replica managers have elecation, and dont 
									//take requests from clients.

	public RTMessageRepeater(LinkedBlockingQueue m_RTMessageQueue, LinkedBlockingQueue m_messageQueue, int rT) {
		this.m_RTTMessageQueue = m_RTMessageQueue;
		this.m_messageQueue = m_messageQueue;
		roundTime = rT;
	}

	public RTMessageRepeater(LinkedBlockingQueue m_RTMessageQueue, LinkedBlockingQueue m_messageQueue) {
		this.m_RTTMessageQueue = m_RTMessageQueue;
		this.m_messageQueue = m_messageQueue;
	}

	@Override
	public void run() {
		while (true) {
			// receive message from Sender:
			LocalMessage msg = null;
			try {
				msg = m_RTTMessageQueue.take();
				Thread.sleep(roundTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (!msg.isAcknowledged()) {
				m_messageQueue.add(msg);
			}
		}
	}
}
