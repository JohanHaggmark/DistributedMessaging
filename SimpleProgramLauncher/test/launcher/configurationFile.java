package launcher;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.PrintWriter;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class configurationFile {
	File correctFile1;
	File correctFile2;
	private File correctFile3;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		correctFile1 = File.createTempFile("correctFile", "json");
		String correctFile1Text = 
				"[\n" 		
						+ "\t{\n"
						+ "\t\t\"argument\": [\"C:/windows/notepad.exe\"],\n"
						+ "\t\t\"relaunch\": true,\n"
						+ "\t\t\"noOfRetries\": 5,\n"
						+ "\t\t\"deadReckoningOfStartedTimeInMilliSeconds\": 1000"
						+"\t}\n"
						+"]\n";
		PrintWriter out1 = new PrintWriter(correctFile1);
		out1.print(correctFile1Text);
		out1.close();
		System.out.print(correctFile1Text);

		correctFile3 = File.createTempFile("correctFile", "json");
		String correctFile3Text = 
				"[\n" 		
						+ "\t{\n"
						+ "\t\t\"argument\": [\"C:/windows/System32/cmd.exe\"],\n"
						+ "\t\t\"relaunch\": true,\n"
						+ "\t\t\"noOfRetries\": 5,\n"
						+ "\t\t\"deadReckoningOfStartedTimeInMilliSeconds\": 1000"
						+"\t}\n"
						+"]\n";
		PrintWriter out3 = new PrintWriter(correctFile3);
		out3.print(correctFile3Text);
		out3.close();
		System.out.print(correctFile3Text);

		correctFile2 = File.createTempFile("correctFile2", "json");
		String correctFile2Text =
				"[\n"
						+ "\t{\n"	
						+ "\t\t\"argument\": [\"C:/windows/notepad.exe\"],\n"
						+ "\t\t\"relaunch\": true,\n"
						+ "\t\t\"noOfRetries\": 5,\n"
						+ "\t\t\"deadReckoningOfStartedTimeInMilliSeconds\": 1000"
						+"\t},\n"
						+""
						+ "\t{\n"	
						+ "\t\t\"argument\": [\"C:/windows/System32/cmd.exe\"],\n"
						+ "\t\t\"relaunch\": true,\n"
						+ "\t\t\"noOfRetries\": 5,\n"
						+ "\t\t\"deadReckoningOfStartedTimeInMilliSeconds\": 1000"
						+"\t}\n"
						+"]\n";
		PrintWriter out2 = new PrintWriter(correctFile2);
		out2.print(correctFile2Text);
		out2.close();
		System.out.print(correctFile2Text);
		
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testCreateMonitorFile() {
		ProcessMonitor[] monitoredProcessArray = ProcessMonitor.createMonitor(correctFile1);
		assertTrue(monitoredProcessArray[0].isAlive());
		for (ProcessMonitor monitoredProcess: monitoredProcessArray) {
			try {
				monitoredProcess.terminate();
			} catch (InterruptedException e) {
				fail("Should not throw exception");
				e.printStackTrace();
			}
		}
		
		ProcessMonitor[] monitoredProcessArray3 = ProcessMonitor.createMonitor(correctFile3);
		assertTrue(monitoredProcessArray3[0].isAlive());
		for (ProcessMonitor monitoredProcess:monitoredProcessArray3) {
			try {
				monitoredProcess.terminate();
			} catch(InterruptedException e) {
				fail("Should not throw exception");
				e.printStackTrace();
			}
		}
		ProcessMonitor[] monitoredProcessArray2 = ProcessMonitor.createMonitor(correctFile2);
		for (ProcessMonitor monitoredProcess: monitoredProcessArray2) {
			assertTrue(monitoredProcess.isAlive());
		}
		for (ProcessMonitor monitoredProcess: monitoredProcessArray2) {
			monitoredProcess.process.destroy();
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (ProcessMonitor monitoredProcess: monitoredProcessArray2) {
			assertTrue(monitoredProcess.isAlive());
		}
	}

}
