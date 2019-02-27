/**
 *
 * @author brom
 */

package DCAD;

import java.net.SocketException;
import java.net.UnknownHostException;

import Communication.Messages;
import Communication.RMConnection;

public class Cad {
	private GUI gui;
	private RMConnection rmConnection;
	private Messages messages;

	public Cad(RMConnection rmc, GUI gui) throws UnknownHostException, SocketException {
		this.messages = new Messages();
		this.gui = gui;
		this.rmConnection = rmc;
		gui.addCad(this);
		gui.addToListener();
	}

	public void sendNewObject(GObject obj) {
		messages.addNewMessage(obj);
	}


}
