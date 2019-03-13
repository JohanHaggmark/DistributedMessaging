package Logging;
import java.io.IOException;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ProjectLogger {
	Logger logger;
	Logger debugLogger;
	Logger criticalLogger;
	FileHandler fileHandler;
	FileHandler debugFileHandler;
	FileHandler criticalFileHandler;
	String date;
	String fileName;
	String debugFileName;
	String criticalFileName;
	
	public ProjectLogger(String filename) {
		logger = Logger.getLogger("logger");  
        debugLogger = Logger.getLogger("debugLogger");
        criticalLogger = Logger.getLogger("criticalLogger");
        try {  
            // This block configure the logger with handler and formatter  
            SimpleFormatter formatter = new SimpleFormatter(); 
    		Calendar cal = Calendar.getInstance();	
            date = (cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.DAY_OF_MONTH));
            
            fileName = "C:/java/logs/log/" + date + "_" + filename + ".txt";
        	fileHandler = new FileHandler(fileName);  
            logger.addHandler(fileHandler);  
            fileHandler.setFormatter(formatter);  
            
            debugFileName = "C:/java/logs/debug/" + date + "_debug_" + filename + ".txt";
            debugFileHandler = new FileHandler(debugFileName); 
            debugLogger.addHandler(debugFileHandler); 
            debugFileHandler.setFormatter(formatter);
            
            criticalFileName = "C:/java/logs/critical/" + date + "_critical_" + filename + ".txt";
            criticalFileHandler = new FileHandler(criticalFileName); 
            criticalLogger.addHandler(criticalFileHandler); 
            criticalFileHandler.setFormatter(formatter);
        } catch (SecurityException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public String getDebugFileName() {
		return debugFileName;
	}
	
	public String getCriticalFileName() {
		return criticalFileName;
	}
	
	public void log(String message) {	
		logger.info(date + ":   " + message);
	}
	
	public void debugLog(String message) {
		debugLogger.info(date + ":   " + message);		
	}
	
	public void criticalLog(String message) {	
		criticalLogger.info(date + ":   " + message);
	}
}
