package edu.ucsd.cse110.client;

import edu.ucsd.cse110.shared.ChatMessage;

public interface ServerMessageListener {
	
	public void onMessageReceived( ChatMessage m );

}