package Communication;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Optional;

import DCAD.GUI;
import se.his.drts.message.AbstractMessageTopClass;
import se.his.drts.message.MessagePayload;

public class Receiver extends Thread {

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

				msg.executeInClient();

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
