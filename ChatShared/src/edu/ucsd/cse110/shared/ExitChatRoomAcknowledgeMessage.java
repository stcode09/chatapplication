package edu.ucsd.cse110.shared;

import edu.ucsd.cse110.shared.Constants.MessageType;

public class ExitChatRoomAcknowledgeMessage implements ChatMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5633577432367254016L;
	
	private ClientID client;
	
	public ExitChatRoomAcknowledgeMessage( ClientID id ) {
		client = id;
	}

	@Override
	public MessageType getMessageType() {
		return MessageType.EXITCHATROOM;
	}

	@Override
	public ClientID getClient() {
		return client;
	}
	
}
