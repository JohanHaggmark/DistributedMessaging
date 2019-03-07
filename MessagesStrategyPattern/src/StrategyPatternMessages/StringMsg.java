package StrategyPatternMessages;

import java.io.Serializable;
import java.util.UUID;

import se.his.drts.message.MessagePayload;

public class StringMsg extends AbstractMessageTopClass {

	private static UUID uuid = UUID.fromString("d1ca604c-fba1-4011-ae53-2a622f95c1c8");
	
	private String name;

	
	
	protected StringMsg() {
		super(StringMsg.uuid);
	}

	public StringMsg(String name) {
		super(StringMsg.uuid);
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public void executeInClient() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void executeInFrontEndFromRM() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void executeInFrontEndFromClient() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void executeInReplicaManager() {
		// TODO Auto-generated method stub
		
	}

}
