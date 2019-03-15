package se.his.drts.message;

import java.math.BigInteger;
import java.util.UUID;


public abstract class AbstractMessageTopClass extends MessagePayload {
	
	private static UUID uuid = UUID.fromString("d96d5262-4dfb-4639-82c3-15eb3c0fa789");
	private static final long serialVersionUID = 1L;
	private static BigInteger nextSubIdentity = BigInteger.ONE;
	private static Integer messageID = 0;
	private Integer messageNumber;
	private Integer id;
	
	protected String name;
	protected Integer pid;
	protected String type;
	protected String destination;
	protected Object GObjectList;
	
	public AbstractMessageTopClass() {
		super(AbstractMessageTopClass.uuid);
		this.messageNumber = messageID;
	}

	protected AbstractMessageTopClass(UUID uuid) {
		super(uuid);
		this.messageNumber = messageID;
		if (uuid.toString().equals("54f642d7-eaf6-4d62-ad2d-316e4b821c03")) {
			messageID++;
			System.out.println("MessageID: " + messageID + "  other id: " + messageNumber);
		}
	}

	public abstract Object executeInClient();

	public abstract Object executeInFrontEnd();

	public abstract Object executeInReplicaManager();

	public abstract UUID getUUID();
	
	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}
	
	public String getDestination() {
		return destination;
	}
	
	public Integer getMessageNumber() {
		return messageNumber;
	}
}
