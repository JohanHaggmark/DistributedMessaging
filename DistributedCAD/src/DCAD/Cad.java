/**
 *
 * @author brom
 */

package DCAD;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

import Communication.RMConnection;
import Logging.ProjectLogger;
import se.his.drts.message.DrawObjectsMessage;
import se.his.drts.message.LocalMessages;

public class Cad {
	public static ProjectLogger logger = new ProjectLogger("CAD");
	public static String connectionName = null;
	public static LinkedBlockingQueue<LinkedList<GObject>> resendQueue = new LinkedBlockingQueue();
	public static boolean hasFrontEnd;
	private GUI gui;
	private RMConnection rmConnection;
	private LocalMessages messages;

	public Cad(RMConnection rmc, GUI gui, LocalMessages messages) {
		this.messages = messages;
		this.gui = gui;
		this.rmConnection = rmc;
		gui.addCad(this);
		gui.addToListener();
	}

	public void sendState(LinkedList<GObject> objectList) {
		Cad.logger.debugLog("sendState() - sending");
		if (connectionName != null) {
			Cad.logger.debugLog("Cad() connectionName != null");
			messages.addNewMessageWithAcknowledge(new DrawObjectsMessage(objectList, connectionName));
			if (!hasFrontEnd) {
				Cad.logger.debugLog("Cad() hasFrontEnd true");
				hasFrontEnd = true;
				new ResendThread().start();
			}
		} else {
			Cad.logger.debugLog("Cad() hasFrontEnd false");
			hasFrontEnd = false;
			try {
				resendQueue.put(objectList);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private class ResendThread extends Thread {
		@Override
		public void run() {
			Cad.logger.debugLog("Cad() - resendThread started");
			try {
				while (hasFrontEnd) {
					if (!hasFrontEnd) {
						Cad.logger.criticalLog("HAS NO FRONTEND BUT THREAD IS RUNNING ANYWAY");
					}
					LinkedList<GObject> objectList = new LinkedList();
					sendState(objectList = resendQueue.take());
					if (!hasFrontEnd) {
						Cad.logger.criticalLog("HAS NO FRONTEND BUT THREAD IS RUNNING ANYWAY");
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
