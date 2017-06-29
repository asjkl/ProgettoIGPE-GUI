package progettoIGPE.davide.giovanni.unical2016.GUI;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JLabel;
import javax.swing.JPanel;
import progettoIGPE.davide.giovanni.unical2016.GameManager;
import progettoIGPE.davide.giovanni.unical2016.PlayerTank;
import progettoIGPE.davide.giovanni.unical2016.Power;

@SuppressWarnings("serial")
public class FullGamePanel extends JPanel {

	public GameManager gameManager;
	public GamePanel gamePanel;
	private String valueMap;
	private JLabel labelValueMap;
	private PanelPlayersInfo[] panelOfInfo;
	private int x;
	private int y;	
	public int shift = 17;

	public FullGamePanel(final int WIDTH, int HEIGHT, int gameWidth, int gameHeight, PanelSwitcher switcher,GamePanel gamePanel) {

		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setLayout(null);
		setBackground(Color.GRAY);
		this.gamePanel = gamePanel;
		this.gameManager = gamePanel.getGame();
		
		// +30 importante ( effects explosion )
	
		gamePanel.setFocusable(true);
		gamePanel.setBounds(282-shift, 20-shift, gameWidth+shift*2+3, gameHeight+shift*2);
		valueMap = gameManager.getFilename().getText().replaceAll("[^0-9]", "");
		labelValueMap = new JLabel();
		labelValueMap.setBounds(1150, 625, 30, 30);
		labelValueMap.setPreferredSize(new Dimension(30, 30));
		labelValueMap.setForeground(Color.BLACK);
		labelValueMap.setFont(LoadPanel.customFontM);
		labelValueMap.setText(valueMap);
		panelOfInfo = new PanelPlayersInfo[2];
		int position = 50;
		for (int a = 0; a < gameManager.getPlayersArray().size(); a++) {			
			panelOfInfo[a] = new PanelPlayersInfo(gameManager.getPlayersArray().get(a).toString());
			panelOfInfo[a].setForeground(Color.BLACK);
			panelOfInfo[a].setBounds(10, position, 250, 300);
			add(panelOfInfo[a]);
			position += 350;
		}
		add(gamePanel);
		add(labelValueMap);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		x = 1125;
		y = 15;
		for (int i = 0; i < gamePanel.getGameManager().getEnemy().size(); i++) {

			if (i % 3 == 0) {

				x = 1125;
				y += 40;
			}
			g.drawImage(ImageProvider.getIconEnemy(), x, y, null);

			x += 40;
		}
		g.drawImage(ImageProvider.getIconFlag(), 1125, 600, null);
	}

	public String getValueMap() {
		return valueMap;
	}

	private class PanelPlayersInfo extends JPanel {
		private String player;
		private JLabel PlayerLabel;
		private int x;
		private int y;

