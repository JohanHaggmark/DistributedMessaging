package Communication;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import DCAD.GObject;
import se.his.drts.message.AbstractMessageTopClass;
import se.his.drts.message.DrawObjects;

//Class purpose is to enable threads to communicate and pass messages with each other
public class Messages {
	private LinkedBlockingQueue m_messageQueue;
	private LinkedBlockingQueue m_RTTMessageQueue;
	private ConcurrentHashMap<Integer, Message> m_mapOfMessages;
	Integer id = 0;

	public Messages() {
		m_messageQueue = new LinkedBlockingQueue<Message>();
		m_RTTMessageQueue = new LinkedBlockingQueue<Message>();
		m_mapOfMessages = new ConcurrentHashMap<Integer, Message>();
	}

	public void addNewMessage(LinkedList<GObject> objectList) {
		Message msg = new Message(createMessageTopClass(objectList));
		m_mapOfMessages.put(msg.getId(), msg);
		addToMessageQueue(msg);
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
	
	public ConcurrentHashMap<Integer, Message> getMapOfMessages() {
		return m_mapOfMessages;
	}

	private AbstractMessageTopClass createMessageTopClass(LinkedList<GObject> objectList) {
		return new DrawObjects(objectList);
	}
	
	public void acknowledgeMessage(Integer id) {
		if(m_mapOfMessages.containsKey(id)) {
			m_mapOfMessages.get(id).setAcknowledge();
			m_mapOfMessages.remove(id);
		}
	}
}
