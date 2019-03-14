package se.his.drts.message;

import java.util.UUID;

public class CoordinatorMessage extends AbstractMessageTopClass {
	
	private static UUID uuid = UUID.fromString("88486f0c-1a3e-428e-a90c-3ceda5426f27");
	private Integer pid;
	
	public CoordinatorMessage(Integer pid) {
		super(uuid);
		this.pid = pid;
	}
	
	public CoordinatorMessage() {
		super(uuid);
	}
	
	@Override
	public Object executeInClient() {
		// TODO Auto-generated method stub
		return null;
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
		return pid;
	}

	@Override
	public UUID getUUID() {
		// TODO Auto-generated method stub
		return uuid;
	}
	
	@Override
	public String getDestination() {
		return null;
	}
}
