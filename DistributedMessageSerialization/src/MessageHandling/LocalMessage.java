package MessageHandling;

import se.his.drts.message.AbstractMessageTopClass;

public class LocalMessage {

	private Integer id;
	private int attempt = 0;
	private boolean isAcknowledgeMessage;
	private boolean isAcknowledged = false;
	private AbstractMessageTopClass msgTopClass;
	private int exp = 0;

	public LocalMessage(AbstractMessageTopClass msgTopClass, boolean isAcknowledgeMessage) {
		this.msgTopClass = msgTopClass;
		this.id = msgTopClass.getMessageNumber();
		this.isAcknowledgeMessage = isAcknowledgeMessage;
	}
	

	public LocalMessage(AbstractMessageTopClass msgTopClass, boolean isAcknowledgeMessage, int exp) {
		this.msgTopClass = msgTopClass;
		this.id = msgTopClass.getMessageNumber();
		this.isAcknowledgeMessage = isAcknowledgeMessage;
		this.exp = exp;
	}
	
	// Set whether the message should be acknowledged or not
	public boolean isAcknowledgeMessage() {
		return isAcknowledgeMessage;
	}
	
	public int getAttempt() {
		return attempt;
	}
	
	public void incrementAttempt() {
		attempt++;
		exp *= 2;
	}
	
	public void setToAcknowledged() {
		isAcknowledged = true;
	}
	
	public boolean isAcknowledged() {
		return isAcknowledged;
	}
	
	public AbstractMessageTopClass getMsgTopClass() {
		return this.msgTopClass;
	}
	
	public Integer getId() {
		return id;
	}
	
	public int getExp() {
		return exp;
	}
}
