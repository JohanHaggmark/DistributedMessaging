package se.his.drts.message;

import java.math.BigInteger;
import java.util.UUID;


public abstract class AbstractMessageTopClass extends MessagePayload {
	
	private static Integer messageID = 0;
	private static final long serialVersionUID = 1L;
	private int attempt = 0;
	private static BigInteger nextSubIdentity = BigInteger.ONE;
	private static UUID uuid = UUID.fromString("d96d5262-4dfb-4639-82c3-15eb3c0fa789");
	private Integer id;
	
	public AbstractMessageTopClass() {
		super(AbstractMessageTopClass.uuid);
		this.id = messageID;
	}

	protected AbstractMessageTopClass(UUID uuid) {
		super(uuid);
		this.id = messageID;
		if (uuid.toString().equals("54f642d7-eaf6-4d62-ad2d-316e4b821c03")) {
			messageID++;
			System.out.println("MessageID: " + messageID + "  other id: " + id);
		}
	}

	public abstract Object executeInClient();

	public abstract Object executeInFrontEndFromRM();

	public abstract Object executeInFrontEndFromClient();

	public abstract Object executeInReplicaManager() ;

	public abstract UUID getUUID();
	
	public abstract String getDestination();
	
	public Integer getId() {
		return this.id;
	}
}
