package se.his.drts.message;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

//Class purpose is to enable threads to communicate and pass messages with each other
public class LocalMessages {
	private LinkedBlockingQueue m_messageQueue;
	private LinkedBlockingQueue m_RTTMessageQueue;
	private ConcurrentHashMap<Integer, LocalMessage> m_mapOfMessages;
	Integer id = 0;

	public LocalMessages() {
		m_messageQueue = new LinkedBlockingQueue<LocalMessage>();
		m_RTTMessageQueue = new LinkedBlockingQueue<LocalMessage>();
		m_mapOfMessages = new ConcurrentHashMap<Integer, LocalMessage>();
	}

	public void addNewMessage(Object object) {
		LocalMessage msg = new LocalMessage(createMessageTopClass(object));
		m_mapOfMessages.put(msg.getId(), msg);
		addToMessageQueue(msg);
	}

	public void addToMessageQueue(LocalMessage msg) {
		m_messageQueue.add(msg);
	}

	public void addToRTTMessageQueue(LocalMessage msg) {
		m_RTTMessageQueue.add(msg);
	}

	public LinkedBlockingQueue getMessageQueue() {
		return m_messageQueue;
	}

	public LinkedBlockingQueue getRTTMessageQueue() {
		return m_RTTMessageQueue;
	}
	
	public ConcurrentHashMap<Integer, LocalMessage> getMapOfMessages() {
		return m_mapOfMessages;
	}

	private AbstractMessageTopClass createMessageTopClass(Object object) {
		return new DrawObjects(object);
	}
	
	public void acknowledgeMessage(Integer id) {
		if(m_mapOfMessages.containsKey(id)) {
			m_mapOfMessages.get(id).setAcknowledge();
			m_mapOfMessages.remove(id);
		}
	}
}
