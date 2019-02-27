package StrategyPatternMessages;

import se.his.drts.message.MessagePayload;

public abstract class ApplicationSpecificTopClass extends MessagePayload {

	private int attempt = 0;
	
	protected ApplicationSpecificTopClass(MessagePayload message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public abstract void executeInClient();

	public abstract void executeInFrontEndFromRM();

	public abstract void executeInFrontEndFromClient();

	public abstract void executeInReplicaManager();

}
