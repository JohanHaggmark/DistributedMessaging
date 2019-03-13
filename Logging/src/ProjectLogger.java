import java.io.IOException;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ProjectLogger {
	Logger logger;
	FileHandler fh;
	
	public ProjectLogger(String filename) {
		logger = Logger.getLogger("MyLog");  
        
        try {  
            // This block configure the logger with handler and formatter  
            fh = new FileHandler("C:/java/logs/" + filename);  
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();  
            fh.setFormatter(formatter);  
            
        } catch (SecurityException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
	}
	
	public void log(String message) {
		Calendar cal = Calendar.getInstance();		
		logger.info(cal.getTime() + ":   " + message);
	}
}
