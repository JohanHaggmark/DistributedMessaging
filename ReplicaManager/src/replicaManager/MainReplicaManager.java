package replicaManager;

import TestingControllability.ShutdownChannel;

public class MainReplicaManager {

	String user_name = System.getProperty("user.name", "n/a");

	public static void main(String[] args) throws Exception {
		ShutdownChannel.startShutdownChannel(28000);
		
		JGroups.start();
	}
}
