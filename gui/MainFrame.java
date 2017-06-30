package progettoIGPE.davide.giovanni.unical2016.GUI;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.swing.*;

import net.ConnectionManager;
import net.NetworkPanel;
import progettoIGPE.davide.giovanni.unical2016.GameManager;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements PanelSwitcher {
	
//  private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();	
  private final int WIDTH = 1300;
  private final int HEIGHT = 740;
  private final int gameWidth = WIDTH - 565;
  private final int gameHeight = HEIGHT - 40;
  
  public static boolean slide = true;
  public static Font customFontM;
  public static Font customFontB;
  public static Font customFontS;
  public static boolean transparent = false;
  private GraphicsEnvironment graphicscEnvironment;
  
  private NetworkPanel network;
  private MenuPanel menu;
  private PlayerPanel player; 
  private StagePanelFirst firstStage;
  private StagePanelSecond secondStage;
  private ScoresPanel scores;
  private ConstructionPanel editor;
  private SettingsPanel settings;
  private SlideContainer slideContainer;
  public FullGamePanel play; 
  private GameManager gameManager;
  public GamePanel gamePanel;
  private SoundsProvider sounds;
  private ImageProvider images;
  
  public MainFrame() {
	  	
		 setFont();
		 network = new NetworkPanel(WIDTH, HEIGHT, this);
	  	 menu = new MenuPanel(WIDTH, HEIGHT, this);
	  	 
	  	 player = new PlayerPanel(WIDTH, HEIGHT, this);
	  	 firstStage = new StagePanelFirst(WIDTH, HEIGHT, this);
	  	 secondStage = new StagePanelSecond(WIDTH, HEIGHT, this);
	  	 editor = new ConstructionPanel(WIDTH, HEIGHT, this);
	  	 settings = new SettingsPanel(WIDTH, HEIGHT, this);
	  	 setImages(new ImageProvider());
	  	 setSounds(new SoundsProvider());

	  	 this.setLayout(new BorderLayout());
	  	 this.setTitle("BATTLE CITY UNICAL");  
	     this.setResizable(false);
	     switchTo(menu);
	     this.pack();
	     this.setLocationRelativeTo(null);
	     this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     this.setVisible(true);
	    
  	}
  	
	  public void switchTo(JComponent component) {   
		 this.getContentPane().removeAll();
		 this.add(component);
		 this.validate();
		 this.repaint();
		 component.transferFocus();
		 selectFocusButton(component);
	}

	  private void selectFocusButton(JComponent component) {
		  
		  if(component instanceof MenuPanel) { 
			  ((MenuPanel)component).getButton(menu.getCursorPosition()).requestFocus();
		  }
		  else 
			  if(component instanceof PlayerPanel) { 
				  ((PlayerPanel)component).getButton(player.getCursorPosition()).requestFocus();
		  }
		  else 
			  if(component instanceof StagePanelFirst) {
				  ((StagePanelFirst)component).getButton(firstStage.getCursorPosition()).requestFocus();
		  }
		  else 
			  if(component instanceof StagePanelSecond) {
				  ((StagePanelSecond)component).getButton(secondStage.getCursorPosition()).requestFocus();
		  }
		  else 
			  if(component instanceof ConstructionPanel) {
				  ((ConstructionPanel)component).getButton(editor.getCursorPosition()).requestFocus();
		  }
		  else 
			  if(component instanceof SettingsPanel) {
				  ((SettingsPanel)component).getButton(settings.getCursorPosition()).requestFocus();	  
		  }
		  else 
			  if(component instanceof NetworkPanel) {
				  ((NetworkPanel)component).getButton(network.getCursorPosition()).requestFocus();
			  }
	  }
	  
	  private void setFont(){
			
			try {
				
				customFontM = Font.createFont(Font.TRUETYPE_FONT, new File("./font/Minecraft.ttf")).deriveFont(25f);
				customFontB = Font.createFont(Font.TRUETYPE_FONT, new File("./font/Minecraft.ttf")).deriveFont(40f);
				customFontS = Font.createFont(Font.TRUETYPE_FONT, new File("./font/Minecraft.ttf")).deriveFont(16f);
				graphicscEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
				graphicscEnvironment.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("./font/Minecraft.ttf")));
			
			} catch(IOException e) {
				e.printStackTrace();
				
			} catch(FontFormatException e) {
				e.printStackTrace();
			}
		}
	  
	  @Override
	  public void showMenu() {
		  
		  menu.drawScore();
		  GameManager.offline = false;
		  if(slide) {  
			slideContainer = new SlideContainer(WIDTH, HEIGHT);
		  	slideContainer.add(menu);
		  	switchTo(slideContainer);
		  	slide = false;
		  }
		  else 
			  switchTo(menu);
	  }
	  
	  @Override
	  public void showPlayer() {
		  switchTo(player);
	  }
	  
	  @Override
	  public void showGame(JTextField filename) { 
		  gameManager = new GameManager(filename, PlayerPanel.singlePlayer);	 
		  gamePanel = new GamePanel(gameWidth, gameHeight, this, gameManager);
		  play = new FullGamePanel(WIDTH, HEIGHT, gameWidth, gameHeight, this, gamePanel);
		  gamePanel.setFullGamePanel(play); //IMPORTANTE
		 
		  switchTo(play);
	  }
	  
	  @Override
	  public void showFirstStage() {
		  firstStage.repaint();
		  switchTo(firstStage);
	  }
	  
	  @Override
	  public void showSecondStage() {
		  secondStage.repaint();
		  switchTo(secondStage);
	  }
	  
	  @Override
	  public void showScores(String stage) {  
		  scores = new ScoresPanel(WIDTH, HEIGHT, this, gameManager, stage);
		  switchTo(scores);
	  }
	  
	  @Override
	  public void showConstruction() {
		  switchTo(editor);
	  }
	  
	  @Override
	  public void showSettings() {
		  switchTo(settings);
	  }
	  
	  @Override
	  public void showNetwork() {
		  switchTo(network);
	  }
	  
	  public GameManager showNetwork(ConnectionManager connectionManager, JTextField filename) {
		  gamePanel = new GamePanel();
		  gameManager = gamePanel.startNetwork(connectionManager);
		  gamePanel.setGame(gameManager);
		  play = new FullGamePanel(WIDTH, HEIGHT, gameWidth, gameHeight, this, gamePanel);
		  switchTo(play);
		  return gameManager;
	  }
	   
	  public static void main(String[] args) {
		   new MainFrame();
	  }

	  public SoundsProvider getSounds() {
		return sounds;
	}

	  public void setSounds(SoundsProvider sounds) {
		this.sounds = sounds;
	}

	  public ImageProvider getImages() {
		return images;
	}

	  public void setImages(ImageProvider images) {
		this.images = images;
	}
}