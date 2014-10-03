package edu.ucsd.cse110.client;

import java.util.List;

public class Relay {

	static private Relay relay = new Relay();
	
	private boolean loggedIn = false;
	private boolean registered = false;
	private boolean received = false;
	private boolean loggedOff = true;
	private boolean list = false;
	private boolean receivedMsg = false;
	public boolean chatList = false;
	public boolean chatRoomUserList = false;
	
	public boolean isChatRoomUserList() {
		return chatRoomUserList;
	}

	public void setChatRoomUserList(boolean chatRoomUserList) {
		this.chatRoomUserList = chatRoomUserList;
	}

	public boolean isChatList() {
		return chatList;
	}

	public void setChatList(boolean chatList) {
		this.chatList = chatList;
	}

	private List<String> chatRooms;
	private String chatRoomName = null;
	private List<String> chatRoomUsers = null;
	
	private List<String> userList;
	private String name;
	
	private String msg;
	
	// prevent instantiation from other classes
	private Relay(){};
	
	static public Relay getInstance() {
		return relay;
	}

	public List<String> getChatRoomUsers() {
		return chatRoomUsers;
	}

	public void setChatRoomUsers(List<String> chatRoomUsers) {
		this.chatRoomUsers = chatRoomUsers;
	}
	
	public String getChatRoomName() {
		return chatRoomName;
	}

	public void setChatRoomName(String chatRoomName) {
		this.chatRoomName = chatRoomName;
	}
	
	public List<String> getChatRooms() {
		return chatRooms;
	}

	public void setChatRooms(List<String> chatRooms) {
		this.chatRooms = chatRooms;
	}
	
	public boolean isReceivedMsg() {
		return receivedMsg;
	}

	public void setReceivedMsg(boolean receivedMsg) {
		this.receivedMsg = receivedMsg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isLoggedOff() {
		return loggedOff;
	}

	public void setLoggedOff(boolean loggedOff) {
		this.loggedOff = loggedOff;
	}
		
	public boolean isList() {
		return list;
	}

	public void setList(boolean list) {
		this.list = list;
	}

	public List<String> getUserList() {
		return userList;
	}

	public void setUserList(List<String> userList) {
		this.userList = userList;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public boolean isRegistered() {
		return registered;
	}

	public void setRegistered(boolean registered) {
		this.registered = registered;
	}
	
	public boolean isReceived() {
		return received;
	}

	public void setReceived(boolean received) {
		this.received = received;
	}
	
}
