package se.his.drts.message;

import java.util.UUID;

public class Acknowledge extends AbstractMessageTopClass{
	private Object id;
	
	public Acknowledge(Object id) {
		super(UUID.fromString("bb5eeb2c-fa66-4e70-891b-382d87b64814"));
		this.id = id;
	}
	
	@Override
	public Object executeInClient() {
		return id;
	}

	@Override
	public void executeInFrontEndFromRM() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void executeInFrontEndFromClient() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object executeInReplicaManager() {
		// TODO Auto-generated method stub
		return id;
	}

}
