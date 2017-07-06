package net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;

import progettoIGPE.davide.giovanni.unical2016.BrickWall;
import progettoIGPE.davide.giovanni.unical2016.GameManager;
import progettoIGPE.davide.giovanni.unical2016.PlayerTank;
import progettoIGPE.davide.giovanni.unical2016.SteelWall;
import progettoIGPE.davide.giovanni.unical2016.GUI.MainFrame;
import progettoIGPE.davide.giovanni.unical2016.GUI.SoundsProvider;

public class ConnectionManager implements Runnable {

	private MainFrame mainFrame;
	private final String name;
	private List<String> playerNames;
	private PrintWriter pw;
	private final Socket socket;

	public ConnectionManager(final Socket socket, final String name, MainFrame mainFrame) {
		this.socket = socket;
		this.name = name;
		this.mainFrame = mainFrame;

	}

	public void close() {
		try {
			socket.close();
		} catch (final IOException e) {}
	}

	public void dispatch(final String message) {
		pw.println(message);
	}

	public List<String> getAllPlayerNames() {
		return playerNames;
	}

	public String getPlayerName() {
		return name;
	}

	@Override
	public void run() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw = new PrintWriter(socket.getOutputStream(), true);
			pw.println(getPlayerName());
			String buffer = br.readLine();
			while (!buffer.equals("#START")) {
				final String[] split = buffer.split(";");
				if (split.length != 0) {
					playerNames = new ArrayList<>();
					for (final String name : split) {
						playerNames.add(name);
					}
				}
				buffer = br.readLine();
			}
			JTextField filename=new JTextField("stage1.txt");
			final GameManager gameManager = mainFrame.showNetwork(this,filename);
			buffer = br.readLine();
			while (buffer != null) {
				// System.out.println(buffer);
				// if (buffer.equals("LOSE")) {
				// JOptionPane.showMessageDialog(mainFrame, "You Won!");
				// buffer = null;
				// socket.close();
				// mainFrame.showMenu();
				// } else if (buffer.equals("WIN")) {
				// JOptionPane.showMessageDialog(mainFrame, "You Lose!");
				// buffer = null;
				// socket.close();
				// mainFrame.showMenu();
				// } else {
				gameManager.parseStatusFromString(buffer);
				playSounds(gameManager);
				mainFrame.gamePanel.repaint();
				mainFrame.play.repaint();
				if(gameManager.isExit()){
					buffer=null;
					SoundsProvider.cancelMove();
					SoundsProvider.cancelStop();
					mainFrame.showNetwork();
				}else{
					buffer = br.readLine();
				}
				// }
			}
		} catch (final IOException e) {
			System.out.println("Connection closed");
		}
	}

	private void playSounds(GameManager game) {
		if(game.isSoundPowerUp()){
			SoundsProvider.playPowerUpAppear();
		}

		if(game.isExplosion()){
			SoundsProvider.playExplosion1();
			SoundsProvider.playExplosion2();
		}
		
		// players
		for (int a = 0; a < game.getPlayersArray().size(); a++) {
			if (game.getPlayersArray().get(a).isShot()) {
				SoundsProvider.playBulletShot();
			}
			
			if (!game.getPlayersArray().get(a).isPressed())
				SoundsProvider.playStop();
			else
				SoundsProvider.playMove();
			}
		
		// rockets
		for (int a = 0; a < game.getRocket().size(); a++) {
			if (game.getRocket().get(a).getTank() instanceof PlayerTank) {
				if (game.getRocket().get(a).getNext() instanceof BrickWall)
					SoundsProvider.playBulletHit2();
				else if (game.getRocket().get(a).getNext() instanceof SteelWall
						|| game.getRocket().get(a).isOnBorder()) {
					SoundsProvider.playBulletHit1();
				}
			}
		}
	}
}
