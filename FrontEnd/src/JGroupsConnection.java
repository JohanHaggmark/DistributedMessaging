import java.util.concurrent.LinkedBlockingQueue;

import org.jgroups.JChannel;

import se.his.drts.message.AbstractMessageTopClass;

public class JGroupsConnection implements Runnable{

	JChannel channel;
	LinkedBlockingQueue<byte[]> messages;
	public JGroupsConnection(LinkedBlockingQueue<byte[]> messages) {
		this.messages = messages;
	}
	@Override
	public void run() {
		try {
			channel = new JChannel();  //default config?
			channel.connect("cluster");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private class Sender implements Runnable{

		@Override
		public void run() {
			while(true) {
				//måste ta reda på vilken replica manager som ska ha meddelandet.
				channel.send("Addresss", messages.take());
			}		
		}	
	}
}
