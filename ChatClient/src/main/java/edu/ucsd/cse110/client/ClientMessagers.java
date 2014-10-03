package edu.ucsd.cse110.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.jms.JMSException;

public class ClientMessagers {
			
	public void sendMessages(ChatClient client) throws JMSException{
		
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	    String msg;
		System.out.println("Enter messages to send (type \"stop\" to return to lobby):");
		try {
		while((msg = in.readLine()) != null) {
			if(msg.equalsIgnoreCase("stop")) {
				System.out.println("Stopping...");
				break;
			}
			else if(msg.length() != 0)
				client.send(msg);		
		}
		} catch ( IOException exception ) {
			throw new RuntimeException(exception);
		}
		
	}
}
