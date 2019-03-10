package se.his.drts.message;

import java.util.UUID;


public class StringMsg extends AbstractMessageTopClass {

	private static UUID uuid = UUID.fromString("9dbc5c87-9bf2-477a-8437-a2f617f0bad8");
	private String name;
	
	
	public StringMsg() {
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
	public Object executeInClient() {
		// TODO Auto-generated method stub
		System.out.println("hejsan");
		return new Object();
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
		// TODO Auto-generated method 
		
	}

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
