package Communication;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.Optional;
import java.util.UUID;

import DCAD.GObject;
import DCAD.GUI;
import se.his.drts.message.AbstractMessageTopClass;
import se.his.drts.message.MessagePayload;

public class Receiver implements Runnable {

	RMConnection rmConnection;
	GUI gui;
	Messages messages;

	public Receiver(RMConnection rmConnection, GUI gui, Messages messages) {
		this.rmConnection = rmConnection;
		this.gui = gui;
		this.messages = messages;
	}

	@Override
	public void run() {
		boolean runThread = true;
		while (runThread) {
			try {
				// jackson
				InputStream in = rmConnection.getSocket().getInputStream();
				DataInputStream din = new DataInputStream(in);
				ObjectInputStream oin = new ObjectInputStream(din);
				byte[] bytes = (byte[]) oin.readObject();
				Optional<MessagePayload> opt = MessagePayload.createMessage(bytes);
				AbstractMessageTopClass msg = (AbstractMessageTopClass) opt.get();

				// if message is an acknowledge message
				if (msg.getUUID().equals(UUID.fromString("bb5eeb2c-fa66-4e70-891b-382d87b64814"))) {
					messages.acknowledgeMessage((Integer) msg.executeInClient());
					System.out.println("acknowledged Receiver 42");
				}
				// if messages is a draw message
				else if (msg.getUUID().equals(UUID.fromString("54f642d7-eaf6-4d62-ad2d-316e4b821c03"))) {
					gui.setObjectList((LinkedList<GObject>) msg.executeInClient());
				}
			} catch (IOException e) {
				e.printStackTrace();
				runThread = false;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				runThread = false;
			}
		}
	}
}
