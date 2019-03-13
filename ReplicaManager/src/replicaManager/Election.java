package replicaManager;

import java.util.UUID;

import org.jgroups.Address;
import org.jgroups.JChannel;

import se.his.drts.message.AbstractMessageTopClass;
import se.his.drts.message.CoordinatorMessage;
import se.his.drts.message.LocalMessages;

public class Election implements Runnable {
	private Integer m_id;
	private Address m_address;
	private LocalMessages m_messages;
	private long m_timeStamp;

	public Election(JChannel channel, LocalMessages messages) {
		this.m_messages = messages;
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
		JGroups.isCoordinator = true;
		m_messages.addNewMessage(msgTopClass);
	}

	private void endElection() {
		if (JGroups.isCoordinator) {
			JGroups.primaryRM = this.m_address;
			m_messages.addNewMessage(new CoordinatorMessage(this.m_id));
		} else {
			System.out.println(JGroups.primaryRM + " is the new coordinator");
		}
	}
}