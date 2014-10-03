package edu.ucsd.cse110.client;

import java.io.IOException;
import java.util.Scanner;

import javax.jms.JMSException;

public class CLI {

	private ChatClient client;
	private String clientName;
	private Relay relay = Relay.getInstance();

	public CLI(ChatClient client) {
		this.client = client;
	}

	// Display prompt
	public void prompt(String prompt) {
		System.out.print(prompt + " ");
		System.out.flush();
	}

	// Flush buffer
	public void flush() {
		int i;
		try {
			while(System.in.available() != 0) 
				i = System.in.read();
		} catch (java.io.IOException e) {
			System.out.println("Input Error Occured");
		}
	}

	// Get input from console
	public String readInput() {

		int c;
		String str = "";
		boolean end = false;

		while(!end) {
			try {
				c = System.in.read();
				if(c < 0 || (char) c == '\n')
					end = true;
				else if((char) c != '\r')
					str = str + (char) c;
			} catch (java.io.IOException e) {
				System.out.println("Input Error Occured");
				end = true;
			}
		}

		return str;
	}

	// Read integer from the console for selection
	public int readInt(String prompt) {
		while(true) {
			flush();
			prompt(prompt);
			try {
				return Integer.valueOf(readInput().trim()).intValue();
			} catch (NumberFormatException e) {
				System.out.println("Input Error. Input is not an integer");
			}
		}	
	}

	// Initial display menu for command line interface
	public void displayMenu() {
		System.out.println("============================");
		System.out.println("|   CHAT APPLICATION CLI   |");
		System.out.println("============================");
		System.out.println("| Select An Option:        |");
		System.out.println("|    1. Login              |");
		System.out.println("|    2. Register           |");
		System.out.println("|    3. Menu               |");
		System.out.println("|    4. Exit               |");
		System.out.println("============================");
	}
	
	public String[] getLogin() {
		@SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
				
		System.out.print("Username: ");
		String username = input.nextLine();
		System.out.print("Password: ");
		String password = input.nextLine();
		
		return new String[] { username, password };
	}

	// Login client and setup lobby
	public boolean login() throws IOException, JMSException, InterruptedException {
		String[] login = getLogin();
		client.sendLoginMessage( login[0], login[1]);

		while(true) {
			System.out.print("");
			if(relay.isReceived())
				break;
		}
		relay.setReceived(false);

		Thread.sleep(100);

		if(relay.isLoggedIn()) {
			System.out.println("Login Successful! Joining main lobby...");
			relay.setName(client.getClientName());
			return true;
		}
		else {
			System.out.println("Login failed! Please check your input and try again.");
			return false;
		}
	}

	// Register client 
	public boolean register() throws JMSException, InterruptedException {
		String[] login = getLogin();

		client.sendRegisterMessage( login[0], login[1] );

		while(true) {
			System.out.print("");
			if(relay.isReceived())
				break;
		}
		relay.setReceived(false);

		Thread.sleep(100);

		if(relay.isRegistered()) {
			System.out.println("User Registered!");
			return true;
		}
		else {
			System.out.println("Registration failed. Please try again.");
			return false;
		}	
	}

	public boolean logoff() throws JMSException {

		client.sendLogOffMessage();
		System.out.println("Log Off Successful");
		relay.setChatRoomName(null);
		
		return true;
	}

	public boolean userList() throws JMSException, InterruptedException {

		client.sendUserListMessage();
		while(true) {
			System.out.print("");
			if(relay.isReceived())
				break;
		}
		relay.setReceived(false);

		Thread.sleep(100);

		if(relay.isList()) {
			System.out.println("\nOnline users: " + relay.getUserList().size());
			for(String user: relay.getUserList())
				System.out.println("User: " + user);
			System.out.println();
			return true;
		}
		else {
			System.out.println("Something went wrong...");
			return false;
		}
	}

	public boolean listChatRooms() throws JMSException, InterruptedException {
		client.sendListChatRoomsMessage();
		while(true) {
			System.out.print("");
			if(relay.isReceived())
				break;
		}
		relay.setReceived(false);
		
		Thread.sleep(100);

		if(relay.isChatList()) {
			System.out.println("\nActive Chat Rooms: " + relay.getChatRooms().size());
			int i = 1;
			for(String rooms: relay.getChatRooms()) {
				System.out.println(i +". " + rooms);
				i++;
			}
			System.out.println();
			return true;
		}
		else {
			System.out.println("Something went wrong...");
			return false;
		}

	}
	
