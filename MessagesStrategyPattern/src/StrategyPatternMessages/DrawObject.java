package StrategyPatternMessages;

import se.his.drts.message.MessagePayload;

public class DrawObject extends Draw {

	protected DrawObject(MessagePayload message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void executeInClient() {
		// rita upp GObjectet

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
