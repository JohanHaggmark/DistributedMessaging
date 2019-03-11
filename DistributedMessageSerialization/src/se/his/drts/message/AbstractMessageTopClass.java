package se.his.drts.message;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.UUID;

import se.his.drts.message.MessagePayload;


public abstract class AbstractMessageTopClass extends MessagePayload {
	
	private static final long serialVersionUID = 1L;
	private int attempt = 0;
	private static BigInteger nextSubIdentity = BigInteger.ONE;
	private static UUID uuid = UUID.fromString("d96d5262-4dfb-4639-82c3-15eb3c0fa789");
	private static Integer id = 0;
	
	public AbstractMessageTopClass() {
		super(AbstractMessageTopClass.uuid);
	}

	protected AbstractMessageTopClass(UUID uuid) {
		super(uuid);
		if (!uuid.toString().equals("bb5eeb2c-fa66-4e70-891b-382d87b64814")) {
			id++;
		}
	}
	
//	public abstract UUID getClassUUID();

	public abstract Object executeInClient();

	public abstract void executeInFrontEndFromRM();

	public abstract void executeInFrontEndFromClient();

	public abstract Object executeInReplicaManager() ;

	public abstract UUID getUUID();
	
	public Integer getId() {
		return id;
	}
}
