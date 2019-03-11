/**
 *
 * @author brom
 */

package DCAD;

import java.util.LinkedList;

import Communication.RMConnection;
import se.his.drts.message.LocalMessages;

public class Cad {
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
		messages.addNewMessage(objectList);
	}
}
