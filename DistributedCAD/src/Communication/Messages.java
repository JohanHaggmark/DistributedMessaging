package Communication;

import java.util.concurrent.LinkedBlockingQueue;
import DCAD.GObject;



public class Messages {
	private LinkedBlockingQueue m_messageQueue;
	private LinkedBlockingQueue m_RTTMessageQueue;
	//private ConcurrentHasMap Skapa hashmappen som h�ller alla meddelanden
	int id = 0;
	
	public Messages() {
		m_messageQueue = new LinkedBlockingQueue<Message>();
		m_RTTMessageQueue = new LinkedBlockingQueue<Message>();
	}
	
	
	public void addNewMessage(GObject obj) {
		addToMessageQueue(new Message(id));
		id++;
	}
	
	public void addToMessageQueue(Message msg) {
		//L�GG TILL N�NTING SOM INTE �R ENVELOPE
		//m_messageQueue.add();
	}
	public void addToRTTMessageQueue(Message msg) {
		//L�GG TILL N�NTING SOM INTE �R ENVELOPE
		//m_RTTMessageQueue.add();
	}
	
	public LinkedBlockingQueue getMessageQueue() {
		return m_messageQueue;
	}
	
	public LinkedBlockingQueue getRTTMessageQueue() {
		return m_RTTMessageQueue;
	}
}
