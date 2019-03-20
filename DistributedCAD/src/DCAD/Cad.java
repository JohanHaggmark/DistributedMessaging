/**
 *
 * @author brom
 */

package DCAD;

import java.util.HashMap;

import Communication.RMConnection;
import Logging.ProjectLogger;
import MessageHandling.LocalMessages;
import se.his.drts.message.DrawObjectsMessage;

public class Cad {
	public static ProjectLogger logger = new ProjectLogger("CAD");

	private LocalMessages messages;

	public Cad(GUI gui, LocalMessages messages) {
		this.messages = messages;

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