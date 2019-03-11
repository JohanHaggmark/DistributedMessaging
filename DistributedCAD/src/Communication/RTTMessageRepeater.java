package Communication;

import java.util.concurrent.LinkedBlockingQueue;

import se.his.drts.message.LocalMessage;


public class RTTMessageRepeater implements Runnable {

	LinkedBlockingQueue<LocalMessage> m_RTTMessageQueue;
	LinkedBlockingQueue<LocalMessage> m_messageQueue;
	// https://wondernetwork.com/pings, average ping from sweden to common places in
	// the world
	//måste göras om för nytt vettigt värde
	private final int averageRTT = 117;
	
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
			m_messageQueue.add(msg);			
		}
	}
}
