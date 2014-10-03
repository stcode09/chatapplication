/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.ucsd.cse110.client;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.jms.JMSException;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicTabbedPaneUI.TabbedPaneLayout;

import edu.ucsd.cse110.shared.ChatMessage;
import edu.ucsd.cse110.shared.ChatRoomUsersMessage;
import edu.ucsd.cse110.shared.ChatTextMessage;
import edu.ucsd.cse110.shared.JoinChatRoomMessage;
import edu.ucsd.cse110.shared.UpdateUserListMessage;
import edu.ucsd.cse110.shared.UserListMessage;

public class GUI extends javax.swing.JFrame {

	private ChatClient client;
	private String clientName;

	// private Relay relay = Relay.getInstance();

	public GUI(ChatClient client) {
		this.client = client;
		setTitle("Jingle Chat");
		initComponents();
	}

	public ChatClient getClient() {
		return client;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getClientName() {
		return clientName;
	}

	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jPopupMenu1 = new javax.swing.JPopupMenu();
		jMenuItem6 = new javax.swing.JMenuItem();
		jMenuItem7 = new javax.swing.JMenuItem();
		sendButton = new javax.swing.JButton();
		jScrollPane3 = new javax.swing.JScrollPane();
		userList = new javax.swing.JList();
		userListLabel = new javax.swing.JLabel();
		inputTextField = new javax.swing.JTextField();
		typeHereLabel = new javax.swing.JLabel();
		jMenuBar1 = new javax.swing.JMenuBar();
		chatRoomMenu = new javax.swing.JMenu();
		newChatRoomItem = new javax.swing.JMenuItem();
		jMenuItem8 = new javax.swing.JMenuItem();
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);

