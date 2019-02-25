package launcher;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The purpose of this class is to monitor an arbitrary process and restart it if terminates. This is useful in 24/7
 * applications where, if processes crash, they need to be restarted.  
 * 
 * @author melj
 *
 */
public class ProcessMonitor  {
	
	public static class LaunchSpecification {
		private String[] argument;
		private Boolean relaunch;
		private int noOfRetries;
		private int deadReckoningOfStartedTimeInMilliSeconds;
		public LaunchSpecification() {
			super();
			this.argument = argument;
			this.relaunch = relaunch;
			this.noOfRetries = noOfRetries;
			this.deadReckoningOfStartedTimeInMilliSeconds = 1000;

			
		}
		/**
		 * @param argument
		 * @param relaunch
		 * @param noOfRetries
		 * @param deadReckoningOfStartedTimeInMilliSeconds 
		 */
		public LaunchSpecification(String[] argument, Boolean relaunch, int noOfRetries, int deadReckoningOfStartedTimeInMilliSeconds) {
			super();
			this.argument = argument;
			this.relaunch = relaunch;
			this.noOfRetries = noOfRetries;
			this.deadReckoningOfStartedTimeInMilliSeconds = deadReckoningOfStartedTimeInMilliSeconds;
		}
		
		/**
		 * @return the argument
		 */
		public final String[] getArgument() {
			return argument;
		}
		/**
		 * @return the deadReckoningOfStartedTimeInMilliSeconds
		 */
		public final int getDeadReckoningOfStartedTimeInMilliSeconds() {
			return deadReckoningOfStartedTimeInMilliSeconds;
		}
		/**
		 * @return the noOfRetries
		 */
		public final int getNoOfRetries() {
			return noOfRetries;
		}
		/**
		 * @return the relaunch
		 */
		public final Boolean getRelaunch() {
			return relaunch;
		}
		/**
		 * @param argument the argument to set
		 */
		public final void setArgument(String[] argument) {
			this.argument = argument;
		}
		/**
		 * @param deadReckoningOfStartedTimeInMilliSeconds the deadReckoningOfStartedTimeInMilliSeconds to set
		 */
		public final void setDeadReckoningOfStartedTimeInMilliSeconds(int deadReckoningOfStartedTimeInMilliSeconds) {
			this.deadReckoningOfStartedTimeInMilliSeconds = deadReckoningOfStartedTimeInMilliSeconds;
		}
		/**
		 * @param noOfRetries the noOfRetries to set
		 */
		public final void setNoOfRetries(int noOfRetries) {
			this.noOfRetries = noOfRetries;
		}
		/**
		 * @param relaunch the relaunch to set
		 */
		public final void setRelaunch(Boolean relaunch) {
			this.relaunch = relaunch;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "LaunchSpecification [argument=" + Arrays.toString(argument) + ", relaunch=" + relaunch
					+ ", noOfRetries=" + noOfRetries + "]";
		}
		
	}
	/**
	 * The purpose of this class is the actual thread that monitors the process. The reason is that the main object should
	 * be possible to use.
	 * 
	 * @author melj
	 *
	 */
	private static class ProcessMonitorThread extends Thread {
		private ProcessMonitor monitor;
		private long launchTime;
		private TreeMap<Long,Integer> result = new TreeMap<>();
		public ProcessMonitorThread(ProcessMonitor monitor) {
			super();
			this.monitor = monitor;
		}
		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			int launchedProcessCrashedOnStartup = 0;
			super.run();
			try {
				while (monitor.getRelaunch()) {
					monitor.process = Runtime.getRuntime().exec(monitor.argument);
					this.launchTime = System.currentTimeMillis();
					synchronized(monitor.beingStarted) {
						if (monitor.beingStarted) {
							monitor.awaitProcessCreated.release();
							monitor.beingStarted = false;
						}
					}
					int result = monitor.process.waitFor();
					this.result.put(System.currentTimeMillis(),result);

					// if process crashed and it did so before the dead reckoning check for that the process should be alive,
					// then increase number of times it has crashed. If the process terminates after the dead reckoning time, a new
					// sequence of relaunch attempts are initiated. 

					if (result != 0 && System.currentTimeMillis()-this.launchTime<monitor.getDeadReckoningOfStartedTimeInMilliSeconds()) {
						launchedProcessCrashedOnStartup++;
					} else {
						launchedProcessCrashedOnStartup = 0;
					}
					if (launchedProcessCrashedOnStartup >= monitor.getNoOfRetries()) {
						throw new IllegalStateException("Cannot start process \""+this.monitor.argument[0]+"\"");
					}

				}
			} catch (InterruptedException|IOException|IllegalStateException e) {
				monitor.exception = e;
				if (monitor.process != null) {
					monitor.process.destroy();
				}
				monitor.process = null;
				monitor.setRelaunch(false);
				monitor.awaitProcessCreated.release();
			} 
		}
		
	}
	/**
	 * @param configurationFile
	 * @return
	 */
	public static ProcessMonitor[] createMonitor(File configurationFile) {
		
		try {
			ObjectMapper objectMapper = new ObjectMapper();

			List<LaunchSpecification> launchSpecificationList = (List<LaunchSpecification>)objectMapper.readValue(configurationFile,new TypeReference<List<LaunchSpecification>>() {});
			ProcessMonitor[] monitoredProcess = new ProcessMonitor[launchSpecificationList.size()];
			for (int i = 0; i < launchSpecificationList.size(); ++i) {
				Object object = launchSpecificationList.get(i);
				LaunchSpecification launchSpecification = launchSpecificationList.get(i);
				monitoredProcess[i] = ProcessMonitor.createMonitor(launchSpecification.getArgument());
				monitoredProcess[i].setRelaunch(launchSpecification.getRelaunch());
				monitoredProcess[i].setNoOfRetries(launchSpecification.getNoOfRetries());
				monitoredProcess[i].setDeadReckoningOfStartedTimeInMilliSeconds(launchSpecification.getDeadReckoningOfStartedTimeInMilliSeconds());
			}

			return monitoredProcess;
		} catch (IllegalArgumentException | IOException e) {
			System.err.println("Something is seriously wrong with you configuration file");
			e.printStackTrace(System.err);
			System.exit(1);
		} catch (Exception e) {
			System.err.println("Something could not be started");
			e.printStackTrace();
			System.exit(1);
		}
		throw new IllegalStateException("This row should never be reached!!!");
	}
	/**
	 * The factory method for creating process monitor objects. It also creates and starts a ProcessMonitorThread object that
	 * performs the actual monitoring. 
	 * 
	 * @param argument an array of strings with the program and its arguments to the program
	 * @return a process monitor object
	 * @throws Exception 
	 */
	public static ProcessMonitor createMonitor(String[] argument) throws Exception {
		ProcessMonitor monitor = new ProcessMonitor(argument);
		synchronized(monitor.beingStarted) {
			monitor.beingStarted = true;
		}
		monitor.thread = new ProcessMonitorThread(monitor);
		monitor.thread.start();
		// wait for the thread to start and complete initialization
		try {
			monitor.awaitProcessCreated.acquire();
		} catch (InterruptedException e) {
		}
		if (monitor.getException().isPresent()) {
			throw monitor.getException().get();
		}
		return monitor;
		
	}
	private String[] argument;
	private int deadReckoningOfStartedTimeInMilliSeconds;
	private Exception exception = null;
	Process process = null;
	private Thread thread = null;

	private Boolean relaunch = true;
	private Integer noOfRetries = 10;
	private Boolean beingStarted = false;
	
	private Semaphore awaitProcessCreated = new Semaphore(0); // semaphore that is used to delay responses in the process monitor until the thread is ready
	
	private Semaphore awaitProcessTerminated = new Semaphore(0); // semaphore that is used to delay responses in the process monitor object until the thread has terminated the monitored process
	
	/**
	 * Create a process monitor object. Check if argument is the path to an executable program.
	 * 
	 * @param argument an array of strings with the program and its arguments to the program
	 */
	private ProcessMonitor(String[] argument) {
		if (argument == null) {
			throw new IllegalArgumentException("Argument is null");
		}
		if (argument.length < 1) {
			throw new IllegalArgumentException("No arguments passed to "+this.getClass().getName()+" constructor");
		}
		File file = new File(argument[0]);
		if (!file.exists()) {
			throw new IllegalArgumentException("Command at path \""+file.getPath()+"\" does not exist");
		}
		if (!file.canExecute()) {
			throw new IllegalArgumentException("Command at path \""+file.getPath()+"\" cannot be executed");			
		}
		this.argument = Arrays.copyOf(argument, argument.length);
	}
	
	/**
	 * @return the deadReckoningOfStartedTimeInMilliSeconds
	 */
	public final int getDeadReckoningOfStartedTimeInMilliSeconds() {
		return deadReckoningOfStartedTimeInMilliSeconds;
	}
	
	/**
	 * Return the exception, if there is one. If there is no exception, then Optional is empyt
	 * @return an optional exception object
	 */
	Optional<Exception> getException() {
		Optional<Exception> result;
		if (this.exception != null) {
			result = Optional.of(this.exception);
		} else {
			result = Optional.empty();
		}
		return result;
	}

	
	/**
	 * @return the noOfRetries
	 */
	public final Integer getNoOfRetries() {
		return noOfRetries;
	}

	/**
	 * Gets the relaunch flag.
	 * @return
	 */
	public synchronized boolean getRelaunch() {
		return this.relaunch;
	}
	
	/**
	 * Checks if the monitored process is alive or not. 
	 * @return
	 */
	public boolean isAlive() {
		return process != null && process.isAlive();
	}
	
	
	
	/**
	 * @param deadReckoningOfStartedTimeInMilliSeconds the deadReckoningOfStartedTimeInMilliSeconds to set
	 */
	private final void setDeadReckoningOfStartedTimeInMilliSeconds(int deadReckoningOfStartedTimeInMilliSeconds) {
		this.deadReckoningOfStartedTimeInMilliSeconds = deadReckoningOfStartedTimeInMilliSeconds;
	}
	private void setNoOfRetries(Integer noOfRetries) {
		this.noOfRetries = noOfRetries;
	}
	
	/**
	 * Sets the relaunch flag to either true of false.
	 * @param value
	 */
	public synchronized void setRelaunch(boolean value) {
		this.relaunch = value;
	}
	
	/**
	 * Terminates the monitoring and the process. 
	 * @throws InterruptedException 
	 * 
	 */
	public  void terminate() throws InterruptedException {
		setRelaunch(false);
		if (thread.isAlive()) {
			thread.interrupt();
		}
		thread.join();

	}
	
	
	
}
