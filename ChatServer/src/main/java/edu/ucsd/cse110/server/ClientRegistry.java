package edu.ucsd.cse110.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.shared.ClientID;
import edu.ucsd.cse110.shared.ClientInfo;

// Users class to handle user database, user register, and user login
public class ClientRegistry {
	
	private String fileName = "userlist";
	
	private FileWriter fileWriter;
	private BufferedWriter bufferedWriter;
	
	private FileReader fileReader;
	private BufferedReader bufferedReader;
		
	private List<ClientInfo> clients;
	private List<ClientInfo> onlineClients;
	
	private String line = "";
	private String split = ";";
	
	private static ClientRegistry elvis;
	
	public static ClientRegistry getInstance() {
		return (elvis == null) ? elvis = new ClientRegistry() : elvis;
	}
	
	private ClientRegistry() {		
		clients = new ArrayList<ClientInfo>();
		onlineClients = new ArrayList<ClientInfo>();
		try {
			this.readClients();
			this.updateClients();
		} catch (IOException e) {
			throw new RuntimeException( e );
		}
	}
	
	// Update user list
	private boolean updateClients() {
		try {
			fileWriter = new FileWriter(fileName);
			bufferedWriter = new BufferedWriter(fileWriter);

			for (ClientInfo ent : clients ) {
				bufferedWriter.write(ent.getUsername() + ";"
						+ ent.getPassword() + "\n");
			}

			bufferedWriter.close();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
		System.out.println("Done updating userlist...");

		return true;

	}
	
	// Read the users into database
	private boolean readClients() throws IOException {
		
		fileReader = new FileReader(fileName);
		bufferedReader = new BufferedReader(fileReader);
			
		while ((line = bufferedReader.readLine()) != null) {
			
			String[] str = line.split(split);
			clients.add(new ClientInfo(null, str[0], str[1]));
			System.out.println( "USER: (" + str[0] + "," + str[1] + ")" );
			
		}
		
		bufferedReader.close();
		System.out.println("Done reading users...");
		
		return true;
				
	}
	
	// Register a user
	public boolean registerClient(ClientInfo info) {
				
		if(clients.contains(info)) {
			System.out.println("User Already Exists!");
			return false;
		}
		else {
			info.setOnline(false);
			clients.add(info);
			this.updateClients();
			System.out.println("User: " + info.getUsername() + " Registered!");
			
			return true;
		}
		
	}
	
	/*public boolean unregisterClient(ClientID id) {
		
		if(!clients.contains(id)) {
			System.out.println("User does not exist!");
			return false;
		}
		else {
			ClientInfo info = clients.remove(clients.indexOf(id));
			System.out.println("User: " + info.getUsername() + " unregistered!");
			this.updateClients();
			return true;
		}
		
	}*/
	
	// Handle login. Called when server gets request to login
	public boolean handleLogin(ClientInfo info) {
		System.out.println( "LOGIN " + info );
		//System.out.println("Database:" );
		//for( ClientInfo i : noIDClients ) {
		//	System.out.println( i );
		//}
		
		if(!clients.contains(info)) {
			System.out.println("User not in database");
			return false;
		}
		else {
			if( clients.contains(info) ) {
				if( clients.get(clients.indexOf(info)).isOnline() ) {
						System.out.println("User already logged in");
						return false;
				} else {
					clients.get(clients.indexOf(info)).setOnline(true);
					onlineClients.add(info);
					System.out.println("Login success");
					return true;
				}
			} else {
				System.out.println("Password mismatch");
				return false;
			}
		}
	}
	
	// Handle log off
	public boolean handleLogOff(ClientID id) {
				
		for( ClientInfo info : onlineClients ) {
			if ( info.getID().equals(id) ) {
				onlineClients.remove(info);
				clients.get( clients.indexOf(info)).setOnline(false);
				return true;
			}
		}
		
		return false;
	}
	
	// Get user list
	public List<ClientInfo> getAllClients() {
		return clients;
	}
	
	// Get online users
	public List<ClientInfo> getOnlineClients() {
		return onlineClients;
	}
	
	// Get number of online users
	public int getNumberOfOnlineClients() {
		return onlineClients.size();
	}
	
	public ClientInfo lookupClient( ClientID id ) {
		for( ClientInfo info : clients ) {
			ClientID id2 = info.getID();
			if( id2 != null && id2.equals(id)) {
				return info;
			}
		}
		
		return null;
	}
	
	/* TEST to add the users to database. 
	 * registerUser method should be called when server gets a request to register a user. 
	 * Working on other methods...
	 */
	/*
	public static void main(String[] args) {
		Users u = new Users();
		try {
			System.out.println("Register user: " + u.registerUser("Shashank", "1234"));
			System.out.println("Login user: " + u.handleLogin("Shashank", "1234"));
			System.out.println("Login user: " + u.handleLogin("Shank", "12345"));
			u.handleLogin("Shashank", "1234");
			System.out.println("Users online: " + u.getNumberOfOnlineUsers());
			System.out.println("Login user: " + u.handleLogin("Max", "1234"));
			System.out.println("Users online: " + u.getNumberOfOnlineUsers());
			System.out.println("Logoff user: " + u.handleLogOff("Shashank"));
			System.out.println("Logoff user: " + u.handleLogOff("Shank"));
			System.out.println("Logoff user: " + u.handleLogOff("Max"));
			System.out.println("Users online: " + u.getNumberOfOnlineUsers());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} */
	

}
