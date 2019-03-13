package replicaManager;

import java.util.concurrent.LinkedBlockingQueue;

import org.jgroups.Address;
import org.jgroups.JChannel;

import Logging.ProjectLogger;
import se.his.drts.message.AbstractMessageTopClass;
import se.his.drts.message.LocalMessages;

public class JGroups {
	public static Address primaryRM = null;
	public static Address frontEnd = null;
	public static volatile boolean isCoordinator = false;
	public static LinkedBlockingQueue electionQueue = new LinkedBlockingQueue<AbstractMessageTopClass>();
	public static ProjectLogger logger = new ProjectLogger("ReplicaManager");

	public static void start() {
		try {
			LocalMessages messages = new LocalMessages();
			JChannel channel;
			channel = new JChannel();
			new Receiver(channel, messages).start();
			new JGroups();
			new Thread(new Sender(channel, messages)).start();
			new Thread(new RTTMessageRepeater(messages.getMessageQueue(), messages.getRTTMessageQueue())).start();
			
			new Thread(new Election(channel, messages)).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
