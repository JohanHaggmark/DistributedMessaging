package StrategyPatternMessages;

import java.util.UUID;

import se.his.drts.message.MessagePayload;

public abstract class AbstractMessageTopClass extends MessagePayload {

	// skapa metod för att göra ett unikt UUID

	private int attempt = 0;

	protected AbstractMessageTopClass(MessagePayload message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public abstract void executeInClient();

	public abstract void executeInFrontEndFromRM();

	public abstract void executeInFrontEndFromClient();

	public abstract void executeInReplicaManager();

}
