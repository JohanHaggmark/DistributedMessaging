package frontEnd;

import java.util.UUID;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;

import se.his.drts.message.AbstractMessageTopClass;
import se.his.drts.message.ElectionMessage;
import se.his.drts.message.LocalMessages;
import se.his.drts.message.PresentationMessage;

public class JGroupsReceiver extends ReceiverAdapter {

	private Integer m_id;
	private JChannel m_channel;
	private LocalMessages m_messages;
	
	public JGroupsReceiver(JChannel channel, LocalMessages messages) {
		this.m_channel = channel;
		this.m_messages = messages;
	}
	
	public void start() throws Exception {
		FrontEnd.logger.debugLog("Starting Front End");
		m_channel.setReceiver(this);
		m_channel.connect("ChatCluster");
		setId();
		sendPresentationMessage();
	}
	
	private void setId() {
		String[] split = m_channel.getName().split("-");
		this.m_id = Integer.parseInt(split[split.length - 1]);
	}
	
	private void sendPresentationMessage() {
		// FUNKAR ATT SKICKA
//		TimeOutMessage msg = new TimeOutMessage(1);
//		PresentationMessage msg = PresentationMessage.createFrontEndPresentation();
//		StringMsg msg = new StringMsg("helo");
//		CoordinatorMessage msg = new CoordinatorMessage(1);
//		ElectionMessage msg = new ElectionMessage(1);
//		TestMessage msg = new TestMessage("katt");
//		AcknowledgeMessage msg = new AcknowledgeMessage(1, "hej", "räva");
//		DrawObjectsMessage msg = new DrawObjectsMessage(new Object(), "helo");

		PresentationMessage msg = PresentationMessage.createFrontEndPresentation();
		
		sendToPrimary(msg);
	}
	
	private void sendToPrimary(AbstractMessageTopClass msg) {
		if(FrontEnd.primaryRM != null) {
			FrontEnd.logger.debugLog("Sending to primary");
			try {
				m_channel.send(FrontEnd.primaryRM, msg.serialize());
			} catch (Exception e) {
				FrontEnd.logger.criticalLog("could not send message to primaryRM");
				e.printStackTrace();
			}
		}
	}

	public void viewAccepted(View new_view) {
		FrontEnd.logger.debugLog("View changed");
		
		//Election happens when primary left:
		if (FrontEnd.primaryRM != null && !new_view.containsMember(FrontEnd.primaryRM)) {
			FrontEnd.primaryRM = null;
			FrontEnd.logger.debugLog("Primary left");
		}
	}
	
	public void receive(Message msg) {
		FrontEnd.logger.debugLog("receive - jgroupsreceiver 62 " + msg.getObject());
		AbstractMessageTopClass msgTopClass = (AbstractMessageTopClass) msg.getObject();
		// AcknowledgeMessage
		if (msgTopClass.getUUID().equals(UUID.fromString("bb5eeb2c-fa66-4e70-891b-382d87b64814"))) {
			FrontEnd.logger.debugLog("Received AcknowledgeMessage");
		}
		// DrawObjectsMessage
		else if (msgTopClass.getUUID().equals(UUID.fromString("54f642d7-eaf6-4d62-ad2d-316e4b821c03"))) {
			FrontEnd.logger.debugLog("Received DrawObjectsMessage");
		}
		// PresentationMessage
		else if (msgTopClass.getUUID().equals(UUID.fromString("8e69d7fb-4ca9-46de-b33d-cf1dc72377cd"))) {
			FrontEnd.logger.debugLog("Received PresentationMessage");
			FrontEnd.logger.debugLog("SOMETHING IS WRONG IF FRONT END RECEIVES PRESENTATION MESSAGES YOU KNOW...");
		}
		// ElectionMessage
		else if (msgTopClass.getUUID().equals(UUID.fromString("eceb2eb4-361c-425f-a760-a2cd434bbdff"))) {
			FrontEnd.logger.debugLog("Received ElectionMessage");
			sendToPrimary(new ElectionMessage((Integer) msgTopClass.executeInReplicaManager()));
		}
		// CoordinatorMessage
		else if (msgTopClass.getUUID().equals(UUID.fromString("88486f0c-1a3e-428e-a90c-3ceda5426f27"))) {
			FrontEnd.logger.debugLog("Received CoordinatorMessage");
			FrontEnd.primaryRM = msg.getSrc();
			FrontEnd.logger.debugLog(msgTopClass.getName() + " destination");
			FrontEnd.logger.debugLog("HAVE I GOTTEN THIS FAR??");;
		} 
		else {
			FrontEnd.logger.debugLog("Could not find the correct type");
		}
	}
}
