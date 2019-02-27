package StrategyPatternMessages;

import se.his.drts.message.MessagePayload;

public abstract class Draw extends ApplicationSpecificTopClass {

	protected Draw(MessagePayload message) {
		super(message);

	}
}
