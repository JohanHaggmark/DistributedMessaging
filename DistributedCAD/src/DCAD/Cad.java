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
		try {
			Object object = Class.forName(State.class.getName()).cast(state);
			messages.addNewMessageWithAcknowledge(new DrawObjectsMessage(object, RMConnection.connectionName));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}