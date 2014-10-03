package edu.ucsd.cse110.shared;

import java.io.Serializable;

public class ClientID implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2355868123720790075L;
	private final String token;
	private String clientName; 
	
	public ClientID( String id ) {
		token = id;
	}
	
	public String getID() {
		return token;
	}
	
	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	
	public int hashCode() {
		return token.hashCode();
	}
	
	public String toString() {
		return token;
	}
	
	public boolean equals( Object other ) {
		if( !(other instanceof ClientID) ) {
			return false;
		}
		
		return token.equals(((ClientID) other).token);
	}
}
