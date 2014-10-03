package edu.ucsd.cse110.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import edu.ucsd.cse110.shared.ChatRoomUsersMessage;
import edu.ucsd.cse110.shared.ChatTextMessage;
import edu.ucsd.cse110.shared.ClientID;
import edu.ucsd.cse110.shared.ExitChatRoomMessage;
import edu.ucsd.cse110.shared.JoinChatRoomMessage;
import edu.ucsd.cse110.shared.ListChatRoomsMessage;
import edu.ucsd.cse110.shared.LogOutMessage;
import edu.ucsd.cse110.shared.LoginMessage;
import edu.ucsd.cse110.shared.RegisterMessage;
import edu.ucsd.cse110.shared.UserListMessage;

public class ChatClient {
	private final MessageProducer producer;
	private final Destination privateQueue;
	private final Session session;
	private final ClientID id;
	private String clientName;
	private Map<String, Destination> chatRooms;
	private Destination currChatRoom;
	private MessageProducer currChatRoomProducer;
	private String currChatRoomName;

	public ChatClient(MessageProducer producer, Destination dest,
			Session session) {
		super();
		this.producer = producer;
		this.session = session;
		privateQueue = dest;
		id = new ClientID(createRandomString());
		currChatRoomName = null;
		chatRooms = new HashMap<String,Destination>();
	}

	public String getChatRoomName() {
		return currChatRoomName;
	}
	
	public void addChatRoom( String name, Destination destination ) {
		chatRooms.put(name, destination);
	}
	
	public void removeChatRoomDestination( String name ) {
		chatRooms.remove(name);
	}

	public void setChatRoom(String name) {
		currChatRoomName = name;
		setDestination(chatRooms.get(name));
		
		System.out.println( "NEW CHATROOM (" + name + ", "+ currChatRoom + ")");
	}
	
	public void setDestination( Destination destination ) {
		currChatRoom = destination;
		try {
			currChatRoomProducer = session.createProducer(destination);
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
	}

	public void send(String msg) throws JMSException {
		ObjectMessage m = session.createObjectMessage();
		m.setObject(new ChatTextMessage(id, clientName, msg));
		m.setJMSReplyTo(privateQueue);
		m.setJMSCorrelationID(createRandomString());

		if (currChatRoom == null) {
			producer.send(m);
		} else {
			currChatRoomProducer.send(m);
		}
	}

	public void sendJoinChatRoomMessage(String roomName) throws JMSException {
		ObjectMessage msg = session.createObjectMessage();
		msg.setObject(new JoinChatRoomMessage(id, roomName));
		msg.setJMSReplyTo(privateQueue);
		msg.setJMSCorrelationID(createRandomString());

		producer.send(msg);
	}

	public void sendListChatRoomsMessage() throws JMSException {
		ObjectMessage msg = session.createObjectMessage();
		msg.setObject(new ListChatRoomsMessage(id));
		msg.setJMSReplyTo(privateQueue);
		msg.setJMSCorrelationID(createRandomString());

		producer.send(msg);
	}

	public void sendLoginMessage(String username, String password)
			throws JMSException {
		ObjectMessage msg = session.createObjectMessage();
		this.clientName = username;
		this.id.setClientName(clientName);
		msg.setObject(new LoginMessage(id, username, password));
		msg.setJMSReplyTo(privateQueue);
		msg.setJMSCorrelationID(createRandomString());

		producer.send(msg);
	}

	public void sendUserListMessage() throws JMSException {
		ObjectMessage msg = session.createObjectMessage();
		msg.setObject(new UserListMessage(id));
		msg.setJMSReplyTo(privateQueue);
		msg.setJMSCorrelationID(createRandomString());
		producer.send(msg);
	}

	public void sendChatRoomUserListMessage(String chatRoom)
			throws JMSException {
		ObjectMessage msg = session.createObjectMessage();
		msg.setObject(new ChatRoomUsersMessage(id, chatRoom));
		msg.setJMSReplyTo(privateQueue);
		msg.setJMSCorrelationID(createRandomString());
		producer.send(msg);
	}

	public void sendLogOffMessage() throws JMSException {
		ObjectMessage msg = session.createObjectMessage();
		msg.setObject(new LogOutMessage(id));
		msg.setJMSReplyTo(privateQueue);
		msg.setJMSCorrelationID(createRandomString());
		producer.send(msg);
	}

	public void sendExitChatRoomMessage(String chatRoom) throws JMSException {
		ObjectMessage msg = session.createObjectMessage();
		msg.setObject(new ExitChatRoomMessage(id, chatRoom));
		msg.setJMSReplyTo(privateQueue);
		msg.setJMSCorrelationID(createRandomString());
		producer.send(msg);
	}

	public void sendRegisterMessage(String username, String password)
			throws JMSException {
		ObjectMessage msg = session.createObjectMessage();

		msg.setObject(new RegisterMessage(id, username, password));
		msg.setJMSReplyTo(privateQueue);
		msg.setJMSCorrelationID(createRandomString());
		producer.send(msg);

	}

	private static String createRandomString() {
		Random r = new Random();
		return Long.toHexString(r.nextLong());
	}

	public String getClientName() {
		return this.clientName;
	}

	public Destination getCurrentDestination() {
		return currChatRoom;
	}
	
	public boolean hasChatRoom( Destination destination ) {
		return chatRooms.containsValue(destination);
	}
}
