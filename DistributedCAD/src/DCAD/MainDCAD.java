package DCAD;

import Communication.RMConnection;
import Communication.Receiver;
import Communication.Sender;
import MessageHandling.LocalMessages;
import MessageHandling.RTTMessageRepeater;

public class MainDCAD {
	private static GUI gui = new GUI(750, 600);
	private static RMConnection rmConnection;
	private static LocalMessages messages;

	//starts applications threads here:
	public static void main(String[] args)  {
    	rmConnection = new RMConnection("127.0.0.1", 25000);
    	messages = new LocalMessages();
		new Cad(rmConnection, gui, messages);	
		new Thread(new Receiver(rmConnection, gui, messages)).start();
        new Thread(new Sender(rmConnection, messages)).start();
        new Thread(new RTTMessageRepeater(messages.getMessageQueue(), messages.getRTTMessageQueue())).start();
    }
}
