package Communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import se.his.drts.message.Envelope;


public class RMConnection {
	private final InetAddress m_serverAddress;
	private final int m_serverPort;
	private final DatagramSocket m_socket;
	
	
	public RMConnection(String address, int port) throws UnknownHostException, SocketException {
		m_serverAddress = InetAddress.getByName(address);
		m_serverPort = port;
		
		m_socket = new DatagramSocket();
	}
	
	public void sendMessage(Envelope msg) {
		byte[] buffer = new byte[msg.getSerializedMessage().length];
		DatagramPacket dp = new DatagramPacket(buffer, buffer.length, m_serverAddress, m_serverPort);
		try {
			m_socket.send(dp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String receive() throws IOException {
		byte[] buf = new byte[1024];
		DatagramPacket dp = new DatagramPacket(buf, buf.length);
		m_socket.receive(dp);
		String message = new String(dp.getData(), 0, dp.getLength());
		System.out.println(message);
		return message;
	}
}
