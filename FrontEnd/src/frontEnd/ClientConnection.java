package frontEnd;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import se.his.drts.message.AbstractMessageTopClass;
import se.his.drts.message.PresentationMessage;

public class ClientConnection {

	private Socket m_socket;
	private LinkedBlockingQueue<byte[]> m_messagesFromClients;
	private LinkedBlockingQueue<AbstractMessageTopClass> m_messagesToClient = new LinkedBlockingQueue();

	public ClientConnection(Socket socket, LinkedBlockingQueue<byte[]> messagesFromClients) {
		this.m_socket = socket;
		this.m_messagesFromClients = messagesFromClients;
		new Thread(new ClientReceiver(m_socket, messagesFromClients)).start();
		new Thread(new ClientSender(m_socket, m_messagesToClient)).start();
		sendPresentationMessage();
	}

	private void sendPresentationMessage() {
		PresentationMessage msg = PresentationMessage.createClientConnectionPresentation(m_socket.toString());
		try {
			m_messagesToClient.put(msg);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void addMessageToClient(AbstractMessageTopClass msgTopClass) {
		m_messagesToClient.add(msgTopClass);
	}
	
	public void closeSocket() {
		try {
			m_socket.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		System.exit(0);
	}
}
