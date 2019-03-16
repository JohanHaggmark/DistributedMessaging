package frontEnd;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import se.his.drts.message.AbstractMessageTopClass;

public class ClientSender implements Runnable {
	private Socket m_socket;
	private LinkedBlockingQueue<AbstractMessageTopClass> m_messagesToClient;

	public ClientSender(Socket socket, LinkedBlockingQueue<AbstractMessageTopClass> messagesToClient) {
		this.m_socket = socket;
		this.m_messagesToClient = messagesToClient;
	}

	@Override
	public void run() {
		try {
			OutputStream os;
			os = m_socket.getOutputStream();
			ObjectOutputStream oos;
			oos = new ObjectOutputStream(os);

			while (true) {
				FrontEnd.logger.debugLog("ClientSender() - run() sending message to client");
				AbstractMessageTopClass msg = m_messagesToClient.take();
				FrontEnd.logger.debugLog("ClientSender() - check type before sending:  " + msg.getUUID());
				oos.writeObject(msg.serialize());
				FrontEnd.logger.debugLog("ClientSender() - run() sending message to client ------ take()");
			}
		} catch (IOException | InterruptedException e) {
			FrontEnd.logger.criticalLog("INTERRUPT IN CLIENTSENDER");	
			FrontEnd.logger.debugLog("INTERRUPT IN CLIENTSENDER");			
			e.printStackTrace();
		}
	}
}
