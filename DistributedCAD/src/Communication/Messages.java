package Communication;

import java.util.concurrent.LinkedBlockingQueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import DCAD.GObject;
import se.his.drts.message.Envelope;
import se.his.drts.message.MessagePayload.IncorrectMessageException;



public class Messages {
	private LinkedBlockingQueue m_messageQueue;
	
	public Messages() {
		m_messageQueue = new LinkedBlockingQueue<Message>();
	}
	
	public void addToMessageQueue(GObject obj) {
		m_messageQueue.add(convertObjectToMessage(obj));
	}
	
	private Envelope convertObjectToMessage(GObject obj) {
		ObjectMapper om = new ObjectMapper();
		byte[] b;
		try {
			b = om.writeValueAsBytes(obj);
			
			return Envelope.createEnvelope(b);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IncorrectMessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public LinkedBlockingQueue getMessageQueue() {
		return m_messageQueue;
	}
}
