package frontEnd;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;

import se.his.drts.message.AbstractMessageTopClass;
import se.his.drts.message.AcknowledgeMessage;
import se.his.drts.message.MessagePayload;

public class ClientConnection extends Thread {

	Socket m_socket;
	LinkedBlockingQueue messagesFromClients;

	public ClientConnection(Socket socket, LinkedBlockingQueue<byte[]> messagesFromClients) {
		this.m_socket = socket;
		this.messagesFromClients = messagesFromClients;
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
				messagesFromClients.add(bytes);
//				sendAcknowledgeTemp(bytes);
			} catch (IOException e) {
				e.printStackTrace();
				runThread = false;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				runThread = false;
			}
		}
	}

	// TEMPORARY METHOD TO CHECK CAD ACKNOWLEDGE
//	private void sendAcknowledgeTemp(byte[] bytes) {
//		Optional<MessagePayload> opt = MessagePayload.createMessage(bytes);
//		AbstractMessageTopClass msg = (AbstractMessageTopClass) opt.get();
//		try {
//			OutputStream os = m_socket.getOutputStream();
//			ObjectOutputStream oos = new ObjectOutputStream(os);
//			oos.writeObject(new AcknowledgeMessage(msg.getId()).serialize());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
}
