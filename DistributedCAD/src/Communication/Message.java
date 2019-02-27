package Communication;

import se.his.drts.message.Envelope;

public class Message {

	private Envelope envelope;
	private int id;
	private int attempt = 0;
	public boolean isAcknowledge = false;

	public Message(int id, Envelope envelope) {
		this.envelope = envelope;
		this.id = id;
	}

	public Envelope getEnvelope() {
		return envelope;
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
