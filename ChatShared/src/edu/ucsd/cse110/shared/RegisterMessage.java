package edu.ucsd.cse110.shared;

import javax.jms.ObjectMessage;

import edu.ucsd.cse110.shared.Constants.MessageType;

public class RegisterMessage implements ChatMessage {
	
	private static final long serialVersionUID = 133777016969482532L;
	private final String username;
	private final String password;
	private final ClientID client;
	private boolean status;
	
	public RegisterMessage( ClientID client, String username, String password ) {
		this.client = client;
		this.username = username;
		this.password = password;
	}
	
	public RegisterMessage( ClientID client, String username, String password, boolean status ) {
		this.client = client;
		this.username = username;
		this.password = password;
		this.status = status;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public MessageType getMessageType() {
		return MessageType.REGISTER;
	}
	
	public ClientID getClient() {
		return client;
	}
	
	public boolean getStatus() {
		return status;
	}
}