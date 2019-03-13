package replicaManager;

public class MainReplicaManager {

	String user_name = System.getProperty("user.name", "n/a");

	public static void main(String[] args) throws Exception {
		JGroups.start();
	}
}
