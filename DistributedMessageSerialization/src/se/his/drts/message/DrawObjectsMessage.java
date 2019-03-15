package se.his.drts.message;

import java.util.UUID;

public class DrawObjectsMessage extends AbstractMessageTopClass {
	
	private static UUID uuid = UUID.fromString("54f642d7-eaf6-4d62-ad2d-316e4b821c03");
	
	public DrawObjectsMessage(Object objectList, String name, String destination) {
		super(uuid);
		this.GObjectList = objectList;
		this.destination = destination;
		this.name = name;
	}
	
	public DrawObjectsMessage(Object objectList, String name) {
		super(uuid);
		this.GObjectList = objectList;
		this.name = name;
	}

	public DrawObjectsMessage() {
		super(uuid);
	}
	
	@Override
	public Object executeInClient() {
		return GObjectList;
	}

	@Override
	public Object executeInFrontEnd() {
		return null;
	}

	@Override
	public Object executeInReplicaManager() {
		// spara det ritade state
		// skicka ut meddelandet till FrontEnd
		return GObjectList;
	}
	
	@Override
	public UUID getUUID() {
		return uuid;
	}
}
