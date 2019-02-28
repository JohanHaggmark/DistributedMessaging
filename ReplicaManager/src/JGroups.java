import org.jgroups.JChannel;

public class JGroups {

	JChannel channel;
	String user_name = System.getProperty("user.name", "n/a");

	public void start() throws Exception {
		
			channel=new JChannel();
	        channel.connect("ChatCluster");
	}
	
}


