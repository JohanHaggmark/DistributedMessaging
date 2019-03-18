package replicaManager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;

import org.jgroups.Address;

import se.his.drts.message.DrawObjectsMessage;

public class State implements Serializable{

	private final LinkedList<String> clientList;
	private final LinkedList<String> objectList;

	public State() {
		objectList = new LinkedList<String>();
		clientList = new LinkedList<String>();
	}
	
	public void addObject(String object) {
		objectList.addLast(object);
	}

	public void removeObject(String object) {
		objectList.remove(object);
	}

	public LinkedList<String> getObjectList() {
		return objectList;
	}

	public void setState(LinkedList<String> list) {
		objectList.clear();
		objectList.addAll(list);
	}
	
	public DrawObjectsMessage getStateMessage(String name) {
		HashMap<String, String> objects = new HashMap();
		for(String string : objectList) {
			objects.put(string, "add");
		}
		return new DrawObjectsMessage(objects, name);
	}

	public LinkedList<String> getClients() {
		return clientList;
	}
}
