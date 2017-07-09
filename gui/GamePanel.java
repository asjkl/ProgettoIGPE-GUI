package progettoIGPE.davide.giovanni.unical2016.GUI;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.ConnectionManager;
import progettoIGPE.davide.giovanni.unical2016.AbstractDynamicObject;
import progettoIGPE.davide.giovanni.unical2016.AbstractStaticObject;
import progettoIGPE.davide.giovanni.unical2016.ArmorTank;
import progettoIGPE.davide.giovanni.unical2016.BasicTank;
import progettoIGPE.davide.giovanni.unical2016.BrickWall;
import progettoIGPE.davide.giovanni.unical2016.Direction;
import progettoIGPE.davide.giovanni.unical2016.EnemyTank;
import progettoIGPE.davide.giovanni.unical2016.FastTank;
import progettoIGPE.davide.giovanni.unical2016.Flag;
import progettoIGPE.davide.giovanni.unical2016.GameManager;
import progettoIGPE.davide.giovanni.unical2016.Ice;
import progettoIGPE.davide.giovanni.unical2016.PlayerTank;
import progettoIGPE.davide.giovanni.unical2016.Power;
import progettoIGPE.davide.giovanni.unical2016.PowerTank;
import progettoIGPE.davide.giovanni.unical2016.PowerUp;
import progettoIGPE.davide.giovanni.unical2016.Rocket;
import progettoIGPE.davide.giovanni.unical2016.SteelWall;
import progettoIGPE.davide.giovanni.unical2016.Tank;
import progettoIGPE.davide.giovanni.unical2016.Tree;
import progettoIGPE.davide.giovanni.unical2016.Water;

@SuppressWarnings("serial")
public class GamePanel extends JPanel {
	
	public int tile;
	private double end;
	private long start;
	private int cursorPositionDialog;
	private static double tempFPS;
	private PanelSwitcher switcher;
	private int shift;
	private Long longTime;
	private FullGamePanel fullGamePanel;
	private JDialog dialog;
	private JButton[] buttons;
	private GameManager game;
	private String difficult;
	private ConnectionManager connectionManager;
	private String playerName;
	private final int ExitDelay = 500;
	
	//SCORE
	private int currentResumeP1;
	private int currentLevelP1;
	private int currentResumeP2;
	private int currentLevelP2;
	
	//online
	public GamePanel(PanelSwitcher switcher, String difficult) {
		this.tile = 35;
		this.dialog = new JDialog();
		this.setBackground(Color.BLACK);
		this.longTime = new Long(0);
		tempFPS = 1.5d;
		this.shift = 17;
		this.difficult = difficult;
		this.setSwitcher(switcher);
		this.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(final KeyEvent event) {
	
				if (!SoundsProvider.stageStartClip.isActive()) {
					if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
						if (!game.pauseOptionDialog && !game.isExit()) {
							((MainFrame)getSwitcher()).setTransparent(true);
							game.pauseOptionDialog = true;
							option();
						}
					} else if (event.getKeyCode() == KeyEvent.VK_ENTER) {
						if (!game.paused && !game.isExit()) {
							SoundsProvider.playPause();
							game.paused = true;
						} else {
							game.paused = false;
						}
					}

				}

				int keyCode = event.getKeyCode();
				// MULTIPLAYER
				if (game.getPlayersArray().get(0).getDefaultKeysPlayer().contains(keyCode) && keyCode != 32) {
					if (!game.pauseOptionDialog) {
						if (!game.getPlayersArray().get(0).keyBits.get(keyCode) && !game.getPlayersArray().get(0).isPressed())
							game.getPlayersArray().get(0).setKeyPressedMillis(System.currentTimeMillis());
					}
				}
				game.getPlayersArray().get(0).keyBits.set(keyCode);
				connectionManager.dispatch(getUpdateMessage(event,"YES", game.getPlayersArray().get(0).getKeyPressedMillis(),
						game.getPlayersArray().get(0).isReleaseKeyRocket(), game.pauseOptionDialog, game.paused));
			}

