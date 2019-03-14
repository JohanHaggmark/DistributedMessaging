package se.his.drts.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DrawObjectsMessage extends AbstractMessageTopClass {
	private Object infoMap;
	private Object GObjectList;
	private String destination = null;
	private String address;
	private static UUID uuid = UUID.fromString("54f642d7-eaf6-4d62-ad2d-316e4b821c03");
	
	public DrawObjectsMessage(Object objectList, String address, String destination) {
		super(uuid);
		this.GObjectList = objectList;
		this.destination = destination;
		this.address = address;
		
		Map<String, Object> map = new HashMap();
		map.put("Address", address);
		map.put("GObjectList", GObjectList);
		infoMap = map;
	}
	
	public DrawObjectsMessage(Object objectList, String address) {
		super(uuid);
		this.GObjectList = objectList;
		this.address = address;
		
		ArrayList<Object> list = new ArrayList<>();
		list.add(address);
		list.add(objectList);
		infoMap = list;
	}

	public DrawObjectsMessage() {
		super(uuid);
	}
	
	@Override
	public Object executeInClient() {
		return GObjectList;
	}

	@Override
	public Object executeInFrontEndFromClient() {
		// skickavidare till destination
		return null;
	}

	@Override
	public Object executeInFrontEndFromRM() {
		// skickavidare till destination
		return null;
	}

	@Override
	public Object executeInReplicaManager() {
		// spara det ritade state
		// skicka ut meddelandet till FrontEnd
		return infoMap;
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
