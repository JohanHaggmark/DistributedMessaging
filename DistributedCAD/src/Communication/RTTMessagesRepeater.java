package Communication;

import java.util.concurrent.LinkedBlockingQueue;

import se.his.drts.message.Envelope;

public class RTTMessagesRepeater implements Runnable {

	LinkedBlockingQueue<Envelope> messages;
	LinkedBlockingQueue<Envelope> messageQueue;

	// https://wondernetwork.com/pings, average ping from sweden to common places in
	// the world
	private final int averageRTT = 117;

	// public RTTMessagesRepeater(LinkedBlockingQueue<MessageAndAttempts>
	// messageQueue) {
	// messages = new LinkedBlockingQueue<MessageAndAttempts>();
	// this.messageQueue = messageQueue;

	// }

	@Override
	public void run() {
		while (true) {
			// receive message from acknowledgehandler:
			// MessageAndAttempts message = null;
			/*
			 * try { // message = messages.take(); } catch (InterruptedException e1) { //
			 * TODO Auto-generated catch block e1.printStackTrace(); } try {
			 * Thread.sleep(averageRTT); } catch (InterruptedException e) {
			 * e.printStackTrace(); } // messageQueue.add(message); // add message to
			 * acknowledgehandler }
			 */
		}

		// public void addMessage(MessageAndAttempts message) {
		// messages.add(message);
		// }
	}
}
