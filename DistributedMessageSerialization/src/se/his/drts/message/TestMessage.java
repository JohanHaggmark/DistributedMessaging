package se.his.drts.message;

import java.util.UUID;

public class TestMessage extends AbstractMessageTopClass {
	
	private static UUID uuid = UUID.fromString("c6600823-9c85-41d6-b116-9877c195c1a8");
//	private Object infoMap;
//	private Object GObjectList;
	private String destination = null;
//	private String address;
	
	public TestMessage(String address) {
		super(uuid);
	}

	public TestMessage() {
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
		return null;
	}
	
	@Override
	public UUID getUUID() {
		return uuid;
	}
}
