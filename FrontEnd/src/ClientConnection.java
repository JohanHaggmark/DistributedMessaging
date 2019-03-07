import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

import DCAD.GObject;
import StrategyPatternMessages.AbstractMessageTopClass;
import StrategyPatternMessages.StringMsg;
import se.his.drts.message.Envelope;
import se.his.drts.message.MessagePayload;
import se.his.drts.message.MessagePayload.IncorrectMessageException;

public class ClientConnection extends Thread {

	Socket m_socket;

	public ClientConnection(Socket socket) {
		this.m_socket = socket;
		this.start();
	}

	@Override
	public void run() {
		boolean runThread = true;
		while (runThread) {
			try {
				// jackson
				InputStream in = m_socket.getInputStream();
				DataInputStream din = new DataInputStream(in);
				ObjectInputStream oin = new ObjectInputStream(din);
				byte[] bytes = (byte[]) oin.readObject();
				Optional<MessagePayload> mpl = MessagePayload.createMessage(bytes);
			
				
				MessagePayload obj = (MessagePayload) mpl.get();
				obj.executeInClient();

			} catch (IOException e) {
				e.printStackTrace();
				runThread = false;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				runThread = false;
			}
		}
	}
}
