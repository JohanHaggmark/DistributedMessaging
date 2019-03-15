package MessageHandling;

import java.util.concurrent.LinkedBlockingQueue;

public class Resender implements Runnable{
	LinkedBlockingQueue<LocalMessage> m_messagesToResender;
	LinkedBlockingQueue<LocalMessage> m_messagesToSender;
	// https://wondernetwork.com/pings, average ping from sweden to common places in
	// the world
	// måste göras om för nytt vettigt värde
	private int averageRTT = 117;
	boolean senderHasConnection;
	boolean runThread = true;

	public Resender(LinkedBlockingQueue m_messagesToResender, LinkedBlockingQueue m_messageQueue, int rTT,
			boolean senderHasConnection) {
		this.m_messagesToResender = m_messagesToResender;
		this.m_messagesToSender = m_messageQueue;
		averageRTT = rTT;
		this.senderHasConnection = senderHasConnection;
	}

	public Resender(LinkedBlockingQueue m_messagesToResender, LinkedBlockingQueue m_messageQueue,
			boolean senderHasConnection) {
		this.m_messagesToResender = m_messagesToResender;
		this.m_messagesToSender = m_messageQueue;
		this.senderHasConnection = senderHasConnection;
	}

	@Override
	public void run() {
		while (runThread) {
			LocalMessage msg;
			try {
				msg = m_messagesToResender.take();
				if (senderHasConnection) {
					m_messagesToSender.add(msg);
				} else {
					this.m_messagesToResender.add(msg);
					runThread = false;
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}
}
