package se.his.drts.message;

import DCAD.GObject;
import DCAD.GUI;
import se.his.drts.message.MessagePayload;

public class DrawObject extends Draw {

	GObject obj;
	public DrawObject(GObject obj) {
		this.obj = obj;
	}

	@Override
	public void executeInClient() {
		
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
	public void executeInReplicaManager() {
		// spara det ritade state
		// skicka ut meddelandet till FrontEnd

	}

}
