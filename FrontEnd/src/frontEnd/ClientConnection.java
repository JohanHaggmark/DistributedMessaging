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

public class ClientConnection {

	Socket m_socket;
	LinkedBlockingQueue messagesFromClients;
	LinkedBlockingQueue messagesToClient = new LinkedBlockingQueue();

	public ClientConnection(Socket socket, LinkedBlockingQueue<byte[]> messagesFromClients) {
		this.m_socket = socket;
		this.messagesFromClients = messagesFromClients;
		new Thread(new ClientReceiver(m_socket, messagesFromClients)).start();
		new Thread(new ClientSender(m_socket, messagesToClient)).start();
		sendPresentationMessage();
	}

	private void sendPresentationMessage() {
		FrontEnd.logger.debugLog("PRE ClientConnection(), sendPresentationMessage");
		PresentationMessage msg = PresentationMessage.createClientConnectionPresentation(m_socket.toString());
		try {
			FrontEnd.logger.debugLog("POST ClientConnection(), sendPresentationMessage");
			messagesToClient.put(msg);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public LinkedBlockingQueue getLBQ() {
		return messagesToClient;
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
