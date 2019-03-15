package se.his.drts.message;

import java.util.UUID;

public class AcknowledgeMessage extends AbstractMessageTopClass{
	
	private static UUID uuid = UUID.fromString("bb5eeb2c-fa66-4e70-891b-382d87b64814");
	
	public AcknowledgeMessage(Integer ackID, String name) {
		super(uuid);
		//this.pid = pid;
		this.name = name;
		this.ackID = ackID;
	}
	
	public AcknowledgeMessage() {
		super(uuid);
	}
	
	@Override
	public Object executeInClient() {
		return ackID;
	}

	@Override
	public Object executeInFrontEnd() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object executeInReplicaManager() {
		// TODO Auto-generated method stub
		return ackID;
	}
	
	@Override
	public UUID getUUID() {
		return uuid;
	}
}
