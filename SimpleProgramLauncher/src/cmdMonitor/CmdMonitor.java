package cmdMonitor;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import monitor.ProcessMonitor;

public class CmdMonitor {

	public static void main(String[] args) {
		startReplicaMonitor();
	}
	
	private static void startReplicaMonitor() {
		Thread rmThread = new Thread() {
			@Override
			public void run() {
				try {
					File correctFile1;
					correctFile1 = File.createTempFile("correctFile", "json");
					String correctFile1Text = 
							"[\n" 		
									+ "\t{\n"
									+ "\t\t\"argument\": [\"C:/java/exe/ReplicaManager.exe\"],\n"
									+ "\t\t\"relaunch\": true,\n"
									+ "\t\t\"noOfRetries\": 10,\n"
									+ "\t\t\"deadReckoningOfStartedTimeInMilliSeconds\": 1000"
									+"\t}\n"
									+"]\n";
					PrintWriter out1 = new PrintWriter(correctFile1);
					out1.print(correctFile1Text);
					out1.close();
					System.out.print(correctFile1Text);					
					
					ProcessMonitor[] monitoredProcessArray = ProcessMonitor.createMonitor(correctFile1);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		};
		rmThread.start();
	}
	
	private static void startFrontEndMonitor() {
		Thread rmThread = new Thread() {
			@Override
			public void run() {
				try {
					File correctFile1;
					correctFile1 = File.createTempFile("correctFile", "json");
					String correctFile1Text = 
							"[\n" 		
									+ "\t{\n"
									+ "\t\t\"argument\": [\"C:/java/exe/FrontEnd.exe\"],\n"
									+ "\t\t\"relaunch\": true,\n"
									+ "\t\t\"noOfRetries\": 10,\n"
									+ "\t\t\"deadReckoningOfStartedTimeInMilliSeconds\": 1000"
									+"\t}\n"
									+"]\n";
					PrintWriter out1 = new PrintWriter(correctFile1);
					out1.print(correctFile1Text);
					out1.close();
					System.out.print(correctFile1Text);					
					
					ProcessMonitor[] monitoredProcessArray = ProcessMonitor.createMonitor(correctFile1);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		};
		rmThread.start();
	}
}
