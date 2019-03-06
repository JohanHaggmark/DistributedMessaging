import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;

public class Receiver extends ReceiverAdapter{

	JChannel channel;
	
	
	
	private void start() throws Exception {
		channel = new JChannel();
		channel.setReceiver(this);
		channel.connect("Replica Cluster");
		//eventLoop();
	}
	
	public void viewAccepted(View new_view) {
		System.out.println("view " + new_view);
	}
	
	public void receive(Message msg) {
		
	}
}
