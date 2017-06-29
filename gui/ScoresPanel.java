//package progettoIGPE.davide.giovanni.unical2016.GUI;
//
//import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.Graphics;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.StringTokenizer;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.Timer;
//
//import progettoIGPE.davide.giovanni.unical2016.GameManager;
//
//@SuppressWarnings("serial")
//public class ScoresPanel extends JPanel {
//
//	private boolean enable;
//	private int totalEnemies;
//	private int highScore;
//	private int currScore;
//	private final int TANKS = 4;
//	private int occurrence[];
//	private PanelSwitcher panelSwitcher;	
//	private GameManager game;
//	private String value;
//	
//	public ScoresPanel(final int w, final int h, PanelSwitcher panelSwitcher, GameManager game, String value) {
//	
//		this.setPreferredSize(new Dimension(w, h));
//		this.setBackground(Color.BLACK);
//		this.setLayout(null);
//	
//		this.game = game;
//		this.value = value;
//		setEnable(false);
//		setSwitcher(panelSwitcher);
//		
//		game.getPlayersArray().getFirst().getStatistics().setNewRecord();
//		highScore = game.getPlayersArray().getFirst().getStatistics().getHighScore();
//		currScore = game.getPlayersArray().getFirst().getStatistics().getCurrScore();
//		totalEnemies = game.getPlayersArray().getFirst().getStatistics().getTotalOccurr();
//		
//		occurrence = new int[TANKS];
//
//		for(int i = 0 ; i < occurrence.length; i++)
//			setArray(i);
//		
//		updateHighScore();
//		drawLabel();
//		
//		Timer timer = new Timer(8000,new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				getSwitcher().showMenu();
//			}
//		});
//		timer.start();
//		timer.setRepeats(false);
//	}
//	
//	public void setArray(int j) {
//		
//		switch(j) {
//		case 0:
//			occurrence[j] = game.getPlayersArray().getFirst().getStatistics().getBasicTankOcc();
//			break;
//		case 1:
//			occurrence[j] = game.getPlayersArray().getFirst().getStatistics().getFastTankOcc();
//			break;
//		case 2:
//			occurrence[j] = game.getPlayersArray().getFirst().getStatistics().getPowerTankOcc();
//			break;
//		case 3:
//			occurrence[j] = game.getPlayersArray().getFirst().getStatistics().getArmorTankOcc();
//			break;
//		default:
//			break;
//		}
//	}
//			
//	private void drawLabel() {
//		
//		JLabel stage = new JLabel();
//		
//		stage.setFont(LoadPanel.customFontB);
//		stage.setBackground(Color.BLACK);
//		stage.setForeground(Color.WHITE);
//		stage.setText("Stage " + value);
//		stage.setBounds(555, 215, 300, 100);
//		
//		this.add(stage);
//		
//		for(int i = 0, y = 310; i < TANKS; i++, y += 65) {
//		
//			JLabel pts = new JLabel();
//			JLabel text = new JLabel();
//			text.setFont(LoadPanel.customFontB);
//			
//			if(i == 0) {
//							
//				text.setBackground(Color.BLACK);
//				text.setForeground(Color.WHITE);
//				text.setText("Total");
//				text.setBounds(545, 585, 300, 100);
//			}
//			else
//				if(i == 1) {
//		
//					JLabel score = new JLabel();	
//					
//					score.setFont(text.getFont());
//					score.setBackground(Color.BLACK);
//					score.setForeground(Color.ORANGE);
//					score.setText(String.valueOf(currScore));
//					score.setBounds(545, 125, 300, 100);
//					this.add(score);
//					
//					text.setForeground(Color.RED);
//					text.setText("I-Player");
//					text.setBounds(365, 125, 300, 100);
//				}
//				else 
//					if(i == 3) {
//		
//					JLabel hiScore = new JLabel();	
//					
//					hiScore.setFont(text.getFont());
//					hiScore.setBackground(Color.BLACK);
//					hiScore.setForeground(Color.ORANGE);
//					hiScore.setText(String.valueOf(highScore));
//					hiScore.setBounds(895, 125, 300, 100);
//					this.add(hiScore);
//					
//					text.setForeground(Color.RED);
//					text.setText("Hi-Score");
//					text.setBounds(685, 125, 300, 100);
//				}
//			
//			pts.setFont(text.getFont());
//			pts.setBackground(Color.BLACK);
//			pts.setForeground(Color.WHITE);
//			pts.setText("pts");
//			pts.setBounds(560, y, 300, 100);
//			
//			this.add(pts);
//			this.add(text);
//		}
//		
//		//LABEL OCCORRENZE ENEMIES UCCISI
//		new Thread() {
//			
//			@Override
//			public void run() {
//				
//				try {
//					
//					int positionX = 675;
//					int positionY = 340;
//					
//					for(int i = 0; i < occurrence.length; i++, positionY += 65) {
//					
//						JLabel occur = new JLabel();
//						JLabel points = new JLabel();
//						
//						points.setFont(LoadPanel.customFontB);
//						points.setBackground(Color.BLACK);
//						points.setForeground(Color.WHITE);
//						
//						occur.setFont(LoadPanel.customFontB);
//						occur.setBackground(Color.BLACK);
//						occur.setForeground(Color.WHITE);
//						
//						add(occur);
//						add(points);
//						
//						int currValue = 0;
//						
//						while(currValue <= occurrence[i]) {
//							
//							SoundsProvider.playScore();
//							
//							points.setText(String.valueOf(currValue * 100 * (i + 1)));
//							
//							if(currValue == 0)
//								points.setBounds(positionX - 199, positionY, 45, 45);
//							else
//								points.setBounds(positionX - 245, positionY, 95, 45);
//							
//							occur.setText(String.valueOf(currValue));
//							occur.setBounds(positionX, positionY, 95, 45);
//							currValue++;
//							Thread.sleep(300);
//						}
//					}
//					
//					JLabel total = new JLabel();
//					
//					total.setFont(LoadPanel.customFontB);
//					total.setBackground(Color.BLACK);
//					total.setForeground(Color.WHITE);
//					add(total);
//					
//					for(int j = 0; j <= totalEnemies; j++) {
//						
//						total.setText(String.valueOf(j));
//						total.setBounds(positionX, positionY + 13, 95, 45);
//						SoundsProvider.playScore();
//						Thread.sleep(300);
//					}
//					
//					setEnable(true);
//					
//					writeScore();
//				}
//				catch(InterruptedException e) {
//						e.printStackTrace();
//				}
//			}
//			
//		}.start();
//	}
//	
//	public void writeScore() {
//		
//		BufferedWriter b = null;
//		PrintWriter w = null;
//	
//		try {
//			
//			w = new PrintWriter("./values.txt");
//			b = new BufferedWriter(w);
//			
//			b.write("SCORE:\n");
//			b.write(String.valueOf(currScore + "\n"));
//			b.write(String.valueOf(highScore + "\n"));
//			b.write("MAPS:\n");
//			b.write(String.valueOf(MenuPanel.unlockedMaps));
//			b.flush();
//			b.close();
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	private void updateHighScore() {
//		
//		BufferedReader reader = null;
//		String line = null;
//		String scores[] = new String[2];
//		
//		try {
//			
//			reader = new BufferedReader(new FileReader("./values.txt"));
//			line = reader.readLine();
//			
//			int i = 0;
//			
//			while(line != null) {
//				
//				StringTokenizer st = new StringTokenizer(line, "");
//				String tmp = null;
//				
//				while(st.hasMoreTokens()) {
//					
//					tmp = st.nextToken();
//					
//					if(!tmp.equals("SCORE:") && !tmp.equals("MAPS:")) {
//						
//						if(Integer.parseInt(tmp) > 24 || 
//								Integer.parseInt(tmp) == 0)
//							scores[i++] = tmp;
//					}
//				}
//				
//				line = reader.readLine();
//			}
//			
//			if((Integer.parseInt(scores[1])) > highScore)
//				highScore = Integer.parseInt(scores[1]);
//			
//		} catch(IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Override
//	protected void paintComponent(Graphics g) {
//		super.paintComponent(g);
//		
//		int positionX = 805; 
//		int positionY = 345;
//		
//		for(int i = 0; i < occurrence.length; i++, positionY += 65) {
//			
//			if(i == 0) {
//				
//				g.drawImage(ImageProvider.getBasicA(), positionX, positionY - 5, null);
//				g.drawImage(ImageProvider.getPtsArrow(), positionX - 70, positionY, null);
//			}	
//			else
//			if(i == 1) {
//				
//				g.drawImage(ImageProvider.getFastA(), positionX, positionY - 5, null);
//				g.drawImage(ImageProvider.getPtsArrow(), positionX - 70, positionY, null);
//			}
//			else
//			if(i == 2) {
//				
//				g.drawImage(ImageProvider.getPowerA(), positionX, positionY - 5, null);
//				g.drawImage(ImageProvider.getPtsArrow(), positionX - 70, positionY, null);
//			}
//			else
//			if(i == 3) {
//				
//				g.drawImage(ImageProvider.getArmorA(), positionX, positionY - 5, null);
//				g.drawImage(ImageProvider.getPtsArrow(), positionX - 70, positionY, null);
//			}		
//		}
//		
//		g.drawImage(ImageProvider.getBar(), 660, 590, null);
//	}
//	
//	public PanelSwitcher getSwitcher() {
//		return panelSwitcher;
//	}
//
//	public void setSwitcher(PanelSwitcher panelSwitcher) {
//		this.panelSwitcher = panelSwitcher;
//	}
//
//	public boolean isEnable() {
//		return enable;
//	}
//
//	public void setEnable(boolean enable) {
//		this.enable = enable;
//	}
//
//}

package progettoIGPE.davide.giovanni.unical2016.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import progettoIGPE.davide.giovanni.unical2016.GameManager;
import progettoIGPE.davide.giovanni.unical2016.PlayerTank;

@SuppressWarnings("serial")
public class ScoresPanel extends JPanel {

	private int totalEnemiesP1;
	private int totalEnemiesP2;
	private int highScoreP1;
	private int highScoreP2;
	private int currScoreP1;
	private int currScoreP2;
	private int x, y;
	private final int TANKS = 4;
	private int occurrence[][];
	private PanelSwitcher panelSwitcher;	
	private GameManager game;
	private String value;
	
	public ScoresPanel(final int w, final int h, PanelSwitcher panelSwitcher, GameManager game, String value) {
	
		this.setPreferredSize(new Dimension(w, h));
		this.setBackground(Color.BLACK);
		this.setLayout(null);
	
		this.game = game;
		this.value = value;
		setSwitcher(panelSwitcher);
		
		occurrence = new int[game.getPlayersArray().size()][TANKS];
		
		if(game.getPlayersArray().size() == 1)
			setUp1Player();
		else 
			setUp2Players();
		
			
		Timer timer = new Timer(8000,new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				getSwitcher().showMenu();
			}
		});
		timer.start();
		timer.setRepeats(false);
	}
	
	public void setUp1Player() {
		
		x = 805;
		y = 345;

		game.getPlayersArray().getFirst().getStatistics().setNewRecord();
		highScoreP1 = game.getPlayersArray().getFirst().getStatistics().getHighScore();
		currScoreP1 = game.getPlayersArray().getFirst().getStatistics().getCurrScore();
		totalEnemiesP1 = game.getPlayersArray().getFirst().getStatistics().getTotalOccurr();
	
		for(int i = 0 ; i < occurrence.length; i++) {
		
			PlayerTank p = game.getPlayersArray().get(i);
		
			for(int j = 0; j < occurrence[i].length; j++) {
				setOccurrence(p, i, j);
			}
		}
	
		updateHighScore();
		drawLabelP1();
	}
	
	public void setUp2Players() {
	
		//repaint();
		
		x = 385;
		y = 305;
		
		game.getPlayersArray().getFirst().getStatistics().setNewRecord();
		highScoreP1 = game.getPlayersArray().getFirst().getStatistics().getHighScore();
		currScoreP1 = game.getPlayersArray().getFirst().getStatistics().getCurrScore();
		totalEnemiesP1 = game.getPlayersArray().getFirst().getStatistics().getTotalOccurr();
		
		game.getPlayersArray().getLast().getStatistics().setNewRecord();
		highScoreP2 = game.getPlayersArray().getLast().getStatistics().getHighScore();
		currScoreP2 = game.getPlayersArray().getLast().getStatistics().getCurrScore();
		totalEnemiesP2 = game.getPlayersArray().getLast().getStatistics().getTotalOccurr();
		
		for(int i = 0 ; i < occurrence.length; i++) {
			
			PlayerTank p = game.getPlayersArray().get(i);
		
			for(int j = 0; j < occurrence[i].length; j++) {
				setOccurrence(p, i, j);
			}
		}
		
		updateHighScore();
		drawLabelP2();
	}
		
	public void setOccurrence(PlayerTank p, int j, int k) {
		
		switch(k) {
		case 0:
			occurrence[j][k] = p.getStatistics().getBasicTankOcc();
			break;
		case 1:
			occurrence[j][k] = p.getStatistics().getFastTankOcc();
			break;
		case 2:
			occurrence[j][k] = p.getStatistics().getPowerTankOcc();
			break;
		case 3:
			occurrence[j][k] = p.getStatistics().getArmorTankOcc();
			break;
		default:
			break;
		}	
	}
	
	//TODO AGGIUNGER POINTS POWER UP
	private void drawLabelP1() {
		
		JLabel stage = new JLabel();
		
		stage.setFont(LoadPanel.customFontB);
		stage.setBackground(Color.BLACK);
		stage.setForeground(Color.WHITE);
		stage.setText("Stage " + value);
		stage.setBounds(555, 215, 300, 100);
		
		this.add(stage);
		
		for(int i = 0, y = 310; i < TANKS; i++, y += 65) {
		
			JLabel pts = new JLabel();
			JLabel text = new JLabel();
			text.setFont(LoadPanel.customFontB);
			
			if(i == 1) {
				
				JLabel score = new JLabel();	
				
				score.setFont(text.getFont());
				score.setBackground(Color.BLACK);
				score.setForeground(Color.ORANGE);
				score.setText(String.valueOf(currScoreP1));
				score.setBounds(545, 125, 300, 100);
				this.add(score);
				
				text.setForeground(Color.RED);
				text.setText("I-Player");
				text.setBounds(365, 125, 300, 100);
			}
			else
				if(i == 2) {
					
					JLabel hiScore = new JLabel();	
					
					hiScore.setFont(text.getFont());
					hiScore.setBackground(Color.BLACK);
					hiScore.setForeground(Color.ORANGE);
					hiScore.setText(String.valueOf(highScoreP1));
					hiScore.setBounds(895, 125, 300, 100);
					this.add(hiScore);
					
					text.setForeground(Color.RED);
					text.setText("Hi-Score");
					text.setBounds(685, 125, 300, 100);
			}
			else
				if(i == 3) {
							
					text.setBackground(Color.BLACK);
					text.setForeground(Color.WHITE);
					text.setText("Total");
					text.setBounds(545, 585, 300, 100);
				}	
			
			pts.setFont(text.getFont());
			pts.setBackground(Color.BLACK);
			pts.setForeground(Color.WHITE);
			pts.setText("pts");
			pts.setBounds(560, y, 300, 100);
			
			this.add(pts);
			this.add(text);
		}
		
		//LABEL OCCORRENZE ENEMIES UCCISI
		new Thread() {
			
			@Override
			public void run() {
				
				try {
					
					int positionX = 675;
					int positionY = 340;
					
					for(int i = 0, j = 0; j < TANKS; j++, positionY += 65) {
					
						JLabel occur = new JLabel();
						JLabel points = new JLabel();
						
						points.setFont(LoadPanel.customFontB);
						points.setBackground(Color.BLACK);
						points.setForeground(Color.WHITE);
						
						occur.setFont(LoadPanel.customFontB);
						occur.setBackground(Color.BLACK);
						occur.setForeground(Color.WHITE);
						
						add(occur);
						add(points);
						
						int currValue = 0;
						
						while(currValue <= occurrence[i][j]) {
							
							points.setText(String.valueOf(currValue * 100 * (j + 1)));
							
							if(currValue == 0)
								points.setBounds(positionX - 199, positionY, 45, 45);
							else
								points.setBounds(positionX - 245, positionY, 95, 45);
							
							occur.setText(String.valueOf(currValue));
							occur.setBounds(positionX, positionY, 95, 45);
							SoundsProvider.playScore();
							currValue++;
							Thread.sleep(200);
						}
					}
					
					JLabel total = new JLabel();
					
					total.setFont(LoadPanel.customFontB);
					total.setBackground(Color.BLACK);
					total.setForeground(Color.WHITE);
					add(total);
					
					for(int j = 0; j <= totalEnemiesP1; j++) {
						
						total.setText(String.valueOf(j));
						total.setBounds(positionX, positionY + 13, 95, 45);
						SoundsProvider.playScore();
						Thread.sleep(300);
					}
					
					writeScore();
				}
				catch(InterruptedException e) {
						e.printStackTrace();
				}
			}
			
		}.start();
	}
	
	private void drawLabelP2() {	
		
		int k = 0;
		
		for(int i = 0; i < game.getPlayersArray().size(); i++) {
		
			for(int j = 0, posY = 270; j < TANKS; j++, posY += 65) {
			
				JLabel pts = new JLabel();
				JLabel text = new JLabel();
				text.setFont(LoadPanel.customFontB);

				if(j == 1) {
			
						JLabel score = new JLabel();	
						
						score.setFont(text.getFont());
						score.setBackground(Color.BLACK);
						score.setForeground(Color.ORANGE);
						score.setText(String.valueOf(currScoreP1));
						score.setBounds(385 + k, 105, 300, 100);
						this.add(score);
						
						text.setForeground(Color.RED);
						text.setText(String.valueOf(i + 1) + "-Player");
						text.setBounds(185 + k, 105, 300, 100);
					}
					else 
						if(j == 2) {
			
						JLabel hiScore = new JLabel();	
						
						hiScore.setFont(text.getFont());
						hiScore.setBackground(Color.BLACK);
						hiScore.setForeground(Color.ORANGE);
						hiScore.setText(String.valueOf(highScoreP1));
						hiScore.setBounds(395 + k, 185, 300, 100);
						this.add(hiScore);
						
						text.setForeground(Color.RED);
						text.setText("Hi-Score");
						text.setBounds(185 + k, 185, 300, 100);
					}
					else
						if(j == 3) {
					
						text.setBackground(Color.BLACK);
						text.setForeground(Color.WHITE);
						text.setText("Total");
						text.setBounds(150 + k, 550, 200, 100);
					}
			
				pts.setFont(LoadPanel.customFontB);
				pts.setBackground(Color.BLACK);
				pts.setForeground(Color.WHITE);
				pts.setText("pts");
				pts.setBounds(185 + k, posY, 100, 100);
				
				this.add(pts);
				this.add(text);
			}
			
			k += 595;
		}
		
		//LABEL OCCORRENZE ENEMIES UCCISI
		new Thread() {
		
			@Override
			public void run() {
				
				try {
				
					int positionX = 270;
					int positionY = 340;
					int positionK = 865;
					
					for(int i = 0; i < occurrence.length; i++) {

						positionY = 340;
						
						for(int j = 0; j < occurrence[i].length; j++, positionY += 65) {
				
							JLabel occur = new JLabel();
							JLabel points = new JLabel();
							
							points.setFont(LoadPanel.customFontB);
							points.setBackground(Color.BLACK);
							points.setForeground(Color.WHITE);
							
							occur.setFont(LoadPanel.customFontB);
							occur.setBackground(Color.BLACK);
							occur.setForeground(Color.WHITE);
							
							add(occur);
							add(points);
							
							int currValue = 0;
							int currPosition = 0;
							
							if(i == 0)
								currPosition = positionX;
							else
								currPosition = positionK;
							
							while(currValue <= occurrence[i][j]) {
								
								points.setText(String.valueOf(currValue * 100 * (j + 1)));
								
								if(currValue == 0)
									points.setBounds(currPosition - 139, positionY - 40, 45, 45);
								else
									points.setBounds(currPosition - 185, positionY - 40, 95, 45);
								
								occur.setText(String.valueOf(currValue));
								occur.setBounds(currPosition, positionY - 40, 95, 45);
								SoundsProvider.playScore();
								currValue++;
								Thread.sleep(200);
							}
						}				
					
					}
					
					JLabel totalP1 = new JLabel();
					
					totalP1.setFont(LoadPanel.customFontB);
					totalP1.setBackground(Color.BLACK);
					totalP1.setForeground(Color.WHITE);
					add(totalP1);
					
					for(int j = 0; j <= totalEnemiesP1; j++) {
						
						totalP1.setText(String.valueOf(j));
						totalP1.setBounds(positionX, positionY - 22, 95, 45);
						SoundsProvider.playScore();
						Thread.sleep(200);
					}
					
					JLabel totalP2 = new JLabel();
					totalP2.setFont(LoadPanel.customFontB);
					totalP2.setBackground(Color.BLACK);
					totalP2.setForeground(Color.WHITE);
					add(totalP2);
					
					
					for(int j = 0; j <= totalEnemiesP2; j++) {
						
						totalP2.setText(String.valueOf(j));
						totalP2.setBounds(positionK, positionY - 22, 95, 45);
						SoundsProvider.playScore();
						Thread.sleep(200);
					}
					
					writeScore();
				}
				catch(InterruptedException e) {
						e.printStackTrace();
				}
			}
			
		}.start();
	}
	
	public void writeScore() {
		
		BufferedWriter b = null;
		PrintWriter w = null;
	
		try {
			
			w = new PrintWriter("./values.txt");
			b = new BufferedWriter(w);
			
			b.write("SCORE:\n");
			b.write(String.valueOf(currScoreP1 + "\n"));
			b.write(String.valueOf(highScoreP1 + "\n"));
			b.write("MAPS:\n");
			b.write(String.valueOf(MenuPanel.unlockedMaps));
			b.flush();
			b.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void updateHighScore() {
		
		BufferedReader reader = null;
		String line = null;
		String scores[] = new String[2];
		
		try {
			
			reader = new BufferedReader(new FileReader("./values.txt"));
			line = reader.readLine();
			
			int i = 0;
			
			while(line != null) {
				
				StringTokenizer st = new StringTokenizer(line, "");
				String tmp = null;
				
				while(st.hasMoreTokens()) {
					
					tmp = st.nextToken();
					
					if(!tmp.equals("SCORE:") && !tmp.equals("MAPS:")) {
						
						if(Integer.parseInt(tmp) > 24 || 
								Integer.parseInt(tmp) == 0)
							scores[i++] = tmp;
					}
				}
				
				line = reader.readLine();
			}
			
			if((Integer.parseInt(scores[1])) > highScoreP1)
				highScoreP1 = Integer.parseInt(scores[1]);
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int positionX = x; 
		
		for(int i = 0; i < occurrence.length; i++, positionX += 595) {
		
			int positionY = y;
			
			for(int j = 0; j < occurrence[i].length; j++, positionY += 65) {
			
				if(i == 0) {
					
					g.drawImage(ImageProvider.getBasicA(), positionX, positionY - 5, null);
					g.drawImage(ImageProvider.getPtsArrow(), positionX - 70, positionY, null);
				}	
				else
				if(i == 1) {
					
					g.drawImage(ImageProvider.getFastA(), positionX, positionY - 5, null);
					g.drawImage(ImageProvider.getPtsArrow(), positionX - 70, positionY, null);
				}
				else
				if(i == 2) {
					
					g.drawImage(ImageProvider.getPowerA(), positionX, positionY - 5, null);
					g.drawImage(ImageProvider.getPtsArrow(), positionX - 70, positionY, null);
				}
				else
				if(i == 3) {
					
					g.drawImage(ImageProvider.getArmorA(), positionX, positionY - 5, null);
					g.drawImage(ImageProvider.getPtsArrow(), positionX - 70, positionY, null);
				}		
			}
			
			g.drawImage(ImageProvider.getBar(), positionX - 145, positionY - 15, null);
		}
	}
	
	public PanelSwitcher getSwitcher() {
		return panelSwitcher;
	}

	public void setSwitcher(PanelSwitcher panelSwitcher) {
		this.panelSwitcher = panelSwitcher;
	}
}