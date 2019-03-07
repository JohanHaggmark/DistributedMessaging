package Communication;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import DCAD.GObject;
import se.his.drts.message.AbstractMessageTopClass;
import se.his.drts.message.DrawObject;

//Class purpose is to enable threads to communicate and pass messages with each other
public class Messages {
	private LinkedBlockingQueue m_messageQueue;
	private LinkedBlockingQueue m_RTTMessageQueue;
	private ConcurrentHashMap<String, Message> m_mapOfMessages;
	int id = 0;

	public Messages() {
		m_messageQueue = new LinkedBlockingQueue<Message>();
		m_RTTMessageQueue = new LinkedBlockingQueue<Message>();
		m_mapOfMessages = new ConcurrentHashMap<String, Message>();
	}

	public void addNewMessage(GObject obj) {
		Message msg = new Message(id, createMessageTopClass(obj));
		m_mapOfMessages.put("nyckel", msg);
		addToMessageQueue(msg);
		id++;
	}

	public void addToMessageQueue(Message msg) {
		m_messageQueue.add(msg);
	}

	public void addToRTTMessageQueue(Message msg) {
		m_RTTMessageQueue.add(msg);
	}

	public LinkedBlockingQueue getMessageQueue() {
		return m_messageQueue;
	}

	public LinkedBlockingQueue getRTTMessageQueue() {
		return m_RTTMessageQueue;
	}
	
	public ConcurrentHashMap<String, Message> getMapOfMessages() {
		return m_mapOfMessages;
	}

	private AbstractMessageTopClass createMessageTopClass(GObject obj) {
		return new DrawObject(obj);
	}
}
