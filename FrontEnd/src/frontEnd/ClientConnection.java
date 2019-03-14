package frontEnd;

import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

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

}
