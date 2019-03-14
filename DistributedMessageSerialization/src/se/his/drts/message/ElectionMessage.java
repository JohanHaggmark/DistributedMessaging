package se.his.drts.message;

import java.util.UUID;

public class ElectionMessage extends AbstractMessageTopClass {
	
	private static UUID uuid = UUID.fromString("eceb2eb4-361c-425f-a760-a2cd434bbdff");
	private Integer pid;
	private String destination = null;
	
	public ElectionMessage() {
		super(uuid);
	}
	
	public ElectionMessage(Integer pid) {
		super(uuid);
		this.pid = pid;
	}
	
	@Override
	public Object executeInClient() {
		return null;
	}

	@Override
	public Object executeInFrontEndFromRM() {
		return pid;		
	}

	@Override
	public Object executeInFrontEndFromClient() {
		return pid;		
	}

	@Override
	public Object executeInReplicaManager() {
		return pid;
	}

	@Override
	public UUID getUUID() {
		return uuid;
	}

	@Override
	public String getDestination() {
		return null;
	}
}
