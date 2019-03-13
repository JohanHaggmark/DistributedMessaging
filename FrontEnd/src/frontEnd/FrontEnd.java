package frontEnd;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import org.jgroups.Address;
import org.jgroups.JChannel;

import Logging.ProjectLogger;
import se.his.drts.message.LocalMessages;

public class FrontEnd {
	public static ProjectLogger logger;
	public static Address primaryRM;
	
	private ArrayList<ClientConnection> m_connectedClients = new ArrayList<ClientConnection>();
	private LinkedBlockingQueue m_messagesFromClients;
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
				m_connectedClients.add(new ClientConnection(clientSocket, m_messagesFromClients));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void startJGroupsConnection() {
		try {
			LocalMessages messages = new LocalMessages();
			JChannel channel = new JChannel(); 	//default config?
			new JGroupsReceiver(channel, messages).start();
			new Thread(new JGroupsSender(channel, m_messagesFromClients)).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
