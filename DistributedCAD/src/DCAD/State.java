package DCAD;

import java.io.Serializable;
import java.util.LinkedList;

import MessageHandling.GObject;

public class State implements Serializable {

	LinkedList<GObject> objectList;
	
	public State(LinkedList<GObject> objectList) {
	this.objectList = objectList;	
	}
	
	public LinkedList<GObject> getObjectList() {
		return objectList;
	}
	
	public void set(LinkedList<GObject> objectList) {
		this.objectList = objectList;
	}

	public void setObjectList(LinkedList<GObject> objectList) {
		this.objectList = objectList;
	}
}
