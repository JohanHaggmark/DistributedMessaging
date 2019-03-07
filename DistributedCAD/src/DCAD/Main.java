package DCAD;

import Communication.Messages;
import Communication.RMConnection;
import Communication.RTTMessageRepeater;
import Communication.Receiver;
import Communication.Sender;

public class Main {
	private static GUI gui = new GUI(750, 600);
	private static RMConnection rmConnection;
	private static Messages messages;

	//starts applications threads here:
	public static void main(String[] args)  {
    	rmConnection = new RMConnection("127.0.0.1", 25001);
    	messages = new Messages();
		new Cad(rmConnection, gui, messages);	
        new Receiver(rmConnection, gui, messages);
       
        new Sender(rmConnection, messages);
        new RTTMessageRepeater(messages.getMessageQueue(), messages.getRTTMessageQueue());
    }
}
