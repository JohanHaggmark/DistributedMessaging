package frontEnd;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;

import MessageHandling.LocalMessages;
import se.his.drts.message.AbstractMessageTopClass;
import se.his.drts.message.MessagePayload;
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
		
	}

	private void setId() {
		String[] split = m_channel.getName().split("-");
		this.m_id = Integer.parseInt(split[split.length - 1]);
	}

	public void viewAccepted(View new_view) {
		FrontEnd.logger.debugLog("View changed");

		// Election happens when primary left:
		if (FrontEnd.primaryRM != null && !new_view.containsMember(FrontEnd.primaryRM)) {
			FrontEnd.primaryRM = null;
			FrontEnd.logger.debugLog("Primary left");
		}
	}

	public void receive(Message msg) {
		
		byte[] bytes = msg.getBuffer();

		//Unpacking msg
		Optional<MessagePayload> mpl = MessagePayload.createMessage(bytes);
		AbstractMessageTopClass msgTopClass = (AbstractMessageTopClass) mpl.get();
		
		// AcknowledgeMessage
		if (msgTopClass.getUUID().equals(UUID.fromString("bb5eeb2c-fa66-4e70-891b-382d87b64814"))) {
			FrontEnd.logger.debugLog("Received AcknowledgeMessage");
			ClientConnection cc = FrontEnd.m_connectedClients.get(msgTopClass.getName());
			cc.addMessageToClient(msgTopClass);
		}
		// DrawObjectsMessage
		else if (msgTopClass.getUUID().equals(UUID.fromString("54f642d7-eaf6-4d62-ad2d-316e4b821c03"))) {
			FrontEnd.logger.debugLog("Received DrawObjectsMessage");
			addMessageToClientsExceptOne(msgTopClass);
		}
		// PresentationMessage
		else if (msgTopClass.getUUID().equals(UUID.fromString("8e69d7fb-4ca9-46de-b33d-cf1dc72377cd"))) {
			FrontEnd.logger.debugLog("Received PresentationMessage");
			FrontEnd.logger.debugLog("SOMETHING IS WRONG IF FRONT END RECEIVES PRESENTATION MESSAGES YOU KNOW...");
		}
		// ElectionMessage
		else if (msgTopClass.getUUID().equals(UUID.fromString("eceb2eb4-361c-425f-a760-a2cd434bbdff"))) {
			FrontEnd.logger.debugLog("Received ElectionMessage");
		}
		// CoordinatorMessage
		else if (msgTopClass.getUUID().equals(UUID.fromString("88486f0c-1a3e-428e-a90c-3ceda5426f27"))) {
			FrontEnd.primaryRM = msg.getSrc();
			FrontEnd.logger.debugLog("Received CoordinatorMessage, will send presentation to primary:" + FrontEnd.primaryRM);
			try {
				//PresentationMessage msg1 = PresentationMessage.createFrontEndPresentation();
				m_channel.send(new Message(FrontEnd.primaryRM, PresentationMessage.createFrontEndPresentation().serialize()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			FrontEnd.logger.debugLog(msg.getSrc() + " destination");
		} else {
			FrontEnd.logger.debugLog("Could not find the correct type");
		}
	}

	private void addMessageToClientsExceptOne(AbstractMessageTopClass msgTopClass) {
		for (Map.Entry<String, ClientConnection> entry : FrontEnd.m_connectedClients.entrySet()) {
			//if(entry.getKey() != msgTopClass.getName()) { //Dont send the state back to the source, only update other clients
				entry.getValue().addMessageToClient(msgTopClass);
				FrontEnd.logger.debugLog("sending drawobjects to Client: " + entry.getKey());
			//}
		}
	}
}
