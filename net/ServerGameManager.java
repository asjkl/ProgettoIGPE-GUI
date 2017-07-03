package net;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JTextField;
import progettoIGPE.davide.giovanni.unical2016.GameManager;
import progettoIGPE.davide.giovanni.unical2016.GUI.GamePanel;

public class ServerGameManager {
	private final Set<ClientManager> clients = new HashSet<ClientManager>();
	private final Set<ClientManager> readyClients = new HashSet<ClientManager>();
	public GameManager gameManager;
	private GamePanel gamePanel;

	public void add(final ClientManager cm) {
		clients.add(cm);
		System.out.println(cm.getName() + " connesso." );
	}

	public void dispatch(final String message) {
		for (final ClientManager cm : clients) {
			if (cm != null) {
				cm.dispatch(message);
			}
		}
	}

	public String getConnectedClientNames() {
		final StringBuilder sb = new StringBuilder();
		for (final ClientManager cm : clients) {
			if (cm.getName() != null) {
				sb.append(cm.getName());
				sb.append(";");
			}
		}
		return sb.toString();
	}

	public void received(final String buffer) {
		final String[] split = buffer.split(":");
		
	
		if(gameManager!=null){
			if(split[0].equals("PAINT")){
				gameManager.setWaitToExit(Boolean.parseBoolean(split[1]));	
			}
			else{
				//BOOLEANE DI SISTEMA
				gameManager.pauseOptionDialog=Boolean.parseBoolean(split[5]);
				gameManager.paused=Boolean.parseBoolean(split[6]);
				
				for(int a=0; a<gameManager.getPlayersArray().size(); a++){
					if(gameManager.getPlayersArray().get(a).toString().equals(split[0])){
						if(split[2].equals("YES")){
							gameManager.getPlayersArray().get(a).keyBits.set(Integer.valueOf(split[1]));
							gameManager.getPlayersArray().get(a).setKeyPressedMillis(Long.parseLong(split[3]));
						}
						else if(split[2].equals("NO")){
							gameManager.getPlayersArray().get(a).keyBits.clear(Integer.valueOf(split[1]));
							gameManager.getPlayersArray().get(a).setReleaseKeyRocket(Boolean.parseBoolean(split[4]));
						}
					}
				}
			}
		}
	}
		

	public void setReady(final ClientManager clientManager) {
		synchronized (readyClients) {
			readyClients.add(clientManager);
			if (readyClients.size() == 2) {
				dispatch("#START");
				System.out.println("ServerGameManager.setReady()");
			}
		}
	}

	public void startGame() throws IOException {
		final List<String> names = new ArrayList<>();
		for (final ClientManager cm : clients) {
			cm.setup();
			new Thread(cm, cm.toString()).start();
			names.add(cm.getName());
			
		}
		JTextField filename=new JTextField("stage1.txt");
		gameManager = new GameManager(new Runnable() {
			@Override
			public void run() {
				final String statusToString = gameManager.statusToString();
				dispatch(statusToString);
			}
		}, names,filename);
		gamePanel=new GamePanel();
		new Thread() {
			@Override
			public void run() {
				gamePanel.gameLoop();
			};
		}.start();
		gamePanel.setGame(gameManager);
	}

	public void disconnetctedClient(String name) {
		for(int a=0; a<gameManager.getPlayersArray().size(); a++){
			if(gameManager.getPlayersArray().get(a).toString().equals(name)){
				gameManager.getPlayersArray().get(a).setResume(0);
				gameManager.destroyPlayerTank(gameManager.getPlayersArray().get(a));
				break;
			}
		}
		System.out.println("Client disconnected: " + name);
	}

}
