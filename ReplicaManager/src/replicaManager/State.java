package replicaManager;

import java.util.HashMap;
import java.util.LinkedList;

import MessageHandling.GObject;
import se.his.drts.message.AbstractMessageTopClass;

public class State {

	private final HashMap<String, String> objectMap;

	public State() {
		objectMap = new HashMap<String, String>();
	}

	public void removeObject(String object) {
		objectMap.remove(object);
	}

	public HashMap<String, String> getObjectMap() {
		return objectMap;
	}

	public void setState(HashMap<String, String> map) {
		this.objectMap.clear();
		objectMap.putAll(map);
	}

	public static void addDrawObjectsRequest(Object object) {
		// LinkedList<GObject> objectList = (LinkedList<GObject>) object;
	}

}
