package Communication;

import java.util.concurrent.LinkedBlockingQueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import DCAD.GObject;
import se.his.drts.message.Envelope;
import se.his.drts.message.MessagePayload.IncorrectMessageException;



public class Messages {
	private LinkedBlockingQueue m_messageQueue;
	private LinkedBlockingQueue m_RTTMessageQueue;
	//private ConcurrentHasMap Skapa hashmappen som håller alla meddelanden
	int id = 0;
	
	public Messages() {
		m_messageQueue = new LinkedBlockingQueue<Message>();
		m_RTTMessageQueue = new LinkedBlockingQueue<Message>();
	}
	
	
	public void addNewMessage(GObject obj) {
		addToMessageQueue(new Message(id, convertObjectToEnvelope(obj)));
		id++;
	}
	
	public void addToMessageQueue(Message msg) {
		m_messageQueue.add(convertObjectToEnvelope(msg));
	}
	public void addToRTTMessageQueue(Message msg) {
		m_RTTMessageQueue.add(convertObjectToEnvelope(msg));
	}
	
	private Envelope convertObjectToEnvelope(Object obj) {
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
	
	/*private Envelope convertObjectToEnvelope(GObject obj) {
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
	}*/
	
	public LinkedBlockingQueue getMessageQueue() {
		return m_messageQueue;
	}
	
	public LinkedBlockingQueue getRTTMessageQueue() {
		return m_RTTMessageQueue;
	}
}
