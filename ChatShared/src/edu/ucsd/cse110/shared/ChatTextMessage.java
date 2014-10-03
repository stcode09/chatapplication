package edu.ucsd.cse110.shared;

import edu.ucsd.cse110.shared.Constants.MessageType;

public class ChatTextMessage implements ChatMessage {
	
	private static final long serialVersionUID = 1033366273275532157L;
	private final String text;
	private final String username;
	private final ClientID client;
	
	public ChatTextMessage( ClientID client, String username, String text ) {
		this.text = text;
		this.username = username;
		this.client = client;
	}

	@Override
	public MessageType getMessageType() {
		return MessageType.TEXT;
	}
	
	public String getText() {
		return text;
	}
	
	public ClientID getClient() {
		return client;
	}
	
	public String getUsername() {
		return username;
	}
}