			@Override
			public void keyReleased(final KeyEvent event) {

				int keyCode = event.getKeyCode();

				if (keyCode == 32) {
					game.getPlayersArray().get(0).setReleaseKeyRocket(false);
				}
				
				game.getPlayersArray().get(0).keyBits.clear(keyCode);
				connectionManager.dispatch(getUpdateMessage(event,"NO",game.getPlayersArray().get(0).getKeyPressedMillis(),
						game.getPlayersArray().get(0).isReleaseKeyRocket(), game.pauseOptionDialog, game.paused));
			}
		});

	}

	//offline
	public GamePanel(final int w, final int h, PanelSwitcher switcher, GameManager game) {
		
		this.setPreferredSize(new Dimension(w, h));
		this.setGame(game);
		this.shift = 17;
		this.tile = 35;
		this.dialog = new JDialog();
		this.setSwitcher(switcher);
		this.setBackground(Color.BLACK);
		this.longTime = new Long(0);
		cursorPositionDialog = 0;
		tempFPS = 1.5d;

		if(game.getPlayersArray().size() == 1) {
			
			currentResumeP1 = ((MainFrame)switcher).getCurrentResumeP1();
			currentLevelP1 = ((MainFrame)switcher).getCurrentLevelP1();
			game.getPlayersArray().get(0).setResume(currentResumeP1);
			game.getPlayersArray().get(0).setLevel(currentLevelP1);
		}
		else 
			loadScoreMulti();

		this.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(final KeyEvent event) {
	
				if (!SoundsProvider.stageStartClip.isActive()) {
					if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
						if (!game.pauseOptionDialog && !game.isExit()) {
							((MainFrame)getSwitcher()).setTransparent(true);
							game.pauseOptionDialog = true;
							if (game.getPlayersArray().size() > 1) {
								game.getPlayersArray().get(0).getKeys().clear();
								game.getPlayersArray().get(1).getKeys().clear();
								game.getPlayersArray().get(1).keyBits.clear();
							} else {
								game.getPlayersArray().get(0).getKeys().clear();
							}
							game.getPlayersArray().get(0).keyBits.clear();
								
							option();
						}
					} else if (event.getKeyCode() == KeyEvent.VK_ENTER) {

						if (!game.paused && !game.isExit()) {
							SoundsProvider.playPause();
							if (game.getPlayersArray().size() > 1) {
								game.getPlayersArray().get(0).getKeys().clear();
								game.getPlayersArray().get(1).getKeys().clear();
								game.getPlayersArray().get(1).keyBits.clear();
							} else {
								game.getPlayersArray().get(0).getKeys().clear();
							}
							game.getPlayersArray().get(0).keyBits.clear();
								
							game.paused = true;
						} else {
							game.paused = false;
						}
					}

				}

				int keyCode = event.getKeyCode();
				// MULTIPLAYER
				if (game.getPlayersArray().size() > 1 && GameManager.offline) {
					
					if (game.getPlayersArray().get(0).getDefaultKeysPlayer().contains(keyCode) && keyCode != 32) {
						if (!game.pauseOptionDialog) {
							if (!game.getPlayersArray().get(0).keyBits.get(keyCode) && !game.getPlayersArray().get(0).isPressed())
								game.getPlayersArray().get(0).setKeyPressedMillis(System.currentTimeMillis());
						}
					}

					if (game.getPlayersArray().get(1).getDefaultKeysPlayer().contains(keyCode) && keyCode != 17) {
						if (!game.pauseOptionDialog) {
							if (!game.getPlayersArray().get(1).keyBits.get(keyCode)&& !game.getPlayersArray().get(1).isPressed())
								game.getPlayersArray().get(1).setKeyPressedMillis(System.currentTimeMillis());
						}
					}
					game.getPlayersArray().get(0).keyBits.set(keyCode);
					game.getPlayersArray().get(1).keyBits.set(keyCode);

				} else { // SINGLEPLAYER
					if (game.getPlayersArray().get(0).getDefaultKeysPlayer().contains(keyCode) && keyCode != 32) {
						if (!game.pauseOptionDialog ) {
							if (!game.getPlayersArray().get(0).keyBits.get(keyCode) && !game.getPlayersArray().get(0).isPressed())
								game.getPlayersArray().get(0).setKeyPressedMillis(System.currentTimeMillis());
						}
					}
					game.getPlayersArray().get(0).keyBits.set(keyCode);
				}		
			}

			@Override
			public void keyReleased(final KeyEvent event) {

				int keyCode = event.getKeyCode();

				if (game.getPlayersArray().size() > 1) {
					if (keyCode == 17) {
						game.getPlayersArray().get(1).setReleaseKeyRocket(false);
					}
					if (keyCode == 32) {
						game.getPlayersArray().get(0).setReleaseKeyRocket(false);
					}
					
					game.getPlayersArray().get(0).keyBits.clear(keyCode);
					game.getPlayersArray().get(1).keyBits.clear(keyCode);
				} else {
					if (keyCode == 32) {
						game.getPlayersArray().get(0).setReleaseKeyRocket(false);
					}
					game.getPlayersArray().get(0).keyBits.clear(keyCode);
				}
	
			}
		});

		new Thread() {

			@Override
			public void run() {
				gameLoop();
			}
		}.start();

	}
	
	// ----------------------GAMELOOP---------------------------//
	
	public void gameLoop() {
		
		while (!game.isExit()) {
			
			if (!game.paused) {
				start = System.nanoTime();
				
				logic();
				graphic();
				
				if (game.runnable != null)
					game.runnable.run();
				
				longTime = (System.nanoTime() - start);
				end = (double) (longTime.doubleValue() / 1000000);

			}else if(game.paused || game.pauseOptionDialog){
				SoundsProvider.cancelMove();
				SoundsProvider.cancelStop();
			}	
			repaint();
			if(fullGamePanel != null)
				fullGamePanel.repaint();
		}
		repaint();
		if(fullGamePanel != null)
			fullGamePanel.repaint();
	}
	
	public void logic() {
		
		// MANAGE KEYS
		keyPresses();
		
		// UPDATE ROCKETS
		rockets();

		// UPDATE ENEMIES
		enemies();

		// UPDATE PLAYERS
		players();

		// MANAGE SOUNDS
		sounds();

		// GAME OVER / WIN
		gameOverOrWin();
	}

	public void graphic() {

		synchronized (this) {

			// ANIMATION ROCKET
			for (int a = 0; a < game.getRocket().size(); a++) {
				Rocket rocket = game.getRocket().get(a);
				game.getRocket().get(a).updateRect();
				if (game.collision(rocket)) {
					game.destroyRocket(rocket);
				} else {
					if (!game.getRocket().get(a).isUpdateObject() && game.getRocket().get(a).isRocketForPlayer()) {

						game.getRocket().get(a)
								.setCont(contFPS(game.getRocket().get(a), game.getRocket().get(a).getDirection(),
										game.getRocket().get(a).getCont(),
										game.returnSpeed(game.getRocket().get(a).getTank().getSpeedShot(),
												game.getRocket().get(a)),
										end));
					}
					// if (game.getRocket().get(a).getCont() >= tile &&
					// game.getRocket().get(a).isFirstAnimationNo()) {
					// game.getRocket().get(a).setFirstAnimationNo(false);
					// }
					
					if(!game.getRocket().get(a).rect.intersects(game.getRocket().get(a).getTank().rect)){
						game.getRocket().get(a).setFirstAnimationNo(false);
					}

					if (game.getRocket().get(a).getCont() >= tile) {

						// prima di creare il secondo rocket al livello > 1 devo
						// aver finito l animazione del primo
						if (game.getRocket().get(a).getTank() instanceof PlayerTank
								&& ((PlayerTank) game.getRocket().get(a).getTank()).isEnter()
								&& ((PlayerTank) game.getRocket().get(a).getTank()).getLevel() >= 1)
							((PlayerTank)game.getRocket().get(a).getTank()).setFinish(true);

						game.getRocket().get(a).FPS();
						if (!game.getRocket().get(a).isOnBorder())
							game.getRocket().get(a).setUpdateObject(true);
					}
				}
			}
		}

		// ANIMATION ENEMY
		for (int a = 0; a < game.getEnemy().size(); a++) {
			((Tank) game.getEnemy().get(a)).updateRect();
			if (game.getEnemy().get(a).isAppearsInTheMap() && !game.getEnemy().get(a).isNoUpdateG()
					&& !game.getEnemy().get(a).isUpdateObject() && game.getEnemy().get(a).canGo
					&& !game.getEnemy().get(a).isStopEnemyGraphic()) {

				game.getEnemy().get(a)
						.setCont(contFPS(game.getEnemy().get(a), game.getEnemy().get(a).getDirection(),
								game.getEnemy().get(a).getCont(),
								game.returnSpeed(game.getEnemy().get(a).getSpeed(), game.getEnemy().get(a)), end));
			}
			if ((game.getEnemy().get(a).getCont() >= tile || game.getEnemy().get(a).isNoUpdateG()
					|| !game.getEnemy().get(a).canGo)

					&& game.getEnemy().get(a).isAppearsInTheMap()) {

				if (game.getEnemy().get(a).isStopEnemy()) {
					game.getEnemy().get(a).setStopEnemyGraphic(true);
				}

				game.getEnemy().get(a).FPS();
				game.getEnemy().get(a).setUpdateObject(true);
			}
		}

		// ANIMATION PLAYER
		for (int a = 0; a < game.getPlayersArray().size(); a++) {
			if (noIntersectPlayerWithEnemy(a)) {
				((Tank) game.getPlayersArray().get(a)).updateRect();
				if (game.getPlayersArray().get(a).isPressed() && game.getPlayersArray().get(a).getKeyPressLength() != 0
						&& game.getPlayersArray().get(a).getKeyPressLength() > 120) {
					if (!game.getPlayersArray().get(a).isOldDirection()) {
						game.getPlayersArray().get(a)
								.setCont(contFPS(game.getPlayersArray().get(a),
										game.getPlayersArray().get(a).getTmpDirection(),
										game.getPlayersArray().get(a).getCont(),
										game.returnSpeed(game.getPlayersArray().get(a).getSpeed(),
												game.getPlayersArray().get(a)),
										end));
					} else {
						game.getPlayersArray().get(a)
								.setCont(contFPS(game.getPlayersArray().get(a), game.getPlayersArray().get(a).getOldD(),
										game.getPlayersArray().get(a).getCont(),
										game.returnSpeed(game.getPlayersArray().get(a).getSpeed(),
												game.getPlayersArray().get(a)),
										end));
					}
				}

				if (game.getPlayersArray().get(a).getCont() >= tile) {
					game.getPlayersArray().get(a).setxGraphics(game.getPlayersArray().get(a).getX() * tile);
					game.getPlayersArray().get(a).setyGraphics(game.getPlayersArray().get(a).getY() * tile);
					game.getPlayersArray().get(a).FPS();
					if (game.getPlayersArray().get(a).isOldDirection())
						game.getPlayersArray().get(a).setOldDirection(false);
					game.getPlayersArray().get(a).setOld(game.getPlayersArray().get(a).getTmpDirection());
					game.getPlayersArray().get(a).setPressed(false);
				}
			}
		}

	}

	// ----------------------------ONLINE----------------------------------------

	protected String getUpdateMessage(KeyEvent code, String string, long getKeyPressedMillis, 
			boolean isReleaseKeyRocket, boolean pauseOptionDialog, boolean paused) {
		return playerName + ":" + code.getKeyCode()+":"+string+":"+getKeyPressedMillis+":"+
			isReleaseKeyRocket+":"+pauseOptionDialog+":"+paused;
	}
	
	protected String getUpdatePaintComponent(boolean isWaitToExit){
		return "PAINT"+":"+isWaitToExit;
	}
		
	GameManager startNetwork(ConnectionManager connectionManager, JTextField filename) {
		this.connectionManager = connectionManager;
		playerName = connectionManager.getNameOfGame();
		System.out.println("GamePanel.startNetwork() " + playerName);
		requestFocus();
		game = new GameManager(filename, playerName);
		return game;
	}
	
	//--------------------------------------------------------------------------
	
	private void changeRotationForIce(Tank t) {

		if (t.getOldD() == Direction.UP) {
			t.setRotateDegrees(0);
		} else if (t.getOldD() == Direction.DOWN) {
			t.setRotateDegrees(180);
		} else if (t.getOldD() == Direction.LEFT) {
			t.setRotateDegrees(270);
		} else if (t.getOldD() == Direction.RIGHT) {
			t.setRotateDegrees(90);
		}
		t.setOldDirection(true);
	}

	public void option() {

		dialog.setPreferredSize(new Dimension(250, 250));
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
//		JPanel buttonspanel = new JPanel(new GridLayout(4, 1, 0, 40));
		JLabel label = new JLabel("Option");
		String[] buttonTxt = { "Retry", "Restart","Menu"};
//		String[] buttonTxt = { "Retry", "Menu", "Restart", "Exit" };
		fullpanel.setPreferredSize(new Dimension(250, 250));
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
			if( i == 1 && !GameManager.offline)
				buttons[i].setForeground(Color.GRAY);
			else
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
							if(GameManager.offline) {
								dialog.dispose();
							}
					} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						((MainFrame)getSwitcher()).setTransparent(false);
						game.pauseOptionDialog = false;
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
		// dialog.setResizable(true);
		dialog.setModal(true);
		dialog.pack();
		dialog.setLocationRelativeTo(this);
		dialog.setVisible(true);
	
	}

	public void optionActionListener(int j) {

		switch (j) {
		case 0: // RETRY
			buttons[j].addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					SoundsProvider.playBulletHit1();
					((MainFrame)getSwitcher()).setTransparent(false);
					game.pauseOptionDialog = false;
					dialog.dispose();
				}
			});
			break;
		case 1:// RESTART
			buttons[j].addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					if(GameManager.offline) {
						SoundsProvider.playStageStart();
						SoundsProvider.playBulletHit1();
						((MainFrame)getSwitcher()).setTransparent(false);
						game.setExit(true);
						dialog.dispose();
						getSwitcher().showSlide(game.getFilename());
						SoundsProvider.cancelMove();
						SoundsProvider.cancelStop();
					}
				}
			});
			break;
		case 2: // MENU
			buttons[j].addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
				
					SoundsProvider.playBulletHit1();
					((MainFrame)getSwitcher()).setTransparent(false);
					game.pauseOptionDialog = false;
					game.setExit(true);
					dialog.dispose();
