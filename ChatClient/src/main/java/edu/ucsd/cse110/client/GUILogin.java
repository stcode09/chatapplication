package edu.ucsd.cse110.client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.jms.JMSException;
import javax.security.auth.login.Configuration;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import edu.ucsd.cse110.shared.ChatMessage;
import edu.ucsd.cse110.shared.LoginMessage;
import edu.ucsd.cse110.shared.RegisterMessage;

public class GUILogin extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1240670362200314870L;
	private ChatClient client;
	private String clientName;

	JButton blogin = new JButton("Login");
	JButton bregister = new JButton("Register");
	JPanel panel = new JPanel();
	JTextField username = new JTextField(15);
		
	JPanel myPanel = new JPanel();
	private final JPanel panel_1 = new JPanel();
	private final JPanel panel_3 = new JPanel();
	private final JPasswordField passwordField = new JPasswordField(15);
	private final JPanel panel_2 = new JPanel();
	private final JPanel panel_4 = new JPanel();
	private final JLabel lblNewLabel = new JLabel("Username");
	private final JLabel lblNewLabel_1 = new JLabel("Password");

	GUILogin(ChatClient client){
		super("Jingle Chat Login");
		this.client = client;
		setLocation(500,280);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		getContentPane().add(panel);

		panel.add(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));

		panel_1.add(panel_2);

		panel_2.add(lblNewLabel);
		panel_2.add(username);
		passwordField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					doLogin();
				}
			}
		});

		panel_1.add(panel_4);

		panel_4.add(lblNewLabel_1);
		panel_4.add(passwordField);

		panel.add(panel_3);
		panel_3.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel_3.add(blogin);

		panel_3.add(bregister);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		actionLogin();
		actionRegister();
		pack();
	}

	public void actionRegister() {
		bregister.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				doRegister();
			}
			
		});
	}
	
	public void actionLogin(){

		blogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				doLogin();
			}
		});
	}
	
	private void doRegister() {
		final String user = username.getText();
		String pass = new String(passwordField.getPassword());

		JPanel panel = new JPanel(new BorderLayout());
		JLabel label = new JLabel( "Please confirm your password" );
		JPasswordField jpf = new JPasswordField();
		panel.add(label, BorderLayout.NORTH);
		panel.add(jpf, BorderLayout.CENTER);
		JOptionPane.showMessageDialog(null, panel, "Please confirm your password", JOptionPane.QUESTION_MESSAGE);
		
		if( !new String(jpf.getPassword()).equals(pass) ) {
			JOptionPane.showMessageDialog(null, "Passwords do not match. Try again.");
			return;
		}


		try {
			client.sendRegisterMessage(user, pass);
		} catch (JMSException e1) {
			e1.printStackTrace();
		}
		
		ChatClientApplicationGUI.addServerMessageListener( new ServerMessageListener() {
			
			public void onMessageReceived(ChatMessage m) {
				if( !(m instanceof RegisterMessage) ) return;
				
				RegisterMessage lm = (RegisterMessage) m;
				
				if(lm.getStatus()) {
					JOptionPane.showMessageDialog(null,"Register successful! Please log in.");
				}
				else {
					JOptionPane.showMessageDialog(null,"Register failed! Please pick another user name and try again.");
					username.setText("");
					passwordField.setText("");
					username.requestFocus();
				}
			}
		});
	}
	
	private void doLogin() {
		final String user = username.getText();
		String pass = new String(passwordField.getPassword());

		try {
			client.sendLoginMessage(user, pass);
		} catch (JMSException e1) {
			e1.printStackTrace();
		}
		
		ChatClientApplicationGUI.addServerMessageListener( new ServerMessageListener() {
			
			public void onMessageReceived(ChatMessage m) {
				if( !(m instanceof LoginMessage) ) return;
				
				LoginMessage lm = (LoginMessage) m;
				
				if(lm.getLoginStatus()) {
					System.out.println("Login Successful! Joining main lobby...");
					clientName = user;
					GUI gui = new GUI(client);
					gui.setClientName(clientName);
					gui.setVisible(true);
					dispose();
				}
				else {
					JOptionPane.showMessageDialog(null,"Login failed! Wrong username/password or user already logged in.");
					username.setText("");
					passwordField.setText("");
					username.requestFocus();
				}
			}
		});
	}
}
