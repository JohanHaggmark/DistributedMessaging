package frontEnd;

import TestingControllability.ShutdownChannel;

public class MainFrontEnd {

	public static void main(String[] args) {
		if(args.length < 1) {
			System.exit(-1);			
		}
		else {
			ShutdownChannel.startShutdownChannel(27000);
			new FrontEnd();		
		}
	}
	
}
