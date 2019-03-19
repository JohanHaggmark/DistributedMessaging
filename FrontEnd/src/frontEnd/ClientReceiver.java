package frontEnd;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientReceiver implements Runnable{
	private LinkedBlockingQueue<byte[]> messagesFromClients;
	private Socket m_socket;
	
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
				FrontEnd.logger.debugLog("ClientReceiver() - Trying to read bytes from client");	
				byte[] bytes = (byte[]) oin.readObject();
				FrontEnd.logger.debugLog("ClientReceiver() - Successfully read bytes from client");				
				messagesFromClients.add(bytes);
			} catch (IOException | ClassNotFoundException e) {
				FrontEnd.logger.debugLog("EXCEPTION IN CLIENTRECEIVER");
				e.printStackTrace();
				runThread = false;
			} 
		}
	}
}
