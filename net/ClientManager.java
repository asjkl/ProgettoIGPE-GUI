package net;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import progettoIGPE.davide.giovanni.unical2016.GameManager;
import progettoIGPE.davide.giovanni.unical2016.GUI.ImageProvider;
import progettoIGPE.davide.giovanni.unical2016.GUI.JFileChooserClass;
import progettoIGPE.davide.giovanni.unical2016.GUI.SoundsProvider;

public class ClientManager implements Runnable {
	
	private BufferedReader reader;
	private PrintWriter printer;
	private String name;
	private JTextField map;
	private String difficult;
	private JFileChooserClass chooser;
	private JButton[] buttons;
	private JDialog dialog;
	private int cursorPositionDialog;
	private ServerGameManager server;
	private Socket socket;
	

	public ClientManager(Socket socket, ServerGameManager server) {
		this.socket = socket;
		this.server = server;
		this.map=new JTextField();
		this.dialog = new JDialog();
	}

	public void dispatch(final String message) {
		if (printer != null && message != null) {
			printer.println(message);
		}
	}

	@Override
	public void run() {
		try {
			if (name.equals("P1")) {
				map = new JTextField();
				chooser = new JFileChooserClass(true);
				if (chooser.functionLoadFile()){
					map.setText("./maps/career/multiplayer/" + chooser.getFilename().getText() + ".txt");
				}
			chooseDifficult();
			server.lock.lock();
			server.startGame=true;
			server.cond.signalAll();
			server.lock.unlock();
			}
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
		} catch (FontFormatException e) {
			e.printStackTrace();
		}

	}

	String setup(String string) throws IOException {
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		printer = new PrintWriter(socket.getOutputStream(), true);
		String s = reader.readLine();
		String[] split = s.split(":");
		name=string;
		server.name.put(split[0], name);
		if(split.length>1){
			map.setText(split[1]);
			difficult=split[2];
		}	
		server.dispatch(server.getConnectedClientNames());
		return name;
	}
	
	private void chooseDifficult() throws FontFormatException, IOException {

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
		label.setFont((Font.createFont(Font.TRUETYPE_FONT, new File("./font/Minecraft.ttf")).deriveFont(40f)));
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
			buttons[i].setFont((Font.createFont(Font.TRUETYPE_FONT, new File("./font/Minecraft.ttf")).deriveFont(25f)));
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
		dialog.setLocationRelativeTo(fullpanel);
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
	
	public String getName() {
		return name;
	}
	
	public JTextField getMap() {
		return map;
	}

	public void setMap(JTextField map) {
		this.map = map;
	}

	public String getDifficult() {
		return difficult;
	}

	public void setDifficult(String difficult) {
		this.difficult = difficult;
	}
}
