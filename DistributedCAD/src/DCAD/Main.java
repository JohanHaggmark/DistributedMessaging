package DCAD;

import java.net.SocketException;
import java.net.UnknownHostException;

import Communication.RMConnection;

public class Main {
	private static GUI gui = new GUI(750,600);
    private static RMConnection rmConnection;
    

    public static void main(String[] args) throws UnknownHostException, SocketException {
    	rmConnection = new RMConnection("127.0.0.1", 25001);
        Cad c = new Cad(rmConnection, gui);
        //Receiver r = new Receiver(rmConnection, gui);
    }
}
