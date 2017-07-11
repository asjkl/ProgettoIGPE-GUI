package net;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.ArrayList;

import javax.lang.model.util.ElementFilter;
import javax.naming.NamingEnumeration;
import javax.swing.JPanel;
import javax.swing.JTextField;

import progettoIGPE.davide.giovanni.unical2016.GUI.MainFrame;

import java.io.*;

@SuppressWarnings("serial")
public class ClientChat extends JPanel implements Runnable {

	private TextField tf1;
	private TextField tf2;

	// private TextField t1;
	// private TextField t2;

	String host;
	String port;
	String clientName;

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
	
	private JTextField ipTextField;
	private JTextField nameTextField;
	private JTextField portTextField;
	private String difficult;
	private String stage;
	
	private String name;

	public ClientChat(String name, String host, String port, MainFrame mainFrame) {
		ipTextField=new JTextField();
		nameTextField=new JTextField();
		portTextField=new JTextField();
		this.name=name;
		this.setNameOfClientsOnline(new ArrayList<>());
		this.mainFrame=mainFrame;
		this.clientName = name;
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
			socket = new Socket(host, Integer.valueOf(port));
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

	void setBooleanOfClients(String message) {
		String[] elements = message.split(" ");
		if (elements[0].equals("p1") && elements[1].equals("true")) {
			readyP1 = true;
		}

		if (elements[0].equals("p1") && elements[1].equals("false")) {
			readyP1 = false;
		}

		if (elements[0].equals("p2") && elements[1].equals("true")) {
			readyP2 = true;
		}

		if (elements[0].equals("p2") && elements[1].equals("false")) {
			readyP2 = false;
		}
	}

	public void run() {

		try {

			while (true) {

				String message = din.readUTF();
				System.out.println("-> " + message);
				String[] elements = message.split(" ");
				
				if(elements[0].contains("connect")){
					nameTextField.setText(name);
					ipTextField.setText(elements[1]);
					portTextField.setText(elements[2]);
					stage=elements[3];
					difficult=elements[4];
					
					try {
						connectoToServer();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				
				
				
				if ((elements[0].equals("p1") || elements[0].equals("p2"))
						&& (elements[1].equals("true") || elements[1].equals("false"))) {
					setBooleanOfClients(message);
					System.out.println(readyP1+" "+readyP2);
				} else {

					if (count == 0 && !(message.equals(null))) { // se non ho
																	// letto
																	// nulla

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
			}

		} catch (IOException ie) {
			System.out.println(ie);
		}
	}
	
	protected void connectoToServer() throws Exception {
		Socket socket = new Socket(ipTextField.getText(), Integer.parseInt(portTextField.getText()));
		ConnectionManager connectionManager = null;

		if (getClientName().equals(getNameOfClientsOnline().get(0))
				&& getNameOfClientsOnline().size() == 2) {
			connectionManager = new ConnectionManager(socket, nameTextField.getText(), mainFrame,
					stage, difficult);
		} else {
			connectionManager = new ConnectionManager(socket, nameTextField.getText(), mainFrame);
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