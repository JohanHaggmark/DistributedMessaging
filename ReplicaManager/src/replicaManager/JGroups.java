package replicaManager;

import org.jgroups.JChannel;

import se.his.drts.message.LocalMessages;

public class JGroups {
	public static String primaryRM = null;
	public static String frontEnd = null;

	public static void start() {
		try {
			LocalMessages messages = new LocalMessages();
			JChannel channel;
			channel = new JChannel();
			new Receiver(channel, messages).start();
			new JGroups();
			new Thread(new Sender(channel, messages)).start();
			new Thread(new RTTMessageRepeater(messages.getMessageQueue(), messages.getRTTMessageQueue())).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