		public PanelPlayersInfo(String player) {
			this.player = player;
			this.setLayout(null);
			this.setPreferredSize(new Dimension(250, 300));
			this.setBackground(Color.GRAY);
			PlayerLabel = new JLabel(player);
			PlayerLabel.setPreferredSize(new Dimension(35, 35));
			PlayerLabel.setFont(LoadPanel.customFontM.deriveFont(Font.BOLD, 25));
			PlayerLabel.setBounds(0, 0, 35, 35);
			PlayerLabel.setForeground(Color.BLACK);
			this.add(PlayerLabel);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(Color.BLACK);
			String lives = null;

			if (player.equals("P1")) {
				if(gameManager.getPlayersArray().get(0).getResume()<0){
					((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
				}
				
				if (gameManager.getPlayersArray().get(0).getLevel() == 0) {
					g.drawImage(ImageProvider.getPlayer1A(), 60, 20, 40, 40, null);
				} else if (gameManager.getPlayersArray().get(0).getLevel() == 1) {
					g.drawImage(ImageProvider.getPlayer1A_s1(), 60, 20, 40, 40, null);
				} else if (gameManager.getPlayersArray().get(0).getLevel() == 2) {
					g.drawImage(ImageProvider.getPlayer1A_s2(), 60, 20, 40, 40, null);
				} else if (gameManager.getPlayersArray().get(0).getLevel() == 3) {
					g.drawImage(ImageProvider.getPlayer1A_s3(), 60, 20, 40, 40, null);
				}
				
				((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
				
				if (gameManager.getPlayersArray().get(0).getResume() >= 0)
					lives = Integer.toString(gameManager.getPlayersArray().get(0).getResume());
			} else if (player.equals("P2")) {
				if (gameManager.getPlayersArray().size() > 1) {
					
					if(gameManager.getPlayersArray().get(1).getResume()<0){
						((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
					}
					
					if (gameManager.getPlayersArray().get(1).getLevel() == 0) {
						g.drawImage(ImageProvider.getPlayer2A(), 60, 20, 40, 40, null);
					} else if (gameManager.getPlayersArray().get(1).getLevel() == 1) {
						g.drawImage(ImageProvider.getPlayer2A_s1(), 60, 20, 40, 40, null);
					} else if (gameManager.getPlayersArray().get(1).getLevel() == 2) {
						g.drawImage(ImageProvider.getPlayer2A_s2(), 60, 20, 40, 40, null);
					} else if (gameManager.getPlayersArray().get(1).getLevel() == 3) {
						g.drawImage(ImageProvider.getPlayer2A_s3(), 60, 20, 40, 40, null);
					}
					
					((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
					
					if (gameManager.getPlayersArray().get(1).getResume() >= 0)
						lives = Integer.toString(gameManager.getPlayersArray().get(1).getResume());
				} else {
					
					if(gameManager.getPlayersArray().get(0).getResume()<0){
						((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
					}
					
					if (gameManager.getPlayersArray().get(0).getLevel() == 0) {
						g.drawImage(ImageProvider.getPlayer2A(), 60, 20, 40, 40, null);
					} else if (gameManager.getPlayersArray().get(0).getLevel() == 1) {
						g.drawImage(ImageProvider.getPlayer2A_s1(), 60, 20, 40, 40, null);
					} else if (gameManager.getPlayersArray().get(0).getLevel() == 2) {
						g.drawImage(ImageProvider.getPlayer2A_s2(), 60, 20, 40, 40, null);
					} else if (gameManager.getPlayersArray().get(0).getLevel() == 3) {
						g.drawImage(ImageProvider.getPlayer2A_s3(), 60, 20, 40, 40, null);
					}

					((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
					
					if (gameManager.getPlayersArray().get(0).getResume() >= 0)
						lives = Integer.toString(gameManager.getPlayersArray().get(0).getResume());
				}
			}
			g.setFont(LoadPanel.customFontM);
			if (lives != null)
				g.drawString(lives, 100, 70);

			x = 25;
			y = 50;
			int cont = 0;
			for (int a = 0; a < gameManager.getPower().size(); a++) {
				if (gameManager.getPower().get(a).isActivate() && gameManager.getPower().get(a).getTank() instanceof PlayerTank
						&& (gameManager.getPower().get(a).getTank().toString().equals(player)
								|| (gameManager.getPower().get(a).getPowerUp() == Power.SHOVEL))) {
					if (cont % 3 == 0) {
						x = 25;
						y += 80;
					}
					String time = Integer.toString((int) gameManager.getPower().get(a).getTime());
	
					if (gameManager.getPower().get(a).getPowerUp() == Power.HELMET) {
						g.drawImage(ImageProvider.getHelmet(), x, y, null);
						g.drawString(time, x, y + 70);
					} else if (gameManager.getPower().get(a).getPowerUp() == Power.TIMER) {
						g.drawImage(ImageProvider.getTimer(), x, y, null);
						g.drawString(time, x, y + 70);
					} else if (gameManager.getPower().get(a).getPowerUp() == Power.SHOVEL) {
						g.drawImage(ImageProvider.getShovel(), x, y, null);
						g.drawString(time, x, y + 70);
					}
					x += 40;
					cont++;
				}
			}
		}

	}
}
