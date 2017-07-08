package net;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

@SuppressWarnings("serial")
public class Client extends Frame implements Runnable{
	
	private TextField tf1;
	private TextField tf2;
	
//	private TextField t1;
//	private TextField t2;
	
	String host;
	String port;
	
	private TextArea ta;
	private TextArea to;
	private Socket socket;
	private DataOutputStream dout;
	private DataInputStream din;
	private int count=0;
	
	public Client(String name, String host, String port){
		
//	String name= JOptionPane.showInputDialog(null,"Enter Name");
	tf1 = new TextField(name+":");
	
//	host= JOptionPane.showInputDialog(null,"Enter Ip");
//	t1 = new TextField(host+":");
//	
//	port= JOptionPane.showInputDialog(null,"Enter port");
//	t2 = new TextField(port+":");
	
	this.setSize(new Dimension(500,500));
	this.setVisible(true);
	this.setTitle("Chatting Client");
	
	
	tf1.setEditable(false);
//	t1.setEditable(false);
//	t2.setEditable(false);
	
	
	tf2 = new TextField();
	ta = new TextArea();
	to = new TextArea("People Online:\n",50,16);
	ta.setEditable(false);
	to.setEditable(false);
	tf2.requestFocus();
	setLayout(new BorderLayout());
	add("North",tf1);
	add("South",tf2);
	add("Center",ta);
	add("West",to);
	
	addWindowListener(new WindowAdapter(){
	
		public void windowClosing(WindowEvent we){
		System.exit(0);
		}
	});
	
	tf2.addActionListener(new ActionListener(){
		
	public void actionPerformed(ActionEvent ae){
		
		processMessage(ae.getActionCommand());
		}
	});
	
	try{
		
		socket = new Socket(host, Integer.valueOf(port));
		System.out.println("Connected to "+socket);
		din = new DataInputStream(socket.getInputStream());
		dout = new DataOutputStream(socket.getOutputStream());
		new Thread(this).start();
		processMessage(tf1.getText()+"^^^^^^");
	}
	catch(IOException e){
		System.out.println(e);
	}
	}//costruttore
	
	private void processMessage(String message){
		
		try{
			
			dout.writeUTF(tf1.getText()+":"+message);
			tf2.setText(" ");
		}
		catch(IOException ie){
			System.out.println(ie);
		}	
	}
	
	public void run(){
	
		try{
			
			while(true){
				
			String message = din.readUTF();
			if(count==0 && !(message.equals(null))){
				
				System.out.println(message);
				String[] names = message.split(" ");
				int i=0;
				
			while(i<names.length){
				to.append(names[i]+"\n");
				i++;
			}
			
			count++;
			}
			else{
				
				boolean name=true;
				int len = message.length();
				
				for(int i=0;i<6;i++){
					
					if(!(message.charAt(len-i-1)=='^')){
						name=false;
						System.out.println(message.charAt(len-i-1));
						break;
					}
				}
				
				if(name==false){
					
					ta.append(message+"n \n");
				}
				else{
					String name1 = "";
					int i=0;
					while(!(message.charAt(i)==':' && message.charAt(i+1)==':')){	
						name1 = name1+message.charAt(i);
						i++;
					}
					to.append(name1+"\n");
				}
			}
		}
	}
	catch(IOException ie){
		System.out.println(ie);
	}
}

//public static void main(String args[])
//{
//	String ip = "127.0.0.1";
//	int port = 1234;
//	
//	Client obj = new Client(ip,port);
//	obj.setSize(new Dimension(500,500));
//	obj.setVisible(true);
//	obj.setTitle("Chatting Client");
//}
}