package Communication;

import se.his.drts.message.AbstractMessageTopClass;

public class Message {

	private int id;
	private int attempt = 0;
	public boolean isAcknowledge = false;
	private AbstractMessageTopClass msgTopClass;

	public Message(int id, AbstractMessageTopClass msgTopClass) {
		this.id = id;
		this.msgTopClass = msgTopClass;
	}
	
	public Message() {
	}
	
	public int getId() {
		return id;
	}

	public int getAttempt() {
		return attempt;
	}
	public void incrementAttempt() {
		attempt++;
	}
}
