package se.his.drts.message;

import java.util.UUID;

public class PresentationMessage extends AbstractMessageTopClass {
	
	private static UUID uuid = UUID.fromString("8e69d7fb-4ca9-46de-b33d-cf1dc72377cd");
//	private String name;
//	private String type;
	private String string;
	private String destination = null;
	
	public static PresentationMessage createClientPresentation(String name) {
		return new PresentationMessage("Client", name);
	}
	
	public static PresentationMessage createClientConnectionPresentation(String name) {
		return new PresentationMessage("ClientConnection", name);
	}

	public static PresentationMessage createFrontEndPresentation() {
		return new PresentationMessage("FrontEnd", 1);
	}
	
	public PresentationMessage() {
		super(uuid);
	}

	public PresentationMessage(String type, Integer fuck) {
		super(uuid);
//		this.type = type;
		this.string = "G�ran";
	}

	public PresentationMessage(String type, String name) {
		super(uuid);
//		this.type = type;
		string = "G�ran";
	}
	
	public PresentationMessage(Integer fisk) {
		super(uuid);
		string = "niklas";
	}

	@Override
	public Object executeInClient() {
		return null;
	}

	@Override
	public Object executeInFrontEndFromRM() {
		return null;
	}

	@Override
	public Object executeInFrontEndFromClient() {
		return null;
	}

	@Override
	public Object executeInReplicaManager() {		
		return null;
	}

	@Override
	public UUID getUUID() {
		return uuid;
	}

	@Override
	public String getDestination() {
		return string;
	}
}
