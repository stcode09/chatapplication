package edu.ucsd.cse110.shared;

import java.util.List;

import edu.ucsd.cse110.shared.Constants.MessageType;

public class UserListMessage implements ChatMessage {
	
	private static final long serialVersionUID = 145377016969482532L;
	private final ClientID client;
	private List<String> onlineUsers; 
	
	public UserListMessage( ClientID client) {
		this.client = client;
	}
			
	public MessageType getMessageType() {
		return MessageType.USERLIST;
	}
	
	public ClientID getClient() {
		return client;
	}
	
	public void setOnlineUsers(List<String> onlineUsers) {
		this.onlineUsers = onlineUsers;
	}
	
	public List<String> getOnlineUsers() {
		return this.onlineUsers;
	}
	
	public int getNumberOfOnlineUsers() {
		return this.onlineUsers.size();
	}

}
