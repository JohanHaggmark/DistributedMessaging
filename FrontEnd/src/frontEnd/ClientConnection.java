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
import se.his.drts.message.PresentationMessage;

public class ClientConnection extends Thread {

	Socket m_socket;
	LinkedBlockingQueue messagesFromClients;

	public ClientConnection(Socket socket, LinkedBlockingQueue<byte[]> messagesFromClients) {
		this.m_socket = socket;
		this.messagesFromClients = messagesFromClients;
		this.start();
		sendPresentationMessage();
	}
	
	private void sendPresentationMessage() {
		PresentationMessage msg = PresentationMessage.createClientConnectionPresentation(m_socket.toString());
		try {
			messagesFromClients.put(msg.serialize());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		boolean runThread = true;
		while (runThread) {
			try {
				InputStream in = m_socket.getInputStream();
				DataInputStream din = new DataInputStream(in);
				ObjectInputStream oin = new ObjectInputStream(din);
				messagesFromClients.add(oin.readObject());
				//Object obj = oin.readObject();
				//byte[] bytes = (byte[]) obj;
				FrontEnd.logger.debugLog("Received From Client: ");
				//messagesFromClients.add(bytes);
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
