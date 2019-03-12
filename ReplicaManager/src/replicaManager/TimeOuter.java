package replicaManager;

import se.his.drts.message.TimeOutMessage;

public class TimeOuter implements Runnable{

	private final int averageRTT = 1170;
	private	long timeStamp;
	
	public TimeOuter(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(averageRTT);
			JGroups.electionQueue.add(new TimeOutMessage(timeStamp));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
