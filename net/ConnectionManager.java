package progettoIGPE.davide.giovanni.unical2016.NET;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JTextField;

import progettoIGPE.davide.giovanni.unical2016.GameManager;
import progettoIGPE.davide.giovanni.unical2016.GUI.JFileChooserClass;
import progettoIGPE.davide.giovanni.unical2016.GUI.MainFrame;

public class ConnectionManager implements Runnable {

	private MainFrame mainFrame;
	private final String name;
	private List<String> playerNames;
	private PrintWriter pw;
	private final Socket socket;
	private JTextField filename;
	
	public ConnectionManager(final Socket socket, final String name, MainFrame mainFrame) {
		this.socket = socket;
		this.name = name;
		this.mainFrame = mainFrame;
		filename = new JTextField();
	}

	public void close() {
		try {
			socket.close();
		} catch (final IOException e) {
			// do nothing
		}
	}

	public void dispatch(final String message) {
		pw.println(message);
	}

	public List<String> getAllPlayerNames() {
		return playerNames;
	}

	public String getPlayerName() {
		return name;
	}

	@Override
	public void run() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw = new PrintWriter(socket.getOutputStream(), true);
			pw.println(getPlayerName());	
			//br non legge fin quando non vengono avviati i clients
			String buffer = br.readLine();
			while (!buffer.equals("#START")) {
				final String[] split = buffer.split(";");
				
				if(split[0].contains(".txt"))
					filename.setText(split[0]);
				else
				if (split.length != 0) {
					playerNames = new ArrayList<>();
					for (final String name : split) {
						playerNames.add(name);
					}
				}
				buffer = br.readLine();
			}
			
			final GameManager gameManager = mainFrame.startNetworkGame(this,filename);
			buffer = br.readLine();
			while (buffer != null) {
				// System.out.println(buffer);
				// if (buffer.equals("LOSE")) {
				// JOptionPane.showMessageDialog(mainFrame, "You Won!");
				// buffer = null;
				// socket.close();
				// mainFrame.showMenu();
				// } else if (buffer.equals("WIN")) {
				// JOptionPane.showMessageDialog(mainFrame, "You Lose!");
				// buffer = null;
				// socket.close();
				// mainFrame.showMenu();
				// } else {
				gameManager.parseStatusFromString(buffer);
				mainFrame.gamePanel.repaint();
				mainFrame.play.repaint();
				buffer = br.readLine();
				// }
			}
		} catch (final IOException e) {
			System.out.println("Connection closed");
		}
	}
}
