package progettoIGPE.davide.giovanni.unical2016.GUI;

import javax.swing.JTextField;

public interface PanelSwitcher {

	void showMenu();
	
	void showPlayer();
	
	void showGame(JTextField f);
	
	void showFirstStage(String s);
	
	void showSecondStage(String s);

	void showScores(String s);
	
	void showNetwork();
	
	void showConstruction();
	
	void showSettings();
	
	void showLoading(JTextField f);
}
