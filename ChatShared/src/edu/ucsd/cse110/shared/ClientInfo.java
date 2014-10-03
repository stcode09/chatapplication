package edu.ucsd.cse110.shared;

public class ClientInfo {
	private boolean isOnline;
	private final String username;
	private final String password;
	private final ClientID id;
	
	public ClientInfo( ClientID id, String username, String password ) {
		this.username = username;
		this.password = password;
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public ClientID getID() {
		return id;
	}
	
	public boolean isOnline() {
		return isOnline;
	}
	
	public void setOnline( boolean b ) {
		isOnline = b;
	}
	
	public boolean equals( Object otherObject ) {
		if( !(otherObject instanceof ClientInfo)) return false;
		ClientInfo info = (ClientInfo) otherObject;

		//System.out.println("EQUALS: " + info.username + "/" + this.username + ":" +
		//		info.password +"/"+ this.password + " (" +(info.username.equals(this.username) &&
		//		info.password.equals(this.password)) + ")" );
		
		return info.username.equals(this.username) &&
				info.password.equals(this.password);
	}
	
	public String toString() {
		return username + "(" + id + ")";
	}
}
