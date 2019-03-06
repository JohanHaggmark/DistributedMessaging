package Communication;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import DCAD.GObject;
import StrategyPatternMessages.StringMsg;
import se.his.drts.message.Envelope;
import se.his.drts.message.MessagePayload;
import se.his.drts.message.MessagePayload.IncorrectMessageException;

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
				Envelope envelope = new Envelope(m);
				OutputStream os = m_socket.getOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(os);
				
				oos.writeObject(envelope);
			
				
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(Optional<MessagePayload> msg) {
		try {
			ObjectMapper om = new ObjectMapper();
			String serializedMessage;
			serializedMessage = om.writeValueAsString(msg);
			OutputStream os = m_socket.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(serializedMessage);
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
