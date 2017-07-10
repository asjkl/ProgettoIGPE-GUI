package progettoIGPE.davide.giovanni.unical2016.GUI;
import javax.swing.JTextField;

import net.ClientChat;

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
	
	void showLobby(ClientChat client, JTextField ip, JTextField name, JTextField port);

	void showSlide(JTextField f);

}
