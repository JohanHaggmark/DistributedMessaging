package replicaManager;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

import org.jgroups.Address;
import org.jgroups.JChannel;

import Logging.ProjectLogger;
import MessageHandling.LocalMessages;
import MessageHandling.RTTMessageRepeater;
import TestingControllability.SemaphoreChannel;
import se.his.drts.message.AbstractMessageTopClass;

public class JGroups {
	public static Address primaryRM = null;
	public static Address frontEnd = null;
	public static Integer id = null;
	public static volatile boolean isCoordinator = false;
	public static LinkedBlockingQueue electionQueue = new LinkedBlockingQueue<AbstractMessageTopClass>();
	public static ProjectLogger logger;


	private JChannel m_channel;
	private LocalMessages m_messages;

	public JGroups() {
		waitForExitMessage();
		this.start();
	}

	private void start() {
		logger = new ProjectLogger("ReplicaManager");
		try {
			new State();
			m_messages = new LocalMessages();
			m_channel = new JChannel();
			new Receiver(m_channel, m_messages).start();
			new Thread(new Sender(m_channel, m_messages)).start();
			new Thread(new RTTMessageRepeater(m_messages.getMessageQueue(), m_messages.getRTTMessageQueue())).start();
			new Thread(new Election(m_channel, m_messages)).start();
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
