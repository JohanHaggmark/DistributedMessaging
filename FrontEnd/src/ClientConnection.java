import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

import org.json.simple.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

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
				//jackson
				InputStream in = m_socket.getInputStream();
				ObjectInputStream ois = new ObjectInputStream(in);
				ObjectMapper om = new ObjectMapper();
				Object obj = ois.readObject();
				String msg = om.readValue(obj.toString(), String.class);
				
				System.out.println(msg + " i FrontEnd CLientCOnnection 31");
				////
				
				
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
