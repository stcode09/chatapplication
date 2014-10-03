package edu.ucsd.cse110.server;

import java.util.ArrayList;
import java.util.List;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;

import edu.ucsd.cse110.shared.ClientInfo;

public class ChatRoomManager {
	
	private List<ChatRoom> rooms = new ArrayList<ChatRoom>();
	
	private static ChatRoomManager elvis;
	
	private ChatRoomManager() {}
	
	public static ChatRoomManager getInstance() {
		return (elvis == null ) ? (elvis = new ChatRoomManager()) : elvis;
	}
	
	public ChatRoom createNewChatRoom( String roomName, MessageProducer p, Session session ) throws JMSException {
		final Destination dest = session.createQueue(roomName);
		final ChatRoom newRoom = new ChatRoom(p, dest, roomName);
		session.createConsumer(dest).setMessageListener( new MessageListener() {
			public void onMessage(Message arg0) {
				try {
					System.out.println( "---RELAYING MESSAGE TO CHATROOM " + newRoom.getName() + "---");
					System.out.println( "---MESSAGE: " + arg0 + "---");
					
					arg0.setJMSReplyTo(dest);
					newRoom.sendMessage(arg0);
				} catch (JMSException e) {
					throw new RuntimeException( e );
				}
			}
		});
		rooms.add(newRoom);
		return newRoom;
	}
	
	public List<String> getChatRoomsList() {
		List<String> roomsList = new ArrayList<String>();
		for( ChatRoom chatRoom : rooms ) {
			roomsList.add(chatRoom.getName());
		}
		
		return roomsList;
	}
	
	public boolean chatRoomExists( String roomName ) {
		for( ChatRoom chatRoom : rooms ) {
			if( chatRoom.getName().equals(roomName) ) return true;
		}
		return false;
	}
	
	public ChatRoom getChatRoomByName( String roomName ) {
		for( ChatRoom chatRoom : rooms ) {
			if( chatRoom.getName().equals(roomName) ) return chatRoom;
		}
		return null;
	}

	public List<ChatRoom> getChatRooms() {
		return rooms;
	}
}
