package Communication;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import se.his.drts.message.AbstractMessageTopClass;

public class RMConnection {
	private InetAddress m_serverAddress;
	private int m_serverPort;
	private Socket m_socket = null;

	public RMConnection(String address, int port) {
		try {
			m_serverAddress = InetAddress.getByName(address);
			m_serverPort = port;
			m_socket = new Socket(address, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(AbstractMessageTopClass msg) {
		try {	
			OutputStream os = m_socket.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(msg.serialize());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Socket getSocket() {
		return m_socket;
	}
}
