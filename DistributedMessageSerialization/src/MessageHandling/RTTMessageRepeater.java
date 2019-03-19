package MessageHandling;

import java.util.concurrent.LinkedBlockingQueue;

public class RTTMessageRepeater implements Runnable {

	LinkedBlockingQueue<LocalMessage> m_RTTMessageQueue;
	LinkedBlockingQueue<LocalMessage> m_messageQueue;
	// https://wondernetwork.com/pings, average ping from sweden to common places in
	// the world
	// måste göras om för nytt vettigt värde
	private int averageRTT = 1170;

	public RTTMessageRepeater(LinkedBlockingQueue m_RTTMessageQueue, LinkedBlockingQueue m_messageQueue, int rTT) {
		this.m_RTTMessageQueue = m_RTTMessageQueue;
		this.m_messageQueue = m_messageQueue;
		averageRTT = rTT;
	}

	public RTTMessageRepeater(LinkedBlockingQueue m_RTTMessageQueue, LinkedBlockingQueue m_messageQueue) {
		this.m_RTTMessageQueue = m_RTTMessageQueue;
		this.m_messageQueue = m_messageQueue;
	}

	@Override
	public void run() {
		while (true) {
			// receive message from Sender:
			LocalMessage msg = null;
			try {
				msg = m_RTTMessageQueue.take();
				Thread.sleep(averageRTT);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (!msg.isAcknowledged()) {
				m_messageQueue.add(msg);
			}
		}
	}
}
