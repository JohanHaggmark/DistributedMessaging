package MessageHandling;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import se.his.drts.message.AbstractMessageTopClass;

//Class purpose is to enable threads to communicate and pass messages with each other
public class LocalMessages {
	private LinkedBlockingQueue<LocalMessage> m_messageQueue;
	private LinkedBlockingQueue<LocalMessage> m_RTTMessageQueue;
	private LinkedBlockingQueue<LocalMessage> m_messagesToResender;
	private ConcurrentHashMap<Integer, LocalMessage> m_mapOfMessages;
	
	private boolean senderHasConnection = false;

	public LocalMessages() {
		m_messageQueue = new LinkedBlockingQueue<LocalMessage>();
		m_RTTMessageQueue = new LinkedBlockingQueue<LocalMessage>();
		m_messagesToResender = new LinkedBlockingQueue<LocalMessage>();
		m_mapOfMessages = new ConcurrentHashMap<Integer, LocalMessage>();
	}

	public void addNewMessageWithAcknowledge(AbstractMessageTopClass msgTopClass) {
		LocalMessage msg = new LocalMessage(msgTopClass, true);
		m_mapOfMessages.put(msgTopClass.getackID(), msg);
		addToMessageQueue(msg);
	}

	public void addNewMessage(AbstractMessageTopClass msgTopClass) {
		LocalMessage msg = new LocalMessage(msgTopClass, false);
		addToMessageQueue(msg);
	}

	public void addToMessageQueue(LocalMessage msg) {
		m_messageQueue.add(msg);
	}

	public void addToRTTMessageQueue(LocalMessage msg) {
		m_RTTMessageQueue.add(msg);
	}

	public void addToMessagesToResender(LocalMessage msg) {
		m_messagesToResender.add(msg);
	}

	public LinkedBlockingQueue<LocalMessage> getMessagesToResender() {
		return m_messagesToResender;
	}

	public LinkedBlockingQueue<LocalMessage> getMessageQueue() {
		return m_messageQueue;
	}

	public LinkedBlockingQueue<LocalMessage> getRTTMessageQueue() {
		return m_RTTMessageQueue;
	}

	public ConcurrentHashMap<Integer, LocalMessage> getMapOfMessages() {
		return m_mapOfMessages;
	}

	public void removeAcknowledgeFromMessage(Integer id) {
		//Must check to avoid null pointer exception
		if (m_mapOfMessages.containsKey(id.intValue())) {
			m_mapOfMessages.get(id.intValue()).isAcknowledged();
			m_mapOfMessages.remove(id);
		}
	}
	
	public boolean isSenderHasConnection() {
		return senderHasConnection;
	}

	public void setSenderHasConnection(boolean senderHasConnection) {
		this.senderHasConnection = senderHasConnection;
	}
}
