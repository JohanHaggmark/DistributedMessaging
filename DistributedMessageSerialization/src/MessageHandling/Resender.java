package MessageHandling;

import java.util.concurrent.LinkedBlockingQueue;

public class Resender implements Runnable{
	LinkedBlockingQueue<LocalMessage> m_messagesToResender;
	LinkedBlockingQueue<LocalMessage> m_messagesToSender;

	boolean senderHasConnection;
	boolean runThread = true;
	private LocalMessages m_messages;

	public Resender(LinkedBlockingQueue m_messagesToResender, LinkedBlockingQueue m_messageQueue,
			LocalMessages messages) {
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
				System.out.println("took msg, I am the resender");
				if (m_messages.isSenderHasConnection()) {
					System.out.println("gives msg to sender! I am resender");
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
