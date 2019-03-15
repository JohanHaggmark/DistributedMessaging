package replicaManager;

import java.util.LinkedList;

import DCAD.GObject;


public class State {
	private static LinkedList<GObject> objectList = new LinkedList<GObject>();
	
	
	public static void addDrawObjectsRequest(Object object) {
		LinkedList<GObject> objectList = (LinkedList<GObject>) object;
	}
}
