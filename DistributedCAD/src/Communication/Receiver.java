package Communication;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;
import java.util.UUID;

import DCAD.Cad;
import DCAD.GObject;
import DCAD.GUI;
import se.his.drts.message.AbstractMessageTopClass;
import se.his.drts.message.LocalMessages;
import se.his.drts.message.MessagePayload;
import se.his.drts.message.PresentationMessage;

public class Receiver implements Runnable {

	RMConnection rmConnection;
	GUI gui;
	LocalMessages m_messages;

	public Receiver(RMConnection rmConnection, GUI gui, LocalMessages messages) {
		this.rmConnection = rmConnection;
		this.gui = gui;
		this.m_messages = messages;
	}

	@Override
	public void run() {
		boolean runThread = true;
		while (runThread) {
			try {
				Cad.logger.debugLog("Trying to read bytes and create a Payload message");
				InputStream in = rmConnection.getSocket().getInputStream();				
				DataInputStream din = new DataInputStream(in);
				ObjectInputStream oin = new ObjectInputStream(din);
				AbstractMessageTopClass msg = (AbstractMessageTopClass) oin.readObject();
				Cad.logger.debugLog("OMG  -  Successfully upacked abstractmessage!!!!!!!!!! " + msg.getUUID());

				// AcknowledgeMessage
				if (msg.getUUID().equals(UUID.fromString("bb5eeb2c-fa66-4e70-891b-382d87b64814"))) {
					m_messages.removeAcknowledgeFromMessage((Integer) msg.executeInClient());
					Cad.logger.debugLog("Received acknowledge");
				}
				// DrawObjectsMessage
				else if (msg.getUUID().equals(UUID.fromString("54f642d7-eaf6-4d62-ad2d-316e4b821c03"))) {
					gui.setObjectList((LinkedList<GObject>) msg.executeInClient());
					Cad.logger.log("Received object");
				}
				// PresentationMessage
				else if (msg.getUUID().equals(UUID.fromString("8e69d7fb-4ca9-46de-b33d-cf1dc72377cd"))) {
					Cad.logger.debugLog("Cad - Receiver --> PRE#SENTATION");
					HashMap<String, String> map = new HashMap();
					Cad.logger.debugLog("PRE#SENTATION 1");
					map = (HashMap<String, String>) msg.executeInClient();
					Cad.logger.debugLog("PRE#SENTATION 2");
					PresentationMessage pms = PresentationMessage.createClientPresentation(map.get("Name"));
					Cad.logger.debugLog("PRE#SENTATION 3");
					Cad.connectionName = map.get("Name");
					Cad.logger.debugLog("PRE#SENTATION 4");
					Cad.hasFrontEnd = true;
					Cad.logger.debugLog("PRE#SENTATION 5");
					m_messages.addNewMessageWithAcknowledge(pms);
					Cad.logger.debugLog("PRE#SENTATION 6");
				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
				runThread = false;
			}
		}
	}
}
