import org.jgroups.JChannel;
import org.jgroups.Message;

public class Sender {
	
	JChannel m_channel;

	
	public Sender(JChannel channel) {
		this.m_channel = channel;
	}
	
	public void receiveFromJGroupsStub(String message) {
		// L�GG TILL N�GON FORM AV LBQ H�R IST�LLET F�R EN STUB EFTERSOM DET BLIR CP ATT RECEIVER SKA HA KOLL P� DETTA OBJEKTET
		this.send(message);
	}

	private void send(String message) {
		Message msg = new Message(null, message.getBytes());
		try {
			m_channel.send(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
