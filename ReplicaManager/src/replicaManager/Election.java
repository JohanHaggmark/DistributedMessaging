package replicaManager;

import java.util.UUID;

import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;

import se.his.drts.message.AbstractMessageTopClass;
import se.his.drts.message.CoordinatorMessage;

public class Election implements Runnable {
	private Address m_address;
	private JChannel m_channel;
	private long m_timeStamp;

	public Election(JChannel channel) {
		this.m_channel = channel;
		this.m_address = channel.getAddress();
	}

	@Override
	public void run() {
		while (true) {
			AbstractMessageTopClass msgTopClass;
			try {
				msgTopClass = (AbstractMessageTopClass) JGroups.electionQueue.take();
				// This is an ElectionMessage
				if (msgTopClass.getUUID().equals(UUID.fromString("eceb2eb4-361c-425f-a760-a2cd434bbdff"))) {
					startElection(msgTopClass);
				}
				// This is a TimeoutMessage
				else if (msgTopClass.getUUID().equals(UUID.fromString("0d61e561-116e-48e3-9259-8170c9623da3"))) {
					if (m_timeStamp == (long) msgTopClass.executeInReplicaManager()) {
						endElection();
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void startElection(AbstractMessageTopClass msgTopClass) {
		m_timeStamp = System.currentTimeMillis();
		new Thread(new TimeOuter(m_timeStamp)).start();
		try {
			m_channel.send(new Message(null, msgTopClass.serialize()));
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	private void endElection() {
		if (JGroups.isCoordinator) {//we are still coordinator if Receive has not received an ElectionMessage with myId<msg.id
			JGroups.primaryRM = this.m_address;
			JGroups.logger.debugLog("Election Sending Coordinator message with: " + this.m_address);
			try {
				m_channel.send(new Message(null, new CoordinatorMessage().serialize()));
			} catch (Exception e) {
				e.printStackTrace();
			}	
		} else {
			JGroups.logger.debugLog(JGroups.primaryRM + " is the new coordinator");
		}
	}
}