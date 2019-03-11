package replicaManager;
import org.jgroups.JChannel;

import se.his.drts.message.LocalMessages;

public class Main {
	
	String user_name = System.getProperty("user.name", "n/a");
	

	public static void main(String[] args) throws Exception {
		LocalMessages messages = new LocalMessages();
		JChannel channel = new JChannel();
		new Receiver(channel, messages).start();
	    new Thread(new Sender(channel, messages)).start();	
	    new Thread(new RTTMessageRepeater(messages.getMessageQueue(), messages.getRTTMessageQueue())).start();
	}
}
