package progettoIGPE.davide.giovanni.unical2016.GUI;
import javax.swing.JTextField;

public interface PanelSwitcher {

	void showMenu();
	
	void showPlayer();
	
	void showGame(JTextField f);
	
	void showFirstStage(String p);
	
	void showSecondStage(String p);

	void showScores(String v);
	
	void showNetwork();
	
	void showConstruction();
	
	void showSettings();

	void showLoading(JTextField f);

}
