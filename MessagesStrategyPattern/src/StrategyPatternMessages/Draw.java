package StrategyPatternMessages;

import se.his.drts.message.MessagePayload;

public abstract class Draw extends AbstractMessageTopClass {

	protected Draw(MessagePayload message) {
		super(message);

	}
}
