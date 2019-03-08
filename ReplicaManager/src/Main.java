import java.util.concurrent.LinkedBlockingQueue;

import org.jgroups.JChannel;

public class Main {
	
	String user_name = System.getProperty("user.name", "n/a");
	

	public static void main(String[] args) throws Exception {
		LinkedBlockingQueue messagesToSender = new LinkedBlockingQueue<String>();
		JChannel channel = new JChannel();
		channel.connect("ChatCluster");
	    new Thread(new Sender(channel, messagesToSender)).start();
		new Receiver(channel, messagesToSender).start();
		new ViewChangedMonitor(channel).start();
	}
}
