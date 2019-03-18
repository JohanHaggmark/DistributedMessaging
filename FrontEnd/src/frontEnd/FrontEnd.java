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
import TestingControllability.SemaphoreChannel;
import se.his.drts.message.PresentationMessage;

public class FrontEnd {
	public static ProjectLogger logger;
	public static Address primaryRM = null;

	public static ConcurrentHashMap<String, ClientConnection> m_connectedClients = new ConcurrentHashMap();
	private LinkedBlockingQueue<byte[]> m_messagesFromClients;
	private ServerSocket m_socket;
	private JChannel m_channel;

	public FrontEnd() {
		waitForExitMessage();
		logger = new ProjectLogger("FrontEnd");
		m_messagesFromClients = new LinkedBlockingQueue<byte[]>();
		startJGroupsConnection();
		try {
			m_socket = new ServerSocket(25000);
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
			m_channel = new JChannel(); // default config?
			new JGroupsReceiver(m_channel, messages).start();
			new Thread(new JGroupsSender(m_channel, m_messagesFromClients)).start();
			m_channel.send(null, PresentationMessage.createFrontEndPresentation().serialize());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void waitForExitMessage() {
		new Thread() {
			@Override
			public void run() {
				SemaphoreChannel channel = new SemaphoreChannel(27000);
				channel.waitForActionMessage();
				shutdownConnections();
				try {
					m_socket.close();
				} catch (IOException e) {
					System.exit(-1);
					e.printStackTrace();
				}
				System.exit(0);
			}
		}.start();
	}

	private void shutdownConnections() {
		new Thread() {
			@Override
			public void run() {
				while (true) {
					for (String key : m_connectedClients.keySet()) {
						m_connectedClients.get(key).closeSocket();
					}
				}
			}
		}.start();
	}
}
