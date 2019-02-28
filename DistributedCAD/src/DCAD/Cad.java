/**
 *
 * @author brom
 */

package DCAD;

import java.net.SocketException;
import java.net.UnknownHostException;

import Communication.Messages;
import Communication.RMConnection;
import StrategyPatternMessages.StringMsg;
import se.his.drts.message.MessagePayload;

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

		// messages.addNewMessage(obj);
		// StringMsg hej = new StringMsg("hejsan cad 31");

		rmConnection.sendMessage("hej Cad 34");
	}

}
