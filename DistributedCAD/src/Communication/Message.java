package Communication;

import se.his.drts.message.AbstractMessageTopClass;

public class Message {

	private Integer id;
	private int attempt = 0;
	public boolean isAcknowledge = false;
	AbstractMessageTopClass msgTopClass;

	public Message(AbstractMessageTopClass msgTopClass) {
		this.msgTopClass = msgTopClass;
		this.id = msgTopClass.getId();
	}
	
	public Message() {
	}

	public int getAttempt() {
		return attempt;
	}
	public void incrementAttempt() {
		attempt++;
	}
	
	public void setAcknowledge() {
		isAcknowledge = true;
	}
	
	public AbstractMessageTopClass getMsgTopClass() {
		return this.msgTopClass;
	}
	
	public Integer getId() {
		return id;
	}
}
