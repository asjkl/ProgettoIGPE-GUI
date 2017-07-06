package net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientManager implements Runnable {
	
	private BufferedReader reader;
	private PrintWriter printer;
	
	private String name;
	
	private ServerGameManager server;
	private Socket socket;

	public ClientManager(Socket socket, ServerGameManager server, String stringnName) {
		this.socket = socket;
		this.server = server;
		this.name=stringnName;
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
		name = reader.readLine();
		server.dispatch(server.getConnectedClientNames());
		return name;
	}
	
	public String getName() {
		return name;
	}
}
