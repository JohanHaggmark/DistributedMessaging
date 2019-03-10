/**
 *
 * @author brom
 */

package DCAD;

import java.util.LinkedList;

import Communication.Messages;
import Communication.RMConnection;

public class Cad {
	private GUI gui;
	private RMConnection rmConnection;
	private Messages messages;

	public Cad(RMConnection rmc, GUI gui, Messages messages) {
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
