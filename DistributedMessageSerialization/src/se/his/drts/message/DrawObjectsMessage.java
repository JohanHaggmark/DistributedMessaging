package se.his.drts.message;

import java.util.UUID;

public class DrawObjectsMessage extends AbstractMessageTopClass {
	
	private Object GObjectList;
	private String destination = null;
	private String address;
	private static UUID uuid = UUID.fromString("54f642d7-eaf6-4d62-ad2d-316e4b821c03");
	
	public DrawObjectsMessage(Object objectList, String address, String destination) {
		super(uuid);
		this.GObjectList = objectList;
		this.destination = destination;
		this.address = address;
	}
	
	public DrawObjectsMessage(Object objectList, String address) {
		super(uuid);
		this.GObjectList = objectList;
		this.address = address;
	}

	public DrawObjectsMessage() {
		super(uuid);
	}
	
	@Override
	public Object executeInClient() {
		return GObjectList;
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
		return GObjectList;
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
