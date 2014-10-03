package edu.ucsd.cse110.shared;

public interface Constants {
	public static String ACTIVEMQ_URL = "tcp://localhost:61616";
	//public static String USERNAME = "max";	
	//public static String PASSWORD = "pwd";	
	public static String ADMIN_QUEUE_NAME = "test";
	public static String SERVER_RELAY = "SERVER_RELAY";
	
	public enum MessageType { 
		LOGIN, TEXT, REGISTER, LOGOFF, USERLIST, JOINCHATROOM, LISTCHATROOMS, LISTCHATROOMUSERS, EXITCHATROOM,
		UPDATEUSERLIST;
	}
}