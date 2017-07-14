package progettoIGPE.davide.giovanni.unical2016.GUI;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

	public class FullScreen implements KeyListener 
	{ 
		GraphicsDevice device;
		JFrame frame;
		boolean fullscreen;
	
	public FullScreen(MainFrame frame) {
	
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		device = ge.getDefaultScreenDevice();
		
		this.frame=frame;
		this.frame.addKeyListener(this);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setSize(800,600);
		this.frame.setResizable(false);
		this.frame.setVisible(true);
	}
	
	private void mainScreenTurnOn() {
		frame.dispose(); 
		frame.setUndecorated(true);
		device.setFullScreenWindow(frame);
		fullscreen = true;
	}
	
	private void mainScreenTurnOff() {
		fullscreen = false;
		device.setFullScreenWindow(null);
		frame.dispose();
		frame.setUndecorated(false);
		frame.setVisible(true);
	}
	
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == e.VK_F11){
		if(!fullscreen)
		mainScreenTurnOn();
		else
		mainScreenTurnOff();
		}
		else System.out.println(e.getKeyCode()+"\n");
	} 
	
	public void keyReleased(KeyEvent arg0) {
	}
	
	public void keyTyped(KeyEvent arg0) {
	
	}

}
