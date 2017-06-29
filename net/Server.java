package net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	int port;
	ServerSocket serverSocket;
	
	public static void main(String[] args) throws IOException {
		final Server server=new Server(1234);
		server.startServer();
	}

	public Server(int port) {
		this.port=port;
	}

	private void startServer() throws IOException {
		serverSocket=new ServerSocket(port);
		System.out.println("WAITING FOR CONNECTION...");

		//Player P1
		Socket socket1=serverSocket.accept();
		ServerGameManager gameManagerServer = new ServerGameManager();
		ClientManager cm1=new ClientManager(socket1, gameManagerServer, "P1");
		gameManagerServer.add(cm1);

		//Player P2
		Socket socket2=serverSocket.accept();
		ClientManager cm2=new ClientManager(socket2, gameManagerServer,"P2");
		gameManagerServer.add(cm2);
		gameManagerServer.startGame();
	}
	
}
