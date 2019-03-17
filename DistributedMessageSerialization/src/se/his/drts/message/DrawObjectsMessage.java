package se.his.drts.message;

import java.util.HashMap;
import java.util.UUID;

public class DrawObjectsMessage extends AbstractMessageTopClass {
	
	private static UUID uuid = UUID.fromString("54f642d7-eaf6-4d62-ad2d-316e4b821c03");


	public DrawObjectsMessage(HashMap<String,String> object, String name) {
		super(uuid);
		//this.state = state;
		this.name = name;
		this.object = object;
	}

	public DrawObjectsMessage() {
		super(uuid);
	}
	
	@Override
	public Object executeInClient() {
		return this.object;
	}

	@Override
	public Object executeInFrontEnd() {
		return null;
	}

	@Override
	public Object executeInReplicaManager() {
		// spara det ritade state
		// skicka ut meddelandet till FrontEnd
		return this.object;
	}
	
	@Override
	public UUID getUUID() {
		return uuid;
	}
}
