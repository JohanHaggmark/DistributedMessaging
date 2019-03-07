package Communication;

public class Message {

	private int id;
	private int attempt = 0;
	public boolean isAcknowledge = false;

	public Message(int id) {
		this.id = id;
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
