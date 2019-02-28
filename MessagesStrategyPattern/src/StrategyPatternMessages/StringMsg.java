package StrategyPatternMessages;

import java.io.Serializable;
import java.util.UUID;

import se.his.drts.message.MessagePayload;

public class StringMsg extends MessagePayload implements Serializable{
	/*public String msg;

	
	public StringMsg(MessagePayload message) {
		super(message);
		//this.msg = msg;
	}
	
	public StringMsg(String msg) {
		super(UUID.fromString("d1ca604c-fba1-4011-ae53-2a622f95c1c8"));
		this.msg = msg;
	}*/
	private String name;
	public StringMsg(MessagePayload message) {
		super(message);
		this.name = ((StringMsg)message).name;
	}
	public StringMsg() {
		super(UUID.fromString("d1ca604c-fba1-4011-ae53-2a622f95c1c8"));
	}
	public StringMsg(String name) {
		super(UUID.fromString("d1ca604c-fba1-4011-ae53-2a622f95c1c8"));
		this.name = name;
		
	}
	
	public String getName() {
		return this.name;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		String objName = ((StringMsg)obj).getName();
		return super.equals(obj) && this.name.compareTo(objName)==0;
	}
	/* (non-Javadoc)
	 * @see se.his.drts.message.Message#compareTo(se.his.drts.message.Message)
	 */
	@Override
	public int compareTo(MessagePayload arg0) {
		final int n = super.compareTo(arg0);
		if (n!=0) {
			return n;
		}
		return this.name.compareTo(((StringMsg)arg0).getName());
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TestMessage [name=" + name + ", getUuid()=" + getUuid() + "]";
	}

	
}


