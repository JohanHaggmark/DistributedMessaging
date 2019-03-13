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
	
	public ProjectLogger(String filename) {
		logger = Logger.getLogger("MyLog");  
        
        try {  
            // This block configure the logger with handler and formatter  
            SimpleFormatter formatter = new SimpleFormatter(); 
    		Calendar cal = Calendar.getInstance();	
            date = (cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.DAY_OF_MONTH));
            
        	fileHandler = new FileHandler("C:/java/logs/" + date + "_" + filename + ".txt");  
            logger.addHandler(fileHandler); 
            fileHandler.setFormatter(formatter);   
            
            debugFileHandler = new FileHandler("C:/java/logs/" + date + "_debug_" + filename + ".txt");  
            debugLogger.addHandler(debugFileHandler); 
            debugFileHandler.setFormatter(formatter); 
            
            criticalFileHandler = new FileHandler("C:/java/logs/" + date + "_critical_" + filename + ".txt");  
            criticalLogger.addHandler(criticalFileHandler); 
            criticalFileHandler.setFormatter(formatter); 
        } catch (SecurityException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
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
