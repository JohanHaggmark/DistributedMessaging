package se.his.drts.message;

import java.math.BigInteger;
import java.util.UUID;

import se.his.drts.message.MessagePayload;


public abstract class AbstractMessageTopClass extends MessagePayload {
	
	private static final long serialVersionUID = 1L;
	private int attempt = 0;
	private static BigInteger nextSubIdentity = BigInteger.ONE;
	private static UUID uuid = UUID.fromString("d96d5262-4dfb-4639-82c3-15eb3c0fa789");
	private BigInteger subIdentity;

	
	public AbstractMessageTopClass() {
		super(AbstractMessageTopClass.uuid);
		synchronized (AbstractMessageTopClass.nextSubIdentity) {
			this.subIdentity = AbstractMessageTopClass.nextSubIdentity;
			AbstractMessageTopClass.nextSubIdentity = AbstractMessageTopClass.nextSubIdentity.add(BigInteger.ONE);
		}
	}

	protected AbstractMessageTopClass(UUID uuid) {
		super(uuid);
		synchronized (AbstractMessageTopClass.nextSubIdentity) {
			this.subIdentity = AbstractMessageTopClass.nextSubIdentity;
			AbstractMessageTopClass.nextSubIdentity = AbstractMessageTopClass.nextSubIdentity.add(BigInteger.ONE);
		}
	}

	public abstract void executeInClient();

	public abstract void executeInFrontEndFromRM();

	public abstract void executeInFrontEndFromClient();

	public abstract void executeInReplicaManager() ;

}
