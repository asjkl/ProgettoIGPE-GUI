package net;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class Server implements Runnable {

	@SuppressWarnings("rawtypes")
	private Hashtable outputStreams = new Hashtable();
	private boolean exitChat = false;
	int port;
	ServerSocket serverSocket;
	ServerGameManager gameManagerServer;
	public static String OnlineNames = "";
	public HashMap<Socket, String> client = new HashMap<>();

	public static void main(String[] args) throws IOException {
		final Server server1 = new Server(1234);
		final Server server2 = new Server(1232);
		new Thread(server1, "game").start();
		new Thread(server2, "chat").start();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {

		if (Thread.currentThread().getName().equals("game")) {
			try {
				serverSocket = new ServerSocket(port);
				System.out.println("SERVER GAME -> " + serverSocket);

				// Player P1
				Socket socket1 = null;
				socket1 = serverSocket.accept();
				gameManagerServer = new ServerGameManager(serverSocket);
				ClientManager cm1 = new ClientManager(socket1, gameManagerServer);
				gameManagerServer.add(cm1);

				// Player P2
				Socket socket2 = null;
				socket2 = serverSocket.accept();
				ClientManager cm2 = new ClientManager(socket2, gameManagerServer);
				gameManagerServer.add(cm2);
				gameManagerServer.setupClient();
				gameManagerServer.startGame();

			} catch (IOException e1) {
				if (serverSocket != null && !serverSocket.isClosed()) {
					System.out.println("CHIUSO_SERVERGAME");
					try {
						serverSocket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		} else {

			try {
				serverSocket = new ServerSocket(port);

				System.out.println("SERVER CHAT -> " + serverSocket);

				while (!exitChat) {

					System.out.println("--------------------------------------------------");
					Socket s = serverSocket.accept();
					System.out.println("Connessione per socket " + s);
					DataOutputStream dout = null;
					dout = new DataOutputStream(s.getOutputStream());
					outputStreams.put(s, dout);
					dout.writeUTF(OnlineNames);
					new ServerThread(this, s);
				}
			} catch (IOException e1) {
				if (serverSocket != null && !serverSocket.isClosed()) {
					try {
						serverSocket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("CHIUSO_SERVERCHAT");
				}
			}
		}

	}

	public Server(int port) {
		this.port = port;
	}

	@SuppressWarnings("rawtypes")
	Enumeration getOutputStreams() {
		return outputStreams.elements();
	}

	public void sendToAll(String message) {
		synchronized (outputStreams) {

			for (@SuppressWarnings("rawtypes")
			Enumeration e = getOutputStreams(); e.hasMoreElements();) {
				DataOutputStream dout = (DataOutputStream) e.nextElement();
				try {
					dout.writeUTF(message);
				} catch (IOException ie) {
					// System.out.println(ie);
				}
			}
		}
	}

	public void sendToSocket(Socket socket, String string) {
		DataOutputStream dout = (DataOutputStream) outputStreams.get(socket);
		try {
			dout.writeUTF(string);
		} catch (IOException ie) {

		}
	}

	public void removeConnection(Socket s) {

		synchronized (outputStreams) {

			System.out.println("RIMOZIONE CONNESSION PER " + s);
			outputStreams.remove(s);
			try {
				s.close();
			} catch (IOException e) {
				System.out.println("Error in closing " + e);
			}
		}
	}

	public void removeConnection(String name) {
		for (Map.Entry<Socket, String> entry : client.entrySet()) {
			Socket key = entry.getKey();
			String names = entry.getValue();
			if (names.contains(name)) {
				removeConnection(key);
				break;
			}
		}
	}

	public void closeServer() {

		OnlineNames = "";
		client.clear();
		outputStreams.clear();

		try {
			System.out.println("CHIUDO-SERVERCHAT");
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isExitChat() {
		return exitChat;
	}

	public void setExitChat(boolean exitChat) {
		this.exitChat = exitChat;
	}
}
