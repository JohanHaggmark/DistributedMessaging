package se.his.drts.message;

import java.util.UUID;


public class StringMsg extends AbstractMessageTopClass {

	private static UUID uuid = UUID.fromString("9dbc5c87-9bf2-477a-8437-a2f617f0bad8");
	private String name;
	private String destination = null;
	
	public StringMsg(Object id, String address, String destination) {
		super(uuid);
//		this.is = id;
//		this.destination = destination;
	}
	
	public StringMsg() {
		super(uuid);
	}

	public StringMsg(String name) {
		super(uuid);
		this.name = name;
	}
//
//	public String getName() {
//		return this.name;
//	}

	@Override
	public Object executeInClient() {
		// TODO Auto-generated method stub
		System.out.println("hejsan");
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
		return new Object();
	}
	
	@Override
	public String getDestination() {
		return null;
	}

	@Override
	public UUID getUUID() {
		return uuid;
	}
}
