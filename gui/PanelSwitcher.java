package progettoIGPE.davide.giovanni.unical2016.GUI;
import javax.swing.JTextField;

public interface PanelSwitcher {

	void showMenu();
	
	void showPlayer();
	
	void showGame(JTextField f);
	
	void showFirstStage();
	
	void showSecondStage();

	void showScores(String s);
	
	void showNetwork();
	
	void showConstruction();
	
	void showSettings();

	void showLoading(JTextField f);

}
