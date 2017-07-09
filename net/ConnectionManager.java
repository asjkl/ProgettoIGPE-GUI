package net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JTextField;

import progettoIGPE.davide.giovanni.unical2016.BrickWall;
import progettoIGPE.davide.giovanni.unical2016.GameManager;
import progettoIGPE.davide.giovanni.unical2016.PlayerTank;
import progettoIGPE.davide.giovanni.unical2016.SteelWall;
import progettoIGPE.davide.giovanni.unical2016.GUI.ImageProvider;
import progettoIGPE.davide.giovanni.unical2016.GUI.MainFrame;
import progettoIGPE.davide.giovanni.unical2016.GUI.SoundsProvider;
import progettoIGPE.davide.giovanni.unical2016.GUI.TranslucentWindow;

public class ConnectionManager implements Runnable {

	private MainFrame mainFrame;
	private final String name;
	private String nameOfGame;
	private BufferedReader br;
	private PrintWriter pw;
	private final Socket socket;
	private JTextField map;
	private String difficult;

	public ConnectionManager(final Socket socket, final String name, MainFrame mainFrame) {
		this.socket = socket;
		this.name = name;
		this.mainFrame = mainFrame;
	}

	public void close() {
		try {
			socket.close();
		} catch (final IOException e) {
		}
	}

	public void dispatch(final String message) {
		pw.println(message);
	}

	public String getPlayerName() {
		return name;
	}

	@Override
	public void run() {
		try {
			
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw = new PrintWriter(socket.getOutputStream(), true);
			pw.println(name);	
			String buffer = br.readLine();
			
			while (!buffer.equals("#START")) {
				
				final String[] split = buffer.split(";");
				if (split.length != 0) {
					for (final String name : split){
						String[] split1 = name.split(":");
						if (this.name.equals(split1[1])){
							nameOfGame=split1[0];
							
						}
					}
				}
				buffer = br.readLine();
			}
			System.out.println("NOME DATO DAL SERVER "+nameOfGame);
			final GameManager gameManager = mainFrame.showNetwork(this, map, difficult);
			buffer = br.readLine();
			while (buffer != null) {
				
				gameManager.parseStatusFromString(buffer);
				playSounds(gameManager);	
				mainFrame.getGamePanel().repaint();
				mainFrame.getFullGamePanel().repaint();
					
					if (gameManager.isExit()) {
						
						buffer=null;
						
						mainFrame.setTransparent(true);
						mainFrame.getGamePanel().repaint();
						mainFrame.getFullGamePanel().repaint();
						
						SoundsProvider.cancelMove();
						SoundsProvider.cancelStop();
						
						if(gameManager.getNumbersOfEnemiesOnline() == 0) {
							SoundsProvider.playStageComplete();
							new TranslucentWindow(mainFrame, null, ImageProvider.getStageComplete());
						}
						else {
							SoundsProvider.playGameOver();
							new TranslucentWindow(mainFrame, null, ImageProvider.getGameOver());
						}
					
				} else {
					buffer = br.readLine();
				}
			}
		} catch (final IOException e) {
			System.out.println("Connection closed");
		}
	}

	private void playSounds(GameManager game) {
		if (game.isSoundPowerUp()) {
			SoundsProvider.playPowerUpAppear();
		}

		if (game.isExplosion()) {
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

	public String getNameOfGame() {
		return nameOfGame;
	}
}
