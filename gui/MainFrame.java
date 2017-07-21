package progettoIGPE.davide.giovanni.unical2016.GUI;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import javax.swing.*;
import net.ConnectionManager;
import progettoIGPE.davide.giovanni.unical2016.Flag;
import progettoIGPE.davide.giovanni.unical2016.GameManager;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements PanelSwitcher {
	
	private int WIDTH = 1300;
	private int HEIGHT = 740;

	private final int gameWidth = WIDTH - 530;
  	private final int gameHeight = HEIGHT - 5;
 
	public static Font customFontM;
	public static Font customFontB;
	public static Font customFontS; 
	 
	private boolean transparent;
	private boolean slide; 
	private int highScoreP1;
	private int highScoreP2;
	private int unlockedMapsP1;
	private int unlockedMapsP2;
	private int resumeP1;
	private int resumeP2;
	private int levelP1;
	private int levelP2;
	private Flag flag;

	//FullScreen
	private GraphicsEnvironment graphicsEnvironment;
	private GraphicsDevice device;
	private boolean fullscreen;
	private boolean pressF11;

	private ArrayList<JComponent> components;
	private NetworkPanel network;
	private Lobby lobby;
	private MenuPanel menu;
	private PlayerPanel player; 
	private StagePanelFirst firstStage;
	private StagePanelSecond secondStage;
	private ScoresPanel scores;
	private ConstructionPanel editor;
	private SettingsPanel settings;
	private SlideContainer slideContainer;
	private FullGamePanel fullGame; 
	private GameManager gameManager;
	private GamePanel gamePanel;
	private SlideStage slideStage;
	private LoadPanel load;
	private JComponent tmp = null;
	
	public MainFrame() {
		
		this.setLayout(new BorderLayout());
		this.setTitle("BATTLE CITY UNICAL");
//		this.setSize(new Dimension(WIDTH, HEIGHT));
		pressF11 = false;
		load = new LoadPanel(WIDTH, HEIGHT, this);
		tmp = load;
		components=new ArrayList<>();
		components.add(load);
		this.add(load);
		this.setResizable(false);
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		KeyboardFocusManager.getCurrentKeyboardFocusManager()
		  .addKeyEventDispatcher(new KeyEventDispatcher() {
		      
			@Override
		      public boolean dispatchKeyEvent(KeyEvent e) {
		    	  if(e.getKeyCode() == KeyEvent.VK_F11 && !pressF11){
		    		  
		    		  if(tmp != null) {
		    		  
						if(!fullscreen) {
							mainScreenTurnOn();
							WIDTH=device.getFullScreenWindow().getWidth();
							HEIGHT=device.getFullScreenWindow().getHeight();
						}
						else {
							mainScreenTurnOff();
							WIDTH = 1300;
							HEIGHT = 740;
						}
						pressF11=true;
		    		  }
		    	  }
		    	  else {
		    		  pressF11=false;
		    	  }	  
		    	  
		    	  for(int a=0; a<components.size(); a++){
		    		  resizeComponent(components.get(a));
		    	  }		    	  
		        return false;
		      }
		});
		
	}

	public static void main(String[] args) {
		MainFrame main = new MainFrame();
		main.instantiate();
	}
	
	//------------------------------------FULLSCREEN-----------------------------------
	
	public void mainScreenTurnOn() {
		dispose(); 
		transparent=false;
		setUndecorated(true);
		device.setFullScreenWindow(this);
		fullscreen = true;
		this.validate();
		this.repaint();
		tmp.transferFocusBackward();
		selectFocusButton(tmp);
	}
	
	public void mainScreenTurnOff() {
		fullscreen = false;
		transparent=false;
		device.setFullScreenWindow(null);
		dispose();
		setUndecorated(false);
		setVisible(true);
		this.validate();
		this.repaint();
		tmp.transferFocusBackward();
		selectFocusButton(tmp);
	}
	
	@Override
	protected void processWindowEvent(WindowEvent e) {
		
	    if (e.getID() == WindowEvent.WINDOW_DEACTIVATED) {
	       return;
	    }        

	    super.processWindowEvent(e);        
	}  
	
	//---------------------------------------------------------------------------------

	private void instantiate() {
	
		Timer timer = new Timer(8000, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showMenu();
			}
		});
	
		new ImageProvider();
		new SoundsProvider();
		graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		device = graphicsEnvironment.getDefaultScreenDevice();
		transparent = false;
		timer.setRepeats(false);
		timer.start();
		setSlide(true);
		new ImageProvider();
		new SoundsProvider();
		setFont();
		
		network = new NetworkPanel(WIDTH, HEIGHT, this);
		components.add(network);
		menu = new MenuPanel(WIDTH, HEIGHT, this);
		components.add(menu);
		menu.loadScore();
		player = new PlayerPanel(WIDTH, HEIGHT, this);
		components.add(player);
		firstStage = new StagePanelFirst(WIDTH, HEIGHT, this);
		components.add(firstStage);
		secondStage = new StagePanelSecond(WIDTH, HEIGHT, this);
		components.add(secondStage);
		editor = new ConstructionPanel(WIDTH, HEIGHT, this, flag);
		components.add(editor);
		settings = new SettingsPanel(WIDTH, HEIGHT, this);
		components.add(settings);
		
	}
	
	public void switchTo(JComponent component) {
		
		if(component instanceof SlideContainer)
			tmp = menu;	
		else
			tmp = component;
		this.getContentPane().removeAll();
		this.add(component);
		this.validate();
		this.repaint();
		component.transferFocus();
		selectFocusButton(component);
	}

	private void selectFocusButton(JComponent component) {

		if (component instanceof MenuPanel) {
			((MenuPanel) component).getButton(menu.getCursorPosition()).requestFocus();
		} else if (component instanceof PlayerPanel) {
			((PlayerPanel) component).getButton(player.getCursorPosition()).requestFocus();
		} else if (component instanceof StagePanelFirst) {
			((StagePanelFirst) component).getButton(firstStage.getCursorPosition()).requestFocus();
		} else if (component instanceof StagePanelSecond) {
			((StagePanelSecond) component).getButton(secondStage.getCursorPosition()).requestFocus();
		} else if (component instanceof ConstructionPanel) {
			((ConstructionPanel) component).getButton(editor.getCursorPosition()).requestFocus();
		} else if (component instanceof SettingsPanel) {
			((SettingsPanel) component).getButton(settings.getCursorPosition()).requestFocus();
		} else if (component instanceof NetworkPanel) {
			((NetworkPanel) component).getButton(network.getCursorPosition()).requestFocus();
		}
	}

	private void setFont() {

		try {

			customFontM = (Font.createFont(Font.TRUETYPE_FONT, new File("./font/Minecraft.ttf")).deriveFont(25f));
			customFontB =(Font.createFont(Font.TRUETYPE_FONT, new File("./font/Minecraft.ttf")).deriveFont(40f));
			customFontS =(Font.createFont(Font.TRUETYPE_FONT, new File("./font/Minecraft.ttf")).deriveFont(16f));
			graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
			graphicsEnvironment.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("./font/Minecraft.ttf")));
			
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();

		}
	} 
	
	private void manageLooby() {
		lobby = new Lobby(WIDTH, HEIGHT, this);	
		lobby.setClient(network.getClient());
		lobby.setNameTextField(network.getNameTextField());
		lobby.setIpTextField(network.getIpTextField());
		lobby.setPortTextField(network.getPortTextField());
		while(!lobby.getClient().isPresentInTheArrayOfClientOnline()){System.out.println("ok");}
		lobby.createChat(lobby.getClient());
		lobby.createOnlinePanel();
		lobby.createDifficultPanel();
		lobby.createMapsPanel();
		
	}
	
	// -----------------------------override methods-----------------------------------

	@Override
	public void showMenu() {

		GameManager.offline = false;

//		if (isSlide()) {
//			slideContainer = new SlideContainer(WIDTH, HEIGHT);
//			slideContainer.add(menu);
//			switchTo(slideContainer);
//			setSlide(false);
//		} else
			switchTo(menu);
	}

	@Override
	public void showPlayer() {
		switchTo(player);
	}

	@Override
	public void showGame() {
		switchTo(fullGame); // tutte le new ora li fa lo slideStage
	}

	@Override
	public void showFirstStage(String path) {
		firstStage.setPath(path);
		firstStage.repaint();
		switchTo(firstStage);
	}

	@Override
	public void showSecondStage(String path) {
		secondStage.setPath(path);
		secondStage.repaint();
		switchTo(secondStage);
	}

	@Override
	public void showScores(JTextField filename) {
		scores = new ScoresPanel(WIDTH, HEIGHT, this, gameManager, filename);
		switchTo(scores);
	}

	@Override
	public void showConstruction() {
		switchTo(editor);
	}

	@Override
	public void showSlideStage(JTextField filename, boolean offline, ConnectionManager connectionManager, String difficult) {
		if(offline)
			slideStage = new SlideStage(WIDTH, HEIGHT, this, filename);
		else
			slideStage = new SlideStage(WIDTH, HEIGHT, this, filename, connectionManager, difficult);
		switchTo(slideStage);
	}

	@Override
	public void showSettings() {
		switchTo(settings);
	}

	@Override
	public void showNetwork() {
		network.getButton(1).setEnabled(true);
		switchTo(network);
	}
	
	@Override
	public void showLobby(boolean gamePanelExit){
		
		if(!gamePanelExit){
			manageLooby();
		}
		switchTo(lobby); 
		lobby.revalidate();//va messo perchè quando faccio il passaggio da un pannello ad un'altro io aggiungo dopo un'altro pannello di sopra
	}

	public GameManager showNetwork(ConnectionManager connectionManager, JTextField filename, String difficult) {
		showSlideStage(filename, false, connectionManager, difficult);
		return gameManager;
	}

	//---------------------------------SET & GET---------------------------------------------
	
	public void resizeComponent(JComponent tmp) {
		
		if(tmp instanceof LoadPanel) {
			((LoadPanel)tmp).setProgressBar(WIDTH, HEIGHT);
			((LoadPanel)tmp).repaint();
		}
		else if(tmp instanceof MenuPanel) {
			
			for(int i = 0; i < ((MenuPanel)tmp).getDIM(); i++) {
				((MenuPanel)tmp).setBoundAndText(WIDTH, HEIGHT, i);
			}
			((MenuPanel)tmp).addLabel(WIDTH);
			((MenuPanel)tmp).repaint();
		}
		else if(tmp instanceof PlayerPanel) {
			
			for(int i = 0; i < ((PlayerPanel)tmp).getDIM(); i++) {
				((PlayerPanel)tmp).setBoundAndText(WIDTH, HEIGHT, i);
			}
			((PlayerPanel)tmp).repaint();
		}
		else if(tmp instanceof SettingsPanel) {
			
			((SettingsPanel)tmp).setPosY(270);
			((SettingsPanel)tmp).setSlider(WIDTH, HEIGHT);
			
			for(int i = 0; i < ((SettingsPanel)tmp).getDIM(); i++) {
				((SettingsPanel)tmp).setBoundsAndText(i);
				
				if(i != 0)
					((SettingsPanel)tmp).setPosY(((SettingsPanel)tmp).getPosY() + 100);
				
				if(i != 3)
					((SettingsPanel)tmp).setBoundRadioButton(HEIGHT, i);
			}
		}
		else if(tmp instanceof ConstructionPanel) {
			
		}
		else if(tmp instanceof GamePanel) {
			
		}
		else if(tmp instanceof NetworkPanel) {
			
		}
	}
	
	public FullGamePanel getFullGamePanel() {
		return fullGame;
	}
	
	public GamePanel getGamePanel() {
		return gamePanel;
	}
	
	public int getCurrentLevelP1() {
		return levelP1;
	}

	public void setCurrentLevelP1(int levelP1) {
		this.levelP1 = levelP1;
	}

	public int getCurrentLevelP2() {
		return levelP2;
	}

	public void setCurrentLevelP2(int levelP2) {
		this.levelP2 = levelP2;
	}
	
	public int getCurrentResumeP2() {
		return resumeP2;
	}

	public void setCurrentResumeP2(int resumeP2) {
		this.resumeP2 = resumeP2;
	}
	
	public int getCurrentResumeP1() {
		return resumeP1;
	}

	public void setCurrentResumeP1(int resumeP1) {
		this.resumeP1 = resumeP1;
	}
	
	public int getUnlockedMapsP2() {
		return unlockedMapsP2;
	}

	public void setUnlockedMapsP2(int unlockedMapsP2) {
		this.unlockedMapsP2 = unlockedMapsP2;
	}

	public int getUnlockedMapsP1() {
		return unlockedMapsP1;
	}
	
	public void setUnlockedMapsP1(int unlockedMapsP1) {
		this.unlockedMapsP1 = unlockedMapsP1;
	}
	
	public int getHighScoreP2() {
		return highScoreP2;
	}

	public void setHighScoreP2(int highScoreP2) {
		this.highScoreP2 = highScoreP2;
	}

	public int getHighScoreP1() {
		return highScoreP1;
	}

	public void setHighScoreP1(int highScoreP1) {
		this.highScoreP1 = highScoreP1;
	}
	
	public boolean isSlide() {
		return slide;
	}

	public void setSlide(boolean slide) {
		this.slide = slide;
	}

	public boolean isTransparent() {
		return transparent;
	}

	public int getWIDTH() {
		return WIDTH;
	}

	public int getHEIGHT() {
		return HEIGHT;
	}

	public int getGameWidth() {
		return gameWidth;
	}

	public int getGameHeight() {
		return gameHeight;
	}

	public FullGamePanel getFullGame() {
		return fullGame;
	}

	public void setFullGame(FullGamePanel fullGame) {
		this.fullGame = fullGame;
	}

	public GameManager getGameManager() {
		return gameManager;
	}

	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}

	public void setGamePanel(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}
	
	public void setTransparent(boolean transparent) {
		this.transparent = transparent;
	}

	public Flag getFlag() {
		return flag;
	}

	public void setFlag(Flag flag) {
		this.flag = flag;
	}
}