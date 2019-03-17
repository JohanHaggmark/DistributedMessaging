package replicaManager;

import TestingControllability.ShutdownChannel;

public class MainReplicaManager {

	String user_name = System.getProperty("user.name", "n/a");

	public static void main(String[] args) throws Exception {
		if(args.length < 1) {
			System.exit(-1);			
		}
		else {
			ShutdownChannel.startShutdownChannel(Integer.parseInt(args[0]));			
			JGroups.start();			
		}
	}
}
