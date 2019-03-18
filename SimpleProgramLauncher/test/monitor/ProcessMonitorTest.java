package monitor;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import monitor.ProcessMonitor;

class ProcessMonitorTest {
	
	ProcessMonitor monitor;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}


	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	final void testCreateMonitor() {
		String[] arg = {"C:/Windows/notepad.exe"};
		try {
			monitor = ProcessMonitor.createMonitor(arg);
		} catch (Exception e2) {
			System.err.println("Something could not be started");
			e2.printStackTrace();
			System.exit(1);
		}
		assertTrue(monitor.isAlive());
		assertTrue(!monitor.getException().isPresent());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			monitor.terminate();
		} catch (InterruptedException e2) {
			fail("Shold not happen");	
			e2.printStackTrace();
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertFalse(monitor.isAlive());
		assertTrue(monitor.getException().isPresent() && monitor.getException().get() instanceof InterruptedException);
		try {
			monitor = ProcessMonitor.createMonitor(arg);
		} catch (Exception e1) {
			System.err.println("Something could not be started");
			e1.printStackTrace();
			System.exit(1);
		}
		assertTrue(monitor.isAlive());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		monitor.process.destroy();
		assertTrue(!monitor.isAlive());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(monitor.isAlive());
		
		
	}

	@Test
	final void testRun() {
		assertTrue(true);
	}

}
