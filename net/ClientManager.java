package net;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import progettoIGPE.davide.giovanni.unical2016.GameManager;
import progettoIGPE.davide.giovanni.unical2016.GUI.ImageProvider;
import progettoIGPE.davide.giovanni.unical2016.GUI.JFileChooserClass;
import progettoIGPE.davide.giovanni.unical2016.GUI.SoundsProvider;

public class ClientManager implements Runnable {
	
	private BufferedReader reader;
	private PrintWriter printer;
	private String name;
	private JTextField map;
	private String difficult;
//	private JFileChooserClass chooser;
//	private JButton[] buttons;
//	private JDialog dialog;
//	private int cursorPositionDialog;
	private ServerGameManager server;
	private Socket socket;
	

	public ClientManager(Socket socket, ServerGameManager server) {
		this.socket = socket;
		this.server = server;
		this.map=new JTextField();
//		this.dialog = new JDialog();
	}

	public void dispatch(final String message) {
		if (printer != null && message != null) {
			printer.println(message);
		}
	}

	@Override
	public void run() {
		try {
			server.setReady(this);
			final boolean running = true;
			while (running) {
				String string = reader.readLine();
				if(string!=null){
					server.received(string);
				}
			}
		} catch (final IOException e) {
			server.disconnetctedClient(name);
		}

	}

	String setup(String string) throws IOException {
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		printer = new PrintWriter(socket.getOutputStream(), true);
		String s = reader.readLine();
		String[] split = s.split(":");
		name=string;
		server.name.put(split[0], name);
		if(split.length>1){
			System.out.println("clientmanager "+split[1]+" "+split[2]);
			map.setText(split[1]);
			difficult=split[2];
		}	
		server.dispatch(server.getConnectedClientNames());
		return name;
	}
	
	public String getName() {
		return name;
	}
	
	public JTextField getMap() {
		return map;
	}

	public void setMap(JTextField map) {
		this.map = map;
	}

	public String getDifficult() {
		return difficult;
	}

	public void setDifficult(String difficult) {
		this.difficult = difficult;
	}
}
