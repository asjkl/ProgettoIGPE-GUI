package gui;
import javax.swing.JTextField;

import net.ConnectionManager;

public interface PanelSwitcher {

	void showMenu();
	
	void showPlayer();
	
	void showGame();
	
	void showFirstStage(String p);
	
	void showSecondStage(String p);

	void showScores(JTextField f);
	
	void showNetwork();
	
	void showConstruction();
	
	void showSettings();
	
	void showLobby(boolean gamePanelExit);

	void showSlideStage(JTextField f, boolean offline, ConnectionManager connectionManager, String difficult); 
}

