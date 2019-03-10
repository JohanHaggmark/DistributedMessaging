package se.his.drts.message;

import java.util.UUID;

import se.his.drts.message.MessagePayload;

public abstract class Draw extends AbstractMessageTopClass {

	public Draw(UUID uuid) {
		super(uuid);
	}

}
