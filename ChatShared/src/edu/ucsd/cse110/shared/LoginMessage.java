package edu.ucsd.cse110.shared;

import edu.ucsd.cse110.shared.Constants.MessageType;

public class LoginMessage implements ChatMessage {
	
	private static final long serialVersionUID = 335377016969482532L;
	private final String username;
	private final String password;
	private final ClientID client;
	private boolean status;
	
	public LoginMessage( ClientID client, String username, String password ) {
		this.username = username;
		this.password = password;
		this.client = client;
	}
	
	public LoginMessage( ClientID client, String username, String password, boolean status ) {
		this.username = username;
		this.password = password;
		this.client = client;
		this.status = status;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public MessageType getMessageType() {
		return MessageType.LOGIN;
	}
	
	public ClientID getClient() {
		return client;
	}
	
	public boolean getLoginStatus() {
		return status;
	}

}
