import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Main {
	


	public static void main(String[] args) {
		
	
		if (args.length < 1) {
			System.err.println("Usage: java Server portnumber");
			System.exit(-1);
		}
		new FrontEnd(Integer.parseInt(args[0]));


	}
	
	

}