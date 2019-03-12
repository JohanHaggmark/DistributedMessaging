package se.his.drts.message;

import java.util.UUID;

public class ElectionMessage extends AbstractMessageTopClass {
	
	private static UUID uuid = UUID.fromString("eceb2eb4-361c-425f-a760-a2cd434bbdff");
	private Integer id;
	
	public ElectionMessage() {
		super(uuid);
	}
	
	public ElectionMessage(Integer id) {
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
		return id;
	}

	@Override
	public UUID getUUID() {
		return uuid;
	}

}
