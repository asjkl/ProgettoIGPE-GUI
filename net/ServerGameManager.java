package net;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JTextField;
import progettoIGPE.davide.giovanni.unical2016.GameManager;
import progettoIGPE.davide.giovanni.unical2016.GUI.GamePanel;

public class ServerGameManager {
	private final ArrayList<ClientManager> clients;
	private final Set<ClientManager> readyClients;
	public HashMap<String,String>name;
	public GameManager gameManager;
	private GamePanel gamePanel;
	private String difficult;
	private JTextField map;
	private ServerSocket server;

	public ServerGameManager(ServerSocket server) {
		this.server=server;
		this.clients = new ArrayList<>();
		this.readyClients = new HashSet<ClientManager>();
		this.name=new HashMap<>();
		this.map=new JTextField();
	}
	
	public void add(final ClientManager cm) {
		clients.add(cm);
		System.out.println("connesso." );
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
				sb.append(cm.getName()+":");
				for(Map.Entry<String, String> entry : name.entrySet()) {
					String key = entry.getKey();
					String names= entry.getValue();	
					if(names.equals(cm.getName())){
						sb.append(key);
					}
				}
				sb.append(";");
			}
		}	
		return sb.toString();
	}

	public void received(final String buffer) {
		final String[] split = buffer.split(":");
				
		if(gameManager!=null){
			if(split[0].equals("EXIT")){
				disconnetctedClient(split[1]);
				gameManager.setExit(Boolean.parseBoolean(split[2]));
			}else{
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
				System.out.println("ServerGame PRONTO!");
			}
		}
	}
	
	public void setupClient() throws IOException {
		ArrayList<String>nameOfPlayers=new ArrayList<>();
		nameOfPlayers.add("P1");
		nameOfPlayers.add("P2");
		final List<String> names = new ArrayList<>();
		for (final ClientManager cm : clients) {
			cm.setup(nameOfPlayers.remove(0));
			new Thread(cm, cm.getName()).start();
			names.add(cm.getName());
		}	
	}

	public void startGame() throws IOException {
		for (final ClientManager cm : clients) {
			map.setText(cm.getMap().getText());
			difficult=cm.getDifficult();
			break;
		}
		
		gameManager = new GameManager(new Runnable() {
			@Override
			public void run() {
				final String statusToString = gameManager.statusToString();
				dispatch(statusToString);
			}
		}, name, map);
		gamePanel=new GamePanel(null, difficult);
		gamePanel.setGame(gameManager);
		new Thread() {
			@Override
			public void run() {
				gamePanel.gameLoop();
				System.out.println("CHIUSO_SERVERGAME");
				try {
					server.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	public void disconnetctedClient(String name) {
		
		int cont = 0;
		
		for(int a=0; a<gameManager.getPlayersArray().size(); a++){
			if(gameManager.getPlayersArray().get(a).toString().equals(name)){
				gameManager.getPlayersArray().get(a).setResume(0);
				gameManager.destroyPlayerTank(gameManager.getPlayersArray().get(a));
				gameManager.getPlayersArray().get(a).setExitOnline(true);
			}
			if(gameManager.getPlayersArray().get(a).getResume()<0){
				cont++;
			}
		}
		if(cont==2){
			gamePanel.gameOverOrWin();
		}
		System.out.println("CLIENTE DISCONNESSO: " + name);
	}
}
