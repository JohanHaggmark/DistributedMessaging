package se.his.drts.message;

import java.util.UUID;

public class DrawObjectsMessage extends AbstractMessageTopClass {
	Object objectList;
	//private static UUID uuid = UUID.fromString("54f642d7-eaf6-4d62-ad2d-316e4b821c03");
	private static UUID uuid = UUID.fromString("f0492e3a-3064-4fcb-a243-68fe326ba43d");
	public DrawObjectsMessage(Object objectList) {
		super(uuid);
		this.objectList = objectList;
	}

	public DrawObjectsMessage() {
		super(uuid);
	}
	
	@Override
	public Object executeInClient() {
		return objectList;
	}

	@Override
	public Object executeInFrontEndFromClient() {
		// skickavidare till destination
		return null;
	}

	@Override
	public Object executeInFrontEndFromRM() {
		// skickavidare till destination
		return null;
	}

	@Override
	public Object executeInReplicaManager() {
		// spara det ritade state
		// skicka ut meddelandet till FrontEnd
		return objectList;
	}
	
	@Override
	public UUID getUUID() {
		return uuid;
	}
}
