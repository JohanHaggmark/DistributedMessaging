package launcher;

public class Singleton {
	private static volatile UI single_instance = null;

	public Singleton() {
		if (single_instance == null) {
			throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
		}
	}

	public static UI getInstance() {
		if (single_instance == null) {
			single_instance = new UI();
		}
		return single_instance;
	}
}
