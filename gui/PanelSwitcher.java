package progettoIGPE.davide.giovanni.unical2016.GUI;
import javax.swing.JTextField;

public interface PanelSwitcher {

	void showMenu();
	
	void showPlayer();
	
	void showGame(JTextField f);
	
	void showFirstStage(String p);
	
	void showSecondStage(String p);

	void showScores(JTextField f);
	
	void showNetwork();
	
	void showConstruction();
	
	void showSettings();
	
	void showLobby();

	void showSlide(JTextField f);

}

