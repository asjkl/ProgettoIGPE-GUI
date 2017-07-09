package progettoIGPE.davide.giovanni.unical2016.GUI;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import net.ConnectionManager;
import progettoIGPE.davide.giovanni.unical2016.GameManager;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements PanelSwitcher {
	
//  private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();	
	private final int WIDTH = 1300;
	private final int HEIGHT = 740;
	private final int gameWidth = WIDTH - 565;
  	private final int gameHeight = HEIGHT - 40;
 
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
	private FullGamePanel fullGame; 
	private GameManager gameManager;
	private GamePanel gamePanel;
	private SlideStage slideStage;
	private LoadPanel load;

	public MainFrame() {
		
		this.setLayout(new BorderLayout());
		this.setTitle("BATTLE CITY UNICAL");
		this.setSize(new Dimension(WIDTH, HEIGHT));
		new ImageProvider();
		load = new LoadPanel(WIDTH, HEIGHT, this);
		this.add(load);

//		this.setResizable(false);
		this.pack();
//		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		MainFrame main = new MainFrame();
		main.instantiate();
	}

	private void instantiate() {
	
		Timer timer = new Timer(9000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				showMenu();
			}
		});
	
		transparent = false;
		timer.setRepeats(false);
		timer.start();
		setSlide(true);
		//Solo per player 2
		setUnlockedMapsP2(1);
		new SoundsProvider();
		setFont();
		network = new NetworkPanel(WIDTH, HEIGHT, this);
		menu = new MenuPanel(WIDTH, HEIGHT, this);
		player = new PlayerPanel(WIDTH, HEIGHT, this);
		firstStage = new StagePanelFirst(WIDTH, HEIGHT, this);
		secondStage = new StagePanelSecond(WIDTH, HEIGHT, this);
		editor = new ConstructionPanel(WIDTH, HEIGHT, this);
		settings = new SettingsPanel(WIDTH, HEIGHT, this);


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
			graphicscEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
			graphicscEnvironment.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("./font/Minecraft.ttf")));

		} catch (IOException e) {
			e.printStackTrace();

		} catch (FontFormatException e) {
			e.printStackTrace();
		}
	}
	// -----------------------------override methods-----------------------------------

	@Override
	public void showMenu() {

		GameManager.offline = false;

		menu.drawScore();
		
		if (isSlide()) {
			slideContainer = new SlideContainer(WIDTH, HEIGHT);
			slideContainer.add(menu);
			switchTo(slideContainer);
			setSlide(false);
		} else
			switchTo(menu);
	}

	@Override
	public void showPlayer() {
		switchTo(player);
	}

	@Override
	public void showGame(JTextField directory) {

		//singleOrMulti(path)
		gameManager = new GameManager(directory);
		if(SettingsPanel.normal)
			gameManager.setMedium(true);
		gamePanel = new GamePanel(gameWidth, gameHeight, this, gameManager);
		fullGame = new FullGamePanel(WIDTH, HEIGHT, gameWidth, gameHeight, this, gamePanel);
		gamePanel.setFullGamePanel(fullGame);
		switchTo(fullGame);
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
	public void showSlide(JTextField filename) {
		slideStage = new SlideStage(WIDTH, HEIGHT, this, filename);
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

	public GameManager showNetwork(ConnectionManager connectionManager, JTextField filename, String difficult) {
		
		gamePanel = new GamePanel(this, difficult);
		gameManager = gamePanel.startNetwork(connectionManager, filename);
		gamePanel.setGame(gameManager);
		fullGame = new FullGamePanel(WIDTH, HEIGHT, gameWidth, gameHeight, this, gamePanel);
		switchTo(fullGame);
		return gameManager;
	}
	
	//---------------------------------SET & GET---------------------------------------------

	
//	private void singleOrMulti(JTextField filename) {
//		File career=null;
//		career = new File("./maps/career/singleplayer/" + filename.getText());
//		if(!career.exists()){
//			career = new File("./maps/career/multiplayer/" + filename.getText());
//			if(!career.exists()){
//				career=new File("./maps/editor/multiplayer/" + filename.getText());
//				if(!career.exists()){
//					career=new File("./maps/editor/singleplayer/" + filename.getText());
//					singlePlayer=true;
//				}else{
//					singlePlayer=false;
//				}
//			}else{
//				singlePlayer=false;
//			}
//		}else{
//			singlePlayer=true;
//		}
//	}
	
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

	public void setTransparent(boolean transparent) {
		this.transparent = transparent;
	}
}