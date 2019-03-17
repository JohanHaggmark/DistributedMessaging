package DCAD;

import Communication.RMConnection;
import Communication.Receiver;
import Communication.Sender;
import MessageHandling.LocalMessages;
import MessageHandling.RTTMessageRepeater;
import TestingControllability.ShutdownChannel;

public class MainDCAD {
	private static GUI gui = new GUI(750, 600);
	private static RMConnection rmConnection;
	private static LocalMessages messages;

	//starts applications threads here:
	public static void main(String[] args)  {
		if(args.length < 1) {
			System.exit(-1);			
		}
		else {
			ShutdownChannel.startShutdownChannel(Integer.parseInt(args[0]));
			
	    	rmConnection = new RMConnection("127.0.0.1", 25000);
	    	messages = new LocalMessages();
	    	new Thread(rmConnection).start();
			new Cad(rmConnection, gui, messages);	
	        new Thread(new RTTMessageRepeater(messages.getRTTMessageQueue(), messages.getMessageQueue())).start();	
		}		
    }
	
	public static void resetConnection() {
		new Thread(new Receiver(rmConnection, gui, messages)).start();
        new Thread(new Sender(rmConnection, messages)).start();

	}
}
