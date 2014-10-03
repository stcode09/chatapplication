package edu.ucsd.cse110.shared;

import edu.ucsd.cse110.shared.Constants.MessageType;

public class UpdateUserListMessage implements ChatMessage {

	@Override
	public MessageType getMessageType() {
		return MessageType.UPDATEUSERLIST;
	}

	@Override
	public ClientID getClient() {
		return null;
	}

}
