package frontEnd;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class FrontEnd {
	ArrayList<ClientConnection> m_connectedClients = new ArrayList<ClientConnection>();
	LinkedBlockingQueue messagesFromClients;

	private ServerSocket m_socket;

	public FrontEnd(int portNumber) {
		messagesFromClients = new LinkedBlockingQueue<byte[]>();
		startJGroupsConnection();

		try {
			m_socket = new ServerSocket(portNumber);
			listenForClientConnections();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void listenForClientConnections() {
		while (true) {
			Socket clientSocket;
			try {
				clientSocket = m_socket.accept();
				m_connectedClients.add(new ClientConnection(clientSocket, messagesFromClients));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void startJGroupsConnection() {
		new Thread(new JGroupsConnection(messagesFromClients)).start();
	}
}
