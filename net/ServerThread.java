package net;

import java.io.*;
import java.net.*;

public class ServerThread extends Thread {

	private Server server;
	private Socket socket;
	private String nameSocket;

	public ServerThread(Server server, Socket socket) {
		this.server = server;
		this.socket = socket;
		this.start();
	}

	public void run() {
		try {
			DataInputStream din = new DataInputStream(socket.getInputStream());

			while (true) {
				String message = din.readUTF();

				String[] elements = message.split(" ");
				if (elements[0].equals("EXITALL")) {
					server.sendToAll("EXITALL");
					server.removeConnection(socket);
					server.closeServer();
				} else if (elements[0].equals("EXIT")) {
					String client = elements[1];
					server.removeConnection(client);
					String[] clients = Server.OnlineNames.split(" ");
					Server.OnlineNames = "";
					for (String s : clients) {
						if (!s.equals(client)) {
							Server.OnlineNames += s;
							Server.OnlineNames += " ";
						}
					}
					server.sendToAll("EXIT " + client);
				} else {

					boolean name = true;
					int len = message.length();

					for (int i = 0; i < 6; i++) {

						if (!(message.charAt(len - i - 1) == '^')) {
							name = false;
							break;
						}
					}

					if (name == true) {
						int i = 0;

						while (!(message.charAt(i) == ':' && message.charAt(i + 1) == ':')) {
							nameSocket += message.charAt(i);
							Server.OnlineNames = Server.OnlineNames + message.charAt(i);
							i++;
						}
						Server.OnlineNames = Server.OnlineNames + " ";
					}
					server.client.put(socket, nameSocket);
					server.sendToAll(message);
				}
			}
		} catch (EOFException e) {
		} catch (IOException ie) {
			System.out.println("SOCKET CLOSE");
		} finally {
			if(socket.isConnected()){
				server.removeConnection(socket);
			}
		}
	}
}