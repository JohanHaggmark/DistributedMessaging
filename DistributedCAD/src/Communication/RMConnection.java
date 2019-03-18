package Communication;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Semaphore;

import DCAD.MainDCAD;
import TestingControllability.SemaphoreChannel;
import se.his.drts.message.AbstractMessageTopClass;

public class RMConnection implements Runnable {
	private InetAddress m_serverAddress;
	private int m_serverPort;
	private Socket m_socket = null;
	private Semaphore m_connected = new Semaphore(1);
	private int m_timeOut = 8;
	public static String connectionName = null;

	public RMConnection(String address, int port) {
		//waitForExitMessage();
		try {
			this.m_serverAddress = InetAddress.getByName(address);
			this.m_serverPort = port;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(AbstractMessageTopClass msg) {
		try {
			OutputStream os = m_socket.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(msg.serialize());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Socket getSocket() {
		return m_socket;
	}

	@Override
	public void run() {
		while (true) {
			try {
				m_connected.acquire();
				m_socket = new Socket(m_serverAddress, m_serverPort);
				MainDCAD.resetConnection();
				m_timeOut = 8;
			} catch (IOException | InterruptedException e) {
				connectionName = null;
				System.out.println("Waiting for... " + m_timeOut + " milliseconds");
				try {
					Thread.sleep(m_timeOut);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				m_timeOut *= 2;
				m_connected.release();
				e.printStackTrace();
			}
		}
	}

	public void releaseSem() {
		m_connected.release();
	}

	private void waitForExitMessage() {
		new Thread() {
			@Override
			public void run() {
				SemaphoreChannel channel = new SemaphoreChannel(26000);
				channel.waitForActionMessage();
				try {
					m_socket.close();
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(-1);
				}
				System.exit(0);
			}
		}.start();
	}
}
