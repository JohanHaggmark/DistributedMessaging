package se.his.drts.message;

import java.util.LinkedList;
import java.util.UUID;

public class DrawObjects extends Draw {
	Object objectList;
	
	
	public DrawObjects(Object objectList) {
		super(UUID.fromString("54f642d7-eaf6-4d62-ad2d-316e4b821c03"));
		this.objectList = objectList;
	}

	@Override
	public Object executeInClient() {
		return objectList;
	}

	@Override
	public void executeInFrontEndFromClient() {
		// skickavidare till destination

	}

	@Override
	public void executeInFrontEndFromRM() {
		// skickavidare till destination

	}

	@Override
	public Object executeInReplicaManager() {
		// spara det ritade state
		// skicka ut meddelandet till FrontEnd
		return objectList;
	}
}
