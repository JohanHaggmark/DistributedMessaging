package replicaManager;

import java.util.concurrent.LinkedBlockingQueue;

import org.jgroups.Address;
import org.jgroups.JChannel;

import Logging.ProjectLogger;
import MessageHandling.LocalMessages;
import MessageHandling.RTMessageRepeater;
import TestingControllability.SemaphoreChannel;
import se.his.drts.message.AbstractMessageTopClass;

public class JGroups {
	public static Address primaryRM = null;
	public static volatile boolean isCoordinator = false;
	public static LinkedBlockingQueue<AbstractMessageTopClass> electionQueue = new LinkedBlockingQueue<AbstractMessageTopClass>();
	public static ProjectLogger logger;
	public static Address frontEnd = null;
	
	private JChannel m_channel;
	private LocalMessages m_messages;

	public JGroups(String arg) {
		if(arg.equals("ReplicaManager")) {
			//waitForExitMessage();
			start();			
		}
	}

	private void start() {
		logger = new ProjectLogger("ReplicaManager");
		try {
			m_messages = new LocalMessages();
			m_channel = new JChannel();
			new Receiver(m_channel, m_messages).start();
			new Thread(new Sender(m_channel, m_messages)).start();
			new Thread(new RTMessageRepeater(m_messages.getMessageQueue(), m_messages.getRTTMessageQueue())).start();
			new Thread(new Election(m_channel)).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void waitForExitMessage() {
		new Thread() {
			@Override
			public void run() {
				SemaphoreChannel channel = new SemaphoreChannel(28000);
				channel.waitForActionMessage();
				System.exit(0);
			}
		}.start();
	}
}
