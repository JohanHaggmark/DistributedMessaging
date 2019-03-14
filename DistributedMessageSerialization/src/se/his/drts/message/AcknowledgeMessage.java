package se.his.drts.message;

import java.util.UUID;

public class AcknowledgeMessage extends AbstractMessageTopClass{
	private Object id;
	private String destination = null;
	private static UUID uuid = UUID.fromString("bb5eeb2c-fa66-4e70-891b-382d87b64814");
	
	public AcknowledgeMessage(Object id, String address, String destination) {
		super(uuid);
		this.id = id;
		this.destination = destination;
	}
	
	@Override
	public Object executeInClient() {
		return id;
	}

	@Override
	public Object executeInFrontEndFromRM() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object executeInFrontEndFromClient() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object executeInReplicaManager() {
		// TODO Auto-generated method stub
		return id;
	}
	
	@Override
	public UUID getUUID() {
		return uuid;
	}
	
	@Override
	public String getDestination() {
		return this.destination;
	}
}
