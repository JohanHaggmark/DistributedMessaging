/**
 *
 * @author brom
 */

package DCAD;

import java.util.LinkedList;

import Communication.RMConnection;
import Logging.ProjectLogger;
import se.his.drts.message.DrawObjectsMessage;
import se.his.drts.message.LocalMessages;

public class Cad {
	public static ProjectLogger logger = new ProjectLogger("CAD");
	
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

	public void sendState(LinkedList<GObject> objectList) {
		messages.addNewMessageWithAcknowledge(new DrawObjectsMessage(objectList));
	}
}
