package net;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import progettoIGPE.davide.giovanni.unical2016.BrickWall;
import progettoIGPE.davide.giovanni.unical2016.GameManager;
import progettoIGPE.davide.giovanni.unical2016.PlayerTank;
import progettoIGPE.davide.giovanni.unical2016.SteelWall;
import progettoIGPE.davide.giovanni.unical2016.GUI.ImageProvider;
import progettoIGPE.davide.giovanni.unical2016.GUI.JFileChooserClass;
import progettoIGPE.davide.giovanni.unical2016.GUI.MainFrame;
import progettoIGPE.davide.giovanni.unical2016.GUI.SoundsProvider;
import progettoIGPE.davide.giovanni.unical2016.GUI.TranslucentWindow;

public class ConnectionManager implements Runnable {

	private MainFrame mainFrame;
	private final String name;
	private List<String> playerNames;
	private BufferedReader br;
	private PrintWriter pw;
	private final Socket socket;
	private JTextField map;
	private String difficult;
	private JFileChooserClass chooser;
	private JButton[] buttons;
	private JDialog dialog;
	private int cursorPositionDialog;

	public ConnectionManager(final Socket socket, final String name, MainFrame mainFrame) {
		this.socket = socket;
		this.name = name;
		this.mainFrame = mainFrame;
		this.map=new JTextField();
		this.dialog = new JDialog();
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

	public List<String> getAllPlayerNames() {
		return playerNames;
	}

	public String getPlayerName() {
		return name;
	}

	@Override
	public void run() {
		try {
			if (getPlayerName().equals("P1")) {
				map = new JTextField();
				chooser = new JFileChooserClass(true);
				if (chooser.functionLoadFile()){
					map.setText("./maps/career/multiplayer/" + chooser.getFilename().getText() + ".txt");
				}
			chooseDifficult();
			}
			
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw = new PrintWriter(socket.getOutputStream(), true);
			if(getPlayerName().equals("P1")){
				pw.println(getPlayerName()+":"+map.getText()+":"+difficult);
			}else{
				pw.println(getPlayerName());
			}
			String buffer = br.readLine();
			
			while (!buffer.equals("#START")) {
				final String[] split = buffer.split(";");
				if (split.length != 0) {
					playerNames = new ArrayList<>();
					for (final String name : split){
						if (name.contains("txt")){
							map.setText(name);
						}
						else {
							if (name.contains("P"))
								playerNames.add(name);
							else
								difficult = name;
						}
					}
				}
				buffer = br.readLine();
			}

			final GameManager gameManager = mainFrame.showNetwork(this, map, difficult);
			buffer = br.readLine();
			while (buffer != null) {
				gameManager.parseStatusFromString(buffer);
				playSounds(gameManager);
				mainFrame.gamePanel.repaint();
				mainFrame.play.repaint();
				if (gameManager.isExit()) {
					
					buffer=null;
					
					mainFrame.setTransparent(true);
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

	private void chooseDifficult() {

		// JFileChooser jfc = new JFileChooser();
		// File f = new File("./maps/difficult/");
		// JTextField t = new JTextField();
		// jfc.setCurrentDirectory(f);
		// int v = jfc.showOpenDialog(null);
		// if (v == JFileChooser.APPROVE_OPTION)
		// t.setText(jfc.getSelectedFile().getName());
		//
		// difficult = t.getText();

		dialog.setPreferredSize(new Dimension(270, 250));
		@SuppressWarnings("serial")
		JPanel fullpanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (cursorPositionDialog == 0) {
					g.drawImage(ImageProvider.getCursorRight(), 30, 80, this);
				} else if (cursorPositionDialog == 1) {
					g.drawImage(ImageProvider.getCursorRight(), 30, 133, this);
				} else {
					g.drawImage(ImageProvider.getCursorRight(), 30, 185, this);
				}
			}
		};
		JPanel text = new JPanel();
		JPanel buttonspanel = new JPanel(new GridLayout(3, 1, 0, 10));
		JLabel label = new JLabel("Option");
		String[] buttonTxt = { "Easy", "Normal", "Hard" };
		fullpanel.setPreferredSize(new Dimension(270, 250));
		fullpanel.setBorder(BorderFactory.createLineBorder(Color.RED));
		fullpanel.setBackground(Color.BLACK);
		buttons = new JButton[buttonTxt.length];
		label.setFont(MainFrame.customFontB);
		label.setForeground(Color.RED);
		label.setBorder(null);
		text.add(label);
		text.setPreferredSize(new Dimension(200, 70));
		text.setMaximumSize(new Dimension(200, 70)); // set max = pref
		text.setBackground(Color.BLACK);
		text.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonspanel.setBackground(Color.BLACK);

		for (int i = 0; i < buttonTxt.length; i++) {

			final int curRow = i;
			buttons[i] = new JButton(buttonTxt[i]);
			buttons[i].setFont(MainFrame.customFontM);
			buttons[i].setBackground(Color.BLACK);
			buttons[i].setForeground(Color.WHITE);
			buttons[i].setBorder(null);
			buttons[i].setFocusPainted(false);
			buttons[i].setContentAreaFilled(false);
			buttons[i].setBorderPainted(false);
			buttons[i].setFocusPainted(false);
			buttons[i].addKeyListener(new KeyAdapter() {

				@Override
				public void keyPressed(KeyEvent e) {

					if (e.getKeyCode() == KeyEvent.VK_ENTER) {

						((JButton) e.getComponent()).doClick();
						if (GameManager.offline) {
							dialog.dispose();
						}
					} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						cursorPositionDialog = 0;
						dialog.dispose();
					}

					else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_LEFT) {
						SoundsProvider.playBulletHit1();
						if (curRow < 1) {
							buttons[buttons.length - 1].requestFocus();
							cursorPositionDialog = buttons.length - 1;

						} else {
							buttons[curRow - 1].requestFocus();
							cursorPositionDialog = curRow - 1;

						}
					} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_RIGHT) {
						SoundsProvider.playBulletHit1();
						buttons[(curRow + 1) % buttons.length].requestFocus();
						cursorPositionDialog = (curRow + 1) % buttons.length;
					}
				}
			});

			buttonspanel.add(buttons[i]);
			buttonspanel.setBackground(Color.BLACK);
			optionActionListener(i);
		}

		buttonspanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonspanel.setPreferredSize(new Dimension(100, 150));
		buttonspanel.setMaximumSize(new Dimension(100, 150));
		buttonspanel.setBackground(Color.BLACK);
		fullpanel.add(text);
		fullpanel.add(buttonspanel);
		dialog.setContentPane(fullpanel);
		dialog.setUndecorated(true);
		dialog.setLocationRelativeTo(mainFrame);
		dialog.setModal(true);
		dialog.pack();
		dialog.setVisible(true);

	}
	
	public void optionActionListener(int j) {

		switch (j) {
		case 0: // RETRY
			buttons[j].addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					difficult="easy";
					dialog.dispose();
				}
			});
			break;
		case 1:// RESTART
			buttons[j].addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					difficult="normal";
					dialog.dispose();
				}
			});
			break;
		case 2: // MENU
			buttons[j].addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					difficult="hard";
					dialog.dispose();
					
				}
			});
			break;
		default:
			break;
		}
	}
}
