package se.his.drts.message;

import java.util.HashMap;
import java.util.UUID;

public class PresentationMessage extends AbstractMessageTopClass {
	
	private static UUID uuid = UUID.fromString("8e69d7fb-4ca9-46de-b33d-cf1dc72377cd");
//	private HashMap<String, String> map = new HashMap();
	private String name;
	private String type;
	
	public static PresentationMessage createClientPresentation(String name) {
		return new PresentationMessage("Client", name);
	}
	
	public static PresentationMessage createClientConnectionPresentation(String name) {
		return new PresentationMessage("ClientConnection", name);
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
//		map = new HashMap();
//		map.put("Type", type);
	}

	public PresentationMessage(String type, String name) {
		super(uuid);
		this.type = type;
		this.name = name;
//		map.put("Type", type);	
//		map.put("Name", name);
	}

	
	public String getName() {
		return this.name;
	}

	@Override
	public Object executeInClient() {
//		return map;
		return type;
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
//		return map;
		return type;
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
