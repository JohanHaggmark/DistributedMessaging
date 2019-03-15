package TestingControllability;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ProgramConnection implements Runnable {
	
	private ServerSocket m_socket;	
	private Socket m_clientSocket;
	
	public ProgramConnection(int portNumber) {
		try {
			m_socket = new ServerSocket(portNumber);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		listenForClientConnections();	
		while (true) {
			// Wait for shutdown call
		}
	}

	private void listenForClientConnections() {
		while (true) {
			try {
				m_clientSocket = m_socket.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendShutdownMessage() {
		try {
			byte[] bytes = "gg".getBytes();
			m_clientSocket.getOutputStream().write(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
