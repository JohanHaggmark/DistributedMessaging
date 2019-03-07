import org.jgroups.JChannel;
import org.jgroups.Message;

public class Sender {
	
	JChannel m_channel;

	
	public Sender(JChannel channel) {
		this.m_channel = channel;
	}
	
	public void receiveFromJGroupsStub(String message) {
		// LÄGG TILL NÅGON FORM AV LBQ HÄR ISTÄLLET FÖR EN STUB EFTERSOM DET BLIR CP ATT RECEIVER SKA HA KOLL PÅ DETTA OBJEKTET
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
