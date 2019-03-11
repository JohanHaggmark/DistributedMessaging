package se.his.drts.message;

public class LocalMessage {

	private Integer id;
	private int attempt = 0;
	public boolean isAcknowledge = false;
	AbstractMessageTopClass msgTopClass;

	public LocalMessage(AbstractMessageTopClass msgTopClass) {
		this.msgTopClass = msgTopClass;
		this.id = msgTopClass.getId();
	}
	
	public LocalMessage() {
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
