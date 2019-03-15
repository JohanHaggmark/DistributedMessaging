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
				AbstractMessageTopClass msgTopClass = (AbstractMessageTopClass) oin.readObject();
				Cad.logger.debugLog("OMG  -  Successfully upacked abstractmessage!!!!!!!!!! " + msgTopClass.getUUID());

				// AcknowledgeMessage
				if (msgTopClass.getUUID().equals(UUID.fromString("bb5eeb2c-fa66-4e70-891b-382d87b64814"))) {
					m_messages.removeAcknowledgeFromMessage((Integer) msgTopClass.executeInClient());
					Cad.logger.debugLog("Received acknowledge");
				}
				// DrawObjectsMessage
				else if (msgTopClass.getUUID().equals(UUID.fromString("54f642d7-eaf6-4d62-ad2d-316e4b821c03"))) {
					gui.setObjectList((LinkedList<GObject>) msgTopClass.executeInClient());
					Cad.logger.log("Received object");
				}
				// PresentationMessage
				else if (msgTopClass.getUUID().equals(UUID.fromString("8e69d7fb-4ca9-46de-b33d-cf1dc72377cd"))) {
					Cad.logger.debugLog("Cad - Receiver --> PRESENTATION");
					String type = (String) msgTopClass.getType();
					if(type.equals("ClientConnection")) {
						Cad.hasFrontEnd = true;
						String name = (String) msgTopClass.getName();
						m_messages.addNewMessageWithAcknowledge(PresentationMessage.createClientPresentation(name));						
					}
					else {
						Cad.logger.debugLog("Cad should not be receiving PresentationMessages from other than ClientConnection");
					}
				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
				runThread = false;
			}
		}
	}
}
