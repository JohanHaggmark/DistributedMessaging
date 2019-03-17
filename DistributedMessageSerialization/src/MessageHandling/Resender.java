package MessageHandling;

import java.util.concurrent.LinkedBlockingQueue;

public class Resender implements Runnable{
	LinkedBlockingQueue<LocalMessage> m_messagesToResender;
	LinkedBlockingQueue<LocalMessage> m_messagesToSender;

	boolean runThread = true;
	private LocalMessages m_messages;
	private int rtt = 117;

	public Resender(LinkedBlockingQueue m_messagesToResender, LinkedBlockingQueue m_messageQueue,
			LocalMessages messages) {
		this.m_messagesToResender = m_messagesToResender;
		this.m_messagesToSender = m_messageQueue;
		this.m_messages = messages;
	}

	@Override
	public void run() {
		while (runThread) {
			LocalMessage msg;
			try {
				msg = m_messagesToResender.take();
				System.out.println("took msg, I am the resender");
				if (m_messages.isSenderHasConnection()) {
					System.out.println("gives msg to sender! I am resender");
					m_messagesToSender.add(msg);
					Thread.sleep(rtt);
				} else {
					System.out.println("give msg to myself. I am resender");
					this.m_messagesToResender.add(msg);
					runThread = false;
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("exception in resender");
			}

		}
	}
}
