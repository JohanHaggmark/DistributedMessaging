import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;

public class Receiver extends ReceiverAdapter {

	JChannel m_channel;
	Sender m_sender;
	
	
	public Receiver(JChannel channel, Sender sender) {
		this.m_channel = channel;
		this.m_sender = sender;
	}
	
	public void start() throws Exception {
		m_channel.setReceiver(this);
	}
	
	public void receive(Message msg) {
		// receive message from frontend and send response
	    System.out.println(msg.getSrc() + ": " + msg.getObject());
	    this.send(msg.getObject());
	}
	
	private void send(String message) {
		// L�GG TILL N�GON FORM AV LBQ H�R IST�LLET F�R EN STUB EFTERSOM DET BLIR CP ATT RECEIVER SKA HA KOLL P� SENDER
		m_sender.receiveFromJGroupsStub(message);
	}
}
