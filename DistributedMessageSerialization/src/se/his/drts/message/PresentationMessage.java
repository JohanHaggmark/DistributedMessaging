package se.his.drts.message;

import java.util.UUID;

public class PresentationMessage extends AbstractMessageTopClass {
	
	private static UUID uuid = UUID.fromString("8e69d7fb-4ca9-46de-b33d-cf1dc72377cd");
	private String name;
	private String type;
	
	public static PresentationMessage createReplicaManagerPresentation() {
		return new PresentationMessage("ReplicaManager");
	}

	public static PresentationMessage createFrontEndPresentation() {
		return new PresentationMessage("FrontEnd");
	}
	
	public PresentationMessage() {
		super(uuid);
	}

	public PresentationMessage(String type) {
		super(uuid);
		this.type = type;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public Object executeInClient() {
		// TODO Auto-generated method stub
		return new Object();
	}

	@Override
	public Object executeInFrontEndFromRM() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object executeInFrontEndFromClient() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object executeInReplicaManager() {
		return this.type;
	}

	@Override
	public boolean equals(Object obj) {
		String objName = ((PresentationMessage)obj).getName();
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
		return this.name.compareTo(((PresentationMessage)arg0).getName());
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TestMessage [name=" + name + ", getUuid()=" + getUuid() + "]";
	}

	@Override
	public UUID getUUID() {
		return uuid;
	}

	@Override
	public String getDestination() {
		return null;
	}
}
