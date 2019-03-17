package se.his.drts.message;

import java.util.UUID;

public class DrawObjectsMessage extends AbstractMessageTopClass {
	
	private static UUID uuid = UUID.fromString("54f642d7-eaf6-4d62-ad2d-316e4b821c03");
	Object m_state;


	public DrawObjectsMessage(Object state, String name) {
		super(uuid);
		//this.state = state;
		this.name = name;
		m_state = state;
	}

	public DrawObjectsMessage() {
		super(uuid);
	}
	
	@Override
	public Object executeInClient() {
		return this.state;
	}

	@Override
	public Object executeInFrontEnd() {
		return null;
	}

	@Override
	public Object executeInReplicaManager() {
		// spara det ritade state
		// skicka ut meddelandet till FrontEnd
		return this.state;
	}
	
	@Override
	public UUID getUUID() {
		return uuid;
	}
	
	public Object getM_state() {
		return m_state;
	}

	public void setM_state(Object m_state) {
		this.m_state = m_state;
	}
}
