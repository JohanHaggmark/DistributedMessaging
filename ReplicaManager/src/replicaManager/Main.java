package replicaManager;

public class Main {

	String user_name = System.getProperty("user.name", "n/a");

	public static void main(String[] args) throws Exception {
		JGroups.start();
	}
}
