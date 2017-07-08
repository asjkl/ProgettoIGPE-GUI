package net;

import java.io.*;
import java.net.*;
public class ServerThread extends Thread{
		
	private Server server;
	private Socket socket;
	
	public ServerThread(Server server,Socket socket){
		
		System.out.println("Hi");
		this.server = server;
		this.socket = socket;
		this.start();
	}
	
	public void run(){
		try{
			DataInputStream din = new DataInputStream(socket.getInputStream());
		
			while(true){
				String message = din.readUTF();
				System.out.println(message);
				boolean name=true;
				int len = message.length();
				
				
				for(int i=0;i<6;i++){
				
					if(!(message.charAt(len-i-1)=='^')){
						name=false;
						break;
					}
				}
				
				if(name==true){
					int i=0;
				
					while(!(message.charAt(i)==':' && message.charAt(i+1)==':')){
						Server.OnlineNames = Server.OnlineNames+message.charAt(i);
						i++;
					}
					Server.OnlineNames = Server.OnlineNames+" ";
				}
				server.sendToAll(message);
			}
		}
	catch(EOFException e){
	}
	catch(IOException ie){
		ie.printStackTrace();
	}
	finally{
		server.removeConnection(socket);
	}
	}
}