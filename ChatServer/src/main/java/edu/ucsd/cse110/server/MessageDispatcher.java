package edu.ucsd.cse110.server;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;

import edu.ucsd.cse110.shared.ClientID;

public class MessageDispatcher {

	private final Map<ClientID, Destination> clientMap = new HashMap<ClientID, Destination>();
	private final MessageProducer producer;
	
	public MessageDispatcher( MessageProducer producer ) {
		this.producer = producer;
	}
	
	public void addClient( ClientID id, Destination dest ) {
		clientMap.put( id, dest );
	}
	
	public void removeClient(ClientID id) {
		clientMap.remove(id);
	}
	
	public void sendToAllClients( Message m ) {
		for( Map.Entry<ClientID, Destination> entry : clientMap.entrySet()) {
			try {
				m.setJMSDestination( entry.getValue() );
				producer.send( entry.getValue(), m);
			} catch (JMSException e) {
				clientMap.remove(entry.getKey());
			}
		}
	}
}
