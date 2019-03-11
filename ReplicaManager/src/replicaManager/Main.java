package replicaManager;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

import org.jgroups.JChannel;

public class Main {
	
	String user_name = System.getProperty("user.name", "n/a");
	

	public static void main(String[] args) throws Exception {
		Messages messages = new Messages();
		LinkedBlockingQueue messagesToSender = new LinkedBlockingQueue<String>();
		JChannel channel = new JChannel();
		new Receiver(channel, messages).start();
	    new Thread(new Sender(channel, messages)).start();	
	    new Thread(new RTTMessageRepeater(messages.getMessageQueue(), messages.getRTTMessageQueue())).start();
	}
}
