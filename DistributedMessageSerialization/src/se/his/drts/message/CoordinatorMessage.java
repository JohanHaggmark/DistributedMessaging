package se.his.drts.message;

import java.util.UUID;

public class CoordinatorMessage extends AbstractMessageTopClass {
	
	private static UUID uuid = UUID.fromString("88486f0c-1a3e-428e-a90c-3ceda5426f27");
	private Integer id;

	
	public CoordinatorMessage() {
		super(uuid);
	}
	
	public CoordinatorMessage(Integer id) {
		super(uuid);
		this.id = id;
	}
	
	@Override
	public Object executeInClient() {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public UUID getUUID() {
		// TODO Auto-generated method stub
		return null;
	}

}
