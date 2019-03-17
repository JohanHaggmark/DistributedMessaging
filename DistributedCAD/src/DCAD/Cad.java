/**
 *
 * @author brom
 */

package DCAD;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

import Communication.RMConnection;
import Logging.ProjectLogger;
import MessageHandling.GObject;
import MessageHandling.LocalMessages;
import se.his.drts.message.DrawObjectsMessage;

public class Cad {
	public static ProjectLogger logger = new ProjectLogger("CAD");
	
	public static LinkedBlockingQueue<LinkedList<GObject>> resendQueue = new LinkedBlockingQueue();
	private GUI gui;
	private RMConnection rmConnection;
	private LocalMessages messages;

	public Cad(RMConnection rmc, GUI gui, LocalMessages messages) {
		this.messages = messages;
		this.gui = gui;
		this.rmConnection = rmc;
		gui.addCad(this);
		gui.addToListener();
	}

	public void sendState(State state) {
		Cad.logger.debugLog("sendState() - adding to message queue");
	//	messages.addNewMessageWithAcknowledge(new DrawObjectsMessage(state, RMConnection.connectionName));
	}
	
	public void sendRemove(GObject object) {
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("shape", "test");
		
		messages.addNewMessageWithAcknowledge(new DrawObjectsMessage(map, RMConnection.connectionName));
	}
	
	public void sendAdd(GObject object) {
		
	}
}