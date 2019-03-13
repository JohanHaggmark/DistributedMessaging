package Logging;
import java.io.IOException;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ProjectLogger {
	Logger casualLogger;
	Logger criticalLogger;
	FileHandler casualFileHandler;
	FileHandler criticalFileHandler;
	
	public ProjectLogger(String filename) {
		casualLogger = Logger.getLogger("MyLog");  
        
        try {  
            // This block configure the logger with handler and formatter  
        	casualFileHandler = new FileHandler("C:/java/logs/" + filename + ".txt");  
            casualLogger.addHandler(casualFileHandler);
            SimpleFormatter formatter = new SimpleFormatter();  
            casualFileHandler.setFormatter(formatter);   
            
            criticalFileHandler = new FileHandler("C:/java/logs/critical_" + filename + ".txt");  
            criticalLogger.addHandler(criticalFileHandler); 
            criticalFileHandler.setFormatter(formatter); 
        } catch (SecurityException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
	}
	
	public void log(String message) {
		Calendar cal = Calendar.getInstance();		
		casualLogger.info(cal.getTime() + ":   " + message);
	}
	
	public void criticalLog(String message) {
		Calendar cal = Calendar.getInstance();		
		casualLogger.info(cal.getTime() + ":   " + message);
	}
}
