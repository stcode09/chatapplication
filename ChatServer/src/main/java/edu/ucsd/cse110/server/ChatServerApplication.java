package edu.ucsd.cse110.server;

import java.util.ArrayList;
import java.util.List;

import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.broker.BrokerService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import edu.ucsd.cse110.shared.ChatMessage;
import edu.ucsd.cse110.shared.ChatRoomUsersMessage;
import edu.ucsd.cse110.shared.ChatTextMessage;
import edu.ucsd.cse110.shared.ClientInfo;
import edu.ucsd.cse110.shared.Constants;
import edu.ucsd.cse110.shared.ExitChatRoomAcknowledgeMessage;
import edu.ucsd.cse110.shared.ExitChatRoomMessage;
import edu.ucsd.cse110.shared.JoinChatRoomMessage;
import edu.ucsd.cse110.shared.ListChatRoomsMessage;
import edu.ucsd.cse110.shared.LogOutMessage;
import edu.ucsd.cse110.shared.LoginMessage;
import edu.ucsd.cse110.shared.RegisterMessage;
import edu.ucsd.cse110.shared.UpdateUserListMessage;
import edu.ucsd.cse110.shared.UserListMessage;

public class ChatServerApplication implements MessageListener {

	private static ChatServerApplication elvis;

	private Session session;
	private MessageProducer replyProducer;
	private MessageDispatcher dispatcher;
	private ChatRoomManager chatRoomManager;
	private ClientRegistry registry;
		
	public static ChatServerApplication getInstance() throws Exception {
		return (elvis == null) ? (elvis = new ChatServerApplication()) : elvis;
	}
	
	private ChatServerApplication() throws Exception {
		// initialize jms broker
		BrokerService broker = new BrokerService();
		broker.addConnector(Constants.ACTIVEMQ_URL);
		broker.setPersistent(false);
		broker.start();

		@SuppressWarnings("resource")
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
				Connection.class);

		Connection c = context.getBean(Connection.class);
		ConnectionFactory cf = c.connectionFactory();
		javax.jms.Connection c2 = cf.createConnection();
		c2.start();

		session = c2.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination adminQueue = session.createQueue("ADMIN.QUEUE");

		replyProducer = session.createProducer(null);
		replyProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

		MessageConsumer consumer = session.createConsumer(adminQueue);
		consumer.setMessageListener(this);
		context.registerShutdownHook();
		
