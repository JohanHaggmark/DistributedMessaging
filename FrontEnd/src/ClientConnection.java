import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Optional;

import org.json.simple.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

import DCAD.GObject;
import StrategyPatternMessages.StringMsg;
import se.his.drts.message.MessagePayload;

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
				MessagePayload m =(MessagePayload) obj;
				
				//GObject msg = om.readValue(obj.toString(), GObject.class);
				
				System.out.println(" X i FrontEnd CLientCOnnection 31");
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