		jMenuItem6.setText("Add Friend");
		jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItem6ActionPerformed(evt);
			}
		});
		jPopupMenu1.add(jMenuItem6);

		jMenuItem7.setText("Private Message");
		jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItem7ActionPerformed(evt);
			}
		});
		jPopupMenu1.add(jMenuItem7);

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		sendButton.setText("Send");
		sendButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				sendButtonActionPerformed(evt);
			}
		});

		try {
			client.sendUserListMessage();
		} catch (JMSException exception) {
			throw new RuntimeException(exception);
		}

		ChatClientApplicationGUI
				.addServerMessageListener(new ServerMessageListener() {

					public void onMessageReceived(ChatMessage m) {
						if (!(m instanceof UserListMessage))
							return;

						final UserListMessage ulm = (UserListMessage) m;

						userList.setModel(new javax.swing.AbstractListModel() {
							List<String> strings = ulm.getOnlineUsers();

							public int getSize() {
								return strings.size();
							}

							public Object getElementAt(int i) {
								return strings.get(i);
							}
						});

					}
				});

		userList.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				userListMousePressed(evt);
			}
		});
		jScrollPane3.setViewportView(userList);

		userListLabel.setText("Online Users");

		inputTextField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent evt) {
				inputTextFieldKeyPressed(evt);
			}
		});

		typeHereLabel.setText("Type Here: ");

		chatRoomMenu.setText("Options");

		newChatRoomItem.setText("New Chatroom");
		newChatRoomItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItem1ActionPerformed(evt);
			}
		});
		jMenuItem8.setText("Log out");
		jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItem8ActionPerformed(evt);
			}
		});

		chatRoomMenu.add(newChatRoomItem);
		chatRoomMenu.add(jMenuItem8);
		jMenuBar1.add(chatRoomMenu);

		setJMenuBar(jMenuBar1);

		JScrollPane scrollPane = new JScrollPane();

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		layout.setHorizontalGroup(layout
				.createParallelGroup(Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												Alignment.LEADING)
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		inputTextField,
																		GroupLayout.PREFERRED_SIZE,
																		338,
																		GroupLayout.PREFERRED_SIZE)
																.addPreferredGap(
																		ComponentPlacement.UNRELATED)
																.addComponent(
																		sendButton,
																		GroupLayout.PREFERRED_SIZE,
																		94,
																		GroupLayout.PREFERRED_SIZE))
												.addComponent(typeHereLabel)
												.addComponent(
														tabbedPane,
														GroupLayout.PREFERRED_SIZE,
														445,
														GroupLayout.PREFERRED_SIZE))
								.addGap(17)
								.addGroup(
										layout.createParallelGroup(
												Alignment.LEADING)
												.addComponent(userListLabel)
												.addComponent(
														jScrollPane3,
														GroupLayout.DEFAULT_SIZE,
														120, Short.MAX_VALUE))
								.addContainerGap()));
		layout.setVerticalGroup(layout
				.createParallelGroup(Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												Alignment.LEADING)
												.addComponent(
														tabbedPane,
														GroupLayout.PREFERRED_SIZE,
														332,
														GroupLayout.PREFERRED_SIZE)
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		userListLabel)
																.addGroup(
																		layout.createParallelGroup(
																				Alignment.LEADING)
																				.addGroup(
																						layout.createSequentialGroup()
																								.addGap(324)
																								.addComponent(
																										typeHereLabel)
																								.addGap(6)
																								.addGroup(
																										layout.createParallelGroup(
																												Alignment.BASELINE)
																												.addComponent(
																														sendButton,
																														GroupLayout.PREFERRED_SIZE,
																														32,
																														GroupLayout.PREFERRED_SIZE)
																												.addComponent(
																														inputTextField,
																														GroupLayout.PREFERRED_SIZE,
																														32,
																														GroupLayout.PREFERRED_SIZE)))
																				.addGroup(
																						layout.createSequentialGroup()
																								.addPreferredGap(
																										ComponentPlacement.RELATED)
																								.addComponent(
																										jScrollPane3,
																										GroupLayout.DEFAULT_SIZE,
																										372,
																										Short.MAX_VALUE)))))
								.addContainerGap()));

		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if ("Main Lobby".equals(tabbedPane.getTitleAt(tabbedPane
						.getSelectedIndex()))) {
					try {
						client.sendUserListMessage();
						client.setChatRoom(null);
					} catch (JMSException e1) {
						throw new RuntimeException(e1);
					}
				} else {
					try {
						client.sendChatRoomUserListMessage(tabbedPane
								.getTitleAt(tabbedPane.getSelectedIndex()));
						client.setChatRoom(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));
					} catch (JMSException e1) {
						throw new RuntimeException(e1);
					}
				}
			}
		});
		final JTextArea textArea = new JTextArea();

		scrollPane.setViewportView(textArea);

		textArea.setEditable(false);
		tabbedPane.addTab("Main Lobby", null, scrollPane, null);

		ChatClientApplicationGUI
				.addServerMessageListener(new ServerMessageListener() {

					public void onMessageReceived(ChatMessage m) {
						if (!(m instanceof ChatTextMessage))
							return;

						final ChatTextMessage ulm = (ChatTextMessage) m;
						textArea.append("[" + ulm.getUsername() + "]: "
								+ ulm.getText() + "\n");
					}
				});

		ChatClientApplicationGUI
				.addServerMessageListener(new ServerMessageListener() {

					public void onMessageReceived(ChatMessage m) {
						if (!(m instanceof UpdateUserListMessage))
							return;

						if ("Main Lobby".equals(tabbedPane
								.getTitleAt(tabbedPane.getSelectedIndex()))) {
							try {
								client.sendUserListMessage();
							} catch (JMSException e1) {
								throw new RuntimeException(e1);
							}
						} else {
							try {
								client.sendChatRoomUserListMessage(tabbedPane
										.getTitleAt(tabbedPane
												.getSelectedIndex()));
							} catch (JMSException e1) {
								throw new RuntimeException(e1);
							}
						}
						ChatClientApplicationGUI.removeServerMessageListener(this);
					}
				});

		getContentPane().setLayout(layout);

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem1ActionPerformed

		final String inputValue = JOptionPane
				.showInputDialog("Enter a chatroom name");
		//final JTextArea textArea = new JTextArea();
		final JButton btnClose = new JButton("x");
		if (inputValue.isEmpty()) {
			JOptionPane.showMessageDialog(GUI.this, "Please enter a name.",
					"Input Error", JOptionPane.WARNING_MESSAGE);
		} else {

			try {
				client.sendJoinChatRoomMessage(inputValue);
			} catch (JMSException e) {
				throw new RuntimeException(e);
			}

			ChatClientApplicationGUI
					.addServerMessageListener(new ServerMessageListener() {

						public void onMessageReceived(ChatMessage m) {
							if (!(m instanceof ChatRoomUsersMessage))
								return;

							final ChatRoomUsersMessage ulm = (ChatRoomUsersMessage) m;

							if (ulm.getChatRoom().equals(
									tabbedPane.getTitleAt(tabbedPane
											.getSelectedIndex()))) {

								userList.setModel(new javax.swing.AbstractListModel() {
									List<String> strings = ulm.getUsers();

									public int getSize() {
										return strings.size();
									}

									public Object getElementAt(int i) {
										return strings.get(i);
									}
								});
							}
						}
					});

			ChatClientApplicationGUI
					.addServerMessageListener(new ServerMessageListener() {

						public void onMessageReceived(ChatMessage m) {
							if (!(m instanceof JoinChatRoomMessage))
								return;

							try {
								client.sendChatRoomUserListMessage(inputValue);
							} catch (JMSException e) {
								throw new RuntimeException(e);
							}
							final JTextArea jta = new JTextArea();
							tabbedPane.addTab(inputValue, null, jta, null);

							ChatClientApplicationGUI
									.addServerMessageListener(new ServerMessageListener() {

										public void onMessageReceived(
												ChatMessage m) {
											if (!(m instanceof ChatTextMessage))
												return;
											final ChatTextMessage ulm = (ChatTextMessage) m;
											
											jta.append("[" + ulm.getUsername() + "]: " + ulm.getText()+ "\n");
											
										}
									});

							int index = tabbedPane.indexOfTab(inputValue);
							JPanel pnlTab = new JPanel();
							pnlTab.setOpaque(false);
							JLabel lblTitle = new JLabel(inputValue);

							pnlTab.add(lblTitle);
							pnlTab.add(btnClose);

							tabbedPane.setTabComponentAt(index, pnlTab);

							btnClose.addActionListener(new java.awt.event.ActionListener() {
								public void actionPerformed(
										java.awt.event.ActionEvent evt) {
									btnCloseActionPerformed(evt);
								}

								private void btnCloseActionPerformed(
										ActionEvent evt) {
									Point mousePos = tabbedPane
											.getMousePosition();
									int mouseX = mousePos.x;
									int mouseY = mousePos.y;
									int index = tabbedPane.indexAtLocation(
											mouseX, mouseY);
									Component selected = tabbedPane
											.getComponentAt(index);
									if (selected != null) {
										tabbedPane.remove(selected);
									}
								}
							});
							//tabbedPane.setSelectedComponent(textArea);
							jta.append("[System]: Joined " + inputValue
									+ "\n");

							ChatClientApplicationGUI.removeServerMessageListener(this);
						}
						
					});
		}
	}

	private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem1ActionPerformed
		// TODO add your handling code here:
		int result = JOptionPane.showConfirmDialog(null,
				"Are you sure you want to log out?", "",
				JOptionPane.YES_NO_OPTION);
		if (result == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

	// GEN-LAST:event_jMenuItem1ActionPerformed

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt)
			throws JMSException, InterruptedException {// GEN-FIRST:event_jMenuItem3ActionPerformed

		userListLabel.setText("Online Users");
		ChatClientApplicationGUI
				.addServerMessageListener(new ServerMessageListener() {

					public void onMessageReceived(ChatMessage m) {
						if (!(m instanceof UserListMessage))
							return;

						final UserListMessage ulm = (UserListMessage) m;

						userList.setModel(new javax.swing.AbstractListModel() {
							List<String> strings = ulm.getOnlineUsers();

							public int getSize() {
								return strings.size();
							}

							public Object getElementAt(int i) {
								return strings.get(i);
							}
						});

					}
				});
	}

	private void userListMousePressed(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_userListMousePressed

		String userName = (String) userList.getSelectedValue();
		if (SwingUtilities.isRightMouseButton(evt)) {
			userList.setSelectedIndex(userList.locationToIndex(evt.getPoint()));
			jPopupMenu1.show(evt.getComponent(), evt.getX(), evt.getY());
		}
	}// GEN-LAST:event_userListMousePressed

	private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem6ActionPerformed
		// TODO add your handling code here:
		String userName = (String) userList.getSelectedValue();
		((JTextArea) tabbedPane.getSelectedComponent())
				.append("[System]: Successfully Added " + "\"" + userName
						+ "\"" + " as a friend\n");
	}// GEN-LAST:event_jMenuItem6ActionPerformed

	private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem7ActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_jMenuItem7ActionPerformed

	private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_sendButtonActionPerformed
		// TODO add your handling code here:
		String text = inputTextField.getText();
		inputTextField.setText("");
		inputTextField.requestFocus();
		if (!text.equals("")) {
			try {
				client.send(text);
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*
			 * ((JTextArea) tabbedPane.getSelectedComponent()).append("[You]: "
			 * + text + "\n"); inputTextField.setText("");
			 */
		}
	}// GEN-LAST:event_sendButtonActionPerformedpublic class
		// MyCloseActionHandler implements ActionListener {

	private void inputTextFieldKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_inputTextFieldKeyPressed
		// TODO add your handling code here:
		if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
			String text = inputTextField.getText();
			inputTextField.setText("");
			try {
				client.send(text);
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			/*
			 * (if(!text.equals("")){ ((JTextArea)
			 * tabbedPane.getSelectedComponent()).append("[You]: " + text +
			 * "\n"); inputTextField.setText(""); }
			 */
		}
	}// GEN-LAST:event_inputTextFieldKeyPressed

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		// <editor-fold defaultstate="collapsed"
		// desc=" Look and feel setting code (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the
		 * default look and feel. For details see
		 * http://download.oracle.com/javase
		 * /tutorial/uiswing/lookandfeel/plaf.html
		 */
		try {

			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager
					.getSystemLookAndFeelClassName());

		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(GUI.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(GUI.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(GUI.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(GUI.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		}
		// </editor-fold>

		/* Create and display the form */
		/*
		 * java.awt.EventQueue.invokeLater(new Runnable() { public void run() {
		 * new GUI().setVisible(true); } });
		 */
	}

	private javax.swing.JMenu chatRoomMenu;
	private javax.swing.JTextField inputTextField;
	private javax.swing.JLabel typeHereLabel;
	private javax.swing.JMenuBar jMenuBar1;
	private javax.swing.JMenuItem newChatRoomItem;
	private javax.swing.JMenuItem jMenuItem6;
	private javax.swing.JMenuItem jMenuItem7;
	private javax.swing.JMenuItem jMenuItem8;
	private javax.swing.JPopupMenu jPopupMenu1;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane3;
	private javax.swing.JButton sendButton;
	private javax.swing.JList userList;
	private javax.swing.JLabel userListLabel;
	private javax.swing.JTabbedPane tabbedPane;
	// private User user1;
}
