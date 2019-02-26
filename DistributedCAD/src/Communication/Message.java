package Communication;

import se.his.drts.message.Envelope;

public class Message {

	private Envelope envelope;
	private String id;
	private int attempt = 0;

	public Message(String id, Envelope envelope) {
		this.envelope = envelope;
		this.id = id;
	}

	public Envelope getEnvelope() {
		return envelope;
	}
	
	public String getId() {
		return id;
	}

	public int getAttempt() {
		return attempt;
	}
}
