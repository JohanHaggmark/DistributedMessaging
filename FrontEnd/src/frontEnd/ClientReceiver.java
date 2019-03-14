package frontEnd;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientReceiver implements Runnable{
	LinkedBlockingQueue messagesFromClients;
	Socket m_socket;
	
	public ClientReceiver(Socket socket, LinkedBlockingQueue<byte[]> messagesFromClients) {
		this.m_socket = socket;
		this.messagesFromClients = messagesFromClients;
	}
	@Override
	public void run() {
		boolean runThread = true;
		while (runThread) {
			try {
				InputStream in = m_socket.getInputStream();
				DataInputStream din = new DataInputStream(in);
				ObjectInputStream oin = new ObjectInputStream(din);
				messagesFromClients.add(oin.readObject());
				//Object obj = oin.readObject();
				//byte[] bytes = (byte[]) obj;
				FrontEnd.logger.debugLog("Received From Client: ");
				//messagesFromClients.add(bytes);
//				sendAcknowledgeTemp(bytes);
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
