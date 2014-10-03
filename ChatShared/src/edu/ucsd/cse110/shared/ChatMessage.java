package edu.ucsd.cse110.shared;

import java.io.Serializable;

import javax.jms.ObjectMessage;

public interface ChatMessage extends Serializable {
	public Constants.MessageType getMessageType();
	public ClientID getClient();
}
