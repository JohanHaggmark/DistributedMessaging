package se.his.drts.message;

import java.util.UUID;

public class ElectionMessage extends AbstractMessageTopClass {
	
	private static UUID uuid = UUID.fromString("eceb2eb4-361c-425f-a760-a2cd434bbdff");
		
	public ElectionMessage(Integer pid) {
		super(uuid);
		this.pid = pid;
	}
	
	public ElectionMessage() {
		super(uuid);
	}
	
	@Override
	public Object executeInClient() {
		return null;
	}

	@Override
	public Object executeInFrontEnd() {
		return null;		
	}

	@Override
	public Object executeInReplicaManager() {
		return pid;
	}

	@Override
	public UUID getUUID() {
		return uuid;
	}
}
