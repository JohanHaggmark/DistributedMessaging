package frontEnd;

import java.util.Optional;
import java.util.UUID;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;

import se.his.drts.message.AbstractMessageTopClass;
import se.his.drts.message.MessagePayload;
import se.his.drts.message.PresentationMessage;

public class JGroupsReceiver extends ReceiverAdapter {

	private JChannel m_channel;

	public JGroupsReceiver(JChannel channel) {
		this.m_channel = channel;
	}

	public void start() throws Exception {
		FrontEnd.logger.debugLog("Starting Front End");
		m_channel.setReceiver(this);
		m_channel.connect("ChatCluster");
	}

	public void viewAccepted(View new_view) {
		FrontEnd.logger.debugLog("View changed, size:" + new_view.size());

		// Election happens when primary left:
		if (FrontEnd.primaryRM != null && !new_view.containsMember(FrontEnd.primaryRM)) {
			FrontEnd.primaryRM = null;
			FrontEnd.logger.debugLog("Primary left");
		}

	}

	public void receive(Message msg) {
		if (!msg.getSrc().equals(m_channel.getAddress())) {
			byte[] bytes = msg.getBuffer();

			// Unpacking msg
			Optional<MessagePayload> mpl = MessagePayload.createMessage(bytes);
			AbstractMessageTopClass msgTopClass = (AbstractMessageTopClass) mpl.get();

			// AcknowledgeMessage
			if (msgTopClass.getUUID().equals(UUID.fromString("bb5eeb2c-fa66-4e70-891b-382d87b64814"))) {
				FrontEnd.logger.debugLog("Received AcknowledgeMessage");
				if (FrontEnd.m_connectedClients.containsKey(msgTopClass.getName())) {
					ClientConnection cc = FrontEnd.m_connectedClients.get(msgTopClass.getName());
					cc.addMessageToClient(msgTopClass);
				}
			}
			// DrawObjectsMessage
			else if (msgTopClass.getUUID().equals(UUID.fromString("54f642d7-eaf6-4d62-ad2d-316e4b821c03"))) {
				FrontEnd.logger.debugLog("Received DrawObjectsMessage");
				if (FrontEnd.m_connectedClients.containsKey(msgTopClass.getName())) {
					FrontEnd.m_connectedClients.get(msgTopClass.getName()).addMessageToClient(msgTopClass);
				}
			}
			// PresentationMessage
			else if (msgTopClass.getUUID().equals(UUID.fromString("8e69d7fb-4ca9-46de-b33d-cf1dc72377cd"))) {
				FrontEnd.logger.debugLog("Received PresentationMessage");
			}
			// ElectionMessage
			else if (msgTopClass.getUUID().equals(UUID.fromString("eceb2eb4-361c-425f-a760-a2cd434bbdff"))) {
				FrontEnd.logger.debugLog("Received ElectionMessage");
				try {
					m_channel.send(new Message(FrontEnd.primaryRM,
							PresentationMessage.createFrontEndPresentation().serialize()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// CoordinatorMessage
			else if (msgTopClass.getUUID().equals(UUID.fromString("88486f0c-1a3e-428e-a90c-3ceda5426f27"))) {
				FrontEnd.primaryRM = msg.getSrc();
				FrontEnd.logger.debugLog(
						"Received CoordinatorMessage, will send presentation to primary:" + FrontEnd.primaryRM);
				try {
					// PresentationMessage msg1 = PresentationMessage.createFrontEndPresentation();
					m_channel.send(new Message(FrontEnd.primaryRM,
							PresentationMessage.createFrontEndPresentation().serialize()));
				} catch (Exception e) {
					e.printStackTrace();
				}

				FrontEnd.logger.debugLog(msg.getSrc() + " destination");
			} else {
				FrontEnd.logger.debugLog("Could not find the correct type");
			}
		}
	}
}
