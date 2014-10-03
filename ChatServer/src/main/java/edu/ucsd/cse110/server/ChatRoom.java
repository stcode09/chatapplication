package edu.ucsd.cse110.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.InvalidDestinationException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;

import edu.ucsd.cse110.shared.ChatTextMessage;
import edu.ucsd.cse110.shared.ClientID;

public class ChatRoom {
	private final Map<ClientID, Destination> members = new HashMap<ClientID, Destination>();
	private final MessageProducer producer;
	private String name;
	private final Destination destination;
	
	public ChatRoom( MessageProducer p, Destination dest, String name ) {
		producer = p;
		this.name = name;
		this.destination = dest;
	}
	
	public void sendMessage( Message m ) throws JMSException {
		
		System.out.println( "---SENDING CHATROOM MESSAGE TO " + members.size() + " CLIENTS---" );
		for( Map.Entry<ClientID, Destination> entry : members.entrySet()) {
			try {
				producer.send( entry.getValue(), m );
			} catch (InvalidDestinationException exception ) {
				members.remove(entry.getKey());
			}
		}
	}
	
	public void addMember( ClientID id, Destination dest ) {
		members.put(id, dest);
	}
	
	public void removeMember( ClientID id ) {
		members.remove(id);
	}
	
	public void setName( String name ) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public Destination getDestination() {
		return destination;
	}
	
	public List<String> getMembers() {
		List<String> retList = new ArrayList<String>();
		
		for( Map.Entry<ClientID, Destination> entry : members.entrySet() ) {
			retList.add(entry.getKey().getClientName());
		}
		return retList;
	}
}