	public boolean listChatRoomUsers() throws JMSException, InterruptedException {
		client.sendChatRoomUserListMessage(relay.getChatRoomName());
		while(true) {
			System.out.print("");
			if(relay.isReceived())
				break;
		}
		relay.setReceived(false);
		
		Thread.sleep(100);

		if(relay.isChatRoomUserList()) {
			System.out.println("\nUsers in Current Chat Room: " + relay.getChatRoomUsers().size());
			int i = 1;
			for(String users: relay.getChatRoomUsers()) {
				System.out.println(i +". " + users);
				i++;
			}
			System.out.println();
			return true;
		}
		else {
			System.out.println("Something went wrong...");
			return false;
		}

	}

	public boolean joinChatRoom() throws JMSException, InterruptedException {
		System.out.println( "\nEnter the name of the chat room you would like to join" );
		prompt("Chat Room Name: >");
		String roomName = readInput();
		client.sendJoinChatRoomMessage(roomName);

		while(true) {
			System.out.print("");
			if(relay.isReceived())
				break;
		}
		relay.setReceived(false);

		Thread.sleep(100);

		System.out.println("Chat Room \"" + roomName + "\" joined");
		relay.setChatRoomName(roomName);

		return true;

	}

	public void sendMessage() throws JMSException {
		System.out.println( "Enter your message" );
		prompt(">");
		client.send( readInput() );
	}
	
	public boolean exitChatRoom() throws JMSException, InterruptedException {
		client.sendExitChatRoomMessage(relay.getChatRoomName());
		while(true) {
			System.out.print("");
			if(relay.isReceived())
				break;
		}
		relay.setReceived(false);

		Thread.sleep(100);
		
		System.out.println("\nExit Chat Room OK\n");
		
		return true;
	}

	public void lobbyMenu() {
		System.out.println("==============================");
		System.out.println("|          LOBBY             |");
		System.out.println("==============================");
		System.out.println("| Select An Option:          |");
		System.out.println("|    1. List Online Users    |");
		System.out.println("|    2. List Chat Room Users |");
		System.out.println("|    3. List Chat Rooms      |");
		System.out.println("|    4. Join Chat Room       |");
		System.out.println("|    5. Start Chatting       |");
		System.out.println("|    6. Exit Chat Room       |");
		System.out.println("|    7. Log Off              |");
		System.out.println("|    8. Broadcast Message    |");
		System.out.println("|    9. Menu                 |");
		System.out.println("==============================");
	}

	public boolean lobby() throws JMSException, InterruptedException, IOException {

		this.lobbyMenu();
		int val;
		boolean exit = false;
		boolean chatRoom = false;
		ClientMessagers c1 = new ClientMessagers();

		while(!exit) {

			if(relay.getChatRoomName() == null) {
				chatRoom = false;
				val = this.readInt("LOBBY: >");
			}
			else
				val = this.readInt("LOBBY [" + relay.getChatRoomName() + "]: >");

			switch(val) {

			case 1:
				userList();
				break;
			case 2:
				if(!chatRoom)
					System.out.println("\nYou must join a chat room first!\n");
				else
					listChatRoomUsers();
				break;
			case 3: 
				listChatRooms();
				break;
			case 4:
				if(joinChatRoom()) {
					chatRoom = true;
					c1.sendMessages(client);	
				}
				break;
			case 5:
				if(!chatRoom) {
					System.out.println("\nYou need to join a chat room first!\n");
				} else {
					c1.sendMessages(client);
				}
				break;
			case 6:
				if(!chatRoom) {
					System.out.println("\nYou need to join a chat room first!\n");
				} else {
					exitChatRoom();
				}
				break;
			case 7: 
				logoff();
				return false;
			case 8:
				sendMessage();
				break;
			case 9: 
				lobbyMenu();
				break;
			default:
				System.out.println("Please enter a valid option!");
				break;
			}
		}
		return false;	
	}

}