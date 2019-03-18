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
import MessageHandling.GObjectFactory;
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

	public void sendRemove(String stringGObject) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(stringGObject, "remove");
		messages.addNewMessageWithAcknowledgeInCad(new DrawObjectsMessage(map, RMConnection.connectionName));
	}

	public void sendAdd(String stringGObject) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(stringGObject, "add");
		messages.addNewMessageWithAcknowledgeInCad(new DrawObjectsMessage(map, RMConnection.connectionName));
	}
}