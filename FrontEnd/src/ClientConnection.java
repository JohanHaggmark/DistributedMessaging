import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;

import se.his.drts.message.AbstractMessageTopClass;
import se.his.drts.message.MessagePayload;


public class ClientConnection extends Thread {

	Socket m_socket;
	LinkedBlockingQueue messages;
	public ClientConnection(Socket socket, LinkedBlockingQueue<AbstractMessageTopClass> messages) {
		this.m_socket = socket;
		this.messages = messages;
		this.start();
	}

	@Override
	public void run() {
		boolean runThread = true;
		while (runThread) {
			try {
				InputStream in = m_socket.getInputStream();
				DataInputStream din = new DataInputStream(in);
				ObjectInputStream oin = new ObjectInputStream(din);
				byte[] bytes = (byte[]) oin.readObject();
				//Optional<MessagePayload> opt = MessagePayload.createMessage(bytes);
				//AbstractMessageTopClass msg = (AbstractMessageTopClass) opt.get();
				
				//Give message to jgroups thread!
				//messages.add(msg);
				//Vi vill ju inte packa upp skiten här egentligen.
				messages.add(bytes);

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