//					if(!GameManager.offline){
//						if(getGameManager().getPlayersArray().get(1).isDied() )
//							connectionManager.dispatch("EXIT"+":"+connectionManager.getNameOfGame()+":"+"true");
//						else
//							connectionManager.dispatch("EXIT"+":"+connectionManager.getNameOfGame()+":"+"false");
//					}
					getSwitcher().showMenu();
					SoundsProvider.cancelMove();
					SoundsProvider.cancelStop();
				}
			});
			break;
			/*
		case 3: // EXIT
			buttons[j].addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}

			});
			break;
			*/
		default:
			break;
		}
	}

	private void rotation(AbstractDynamicObject ob, Direction d) {
		if (d == Direction.UP) {
			ob.setRotateDegrees(0);
		} else if (d == Direction.DOWN) {
			ob.setRotateDegrees(180);
		} else if (d == Direction.LEFT) {
			ob.setRotateDegrees(270);
		} else if (d == Direction.RIGHT) {
			ob.setRotateDegrees(90);
		}
	}

	private double contFPS(AbstractStaticObject object, Direction dir, double contFPSobject, double pixel,
			double delta) {
		if (dir == Direction.LEFT) {
			contFPSobject += (pixel / tempFPS) * delta;
			object.setyGraphics(object.getyGraphics() - ((pixel / tempFPS) * delta));
		} else if (dir == Direction.RIGHT) {
			contFPSobject += (pixel / tempFPS) * delta;
			object.setyGraphics(object.getyGraphics() + ((pixel / tempFPS) * delta));
		} else if (dir == Direction.UP) {
			contFPSobject += (pixel / tempFPS) * delta;
			object.setxGraphics(object.getxGraphics() - ((pixel / tempFPS) * delta));
		} else if (dir == Direction.DOWN) {
			contFPSobject += (pixel / tempFPS) * delta;
			object.setxGraphics(object.getxGraphics() + ((pixel / tempFPS) * delta));
		}
		return contFPSobject;
	}

	private void extend(Direction d, PlayerTank player) {

		if (!player.isPressed()) {
			if (player.getKeyPressLength() > 120) {
				player.setPressed(true);
				player.setDirection(d);
				player.setTmpDirection(d);
				if (player.getNext() instanceof Ice) {
					changeRotationForIce(player);
					player.setDirection(player.getOldD());
				}
			} else {
				player.setTmpDirection(d);
				if (player.getNext() instanceof Ice)
					player.setOld(d);
			}
		}
	}

	private void keyPresses() {

		for (int a = 0; a < game.getPlayersArray().size(); a++) {
				game.getPlayersArray().get(a).setKeyPressLength(
				System.currentTimeMillis() - game.getPlayersArray().get(a).getKeyPressedMillis());
		}

		for (int a = 0; a < game.getPlayersArray().size(); a++) {
		
				for (int i = 0; i < game.getPlayersArray().get(a).keyBits.size(); i++) {
					if (game.getPlayersArray().get(a).keyBits.get(i) && firstTime(i, game.getPlayersArray().get(a).getKeys())
							&& ((a == 0 && game.getPlayersArray().get(0).getDefaultKeysPlayer().contains(i))
									|| (a == 1 && game.getPlayersArray().get(1).getDefaultKeysPlayer().contains(i)))) {
						game.getPlayersArray().get(a).getKeys().add(i);
					}
				}
		}

		for (int a = 0; a < game.getPlayersArray().size(); a++) {
			if (!game.getPlayersArray().get(a).getKeys().isEmpty()) {
				isKeyPressed(game.getPlayersArray().get(a).getKeys().get(game.getPlayersArray().get(a).getKeys().size() - 1), a);
				if (a == 0 && game.getPlayersArray().get(a).getKeys()
						.get(game.getPlayersArray().get(a).getKeys().size() - 1) == 32) { // contemporanemrnate
					// cammini e spari
					// rimani bloccato
					game.getPlayersArray().get(a).getKeys().remove(game.getPlayersArray().get(a).getKeys().size() - 1);
					game.getPlayersArray().get(a).keyBits.clear(32);
				} else if (a == 1 && game.getPlayersArray().get(a).getKeys()
						.get(game.getPlayersArray().get(a).getKeys().size() - 1) == 17) { // contemporanemrnate
					// cammini e spari
					// rimani bloccato
					game.getPlayersArray().get(a).getKeys().remove(game.getPlayersArray().get(a).getKeys().size() - 1);
					game.getPlayersArray().get(a).keyBits.clear(17);
				}
				for (int i = 0; i < game.getPlayersArray().get(a).getKeys().size(); i++) {
					if (!game.getPlayersArray().get(a).keyBits.get(game.getPlayersArray().get(a).getKeys().get(i))) {
						game.getPlayersArray().get(a).getKeys().remove(i);
					}
				}
			}
		}
	}

	public void isKeyPressed(final int keyCode, int i) {
		//SINGLEPLAYER | MULTIPLAYER ONLINE
		if(game.getPlayersArray().size() == 1 || !GameManager.offline) {
			if (keyCode == 37) {
				extend(Direction.LEFT, game.getPlayersArray().get(i));
			} else if (keyCode == 38) {
				extend(Direction.UP, game.getPlayersArray().get(i));
			} else if (keyCode == 39) {
				extend(Direction.RIGHT, game.getPlayersArray().get(i));
			} else if (keyCode == 40) {
				extend(Direction.DOWN, game.getPlayersArray().get(i));
			}
			if (keyCode == 32 && !game.getPlayersArray().get(i).isReleaseKeyRocket()) {
	
				if (game.getPlayersArray().get(i).getContRocket() == 0) {
					game.createRocketTank(game.getPlayersArray().get(i).getTmpDirection(), game.getPlayersArray().get(i));
	
					if (game.getPlayersArray().get(i).getLevel() >= 1 && game.getPlayersArray().get(i).getContRocket() > 0)
						game.createRocketTank(game.getPlayersArray().get(i).getTmpDirection(),
								game.getPlayersArray().get(i));
	
					game.getPlayersArray().get(i).setEnter(true);
					game.getPlayersArray().get(i).setReleaseKeyRocket(true);
	
				}
			}
		}
		//MULTIPLAYER OFFLINE
		else{
				if (keyCode == 65) {
					extend(Direction.LEFT, game.getPlayersArray().get(1));
				} else if (keyCode == 87) {
					extend(Direction.UP, game.getPlayersArray().get(1));
				} else if (keyCode == 68) {
					extend(Direction.RIGHT, game.getPlayersArray().get(1));
				} else if (keyCode == 83) {
					extend(Direction.DOWN, game.getPlayersArray().get(1));
				}
	
				if (keyCode == 17 && !game.getPlayersArray().get(1).isReleaseKeyRocket()) {
					if (game.getPlayersArray().get(1).getContRocket() == 0) {
						game.createRocketTank(game.getPlayersArray().get(1).getTmpDirection(),
								game.getPlayersArray().get(1));
	
						if (game.getPlayersArray().get(1).getLevel() >= 1
								&& game.getPlayersArray().get(1).getContRocket() > 0)
							game.createRocketTank(game.getPlayersArray().get(1).getTmpDirection(),
									game.getPlayersArray().get(1));
						game.getPlayersArray().get(1).setEnter(true);
						game.getPlayersArray().get(1).setReleaseKeyRocket(true);
	
					}
				}
				
				
				if (keyCode == 37) {
					extend(Direction.LEFT, game.getPlayersArray().get(0));
				} else if (keyCode == 38) {
					extend(Direction.UP, game.getPlayersArray().get(0));
				} else if (keyCode == 39) {
					extend(Direction.RIGHT, game.getPlayersArray().get(0));
				} else if (keyCode == 40) {
					extend(Direction.DOWN, game.getPlayersArray().get(0));
				}
				if (keyCode == 32 && !game.getPlayersArray().get(0).isReleaseKeyRocket()) {
	
					if (game.getPlayersArray().get(0).getContRocket() == 0) {
						game.createRocketTank(game.getPlayersArray().get(0).getTmpDirection(), game.getPlayersArray().get(0));
	
						if (game.getPlayersArray().get(0).getLevel() >= 1 && game.getPlayersArray().get(0).getContRocket() > 0)
							game.createRocketTank(game.getPlayersArray().get(0).getTmpDirection(),
									game.getPlayersArray().get(0));
	
						game.getPlayersArray().get(0).setEnter(true);
						game.getPlayersArray().get(0).setReleaseKeyRocket(true);
	
					}
				}
			}
	}

	private boolean firstTime(int key, ArrayList<Integer> keys) {
		for (int i = 0; i < keys.size(); i++)
			if (key == keys.get(i))
				return false;
		return true;
	}

	private void powerUpPickUp(Tank t, PowerUp p) {
	    SoundsProvider.playPowerUpPick();

	    if (!game.isPresent(t, p)) {
	      p.setTank(t);
	      p.setActivate(true);
	      p.setTime(p.getDuration());
	      game.usePowerUp(p);
	      if(!game.getEffects().contains(p))
	        game.getEffects().add(p);

	    } else{
	    	
	    	if(p.getPowerUp() == Power.TIMER)
	    		game.stopEnemies();
	    	
	      game.sumPowerUp(t, p);
	      game.getPower().remove(p);
	    }
	    t.setNext(null); 
	    t.setCurr(null); // da vedere
	  }

	public void gameOverOrWin(){
		
		//SINGLEPLAYER
		if(GameManager.singlePlayer) {
			if (game.getFlag().isHit() || game.getPlayersArray().get(0).getResume() <= 0) {
				gameOver();
			}
			else 
				if (game.getEnemy().size() == 0) 
					win();
		}
		 //MULTIPLAYER
		else if(!GameManager.singlePlayer) {
			
			for(int a=0; a<game.getPlayersArray().size(); a++){
				if(game.getPlayersArray().get(a).getResume()<0 && !game.isExit()){
					game.getPlayersArray().get(a).setExitOnline(true); 
				}
			}
			
			if (((game.getPlayersArray().size() > 1
					&& (game.getPlayersArray().get(0).getResume() <= 0 && game.getPlayersArray().get(1).getResume() <= 0)))
					|| game.getPlayersArray().size() == 1 && ((game.getPlayersArray().get(0).getResume() <= 0 
					|| game.getPlayersArray().get(1).getResume() <= 0)) || game.getFlag().isHit()) {
				gameOver();
			}
			else 
				if (game.getEnemy().size() == 0) 
					win();
		}

	}
	
	private void gameOver() {

		if(GameManager.offline) {
			
			SoundsProvider.cancelMove();
			SoundsProvider.cancelStop();
			((MainFrame)getSwitcher()).setTransparent(true);
			repaint();
			
			((MainFrame)switcher).setSlide(true);
			((MainFrame)switcher).setCurrentResumeP1(3);
			((MainFrame)switcher).setCurrentLevelP1(0);
			
			if(game.getPlayersArray().size() > 1) { 
		
				((MainFrame)switcher).setCurrentResumeP2(3);
				((MainFrame)switcher).setCurrentLevelP2(0);
			}
			
			
			
			SoundsProvider.playGameOver();
			
			try {
				Thread.sleep(ExitDelay);
			} catch (InterruptedException e) {
	
				e.printStackTrace();
			}
			
			new TranslucentWindow(getSwitcher(), game.getFilename(),ImageProvider.getGameOver());
			game.timer.cancel();
			game.timer2.cancel();
		}
		
		game.setExit(true);
	}

	private void win() {
		
		if(GameManager.offline) {
			
			SoundsProvider.cancelMove();
			SoundsProvider.cancelStop();
			((MainFrame)getSwitcher()).setTransparent(true);
			repaint();
			
			currentResumeP1 = game.getPlayersArray().get(0).getResume();
			((MainFrame)switcher).setCurrentResumeP1(currentResumeP1);
			currentLevelP1 = game.getPlayersArray().get(0).getLevel();
			((MainFrame)switcher).setCurrentLevelP1(currentLevelP1);

			if(game.getPlayersArray().size() > 1) {
				currentResumeP2 = game.getPlayersArray().get(1).getResume();
				((MainFrame)switcher).setCurrentResumeP2(currentResumeP2);
				currentLevelP2 = game.getPlayersArray().get(1).getLevel();
				((MainFrame)switcher).setCurrentLevelP2(currentLevelP2);
			}
			
			try {
				Thread.sleep(ExitDelay);
			} catch (InterruptedException e) {
			
				e.printStackTrace();
			}
			
			SoundsProvider.playStageComplete();
			new TranslucentWindow(getSwitcher(), game.getFilename(),ImageProvider.getStageComplete());
			
			game.timer.cancel();
			game.timer2.cancel();
		}
		
		game.setExit(true);
	}

	private void sounds() {

		if (game.isSoundPowerUp()) {
			if(GameManager.offline)
				SoundsProvider.playPowerUpAppear();
			game.setSoundPowerUp(false);
		}

		if (game.isExplosion()) {
			if(GameManager.offline){
				SoundsProvider.playExplosion1();
				SoundsProvider.playExplosion2();
			}
			game.setExplosion(false);
		}

		// players
		for (int a = 0; a < game.getPlayersArray().size(); a++) {

			if (!SoundsProvider.stageStartClip.isActive()) {
				if (game.getPlayersArray().get(a).isShot()) {
					if(GameManager.offline){
						SoundsProvider.playBulletShot();
					}
					game.getPlayersArray().get(a).setShot(false);
				}

				if(GameManager.offline)
					//SINGLEPLAYER OFFLINE
					//TODO
					if (game.getPlayersArray().size() == 1 && a==0) {
						if (!game.getPlayersArray().get(a).isPressed())
							SoundsProvider.playStop();
						else
							SoundsProvider.playMove();
					}else{//MULTIPLAYER OFFLINE
						if (!game.getPlayersArray().get(a).isPressed())
							SoundsProvider.playStop();
						else
							SoundsProvider.playMove();
					}
			}
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

	private void players() {

		// LOGIC
		for (int a = 0; a < game.getPlayersArray().size(); a++) {
		
				if (game.getPlayersArray().get(a).getKeyPressLength() > 120)
					game.getPlayersArray().get(a).update();

				if (game.getPlayersArray().get(a).isReadyToSpawn() && game.getPlayersArray().get(a).isFirst()) {
					game.getPlayersArray().get(a).setSpawnTime((GameManager.currentTime + 4) % 60);
					game.getPlayersArray().get(a).setFirst(false);
				}

				// SE CE UN OSTACOLO APPLICHI MINI EFFETTO
				if (!game.getPlayersArray().get(a).canGo) {
					if (game.getPlayersArray().get(a).getNext() instanceof Tank)
						game.getPlayersArray().get(a).setCont(35);
					else
						game.getPlayersArray().get(a).setCont(30);
					SoundsProvider.playHitForCanGo();
				}

				if (game.getPlayersArray().get(a).getNext() instanceof PowerUp) {
					game.getPlayersArray().get(a).getStatistics().calculate( ((PowerUp) game.getPlayersArray().get(a).getNext()) );
					powerUpPickUp(game.getPlayersArray().get(a), ((PowerUp) game.getPlayersArray().get(a).getNext()));

				}
			}
	}

	private void enemies() {

//		System.out.println(game.getEffects().size());

		// SPAWN ENEMY
		game.spawnEnemy();

		// LOGIC
		for (int a = 0; a < game.getEnemy().size(); a++) {
			if (game.getEnemy().get(a).isUpdateObject() && game.getEnemy().get(a).isAppearsInTheMap()
					&& !game.getEnemy().get(a).isStopEnemy()) {

				game.getEnemy().get(a).setxGraphics(game.getEnemy().get(a).getX() * tile);
				game.getEnemy().get(a).setyGraphics(game.getEnemy().get(a).getY() * tile);

				if(GameManager.offline) {
				// EASY
				if (SettingsPanel.easy)
					game.getEnemy().get(a).easy();
				else
					if(SettingsPanel.normal)
					 game.getEnemy().get(a).medium();
				 else
					if (SettingsPanel.hard) {
						game.getEnemy().get(a).difficult(GameManager.flag.getX(), GameManager.flag.getY());
						
						if (!game.getEnemy().get(a).isHasApath()) {
							PlayerTank player = game.getPlayersArray().get(game.getEnemy().get(a).getRandomObject());
							game.getEnemy().get(a).difficult(player.getX(), player.getY());
						}
					}
				
				}
				else {
					
					if (difficult.equals("easy"))
						game.getEnemy().get(a).easy();
					else
						if(difficult.equals("normal"))
							game.getEnemy().get(a).medium();
					else
						if (difficult.equals("hard")) {
							game.getEnemy().get(a).difficult(GameManager.flag.getX(), GameManager.flag.getY());
						
							if (!game.getEnemy().get(a).isHasApath()) {
								PlayerTank player = game.getPlayersArray().get(game.getEnemy().get(a).getRandomObject());
								game.getEnemy().get(a).difficult(player.getX(), player.getY());
							}
						}	
				}

				game.getEnemy().get(a).setTmpDirection(game.getEnemy().get(a).getDirection());

				if (game.isShotEnabled() && GameManager.currentTime % 2 == 0)
					game.createRocketTank(game.getEnemy().get(a).getDirection(), game.getEnemy().get(a));
			}
		}
		for (int a = 0; a < game.getEnemy().size(); a++) {
			if (game.getEnemy().get(a).isUpdateObject() && game.getEnemy().get(a).isAppearsInTheMap()
					&& !game.getEnemy().get(a).isStopEnemy()) {
				//TODO in prova
				if (game.getEnemy().get(a).getNext() instanceof PowerUp
						&& ((PowerUp) game.getEnemy().get(a).getNext()).getPowerUp() == Power.HELMET) {
					powerUpPickUp(game.getEnemy().get(a), ((PowerUp) game.getEnemy().get(a).getNext()));
				}
				game.getEnemy().get(a).update();

				

				game.getMatrix().world[game.getEnemy().get(a).getX()][game.getEnemy().get(a).getY()] = game.getEnemy()
						.get(a);
				game.getEnemy().get(a).setUpdateObject(false);
			}
		}

	}

	private void rockets() {

		synchronized (this) {

			// LOGIC
			for (int a = 0; a < game.getRocket().size(); a++) {
				if (game.getRocket().get(a).getTank() instanceof PlayerTank
						&& (((PlayerTank)game.getRocket().get(a).getTank()).isFinish() || game.getRocket().get(a).getTank().getContRocket() == 1)
						&& ((PlayerTank) game.getRocket().get(a).getTank()).isEnter()
						&& ((PlayerTank) game.getRocket().get(a).getTank()).getLevel() >= 1) {
					if (!game.getRocket().get(a).isUpdateObject()) {
						game.getRocket().get(a).setUpdateObject(true);
						game.getRocket().get(a).setRocketForPlayer(true);
						((PlayerTank)game.getRocket().get(a).getTank()).setFinish(false);
						((PlayerTank) game.getRocket().get(a).getTank()).setEnter(false);
					}
				}

				if (game.getRocket().get(a).isUpdateObject()) {

					game.updateRocket(game.getRocket().get(a));
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
	}

	private boolean noIntersectPlayerWithEnemy(int a) {
		for (int x = 0; x < game.getEnemy().size(); x++) {
			if (game.getEnemy().get(x).isAppearsInTheMap()
					&& game.getEnemy().get(x).rect.intersects(game.getPlayersArray().get(a).rect)) {
				return false;
			}
		}
		return true;
	}

	// -----------------------PAINT----------------------------//

	private boolean powerUpShovelActive(int a, int b) {
		int x = game.getFlag().getX();
		int y = game.getFlag().getY();
		boolean trovato = false;

		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				if (i == a && j == b) {
					trovato = true;
					break;
				}
			}
		}

		if (trovato) {
			for (int a1 = 0; a1 < game.getPower().size(); a1++) {
				if (game.getPower().get(a1).getPowerUp() == Power.SHOVEL && game.getPower().get(a1).isActivate()) {
					return true;
				}
			}
		}
		return false;
	}

	private void paintPowerUp(Graphics g, PowerUp power) {

		if (power != null) {
			int b = power.getY();
			int a = power.getX();

			if (!(power.getBefore() instanceof Water)) {

				if (power.getPowerUp() == Power.GRENADE) {
					g.drawImage(ImageProvider.getGrenade(), b * tile, a * tile, null);
				}
				if (power.getPowerUp() == Power.HELMET) {
					g.drawImage(ImageProvider.getHelmet(), b * tile, a * tile, null);
				}
				if (power.getPowerUp() == Power.SHOVEL) {
					g.drawImage(ImageProvider.getShovel(), b * tile, a * tile, null);
				}
				if (power.getPowerUp() == Power.STAR) {
					g.drawImage(ImageProvider.getStar(), b * tile, a * tile, null);
				}
				if (power.getPowerUp() == Power.TANK) {
					g.drawImage(ImageProvider.getTank(), b * tile, a * tile, null);
				}
				if (power.getPowerUp() == Power.TIMER) {
					g.drawImage(ImageProvider.getTimer(), b * tile, a * tile, null);
				}
			}

			else // ALTRIMENTI 80 % DENTRO L'ACQUA e 20 % FUORI
			{

				// CASO PARTICOLARE PER DOMANDE CHIEDERE ALOIA
				if (power.getBeforeWater() instanceof BrickWall) {
					g.drawImage(ImageProvider.getBrick(), power.getY() * tile, power.getX() * tile, null);
				} else if (power.getBeforeWater() instanceof SteelWall) {
					g.drawImage(ImageProvider.getSteel(), power.getY() * tile, power.getX() * tile, null);
				} else if (power.getBeforeWater() instanceof Tree) {
					g.drawImage(ImageProvider.getTree(), power.getY() * tile, power.getX() * tile, null);
				} else if (power.getBeforeWater() instanceof Ice) {
					g.drawImage(ImageProvider.getIce(), power.getY() * tile, power.getX() * tile, null);
				}

				int y = power.getBefore().getY() * tile;
				int x = power.getBefore().getX() * tile;

				if (power.getDropDirection() == Direction.LEFT)
					y -= 7;

				if (power.getDropDirection() == Direction.RIGHT)
					y += 7;

				if (power.getDropDirection() == Direction.UP)
					x -= 7;

				if (power.getDropDirection() == Direction.DOWN)
					x += 10;

				if (power.getPowerUp() == Power.GRENADE) {
					g.drawImage(ImageProvider.getGrenade(), y, x, null);
				}
				if (power.getPowerUp() == Power.HELMET) {
					g.drawImage(ImageProvider.getHelmet(), y, x, null);
				}
				if (power.getPowerUp() == Power.SHOVEL) {
					g.drawImage(ImageProvider.getShovel(), y, x, null);
				}
				if (power.getPowerUp() == Power.STAR) {
					g.drawImage(ImageProvider.getStar(), y, x, null);
				}
				if (power.getPowerUp() == Power.TANK) {
					g.drawImage(ImageProvider.getTank(), y, x, null);
				}
				if (power.getPowerUp() == Power.TIMER) {
					g.drawImage(ImageProvider.getTimer(), y, x, null);
				}
			}
		}
	}

	private void paintTrees(Graphics g) {
		for (int a = 0; a < game.getMatrix().getRow(); a++) {
			for (int b = 0; b < game.getMatrix().getColumn(); b++) {
				if (game.getMatrix().objectStatic[a][b] instanceof Tree && !powerUpShovelActive(a, b)) {
					g.drawImage(ImageProvider.getTree(), b * tile, a * tile, null);
				}
			}
		}
	}

	private void paintEnemy(Graphics g, Graphics2D g2d) {

		for (int i = 0; i < game.getEnemy().size(); i++) {

			// EFFETTO NASCITA ENEMY
			if (game.getEnemy().get(i).isReadyToSpawn() && !game.getEnemy().get(i).isAppearsInTheMap()) {

				if (game.getEnemy().get(i).getCountdown() == 0)
					g.drawImage(ImageProvider.getAppear1(), game.getEnemy().get(i).getY() * tile,
							game.getEnemy().get(i).getX() * tile, null);
				else if (game.getEnemy().get(i).getCountdown() == 1)
					g.drawImage(ImageProvider.getAppear2(), game.getEnemy().get(i).getY() * tile,
							game.getEnemy().get(i).getX() * tile, null);
				else if (game.getEnemy().get(i).getCountdown() == 2)
					g.drawImage(ImageProvider.getAppear3(), game.getEnemy().get(i).getY() * tile,
							game.getEnemy().get(i).getX() * tile, null);
				else
					g.drawImage(ImageProvider.getAppear4(), game.getEnemy().get(i).getY() * tile,
							game.getEnemy().get(i).getX() * tile, null);
			}

			if (game.getEnemy().get(i).isAppearsInTheMap()) {
				AffineTransform at = AffineTransform.getTranslateInstance(game.getEnemy().get(i).getyGraphics(),
						game.getEnemy().get(i).getxGraphics());
				rotation(game.getEnemy().get(i), game.getEnemy().get(i).getTmpDirection());
				at.rotate(Math.toRadians(game.getEnemy().get(i).getRotateDegrees()), tile / 2, tile / 2);

				if (game.getEnemy().get(i).getNext() instanceof PowerUp
						&& !(((PowerUp) game.getEnemy().get(i).getNext()).getPowerUp() == Power.HELMET)
						&& ((PowerUp) game.getEnemy().get(i).getNext()).getBefore() instanceof Ice) {
					g.drawImage(ImageProvider.getIce(), game.getEnemy().get(i).getY() * tile,
							game.getEnemy().get(i).getX() * tile, null);
				}

				if (game.getEnemy().get(i).getNext() instanceof PowerUp) {
					paintPowerUp(g, (PowerUp) game.getEnemy().get(i).getNext());
				}

				///
				long time = 0;

				if (!((MainFrame)getSwitcher()).isTransparent())
					time = (System.currentTimeMillis() / 200) % 2;
				///

				if (game.getEnemy().get(i) instanceof ArmorTank) {
					if (time == 0) {
						g2d.drawImage(ImageProvider.getArmorA(), at, null);
					} else {
						if (game.getEnemy().get(i).isPowerUpOn())
							g2d.drawImage(ImageProvider.getArmorPowerUpB(), at, null);
						else
							g2d.drawImage(ImageProvider.getArmorB(), at, null);
					}
				} else if (game.getEnemy().get(i) instanceof FastTank) {
					if (time == 0) {
						g2d.drawImage(ImageProvider.getFastA(), at, null);
					} else {
						if (game.getEnemy().get(i).isPowerUpOn())
							g2d.drawImage(ImageProvider.getFastPowerUpA(), at, null);
						else
							g2d.drawImage(ImageProvider.getFastB(), at, null);
					}
				} else if (game.getEnemy().get(i) instanceof BasicTank) {
					if (time == 0) {
						g2d.drawImage(ImageProvider.getBasicA(), at, null);
					} else {
						if (game.getEnemy().get(i).isPowerUpOn())
							g2d.drawImage(ImageProvider.getBasicPowerUpA(), at, null);
						else
							g2d.drawImage(ImageProvider.getBasicB(), at, null);
					}
				} else if (game.getEnemy().get(i) instanceof PowerTank) {
					if (time == 0) {
						g2d.drawImage(ImageProvider.getPowerA(), at, null);
					} else {
						if (game.getEnemy().get(i).isPowerUpOn())
							g2d.drawImage(ImageProvider.getPowerPowerUpB(), at, null);
						else
							g2d.drawImage(ImageProvider.getPowerB(), at, null);
					}
				}

				if (game.getEnemy().get(i).getNext() instanceof PowerUp
						&& !(((PowerUp) game.getEnemy().get(i).getNext()).getPowerUp() == Power.HELMET)
						&& ((PowerUp) game.getEnemy().get(i).getNext()).getBefore() instanceof Tree) {
					g.drawImage(ImageProvider.getTree(), game.getEnemy().get(i).getY() * tile,
							game.getEnemy().get(i).getX() * tile, null);
				}

				// EFFETTO PROTEZIONE ENEMY
				if (game.getEnemy().get(i).isProtection()) {
					if (game.getEnemy().get(i).getCountdown() == 0)
						g2d.drawImage(ImageProvider.getShield1(), at, null);
					else if (game.getEnemy().get(i).getCountdown() == 1)
						g2d.drawImage(ImageProvider.getShield2(), at, null);
				}
			}
		}
	}

	private void paintIce(Graphics g) {
		for (int a = 0; a < game.getMatrix().getRow(); a++) {
			for (int b = 0; b < game.getMatrix().getColumn(); b++) {
				if (game.getMatrix().objectStatic[a][b] instanceof Ice && !powerUpShovelActive(a, b))
					g.drawImage(ImageProvider.getIce(), b * tile, a * tile, null);
			}
		}
	}

	private void paintRocket(Graphics g, Graphics2D g2d) {

		synchronized (this) {
			for (int i = 0; i < game.getRocket().size(); i++) {
	
				if (GameManager.offline && game.getRocket().get(i).getNext() instanceof PowerUp) {
					paintPowerUp(g, (PowerUp) game.getRocket().get(i).getNext());
				}
	
				if (GameManager.offline && game.getRocket().get(i).getCurr() instanceof PowerUp) {
					paintPowerUp(g, (PowerUp) game.getRocket().get(i).getCurr());
				}
	
				if (!game.getRocket().get(i).isFirstAnimationNo()) {
					AffineTransform at = AffineTransform.getTranslateInstance(game.getRocket().get(i).getyGraphics(),
							game.getRocket().get(i).getxGraphics());
					rotation(game.getRocket().get(i), game.getRocket().get(i).getDirection());
					at.rotate(Math.toRadians(game.getRocket().get(i).getRotateDegrees()), tile / 2, tile / 2);
					g2d.drawImage(ImageProvider.getRocket(), at, null);
				}
	
				// DA SEMPRE PROBLEMI DI ECCEZZIONI
				if (GameManager.offline && game.getRocket().get(i).getNext() instanceof PowerUp)
					if (((PowerUp) game.getRocket().get(i).getNext()).getBefore() instanceof Tree) {
						g.drawImage(ImageProvider.getTree(), game.getRocket().get(i).getY() * tile,
								game.getRocket().get(i).getX() * tile, null);
					}
			}
		}
	}

	private void paintWater(Graphics g) {
		for (int a = 0; a < game.getMatrix().getRow(); a++) {
			for (int b = 0; b < game.getMatrix().getColumn(); b++) {
				if (game.getMatrix().objectStatic[a][b] instanceof Water && !powerUpShovelActive(a, b)) {
					if (GameManager.currentTime % 2 == 0)
						g.drawImage(ImageProvider.getWaterA(), b * tile, a * tile, null);
					else
						g.drawImage(ImageProvider.getWaterB(), b * tile, a * tile, null);
				}
			}
		}
	}

	private void paintEffects(Graphics g, Graphics g2d) {

		for (int i = 0; i < game.getEffects().size(); i++) {

			int X, Y, pixel, inc;
			X = (int) game.getEffects().get(i).getxGraphics();
			Y = (int) game.getEffects().get(i).getyGraphics();

			// ROCKET boom
			if (game.getEffects().get(i) instanceof Rocket) {
				inc = ((Rocket) game.getEffects().get(i)).getInc();
				pixel = 10;
				switch (((Rocket) game.getEffects().get(i)).getDirection()) {
				case UP:
					X += pixel;
					break;
				case DOWN:
					X -= pixel;
					break;
				case LEFT:
					Y += pixel;
					break;
				case RIGHT:
					Y -= pixel;
					break;
				default:
					break;
				}

				if (inc == 1)
					g.drawImage(ImageProvider.getRocketExplosion1(), Y, X, null);
				else if (inc == 2)
					g.drawImage(ImageProvider.getRocketExplosion2(), Y, X, null);
				else if (inc == 3)
					g.drawImage(ImageProvider.getRocketExplosion3(), Y, X, null);
				else if (inc > 3 && GameManager.offline) {
					game.getEffects().remove(game.getEffects().get(i));
					i--;
				}
			}

			// FLAG boom
			else if (game.getEffects().get(i) instanceof Flag) {

				inc = ((Flag) game.getEffects().get(i)).getInc();
				pixel = 17;

				if (inc == 1)
					g.drawImage(ImageProvider.getBigExplosion1(), Y - pixel, X - pixel, null);
				else if (inc == 2)
					g.drawImage(ImageProvider.getBigExplosion2(), Y - pixel, X - pixel, null);
				else if (inc == 3)
					g.drawImage(ImageProvider.getBigExplosion3(), Y - pixel, X - pixel, null);
				else if (inc == 4)
					g.drawImage(ImageProvider.getBigExplosion4(), Y - pixel, X - pixel, null);
				else if (inc == 5)
					g.drawImage(ImageProvider.getBigExplosion5(), Y - pixel, X - pixel, null);
				else if (inc > 5 && GameManager.offline) {
					game.getEffects().remove(game.getEffects().get(i));
					i--;
				}
			}

			// ENEMY & PLAYER boom
			else if (game.getEffects().get(i) instanceof Tank) {

				inc = ((Tank) game.getEffects().get(i)).getInc();
				pixel = 17;

				if (inc == 1)
					g.drawImage(ImageProvider.getBigExplosion1(), Y - pixel, X - pixel, null);
				else if (inc == 2)
					g.drawImage(ImageProvider.getBigExplosion2(), Y - pixel, X - pixel, null);
				else if (inc == 3)
					g.drawImage(ImageProvider.getBigExplosion3(), Y - pixel, X - pixel, null);
				else if (inc == 4)
					g.drawImage(ImageProvider.getBigExplosion4(), Y - pixel, X - pixel, null);
				else if (inc == 5)
					g.drawImage(ImageProvider.getBigExplosion5(), Y - pixel, X - pixel, null);
				
				//ONLY PLAYER
				else if (inc > 5 && game.getEffects().get(i) instanceof PlayerTank ) {
					game.getEffects().remove(game.getEffects().get(i));
					i--;
				}
				// ONLY ENEMY POINTS
				else if (game.getEffects().get(i) instanceof EnemyTank) {
						if (((EnemyTank) game.getEffects().get(i)).getInc() > 5
								&& ((EnemyTank) game.getEffects().get(i)).getInc() < 12) {
							if (game.getEffects().get(i) instanceof BasicTank) {
								g.drawImage(ImageProvider.getPoints100(), game.getEffects().get(i).getY() * tile,
										game.getEffects().get(i).getX() * tile, null);
							} else if (game.getEffects().get(i) instanceof PowerTank) {
								g.drawImage(ImageProvider.getPoints300(), game.getEffects().get(i).getY() * tile,
										game.getEffects().get(i).getX() * tile, null);
							} else if (game.getEffects().get(i) instanceof ArmorTank) {
								g.drawImage(ImageProvider.getPoints400(), game.getEffects().get(i).getY() * tile,
										game.getEffects().get(i).getX() * tile, null);
							} else if (game.getEffects().get(i) instanceof FastTank) {
								g.drawImage(ImageProvider.getPoints200(), game.getEffects().get(i).getY() * tile,
										game.getEffects().get(i).getX() * tile, null);
							}
						} else if (((EnemyTank) game.getEffects().get(i)).getInc() >= 12) {
							game.getEffects().remove(game.getEffects().get(i));
							i--;
						}
					}
			}

	
		
			// PowerUp points
			else if (game.getEffects().get(i) instanceof PowerUp) {
				if (((PowerUp) game.getEffects().get(i)).getInc() > 5
						&& ((PowerUp) game.getEffects().get(i)).getInc() < 12) {
					g.drawImage(ImageProvider.getPoints500(), game.getEffects().get(i).getY() * tile,
							game.getEffects().get(i).getX() * tile, null);
				}
				if (((PowerUp) game.getEffects().get(i)).getInc() >= 12) {
					game.getEffects().remove(game.getEffects().get(i));
					i--;
				}
			}
		}
	}

	private void paintPlayer(Graphics g, Graphics2D g2d) {

		for (int a = 0; a < game.getPlayersArray().size(); a++) {
			
				AffineTransform at = AffineTransform.getTranslateInstance(game.getPlayersArray().get(a).getyGraphics(),
						game.getPlayersArray().get(a).getxGraphics());
				rotation(game.getPlayersArray().get(a), game.getPlayersArray().get(a).getTmpDirection());
				at.rotate(Math.toRadians(game.getPlayersArray().get(a).getRotateDegrees()), tile / 2, tile / 2);

				//OFFLINE
				if (game.getPlayersArray().size() > 1 && GameManager.offline) {

					// PLAYER1
					if (a == 0) {

						if (game.getPlayersArray().get(a).getCont() < 15) {
							if (game.getPlayersArray().get(a).getLevel() == 0)
								g2d.drawImage(ImageProvider.getPlayer1A(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 1)
								g2d.drawImage(ImageProvider.getPlayer1A_s1(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 2)
								g2d.drawImage(ImageProvider.getPlayer1A_s2(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 3)
								g2d.drawImage(ImageProvider.getPlayer1A_s3(), at, null);
						} else {
							if (game.getPlayersArray().get(a).getLevel() == 0)
								g2d.drawImage(ImageProvider.getPlayer1B(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 1)
								g2d.drawImage(ImageProvider.getPlayer1B_s1(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 2)
								g2d.drawImage(ImageProvider.getPlayer1B_s2(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 3)
								g2d.drawImage(ImageProvider.getPlayer1B_s3(), at, null);
						}

					}

					// PLAYER2
					if (a == 1)
						if (game.getPlayersArray().get(a).getCont() < 15) { // MULTIPLAYER

							if (game.getPlayersArray().get(a).getLevel() == 0)
								g2d.drawImage(ImageProvider.getPlayer2A(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 1)
								g2d.drawImage(ImageProvider.getPlayer2A_s1(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 2)
								g2d.drawImage(ImageProvider.getPlayer2A_s2(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 3)
								g2d.drawImage(ImageProvider.getPlayer2A_s3(), at, null);
						} else {

							if (game.getPlayersArray().get(a).getLevel() == 0)
								g2d.drawImage(ImageProvider.getPlayer2B(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 1)
								g2d.drawImage(ImageProvider.getPlayer2B_s1(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 2)
								g2d.drawImage(ImageProvider.getPlayer2B_s2(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 3)
								g2d.drawImage(ImageProvider.getPlayer2B_s3(), at, null);
						}
				} else { 
					// SINGLEPLAYER | ONLINE

					// PLAYER1
					if (game.getPlayersArray().get(a).toString().equals("P1")) {
						if (game.getPlayersArray().get(a).getCont() < 15) {

							if (game.getPlayersArray().get(a).getLevel() == 0)
								g2d.drawImage(ImageProvider.getPlayer1A(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 1)
								g2d.drawImage(ImageProvider.getPlayer1A_s1(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 2)
								g2d.drawImage(ImageProvider.getPlayer1A_s2(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 3)
								g2d.drawImage(ImageProvider.getPlayer1A_s3(), at, null);
						} else {

							if (game.getPlayersArray().get(a).getLevel() == 0)
								g2d.drawImage(ImageProvider.getPlayer1B(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 1)
								g2d.drawImage(ImageProvider.getPlayer1B_s1(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 2)
								g2d.drawImage(ImageProvider.getPlayer1B_s2(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 3)
								g2d.drawImage(ImageProvider.getPlayer1B_s3(), at, null);
						}
					}

					if (game.getPlayersArray().get(a).toString().equals("P2")) {
						if (game.getPlayersArray().get(a).getCont() < 15) { // MULTIPLAYER

							if (game.getPlayersArray().get(a).getLevel() == 0)
								g2d.drawImage(ImageProvider.getPlayer2A(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 1)
								g2d.drawImage(ImageProvider.getPlayer2A_s1(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 2)
								g2d.drawImage(ImageProvider.getPlayer2A_s2(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 3)
								g2d.drawImage(ImageProvider.getPlayer2A_s3(), at, null);
						} else {

							if (game.getPlayersArray().get(a).getLevel() == 0)
								g2d.drawImage(ImageProvider.getPlayer2B(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 1)
								g2d.drawImage(ImageProvider.getPlayer2B_s1(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 2)
								g2d.drawImage(ImageProvider.getPlayer2B_s2(), at, null);
							else if (game.getPlayersArray().get(a).getLevel() == 3)
								g2d.drawImage(ImageProvider.getPlayer2B_s3(), at, null);
						}
					}
				}

				// EFFETTO SPAWN E PROTEZIONE
				if (game.getPlayersArray().get(a).isReadyToSpawn() || game.getPlayersArray().get(a).isProtection()) {
					if (game.getPlayersArray().get(a).getCountdown() == 0)
						g2d.drawImage(ImageProvider.getShield1(), at, null);
					else if (game.getPlayersArray().get(a).getCountdown() == 1)
						g2d.drawImage(ImageProvider.getShield2(), at, null);
				}

				// EFFETTO SPAWN E PROTEZIONE
				if (game.getPlayersArray().get(a).isReadyToSpawn() || game.getPlayersArray().get(a).isProtection()) {
					if (game.getPlayersArray().get(a).getCountdown() == 0)
						g2d.drawImage(ImageProvider.getShield1(), at, null);
					else if (game.getPlayersArray().get(a).getCountdown() == 1)
						g2d.drawImage(ImageProvider.getShield2(), at, null);
				}
			}

	}

	private void paintFlagBrickSteelPower(Graphics g) {

		for (int a = 0; a < game.getMatrix().getRow(); a++) {
			for (int b = 0; b < game.getMatrix().getColumn(); b++) {

				// paintFlag
				if (game.getMatrix().world[a][b] instanceof Flag) {
					if (!game.getFlag().isHit() && game.getPlayersArray().size() > 0)
						g.drawImage(ImageProvider.getFlag(), b * tile, a * tile, null);
					else {
						g.drawImage(ImageProvider.getFlag_destroyed(), b * tile, a * tile, null);
					}
				}

				// paintBrickWall
				if (game.getMatrix().world[a][b] instanceof BrickWall) {

					if (((BrickWall) game.getMatrix().world[a][b]).getBefore() == null) {
						g.drawImage(ImageProvider.getBrick(), b * tile, a * tile, null);
					} else { // altrimenti disegno powerUp
						PowerUp power = ((PowerUp) ((BrickWall) game.getMatrix().world[a][b]).getBefore());

						if (power.isBlink()) {
							if ((System.currentTimeMillis() / 400) % 2 == 0)
								paintPowerUp(g, power);
							else
								g.drawImage(ImageProvider.getBrick(), power.getY() * tile, power.getX() * tile, null);
						} else {
							paintPowerUp(g, (PowerUp) ((BrickWall) game.getMatrix().world[a][b]).getBefore());
						}
					}
				}

				// paintSteelWall
				if (game.getMatrix().world[a][b] instanceof SteelWall) {
					if (((SteelWall) game.getMatrix().world[a][b]).getBefore() == null) {
						g.drawImage(ImageProvider.getSteel(), b * tile, a * tile, null);
					} else { // altrimenti disegno powerUp
						PowerUp power = ((PowerUp) ((SteelWall) game.getMatrix().world[a][b]).getBefore());

						if (power.isBlink()) {
							if ((System.currentTimeMillis() / 400) % 2 == 0)
								paintPowerUp(g, power);
							else
								g.drawImage(ImageProvider.getSteel(), power.getY() * tile, power.getX() * tile, null);
						} else {
							paintPowerUp(g, (PowerUp) ((SteelWall) game.getMatrix().world[a][b]).getBefore());
						}
					}
				}
			}
		}
		
		// paintPowerUp
		for(int a=0; a<game.getPower().size(); a++){
			if (!game.getPower().get(a).isActivate()) {
				PowerUp power = game.getPower().get(a);
	
				if (power.isBlink()) {
					if ((System.currentTimeMillis() / 400) % 2 == 0) {
						paintPowerUp(g, power);
					} else {
						// ice
						if (power.getBefore() instanceof Ice)
							g.drawImage(ImageProvider.getIce(), power.getY() * tile, power.getX() * tile, null);
						// steel
						else if (power.getBefore() instanceof SteelWall)
							g.drawImage(ImageProvider.getSteel(), power.getY() * tile, power.getX() * tile, null);
						// tree
						else if (power.getBefore() instanceof Tree) {
							g.drawImage(ImageProvider.getTree(), power.getY() * tile, power.getX() * tile, null);
						} else if (power.getBefore() instanceof Water) {
	
							if (power.getBeforeWater() instanceof BrickWall) {
								g.drawImage(ImageProvider.getBrick(), power.getY() * tile, power.getX() * tile,
										null);
							} else if (power.getBeforeWater() instanceof SteelWall) {
								g.drawImage(ImageProvider.getSteel(), power.getY() * tile, power.getX() * tile,
										null);
							} else if (power.getBeforeWater() instanceof Tree) {
								g.drawImage(ImageProvider.getTree(), power.getY() * tile, power.getX() * tile,
										null);
							} else if (power.getBeforeWater() instanceof Ice) {
								g.drawImage(ImageProvider.getIce(), power.getY() * tile, power.getX() * tile, null);
							}
						}
	
					}
				} else {
					paintPowerUp(g, power);
				}
			}
		}
	}

	@SuppressWarnings("unused")
	private void printLines(Graphics g, Graphics2D g2d){
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .2f));
		 for (int i = 0; i <= game.getHeight(); i++) {
		
				g.drawLine(0, i * tile, game.getWidth() * tile, i * tile);
				g.drawLine(i * tile, 0, i * tile, game.getHeight() * tile);
		
		 }
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
	}
	
	private void paused(Graphics g, Graphics2D g2d){
		if (game.paused && (System.currentTimeMillis() / 400) % 2 == 0) {
			g2d.drawImage(ImageProvider.getPause(), this.getWidth() / 2 - (70 + shift), getHeight() / 2 - (45 + shift), null);
		}
	}
	
	private void stroke(Graphics g, Graphics2D g2d){
		if (((MainFrame)getSwitcher()).isTransparent())
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		g.setColor(Color.GRAY);
		Stroke oldStroke = g2d.getStroke();
		g2d.setStroke(new BasicStroke(shift * 2 - 2));
		g2d.drawRect(1, 1, this.getWidth() - 3, this.getHeight() - 2);
		g2d.setStroke(oldStroke);
		if (((MainFrame)getSwitcher()).isTransparent())
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
	
		if (((MainFrame)getSwitcher()).isTransparent()) {
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .2f));
			g2d.setColor(getBackground());
			g2d.fill(getBounds());
		}
		
		stroke(g, g2d);
		
		g.translate(shift, shift);

//		printLines(g, g2d);

		paintWater(g);

		paintIce(g);

		if(!GameManager.offline)
			game.lock.lock();
		paintFlagBrickSteelPower(g);
		if(!GameManager.offline)
			game.lock.unlock();

		if(!GameManager.offline)
			game.lock.lock();
		paintEnemy(g, g2d);
		if(!GameManager.offline)
			game.lock.unlock();

		paintPlayer(g, g2d);
		
		if(!GameManager.offline)
			game.lock.lock();
		paintRocket(g, g2d);
		if(!GameManager.offline)
			game.lock.unlock();
		
		paintTrees(g);

		if(!GameManager.offline)
			game.lock.lock();
		paintEffects(g, g2d);
		if(!GameManager.offline)
			game.lock.unlock();

		paused(g, g2d);
		
		
		//TODO non serve piu
//		if(!GameManager.offline){
//			connectionManager.dispatch(getUpdatePaintComponent(game.isWaitToExit()));
//		}
	}
	
	//------------------------SCORE MULTI-------------------------//
	
	private void loadScoreMulti() {
		
		BufferedReader reader = null;
		String line = null;
		
		final int DIM = 7;
		String values[] = new String[DIM];
		
		try {
			
				reader = new BufferedReader(new FileReader("./values/multiCareer.txt"));
				line = reader.readLine();
				
				int i = 0;
				
				while(line != null) {
					
					StringTokenizer st = new StringTokenizer(line, "");
					String tmp = null;
					
					while(st.hasMoreTokens()) {
						
						tmp = st.nextToken();
							
							if(tmp.matches("^[0-9]+") && i < values.length)
								values[i++] = tmp;
					}
					
					line = reader.readLine();
				}
			}	
			catch (Exception e) {
				e.printStackTrace();
			}
			
			((MainFrame)switcher).setCurrentResumeP1(Integer.parseInt(values[1]));
			((MainFrame)switcher).setCurrentLevelP1(Integer.parseInt(values[2]));

			((MainFrame)switcher).setCurrentResumeP2(Integer.parseInt(values[4]));
			((MainFrame)switcher).setCurrentLevelP2(Integer.parseInt(values[5]));
			((MainFrame)switcher).setUnlockedMapsP2(Integer.parseInt(values[6]));
			
			
			//ASSEGNAZIONE VALORI AD ENTRAMBI I PLAYER
			currentResumeP1 = ((MainFrame)switcher).getCurrentResumeP1();
			game.getPlayersArray().get(0).setResume(currentResumeP1);
			
			currentLevelP1 = ((MainFrame)switcher).getCurrentLevelP1();
			game.getPlayersArray().get(0).setLevel(currentLevelP1);
			
			currentResumeP2 = ((MainFrame)switcher).getCurrentResumeP2();
			game.getPlayersArray().get(1).setResume(currentResumeP2);
			
			currentLevelP2 = ((MainFrame)switcher).getCurrentLevelP2();
			game.getPlayersArray().get(1).setLevel(currentLevelP2);
	}
	
	// -----------------------GET & SET--------------------------//

	public GameManager getGame() {
		return game;
	}

	public void setGame(GameManager game) {
		this.game = game;
	}

	public PanelSwitcher getSwitcher() {
		return switcher;
	}

	public GameManager getGameManager() {
		return game;
	}

	public void setSwitcher(PanelSwitcher switcher) {
		this.switcher = switcher;
	}

	public FullGamePanel getFullGamePanel() {
		return fullGamePanel;
	}

	public void setFullGamePanel(FullGamePanel fullGamePanel) {
		this.fullGamePanel = fullGamePanel;
	}

}
