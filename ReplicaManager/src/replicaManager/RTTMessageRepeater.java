package replicaManager;
import java.util.concurrent.LinkedBlockingQueue;

import se.his.drts.message.LocalMessage;


public class RTTMessageRepeater implements Runnable {

	LinkedBlockingQueue<LocalMessage> messagesToSender;
	LinkedBlockingQueue<LocalMessage> m_messageQueue;
	// https://wondernetwork.com/pings, average ping from sweden to common places in
	// the world
	private final int averageRTT = 117;
	
	
	public RTTMessageRepeater(LinkedBlockingQueue messagesToSender, LinkedBlockingQueue m_messageQueue) {
		this.messagesToSender = messagesToSender;
		this.m_messageQueue = m_messageQueue;
	}

	@Override
	public void run() {
		while (true) {
			// receive message from Sender:
			LocalMessage msg = null;
			try {
				msg = m_messageQueue.take();
			} catch (InterruptedException e) {
				System.out.println("RTT interruptException e");
				e.printStackTrace();
			}
			
			try {
				Thread.sleep(averageRTT);
			} catch (InterruptedException e) {
				System.out.println("RTT interruptException sleep");
				e.printStackTrace();
			}
			messagesToSender.add(msg);			
		}
	}
}
