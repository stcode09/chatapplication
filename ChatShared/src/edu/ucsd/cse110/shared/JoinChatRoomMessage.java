package edu.ucsd.cse110.shared;

import edu.ucsd.cse110.shared.Constants.MessageType;

public class JoinChatRoomMessage implements ChatMessage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5533577432367254016L;
	
	private String roomName;
	private ClientID client;
	
	public JoinChatRoomMessage( ClientID id, String roomName ) {
		this.roomName = roomName;
		client = id;
	}

	@Override
	public MessageType getMessageType() {
		return MessageType.JOINCHATROOM;
	}

	@Override
	public ClientID getClient() {
		return client;
	}
	
	public String getChatRoomName() {
		return roomName;
	}

}
