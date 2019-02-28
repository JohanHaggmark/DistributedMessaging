import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class FrontEnd {
	ArrayList<ClientConnection> m_connectedClients = new ArrayList<ClientConnection>();
	private ServerSocket m_socket;

	public FrontEnd(int portNumber) {

		try {
			m_socket = new ServerSocket(portNumber);
			listenForClientConnections();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// startJGroups();

	}

	private void listenForClientConnections() {
		while (true) {
			Socket clientSocket;
			try {
				clientSocket = m_socket.accept();
				m_connectedClients.add(new ClientConnection(clientSocket));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
