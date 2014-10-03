package edu.ucsd.cse110.shared;

import edu.ucsd.cse110.shared.Constants.MessageType;

public class ExitChatRoomMessage implements ChatMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5633577432367254016L;
	
	private String roomName;
	private ClientID client;
	
	public ExitChatRoomMessage( ClientID id, String roomName ) {
		this.roomName = roomName;
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
	
	public String getChatRoomName() {
		return roomName;
	}

}
