/**
 *
 * @author brom
 */

package DCAD;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

import Communication.RMConnection;
import Logging.ProjectLogger;
import MessageHandling.LocalMessages;
import MessageHandling.SerializeObject;
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
		messages.addNewMessageWithAcknowledge(new DrawObjectsMessage(SerializeObject.getBytes((Object)state), RMConnection.connectionName));
	}
}