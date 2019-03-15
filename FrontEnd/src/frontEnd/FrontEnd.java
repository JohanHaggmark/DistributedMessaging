package frontEnd;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.jgroups.Address;
import org.jgroups.JChannel;

import Logging.ProjectLogger;
import MessageHandling.LocalMessages;

public class FrontEnd {
	public static ProjectLogger logger;
	public static Address primaryRM = null;

	public static ConcurrentHashMap<String, ClientConnection> m_connectedClients = new ConcurrentHashMap();
	private LinkedBlockingQueue<byte[]> m_messagesFromClients;
	private ServerSocket m_socket;

	public FrontEnd(int portNumber) {
		logger = new ProjectLogger("FrontEnd");
		m_messagesFromClients = new LinkedBlockingQueue<byte[]>();
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
				m_connectedClients.put(clientSocket.toString(),
						new ClientConnection(clientSocket, m_messagesFromClients));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void startJGroupsConnection() {
		try {
			LocalMessages messages = new LocalMessages();
			JChannel channel = new JChannel(); // default config?
			new JGroupsReceiver(channel, messages).start();
			new Thread(new JGroupsSender(channel, m_messagesFromClients)).start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
