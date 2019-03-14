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
            
            fileName = getFileName(filename);
        	fileHandler = new FileHandler(fileName);  
            logger.addHandler(fileHandler);  
            fileHandler.setFormatter(formatter);  
            
            debugFileName = getDebugFileName(filename);
            debugFileHandler = new FileHandler(debugFileName); 
            debugLogger.addHandler(debugFileHandler); 
            debugFileHandler.setFormatter(formatter);
            
            criticalFileName = getCriticalFileName(filename);
            criticalFileHandler = new FileHandler(criticalFileName); 
            criticalLogger.addHandler(criticalFileHandler); 
            criticalFileHandler.setFormatter(formatter);
            //master
        } catch (SecurityException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
	}
	
	public static String getFileName(String filename) {
		Calendar cal = Calendar.getInstance();	
        String thisDate = (cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.DAY_OF_MONTH));
		return "C:/java/logs/log/" + thisDate + "_" + filename + ".txt";
	}
	
	public static String getDebugFileName(String filename) {
		Calendar cal = Calendar.getInstance();	
        String thisDate = (cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.DAY_OF_MONTH));
        return "C:/java/logs/debug/" + thisDate + "_debug_" + filename + ".txt";
		
	}
	
	public static String getCriticalFileName(String filename) {
		Calendar cal = Calendar.getInstance();	
        String thisDate = (cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.DAY_OF_MONTH));
		return "C:/java/logs/critical/" + thisDate + "_critical_" + filename + ".txt";
	}
	
	public void log(String message) {	
		logger.info(date + ":   " + message + "\n");
	}
	
	public void debugLog(String message) {
		debugLogger.info(date + ":   " + message + "\n");		
	}
	
	public void criticalLog(String message) {	
		criticalLogger.info(date + ":   " + message + "\n");
	}
}
