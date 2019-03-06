import org.jgroups.JChannel;

public class Main {
	
	String user_name = System.getProperty("user.name", "n/a");
	

	public static void main(String[] args) throws Exception {
		JChannel channel = new JChannel();
		channel.connect("ChatCluster");
		Sender sender = new Sender(channel);
		new Receiver(channel, sender).start();;
		new ViewChangedMonitor(channel).start();;
	}

}
