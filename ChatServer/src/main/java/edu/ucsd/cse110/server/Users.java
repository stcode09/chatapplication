package edu.ucsd.cse110.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Users class to handle user database, user register, and user login
public class Users {
	
	private String fileName = "userlist";
	
	private FileWriter fileWriter;
	private BufferedWriter bufferedWriter;
	
	private FileReader fileReader;
	private BufferedReader bufferedReader;
		
	private Map<String, String> users;
	private List<String> onlineUsers;
	
	private String line = "";
	private String split = ";";
	
	public Users() {		
		users = new HashMap<String, String>();
		onlineUsers = new ArrayList<String>();
		try {
			this.readUsers();
			this.updateUsers();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Update user list
	private boolean updateUsers() throws IOException {
		
		fileWriter = new FileWriter(fileName);
		bufferedWriter = new BufferedWriter(fileWriter);
		
		for(Map.Entry<String, String>  ent : users.entrySet()) {
			bufferedWriter.write(ent.getKey() + ";" + ent.getValue() + "\n");
		}
		
		bufferedWriter.close();
		
		System.out.println("Done updating userlist...");
		
		return true;
		
	}
	
	// Read the users into database
	private boolean readUsers() throws IOException {
		
		fileReader = new FileReader(fileName);
		bufferedReader = new BufferedReader(fileReader);
			
		while ((line = bufferedReader.readLine()) != null) {
			
			String[] str = line.split(split);
			users.put(str[0], str[1]);
			
		}
		
		bufferedReader.close();
		System.out.println("Done reading users...");
		
		return true;
				
	}
	
	// Register a user
	public boolean registerUser(String username, String password) throws IOException {
				
		if(users.containsKey(username)) {
			System.out.println("User Already Exists!");
			return false;
		}
		else {
			users.put(username, password);
			this.updateUsers();
			System.out.println("User: " + username + " Registered!");
			return true;
		}
		
	}
	
	// Handle login. Called when server gets request to login
	public boolean handleLogin(String username, String password) {
		
		if(!users.containsKey(username))
			return false;
		else {
			if(password.equals(users.get(username))) {
				if(!onlineUsers.contains(username)) {
					onlineUsers.add(username);
					return true;
				}
				else {
					System.out.println("User already logged in");
					return false;
				}				
			}
			else
				return false;
		}
		
	}
	
	// Handle log off
	public boolean handleLogOff(String username) {
				
		if(onlineUsers.contains(username)) {
			onlineUsers.remove(onlineUsers.indexOf(username));
			return true;
		}
		
		return false;
		
	}
	
	// Get user list
	public Map<String, String> getUsers() {
		return users;
	}
	
	// Get online users
	public List<String> getOnlineUsers() {
		return onlineUsers;
	}
	
	// Get number of online users
	public int getNumberOfOnlineUsers() {
		return onlineUsers.size();
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
