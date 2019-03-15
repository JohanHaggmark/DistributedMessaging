package TestingControllability;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ShutdownChannel implements Runnable {
	
	private Socket m_socket;	
	
	public static void startShutdownChannel(int portNumber) {
		new Thread(new ShutdownChannel(portNumber)).start();
	}
	
	public ShutdownChannel(int portNumber) {
		try {
			m_socket = new Socket("127.0.0.1", portNumber);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void run() {
		try {
			InputStream in = m_socket.getInputStream();
			in.read();
			System.exit(0);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
