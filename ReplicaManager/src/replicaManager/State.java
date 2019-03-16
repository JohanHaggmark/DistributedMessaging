package replicaManager;

import java.util.HashMap;
import java.util.LinkedList;

import DCAD.GObject;
import se.his.drts.message.AbstractMessageTopClass;


public class State {
	private final LinkedList<GObject> objectList = new LinkedList<GObject>();
	
	
//	public HashMap<String, GObject> getMap() {
//		return state;
//	}
	
	public LinkedList<GObject> getList(){
		return objectList;
	}
	
	public void updateState(LinkedList<GObject> state, String name) {
		
		
		//temporary. Dont know how to sync shit yet
		
	}
	
	public void setState(LinkedList<GObject> state) {
		objectList.clear();
		objectList.addAll(state);
//		this.state.clear();
//		this.state.putAll(state);
	}
	
	public static void addDrawObjectsRequest(Object object) {
		//LinkedList<GObject> objectList = (LinkedList<GObject>) object;
	}
	
//	public void setState(InputStream input) throws Exception {
//	    List<String> list;
//	    list=(List<String>)Util.objectFromStream(new DataInputStream(input));
//	    synchronized(state) {
//	        state.clear();
//	        state.addAll(list);
//	    }
//	    System.out.println(list.size() + " messages in chat history):");
//	    for(String str: list)
//	        System.out.println(str);
//	}
}