		dispatcher = new MessageDispatcher( replyProducer );
		registry = ClientRegistry.getInstance();
		chatRoomManager = ChatRoomManager.getInstance();
	}

	public static void main(String[] args) throws Exception {
		ChatServerApplication.getInstance(); // elvis is in the building
	}
	
	public void handleUserList(Message m) throws JMSException {
		
		assert(m instanceof ObjectMessage);
		UserListMessage u = (UserListMessage) ((ObjectMessage) m).getObject();
		
		ObjectMessage msg = session.createObjectMessage();
		
		System.out.println("---SERVER RECEIVE USERLIST OBJECT---");
		System.out.println("ID: " + u.getClient().getID());
				
		UserListMessage list = new UserListMessage(u.getClient());
		List<ClientInfo> clients = registry.getOnlineClients();
		List<String> users = new ArrayList<String>();
		for( ClientInfo id : clients ) {
			users.add(id.getUsername());
		}
		list.setOnlineUsers( users );
		msg.setObject(list);
				
		msg.setJMSCorrelationID(m.getJMSCorrelationID());
		replyProducer.send(m.getJMSReplyTo(), msg);
		
		
	}
	
	public void handleLogin( Message m ) throws JMSException {
		assert ( m instanceof ObjectMessage );
		LoginMessage l;
		
			l = (LoginMessage) ((ObjectMessage) m).getObject();
			
			System.out.println("---SERVER RECEIVE LOGIN OBJECT---");
			System.out.println("ID: " + l.getClient().getID());
			System.out.println("Username: " + l.getUsername());
			System.out.println("Password: " + l.getPassword());
						
			// reply to client
			ObjectMessage response = session.createObjectMessage();
			
			// Check username and password against database
			if(registry.handleLogin(new ClientInfo(l.getClient(), l.getUsername(), l.getPassword()))) {
				dispatcher.addClient(l.getClient(), m.getJMSReplyTo());

				ObjectMessage updateMessage = session.createObjectMessage();
				updateMessage.setObject( new UpdateUserListMessage() );
				dispatcher.sendToAllClients(updateMessage);
				response.setObject( new LoginMessage(l.getClient(), null, null, true));
			}
			else {
				response.setObject( new LoginMessage(l.getClient(), null, null, false));
			}
				
			response.setJMSCorrelationID(m.getJMSCorrelationID());
			
			replyProducer.send(m.getJMSReplyTo(), response);		
	}
	
	public void handleLogOff( Message m ) {
		assert ( m instanceof ObjectMessage );
		LogOutMessage l;
		try {
			
			l = (LogOutMessage) ((ObjectMessage) m).getObject();
			
			System.out.println("---SERVER RECEIVE LOGOFF OBJECT---");
			System.out.println("ID: " + l.getClient().getID());

			// Check username and password against database
			registry.handleLogOff(l.getClient());
			dispatcher.removeClient(l.getClient());
			ObjectMessage updateMessage = session.createObjectMessage();
			updateMessage.setObject( new UpdateUserListMessage() );
			dispatcher.sendToAllClients(updateMessage);
			List<ChatRoom> rooms = chatRoomManager.getChatRooms();
			for (ChatRoom chatRoom : rooms) {
				chatRoom.removeMember(l.getClient());
			}
			
		} catch (JMSException e) {
			throw new RuntimeException( e );
		}	
	}
	
	public void handleExitChatRoom( Message m) throws JMSException {
		System.out.println("---SERVER RECEIVE EXIT CHATROOM OBJECT---");
		ExitChatRoomMessage e = (ExitChatRoomMessage) ((ObjectMessage) m).getObject();
		System.out.println("ID: " + e.getClient().getID() );
		
		ObjectMessage response = session.createObjectMessage();
		List<ChatRoom> rooms = chatRoomManager.getChatRooms();
		for(ChatRoom chatRoom: rooms ) {
			if(chatRoom.getName().equals(e.getChatRoomName())) {
				chatRoom.removeMember(e.getClient());
				ObjectMessage updateMessage = session.createObjectMessage();
				updateMessage.setObject( new UpdateUserListMessage() );
				chatRoom.sendMessage( updateMessage );
			}
		}
		
		response.setObject( new ExitChatRoomAcknowledgeMessage(e.getClient()));
		response.setJMSCorrelationID(m.getJMSCorrelationID());
		replyProducer.send(m.getJMSReplyTo(), response);
	}
	
	public void handleChatRoomUsers( Message m) throws JMSException {
		System.out.println("---SERVER RECEIVE CHATROOMUSERS OBJECT---");
		ChatRoomUsersMessage c = (ChatRoomUsersMessage) ((ObjectMessage) m).getObject();
		System.out.println("ID: " + c.getClient().getID() );
		
		ObjectMessage response = session.createObjectMessage();
		List<String> users = chatRoomManager.getChatRoomByName(c.getChatRoom()).getMembers();
		response.setObject(new ChatRoomUsersMessage(null, c.getChatRoom(), users));
		replyProducer.send(m.getJMSReplyTo(), response);
	}
		
	public void handleRegister( Message m ) {
		assert ( m instanceof ObjectMessage );
		RegisterMessage r;
		try {
			
			r = (RegisterMessage) ((ObjectMessage) m).getObject();
			
			System.out.println("---SERVER RECEIVE REGISTER OBJECT---");
			System.out.println("ID: " + r.getClient().getID());
			System.out.println("Username: " + r.getUsername());
			System.out.println("Password: " + r.getPassword());
						
			// reply to client
			ObjectMessage response = session.createObjectMessage();
			
			// Check username and password against database
			if(registry.registerClient(new ClientInfo(r.getClient(), r.getUsername(), r.getPassword())))
				response.setObject(new RegisterMessage(null, null, null, true));
			else
				response.setObject(new RegisterMessage(null, null, null, false));
			
			response.setJMSCorrelationID(m.getJMSCorrelationID());
			replyProducer.send(m.getJMSReplyTo(), response);
			
		} catch (JMSException e) {
			throw new RuntimeException( e );
		}
	}
	
	public void handleListChatRooms( Message m ) throws JMSException {
		System.out.println("---SERVER RECEIVE LISTCHATROOMS OBJECT---");
		ListChatRoomsMessage lm = (ListChatRoomsMessage) ((ObjectMessage) m).getObject();
		System.out.println("ID: " + lm.getClient().getID() );
		
		ObjectMessage response = session.createObjectMessage();
		response.setObject( new ListChatRoomsMessage(null, chatRoomManager.getChatRoomsList() ) );
		
		replyProducer.send(m.getJMSReplyTo(), response);

	}
		
	public void handleJoinChatRoom( Message m ) throws JMSException {
		System.out.println("---SERVER RECEIVE JOINCHATROOM OBJECT---");
		JoinChatRoomMessage jm = (JoinChatRoomMessage) ((ObjectMessage) m).getObject();
		
		ObjectMessage response = session.createObjectMessage();
		
		if( chatRoomManager.chatRoomExists(jm.getChatRoomName() ) ) {
			// join existing chat room
			ChatRoom room = chatRoomManager.getChatRoomByName(jm.getChatRoomName());
			response.setObject( new JoinChatRoomMessage(null, jm.getChatRoomName()));
			response.setJMSReplyTo( room.getDestination() );
			room.addMember(jm.getClient(), m.getJMSReplyTo());
			ObjectMessage updateMessage = session.createObjectMessage();
			updateMessage.setObject( new UpdateUserListMessage() );
			room.sendMessage(updateMessage);
		} else {
			// create new chat room and join
			ChatRoom room = chatRoomManager.createNewChatRoom(jm.getChatRoomName(), replyProducer, session);
			response.setObject( new JoinChatRoomMessage(null, jm.getChatRoomName()));
			response.setJMSReplyTo(room.getDestination());
			room.addMember(jm.getClient(), m.getJMSReplyTo());
		}
		replyProducer.send(m.getJMSReplyTo(), response);
	}
	
	public void handleText( ChatTextMessage m ) throws JMSException {
		System.out.println("---SERVER RECEIVE TEXT OBJECT---");
		System.out.println("ID: " + m.getClient().getID() );
		System.out.println("TEXT: " + m.getText());
		
		ObjectMessage response = session.createObjectMessage();
		response.setObject(new ChatTextMessage(null, m.getUsername(), m.getText()));
		dispatcher.sendToAllClients(response);
		
	}
	
	public void onMessage(Message m) {
		try {

			if (m instanceof ObjectMessage) {
				ChatMessage message = (ChatMessage) ((ObjectMessage) m).getObject();
				switch (message.getMessageType()) {
				case LOGIN:
					handleLogin(m);
					break;
				case TEXT:
					handleText((ChatTextMessage) message);
					break;
					// Case for registering user called here
				case REGISTER:
					handleRegister(m);
					break;
					// Case for log off user 
				case LOGOFF:
					handleLogOff(m);
					break;
				case USERLIST:
					handleUserList(m);
					break;
				case LISTCHATROOMS:
					handleListChatRooms( m );
					break;
				case JOINCHATROOM:
					handleJoinChatRoom( m );
					break;
				case LISTCHATROOMUSERS:
					handleChatRoomUsers( m );
					break;
				case EXITCHATROOM:
					handleExitChatRoom( m );
					break;
				default:
					throw new RuntimeException("Unknown object message type: "
							+ message.getMessageType());
				}				
			}

		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
	}
}
