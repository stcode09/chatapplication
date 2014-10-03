package edu.ucsd.cse110.client;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;

import edu.ucsd.cse110.shared.ChatMessage;
import edu.ucsd.cse110.shared.ChatTextMessage;
import edu.ucsd.cse110.shared.Constants;
import edu.ucsd.cse110.shared.JoinChatRoomMessage;
import edu.ucsd.cse110.shared.LoginMessage;
import edu.ucsd.cse110.shared.RegisterMessage;
import edu.ucsd.cse110.shared.UserListMessage;

public class ChatClientApplicationGUI {

	/*
	 * This inner class is used to make sure we clean up when the client closes
	 */
	static private class CloseHook extends Thread {
		ActiveMQConnection connection;
		ChatClient client;

		private CloseHook(ActiveMQConnection connection, ChatClient client) {
			this.connection = connection;
			this.client = client;
		}

		public static Thread registerCloseHook(ActiveMQConnection connection,
				ChatClient client) {
			Thread ret = new CloseHook(connection, client);
			Runtime.getRuntime().addShutdownHook(ret);
			return ret;
		}

		public void run() {
			try {
				System.out.println("Closing ActiveMQ connection");

				client.sendLogOffMessage();
				connection.close();
			} catch (JMSException e) {
				/*
				 * This means that the connection was already closed or got some
				 * error while closing. Given that we are closing the client we
				 * can safely ignore this.
				 */
			}
		}
	}

	private static List<ServerMessageListener> listeners = new ArrayList<ServerMessageListener>();

	/*
	 * This method wires the client class to the messaging platform Notice that
	 * ChatClient does not depend on ActiveMQ (the concrete communication
	 * platform we use) but just in the standard JMS interface.
	 */
	private static ChatClient wireClient() throws JMSException,
			URISyntaxException {
		ActiveMQConnection connection = ActiveMQConnection.makeConnection(
		/* Constants.USERNAME, Constants.PASSWORD, */Constants.ACTIVEMQ_URL);
		connection.start();
		Session session = connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);
		Queue destQueue = session.createQueue(Constants.ADMIN_QUEUE_NAME);

		MessageProducer producer = session.createProducer(destQueue);
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

		Destination tempDest = session.createTemporaryQueue();
		MessageConsumer consumer = session.createConsumer(tempDest);

		// final Relay relay = Relay.getInstance();

		final ChatClient client = new ChatClient(producer, tempDest, session);
		CloseHook.registerCloseHook(connection, client);

		consumer.setMessageListener(new MessageListener() {

			public void onMessage(Message arg0) {
				try {
					// Object Message
					if (!(arg0 instanceof ObjectMessage))
						return;

					ChatMessage m = (ChatMessage) ((ObjectMessage) arg0)
							.getObject();
					for (ServerMessageListener listener : new ArrayList<ServerMessageListener>(
							listeners)) {
						listener.onMessageReceived(m);
					}
					
					if( m.getMessageType() == Constants.MessageType.JOINCHATROOM ) {
						JoinChatRoomMessage message = (JoinChatRoomMessage) m;
						
						if( !client.hasChatRoom(arg0.getJMSReplyTo()) ) {
							client.addChatRoom(message.getChatRoomName(), arg0.getJMSReplyTo());
						}
					}
					

				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});

		return client;
	}

	public static void main(String[] args) throws IOException,
			InterruptedException {

		try {
			/*
			 * We have some other function wire the ChatClient to the
			 * communication platform
			 */
			ChatClient client = wireClient();

			System.out.println("Starting ChatClient...");
			// new GUI(client).setVisible(true);
			new GUILogin(client).setVisible(true);

		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void addServerMessageListener(ServerMessageListener listener) {
		listeners.add(listener);
	}

	public static void removeServerMessageListener(
			ServerMessageListener listener) {
		listeners.add(listener);
	}

}
