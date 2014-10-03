package edu.ucsd.cse110.shared;

import java.util.List;

import edu.ucsd.cse110.shared.Constants.MessageType;


public class ListChatRoomsMessage implements ChatMessage {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3600706201439354359L;
	private ClientID id;
	private List<String> rooms;
	
	public ListChatRoomsMessage(ClientID id) {
		this.id = id;
	}
	
	public ListChatRoomsMessage(ClientID id, List<String> rooms) {
		this.id = id;
		this.rooms = rooms;
	}

	public MessageType getMessageType() {
		return MessageType.LISTCHATROOMS;
	}

	public ClientID getClient() {
		return id;
	}
	
	public List<String> getRooms() {
		return rooms;
	}

}
