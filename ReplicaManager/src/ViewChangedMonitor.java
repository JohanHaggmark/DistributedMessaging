import org.jgroups.JChannel;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;

public class ViewChangedMonitor extends ReceiverAdapter {
	
	JChannel m_channel;
	

	public ViewChangedMonitor(JChannel channel) {
		this.m_channel = channel;
	}
	
	public void start() throws Exception {
		m_channel.setReceiver(this);
	}

	public void viewAccepted(View new_view) {
		System.out.println("view " + new_view);
	}
	
}
