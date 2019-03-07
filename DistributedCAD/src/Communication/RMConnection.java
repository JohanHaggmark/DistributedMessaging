package Communication;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import se.his.drts.message.StringMsg;

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendMessage(String msg) {
		try {
			StringMsg m = new StringMsg(msg);
			OutputStream os = m_socket.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(m.serialize());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String receive() {
		InputStream in;
		try {
			in = (InputStream) m_socket.getInputStream();
			return in.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
