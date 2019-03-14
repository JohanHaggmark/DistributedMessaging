package frontEnd;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientSender implements Runnable {
	Socket m_socket;
	LinkedBlockingQueue m_messagesToClient;

	public ClientSender(Socket socket, LinkedBlockingQueue<byte[]> messageToClient) {
		this.m_socket = socket;
		m_messagesToClient = messageToClient;
	}

	@Override
	public void run() {
		while (true) {
			OutputStream os;
			try {
				FrontEnd.logger.debugLog("ClientSender() - run() sending message to client");
				os = m_socket.getOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(os);
				oos.writeObject(m_messagesToClient.take());
				FrontEnd.logger.debugLog("ClientSender() - run() sending message to client ------ take()");
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
