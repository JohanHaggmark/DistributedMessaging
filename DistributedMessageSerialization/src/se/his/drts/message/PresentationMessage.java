package se.his.drts.message;

import java.util.UUID;

public class PresentationMessage extends AbstractMessageTopClass {
	
	private static UUID uuid = UUID.fromString("8e69d7fb-4ca9-46de-b33d-cf1dc72377cd");
	
	public static PresentationMessage createClientPresentation(String name) {
		return new PresentationMessage("Client", name);
	}
	
	public static PresentationMessage createClientConnectionPresentation(String name) {
		return new PresentationMessage("ClientConnection", name);
	}

	public static PresentationMessage createFrontEndPresentation() {
		return new PresentationMessage("FrontEnd");
	}

	public static PresentationMessage createReplicaManagerPresentation() {
		return new PresentationMessage("ReplicaManager");
	}
	
	public PresentationMessage(String type, String name) {
		super(uuid);
		this.type = type;
		this.name = name;
	}

	public PresentationMessage(String type) {
		super(uuid);
		this.type = type;
	}
	
	public PresentationMessage() {
		super(uuid);
	}

	@Override
	public Object executeInClient() {
		return null;
	}

	@Override
	public Object executeInFrontEnd() {
		return null;
	}

	@Override
	public Object executeInReplicaManager() {		
		return type;
	}

	@Override
	public UUID getUUID() {
		return uuid;
	}
}
