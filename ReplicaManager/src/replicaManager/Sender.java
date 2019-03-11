package replicaManager;

import java.util.concurrent.LinkedBlockingQueue;

import org.jgroups.JChannel;
import org.jgroups.Message;

import se.his.drts.message.AbstractMessageTopClass;
import se.his.drts.message.LocalMessage;
import se.his.drts.message.LocalMessages;

public class Sender implements Runnable {

	JChannel m_channel;
	LinkedBlockingQueue<AbstractMessageTopClass> messagesToSender;
	LocalMessages m_messages;
	private static int ATTEMPTS = 10;

	public Sender(JChannel channel, LocalMessages messages) {
		this.m_channel = channel;
		this.messagesToSender = messagesToSender;
		this.m_messages = messages;

	}

	public void receiveFromJGroupsStub(String message) {
		// L�GG TILL N�GON FORM AV LBQ H�R IST�LLET F�R EN STUB EFTERSOM DET BLIR CP ATT
		// RECEIVER SKA HA KOLL P� DETTA OBJEKTET
	}

	@Override
	public void run() {
		while (true) {
			try {
				LocalMessage msg = (LocalMessage) m_messages.getMessageQueue().take();
				if (!msg.isAcknowledge && msg.getAttempt() < ATTEMPTS) {
					m_channel.send(new Message(null, msg.getMsgTopClass()));
					msg.incrementAttempt();
					m_messages.addToRTTMessageQueue(msg);
				}
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
