package edu.ucsd.cse110.shared;

import java.util.List;

import edu.ucsd.cse110.shared.Constants.MessageType;


public class ChatRoomUsersMessage implements ChatMessage {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3640706201439354359L;
	private ClientID id;
	private String chatRoom;
	private List<String> users;
	
	public ChatRoomUsersMessage(ClientID id, String chatRoom) {
		this.id = id;
		this.chatRoom = chatRoom;
		this.users = null;
	}
	
	public ChatRoomUsersMessage(ClientID id, String chatRoom, List<String> users ) {
		this.id = id;
		this.chatRoom = chatRoom;
		this.users = users;
	}

	public MessageType getMessageType() {
		return MessageType.LISTCHATROOMUSERS;
	}

	public ClientID getClient() {
		return id;
	}
	
	public String getChatRoom() {
		return chatRoom;
	}
	
	public List<String> getUsers() {
		return users;
	}

}
