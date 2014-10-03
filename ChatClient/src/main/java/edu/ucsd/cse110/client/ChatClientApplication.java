package edu.ucsd.cse110.client;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;

import edu.ucsd.cse110.shared.ChatMessage;
import edu.ucsd.cse110.shared.ChatRoomUsersMessage;
import edu.ucsd.cse110.shared.ChatTextMessage;
import edu.ucsd.cse110.shared.Constants;
import edu.ucsd.cse110.shared.ExitChatRoomAcknowledgeMessage;
import edu.ucsd.cse110.shared.JoinChatRoomMessage;
import edu.ucsd.cse110.shared.ListChatRoomsMessage;
import edu.ucsd.cse110.shared.LogOutMessage;
import edu.ucsd.cse110.shared.LoginMessage;
import edu.ucsd.cse110.shared.RegisterMessage;
import edu.ucsd.cse110.shared.UserListMessage;

public class ChatClientApplication {

	
	/*
	 * This inner class is used to make sure we clean up when the client closes
	 */
	static private class CloseHook extends Thread {
		ActiveMQConnection connection;
		
		private CloseHook(ActiveMQConnection connection) {
			this.connection = connection;
		}
		
		public static Thread registerCloseHook(ActiveMQConnection connection) {
			Thread ret = new CloseHook(connection);
			Runtime.getRuntime().addShutdownHook(ret);
			return ret;
		}
		
		public void run() {
			try {
				System.out.println("Closing ActiveMQ connection");
				connection.close();
			} catch (JMSException e) {
				/* 
				 * This means that the connection was already closed or got 
				 * some error while closing. Given that we are closing the
				 * client we can safely ignore this.
				*/
			}
		}
	}
	
	private static List<ServerMessageListener> listeners = new ArrayList<ServerMessageListener>();

	/*
	 * This method wires the client class to the messaging platform
	 * Notice that ChatClient does not depend on ActiveMQ (the concrete 
	 * communication platform we use) but just in the standard JMS interface.
	 */
	private static ChatClient wireClient() throws JMSException, URISyntaxException {
		ActiveMQConnection connection = 
				ActiveMQConnection.makeConnection(
				/*Constants.USERNAME, Constants.PASSWORD,*/ Constants.ACTIVEMQ_URL);
        connection.start();
        CloseHook.registerCloseHook(connection);
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue destQueue = session.createQueue(Constants.ADMIN_QUEUE_NAME);
        
        MessageProducer producer = session.createProducer(destQueue);
        producer.setDeliveryMode( DeliveryMode.NON_PERSISTENT );
        
        Destination tempDest = session.createTemporaryQueue();
        MessageConsumer consumer = session.createConsumer( tempDest );
       
		final Relay relay = Relay.getInstance();
		
		final ChatClient client = new ChatClient(producer, tempDest, session);
        
        consumer.setMessageListener( new MessageListener() {
        	
			
			public void onMessage(Message arg0) {
				try {
										
					// Object Message
					if(arg0 instanceof ObjectMessage) {
						ChatMessage m = (ChatMessage) ((ObjectMessage) arg0).getObject();
						String str = m.getMessageType().toString();
						
						if(str.equals("LOGIN")) {
							LoginMessage l = (LoginMessage) m;
							if(l.getLoginStatus()) {
								relay.setLoggedIn(true);
								relay.setLoggedOff(false);
							} else {
								relay.setLoggedIn(false);
								relay.setLoggedOff(true);
							}
						}
						
						if(str.equals("REGISTER")) {
							RegisterMessage l = (RegisterMessage) m;
							if(l.getStatus()) {
								relay.setRegistered(true);
							} else {
								relay.setRegistered(false);
							}
						}
						
						if(str.equals("LOGOFF")) {
							LogOutMessage l = (LogOutMessage) m;
							if(l.getStatus()) {
								relay.setLoggedIn(false);
								relay.setLoggedOff(true);
							} else {
								relay.setLoggedIn(true);
								relay.setLoggedOff(false);
							}
						}
						
						if(str.equals("USERLIST")) {
							UserListMessage u = (UserListMessage) m;
							relay.setUserList(u.getOnlineUsers());
							relay.setList(true);
						}
						
						if(str.equals("LISTCHATROOMS")) {
							ListChatRoomsMessage l = (ListChatRoomsMessage) m;
							relay.setChatRooms(l.getRooms());
							relay.setChatList(true);
						}
						
						if(str.equals("LISTCHATROOMUSERS")) {
							ChatRoomUsersMessage l = (ChatRoomUsersMessage) m;
							relay.setChatRoomUsers(l.getUsers());
							relay.setChatRoomUserList(true);
						}
						
						if(str.equals("JOINCHATROOM")) {
							JoinChatRoomMessage l = (JoinChatRoomMessage) m;
							relay.setChatRoomName(l.getChatRoomName());
						}
						
						if(str.equals("TEXT")) {
							ChatTextMessage c = (ChatTextMessage) m;
							if(c.getUsername().equals(relay.getName()))
								System.out.println("[You]: " + c.getText());
							else
								System.out.println("[" + c.getUsername() + "]: " + c.getText());
						}
						
						if(str.equals("EXITCHATROOM")) {
							ExitChatRoomAcknowledgeMessage l = (ExitChatRoomAcknowledgeMessage) m;
							relay.setChatRoomName(null);
						}
												
						relay.setReceived(true);
						
						Destination dest = arg0.getJMSReplyTo();
						if(dest != null) {
							client.setDestination(dest);
						}
						
					} 
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		} );
        
        return client;
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
					
		try {	
			/* 
			 * We have some other function wire the ChatClient 
			 * to the communication platform
			 */
			ChatClient client = wireClient();
			
	        System.out.println("Starting ChatClient...");
	        
	        CLI cli = new CLI(client);
	        int val;
	        
	        cli.displayMenu();
	        while((val = cli.readInt("CLI: >")) != 4) {
	     
	        	switch(val) {
	        	default:
	        		System.out.println("Please enter a valid option!");
	        		break;
	        	case 1:
	        		if(cli.login())
	        			if(!cli.lobby())
	        				break;
	        		break;
	        	case 2:
	        		cli.register();
	        		break;
	        	case 3:
	        		cli.displayMenu();
	        		break;
	        	}      
	        }
	        
			System.out.println("Exiting...");	
	        System.exit(0);
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		

	}

}