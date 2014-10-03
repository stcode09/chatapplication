package edu.ucsd.cse110.shared;

import edu.ucsd.cse110.shared.Constants.MessageType;

public class LogOutMessage implements ChatMessage {
	
	private static final long serialVersionUID = 145377016969482532L;
	private final ClientID client;
	private boolean status;
	
	public LogOutMessage( ClientID client ) {
		this.client = client;
	}
	
	public LogOutMessage( ClientID client, boolean status ) {
		this.client = client;
		this.status = status;
	}
		
	public MessageType getMessageType() {
		return MessageType.LOGOFF;
	}
	
	public ClientID getClient() {
		return client;
	}
	
	public boolean getStatus() {
		return status;
	}

}
