package replicaManager;

import se.his.drts.message.CoordinatorMessage;
import se.his.drts.message.ElectionMessage;
import se.his.drts.message.LocalMessages;

public class Election implements Runnable {
	private Integer m_id;
	private LocalMessages m_messages;

	public Election(Integer id, LocalMessages messages) {
		JGroups.newestElection = this;
		this.m_id = id;
		this.m_messages = messages;
	}

	@Override
	public void run() {
		try {
			startElection();
			Thread.sleep(5000);
			endElection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void startElection() {
		JGroups.isCoordinator = true;
		System.out.println("ElectionMessage");
		m_messages.addNewMessage(new ElectionMessage(this.m_id));
	}

	private void endElection() {
		if(JGroups.newestElection.equals(this)) {
			if (JGroups.isCoordinator) {
				System.out.println("CoordinatorMessage");
				m_messages.addNewMessage(new CoordinatorMessage(this.m_id));
			}
			JGroups.newestElection = null;
		}
	}
}
