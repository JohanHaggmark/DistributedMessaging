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
import MessageHandling.LocalMessages;
import MessageHandling.Resender;
import se.his.drts.message.AbstractMessageTopClass;
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

		try {
			InputStream in;
			in = rmConnection.getSocket().getInputStream();
			DataInputStream din = new DataInputStream(in);
			ObjectInputStream oin;
			oin = new ObjectInputStream(din);

			while (runThread) {
				AbstractMessageTopClass msgTopClass = (AbstractMessageTopClass) oin.readObject();
				Cad.logger.debugLog("Received UUID: " + msgTopClass.getUUID());

				// AcknowledgeMessage
				if (msgTopClass.getUUID().equals(UUID.fromString("bb5eeb2c-fa66-4e70-891b-382d87b64814"))) {
					Cad.logger.debugLog("AckID: " + msgTopClass.getackID());
					for(Integer key : m_messages.getMapOfMessages().keySet()) {
						Cad.logger.debugLog("AckID in map: " + key);
					}
					m_messages.removeAcknowledgeFromMessage((Integer) msgTopClass.getackID());
				}
				// DrawObjectsMessage
				else if (msgTopClass.getUUID().equals(UUID.fromString("54f642d7-eaf6-4d62-ad2d-316e4b821c03"))) {
					gui.setObjectList((LinkedList<GObject>) msgTopClass.executeInClient());
				}
				// PresentationMessage
				else if (msgTopClass.getUUID().equals(UUID.fromString("8e69d7fb-4ca9-46de-b33d-cf1dc72377cd"))) {
					String type = (String) msgTopClass.getType();
					if (type.equals("ClientConnection")) {
						rmConnection.hasFrontEnd = true;
						String name = (String) msgTopClass.getName();
						rmConnection.connectionName = name;
						m_messages.addNewMessageWithAcknowledge(PresentationMessage.createClientPresentation(name));
						startResender();
					} 
					else {
						Cad.logger.debugLog(
								"Cad should not be receiving PresentationMessages from other than ClientConnection");
					}
				} else {
					Cad.logger.debugLog("UNKNOWN UUID: " + msgTopClass.getUUID());
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			RMConnection.hasFrontEnd = false;
			RMConnection.connectionName = null;
		}

	}

	private void startResender() {
		new Thread(new Resender(
				m_messages.getMessagesToResender(),
				m_messages.getMessageQueue(),
				RMConnection.hasFrontEnd)).start();
	}
}
