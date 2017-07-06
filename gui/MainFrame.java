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
	public static boolean transparent = false;
	private boolean slide;
	private int unlockedMaps1P;
	private int unlockedMaps2P;
	private int currentResume;
	private GraphicsEnvironment graphicscEnvironment;

	public NetworkPanel network;
	public MenuPanel menu;
	public PlayerPanel player; 
	public StagePanelFirst firstStage;
	public StagePanelSecond secondStage;
	public ScoresPanel scores;
	public ConstructionPanel editor;
	public SettingsPanel settings;
	public SlideContainer slideContainer;
	public FullGamePanel play; 
	public GameManager gameManager;
	public GamePanel gamePanel;
	public SoundsProvider sounds;
	public ImageProvider images;
	public LoadGamePanel LoadGamePanel;
	public LoadPanel load;

	public MainFrame() {
		this.getContentPane().setBackground(Color.BLACK);
		new ImageProvider();
		this.setLayout(new BorderLayout());
		this.setTitle("BATTLE CITY UNICAL");
		this.setSize(new Dimension(WIDTH, HEIGHT));

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

		setSlide(true);
		setCurrentResume(3);
		setUnlockedMaps1P(1);
		setUnlockedMaps2P(1);
		Timer timer = new Timer(4000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				showMenu();
			}
		});
		timer.setRepeats(false);
		timer.start();

		new SoundsProvider();
		setFont();
		network = new NetworkPanel(WIDTH, HEIGHT, this);
		menu = new MenuPanel(WIDTH, HEIGHT, this);
		player = new PlayerPanel(WIDTH, HEIGHT, this);
		firstStage = new StagePanelFirst(WIDTH, HEIGHT, this);
		secondStage = new StagePanelSecond(WIDTH, HEIGHT, this);
		editor = new ConstructionPanel(WIDTH, HEIGHT, this);
		settings = new SettingsPanel(WIDTH, HEIGHT, this);
		images = new ImageProvider();
		sounds = new SoundsProvider();
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

	// -----------------------------override methods-----------------------------------

	@Override
	public void showMenu() {

		//TODO ce un problema delle volte controllare
		menu.drawScore();
		GameManager.offline = false;
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
	public void showGame(JTextField filename) {
		gameManager = new GameManager(filename, PlayerPanel.singlePlayer);
		gamePanel = new GamePanel(gameWidth, gameHeight, this, gameManager);
		play = new FullGamePanel(WIDTH, HEIGHT, gameWidth, gameHeight, this, gamePanel);
		gamePanel.setFullGamePanel(play);
		switchTo(play);
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
	public void showScores(String stage) {
		scores = new ScoresPanel(WIDTH, HEIGHT, this, gameManager, stage);
		switchTo(scores);
	}

	@Override
	public void showConstruction() {
		switchTo(editor);
	}

	@Override
	public void showLoading(JTextField f) {
		LoadGamePanel = new LoadGamePanel(WIDTH, HEIGHT, this, f);
		switchTo(LoadGamePanel);
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

	public GameManager showNetwork(ConnectionManager connectionManager, JTextField filename) {
		gamePanel = new GamePanel(this);
		gameManager = gamePanel.startNetwork(connectionManager);
		gamePanel.setGame(gameManager);
		play = new FullGamePanel(WIDTH, HEIGHT, gameWidth, gameHeight, this, gamePanel);
		switchTo(play);
		return gameManager;
	}
	
	//---------------------------------SET & GET---------------------------------------------
	private void setFont() {

		try {

			customFontM = Font.createFont(Font.TRUETYPE_FONT, new File("./font/Minecraft.ttf")).deriveFont(25f);
			customFontB = Font.createFont(Font.TRUETYPE_FONT, new File("./font/Minecraft.ttf")).deriveFont(40f);
			customFontS = Font.createFont(Font.TRUETYPE_FONT, new File("./font/Minecraft.ttf")).deriveFont(16f);
			graphicscEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
			graphicscEnvironment.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("./font/Minecraft.ttf")));

		} catch (IOException e) {
			e.printStackTrace();

		} catch (FontFormatException e) {
			e.printStackTrace();
		}
	}
	
	public int getCurrentResume() {
		return currentResume;
	}

	public void setCurrentResume(int currentResume) {
		this.currentResume = currentResume;
	}
	
	public int getUnlockedMaps1P() {
		return unlockedMaps1P;
	}

	public void setUnlockedMaps1P(int unlockedMaps1P) {
		this.unlockedMaps1P = unlockedMaps1P;
	}

	public int getUnlockedMaps2P() {
		return unlockedMaps2P;
	}

	public void setUnlockedMaps2P(int unlockedMaps2P) {
		this.unlockedMaps2P = unlockedMaps2P;
	}

	public boolean isSlide() {
		return slide;
	}

	public void setSlide(boolean slide) {
		this.slide = slide;
	}
}