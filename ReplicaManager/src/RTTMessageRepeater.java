import java.util.concurrent.LinkedBlockingQueue;
import org.jgroups.Message;


public class RTTMessageRepeater implements Runnable {

	LinkedBlockingQueue<Message> messagesToSender;
	LinkedBlockingQueue<Message> m_messageQueue;
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
			Message msg = null;
			try {
				msg = m_messageQueue.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			try {
				Thread.sleep(averageRTT);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			messagesToSender.add(msg);			
		}
	}
}
