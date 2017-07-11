package net;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.ArrayList;
import javax.swing.JPanel;
import progettoIGPE.davide.giovanni.unical2016.GUI.MainFrame;
import java.io.*;

@SuppressWarnings("serial")
public class ClientChat extends JPanel implements Runnable {

	private TextField tf1;
	private TextField tf2;

	private String clientName;
	private String host;
	private int port;
	private int portChat;
	private String difficult;
	private String stage;

	private TextArea ta;
	private TextArea to;
	private Socket socket;
	public DataOutputStream dout;
	private DataInputStream din;
	private int count = 0;
	private ArrayList<String> nameOfClientsOnline;
	private boolean readyP1 = false;
	private boolean readyP2 = false;
	private MainFrame mainFrame;

	public ClientChat(String name, String host, int portChat, MainFrame mainFrame) {

		this.host = host;
		this.clientName = name;
		this.setPortChat(portChat);
		this.setNameOfClientsOnline(new ArrayList<>());
		this.mainFrame = mainFrame;

		tf1 = new TextField(name + ":");
		this.setSize(new Dimension(500, 300));
		tf1.setEditable(false);
		tf2 = new TextField();
		ta = new TextArea();
		to = new TextArea("People Online: \n", 50, 16);
		ta.setEditable(false);
		to.setEditable(false);

		tf1.setBackground(Color.black);
		tf2.setBackground(Color.black);
		ta.setBackground(Color.black);
		to.setBackground(Color.black);

		tf1.setForeground(Color.white);
		tf2.setForeground(Color.white);
		ta.setForeground(Color.white);
		to.setForeground(Color.white);

		tf1.setFont(MainFrame.customFontS);
		tf2.setFont(MainFrame.customFontS);
		ta.setFont(MainFrame.customFontS);
		to.setFont(MainFrame.customFontS);

		tf2.requestFocus();
		setLayout(new BorderLayout());
		add("North", tf1);
		add("South", tf2);
		add("Center", ta);
		add("West", to);

		tf2.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ae) {

				processMessage(ae.getActionCommand());
			}
		});

		try {
			socket = new Socket(host, Integer.valueOf(portChat));
			System.out.println("Connected to " + socket);
			din = new DataInputStream(socket.getInputStream());
			dout = new DataOutputStream(socket.getOutputStream());
			new Thread(this).start();
			processMessage(tf1.getText() + "^^^^^^");
		} catch (IOException e) {
			System.out.println(e);
		}
	}// costruttore

	private void processMessage(String message) {

		try {
			dout.writeUTF(tf1.getText() + ":" + message);
			tf2.setText(" ");
		} catch (IOException ie) {
			System.out.println(ie);
		}
	}

	public void run() {

		try {

			while (true) {

				String message = din.readUTF();
				String[] elements = message.split(" ");

				// -----------------------------------------------

				System.out.println("-> " + message);

				if (elements[0].equals("EXIT")) {
					String client = elements[1];
					nameOfClientsOnline.remove(client);
					to.setText("People Online: \n");
					for (int a = 0; a < nameOfClientsOnline.size(); a++) {
						to.append(nameOfClientsOnline.get(a) + "\n");
					}
				}

				if (elements.length == 2) {

					if (elements[0].equals("p2") && elements[1].equals("true")) {
						readyP2 = true;
					} else if (elements[0].equals("p2") && elements[1].equals("false")) {
						readyP2 = false;
					} else if (elements[0].equals("p1") && elements[1].equals("true")) {
						readyP1 = true;
					} else if (elements[0].equals("p1") && elements[1].equals("false")) {
						readyP1 = false;
					}

				} else if (elements.length == 4) {

					if (elements[0].contains("connect")) {

						port = Integer.parseInt(elements[1]);
						stage = elements[2];
						difficult = elements[3];
					}

				}

				if (readyP1 && readyP2) { // entrambi si connettono
					try {
						connectoToServer();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}

				// ------------------------------------------------

				if (count == 0 && !(message.equals(null))) {

					String[] names = message.split(" ");
					int i = 0;

					while (i < names.length) {
						if (!names[i].equals("")) {
							to.append(names[i] + "\n");
							nameOfClientsOnline.add(names[i]);
						}
						i++;
					}
					count++;
				}

				else {

					boolean name = true;
					int len = message.length();

					for (int i = 0; i < 6; i++) {

						if (!(message.charAt(len - i - 1) == '^')) {
							name = false;
							System.out.println(message.charAt(len - i - 1));
							break;
						}
					}

					if (name == false) {

						ta.append(message + "\n");
					} else {
						String name1 = "";
						int i = 0;
						while (!(message.charAt(i) == ':' && message.charAt(i + 1) == ':')) {
							name1 = name1 + message.charAt(i);
							i++;
						}
						nameOfClientsOnline.add(name1);
						to.append(name1 + "\n");
					}
				}
			}

		} catch (IOException ie) {
			System.out.println(ie);
		}
	}

	protected void connectoToServer() throws Exception {

		Socket socket = new Socket(host, port);
		ConnectionManager connectionManager = null;

		if (getClientName().equals(getNameOfClientsOnline().get(0)) && getNameOfClientsOnline().size() == 2) {
			connectionManager = new ConnectionManager(socket, clientName, mainFrame, stage, difficult);
		} else {
			connectionManager = new ConnectionManager(socket, clientName, mainFrame);
		}

		new Thread(connectionManager, "Connection Manager").start();
	}

	public ArrayList<String> getNameOfClientsOnline() {
		return nameOfClientsOnline;
	}

	public void setNameOfClientsOnline(ArrayList<String> nameOfClientsOnline) {
		this.nameOfClientsOnline = nameOfClientsOnline;
	}

	public String getClientName() {
		return clientName;
	}

	public boolean isReadyP1() {
		return readyP1;
	}

	public void setReadyP1(boolean readyP1) {
		this.readyP1 = readyP1;
	}

	public boolean isReadyP2() {
		return readyP2;
	}

	public void setReadyP2(boolean readyP2) {
		this.readyP2 = readyP2;
	}

	public int getPortChat() {
		return portChat;
	}

	public void setPortChat(int portChat) {
		this.portChat = portChat;
	}

	// public static void main(String args[])
	// {
	// String ip = "127.0.0.1";
	// int port = 1234;
	//
	// Client obj = new Client(ip,port);
	// obj.setSize(new Dimension(500,500));
	// obj.setVisible(true);
	// obj.setTitle("Chatting Client");
	// }
}