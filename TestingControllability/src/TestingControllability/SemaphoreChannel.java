package TestingControllability;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class SemaphoreChannel {
	private Socket m_socket;	
	private Semaphore takeAction = new Semaphore(1);
	
	public SemaphoreChannel(int portNumber) {
		try {
			takeAction.acquire();
			m_socket = new Socket("127.0.0.1", portNumber);
			System.out.println("Lol");			
		} catch (IOException | InterruptedException e) {
			System.out.println("Please continue");
		}		
	}

	public void waitForActionMessage() {
		try {
			InputStream in = m_socket.getInputStream();
			in.read();
		} catch (IOException e) {
			System.out.println("Tester closed");
		}
	}
}
