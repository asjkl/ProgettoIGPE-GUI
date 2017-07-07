package net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JTextField;

public class ClientManager implements Runnable {
	
	private BufferedReader reader;
	private PrintWriter printer;
	
	private String name;
	
	private JTextField map;
	private String difficult;
	
	private ServerGameManager server;
	private Socket socket;

	public ClientManager(Socket socket, ServerGameManager server, String stringnName) {
		this.socket = socket;
		this.server = server;
		this.name=stringnName;
		this.map=new JTextField();
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

	String setup() throws IOException {
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		printer = new PrintWriter(socket.getOutputStream(), true);
		String s = reader.readLine();
		String[] split = s.split(":");
		name=split[0];
		if(split.length>1){
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
