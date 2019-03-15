package se.his.drts.message;

import java.util.UUID;

public class TimeOutMessage extends AbstractMessageTopClass{
	
	private static UUID uuid = UUID.fromString("0d61e561-116e-48e3-9259-8170c9623da3");
	private long timeStamp;
	private String destination = null;
	
	public TimeOutMessage() {
		super(uuid);
	}
	
	public TimeOutMessage(long timeStamp) {
		super(uuid);
		this.timeStamp = timeStamp;
	}
	
	@Override
	public Object executeInClient() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object executeInFrontEnd() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object executeInReplicaManager() {
		return timeStamp;
	}

	@Override
	public UUID getUUID() {
		return uuid;
	}
}
