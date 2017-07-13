package progettoIGPE.davide.giovanni.unical2016.GUI;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import net.ClientChat;
import net.Server;
import progettoIGPE.davide.giovanni.unical2016.GUI.PanelSwitcher;
import progettoIGPE.davide.giovanni.unical2016.GUI.SoundsProvider;

@SuppressWarnings("serial")
public class NetworkPanel extends JPanel {

	private PanelSwitcher switcher;
	private JTextField ipTextField;
	private JTextField nameTextField;
	private JTextField portTextField;


	private int cursorPosition;
	private int portChat = 1232;

	private ArrayList<JButton> buttons;
	@SuppressWarnings("unused")
	private WarningDialog warning;

	private int DIM = 2;

	private ClientChat client;
	private Server serverChat;
	protected int lengthMaxName=10;
	
	public NetworkPanel(int w, int h, PanelSwitcher switcher) {

		this.setBackground(Color.BLACK);
		this.setPreferredSize(new Dimension(w, h));
		this.setSwitcher(switcher);
		this.setLayout(null);
		
		cursorPosition = 1;
		buttons = new ArrayList<>();

		createLabels();
		createButtons();
	}

	private void createLabels() {

		JLabel label = new JLabel("Server IP");
		label.setBackground(Color.BLACK);
		label.setForeground(Color.WHITE);
		label.setFont(MainFrame.customFontM);
		label.setBounds(440, 325, 200, 40);
		this.add(label);

		ipTextField = new JTextField(10);
		ipTextField.setText("127.0.0.1");
		ipTextField.setBackground(Color.BLACK);
		ipTextField.setForeground(Color.WHITE);
		ipTextField.setFont(MainFrame.customFontM);
		ipTextField.setBounds(640, 320, 200, 40);
		this.add(ipTextField);

		// ----

		JLabel label2 = new JLabel("Server Port");
		label2.setBackground(Color.BLACK);
		label2.setForeground(Color.WHITE);
		label2.setFont(MainFrame.customFontM);
		label2.setBounds(440, 385, 200, 40);
		this.add(label2);
		portTextField = new JTextField(10);
		portTextField.setText("1234");
		portTextField.setBackground(Color.BLACK);
		portTextField.setForeground(Color.WHITE);
		portTextField.setFont(MainFrame.customFontM);
		portTextField.setBounds(640, 380, 200, 40);
		this.add(portTextField);

		// -----

		JLabel label3 = new JLabel("Player Name");
		label3.setBackground(Color.BLACK);
		label3.setForeground(Color.WHITE);
		label3.setFont(MainFrame.customFontM);
		label3.setBounds(440, 445, 200, 40);
		this.add(label3);
		nameTextField = new JTextField(10);
		nameTextField.setText("");
		nameTextField.setBackground(Color.BLACK);
		nameTextField.setForeground(Color.WHITE);
		nameTextField.setFont(MainFrame.customFontM);
		nameTextField.setBounds(640, 440, 200, 40);
		this.add(nameTextField);
	}

	private void createButtons() {

		for (int i = 0; i < DIM; i++) {

			final int curRow = i;
			buttons.add(new JButton());
			buttons.get(i).setBorder(null);
			buttons.get(i).setOpaque(false);
			buttons.get(i).setContentAreaFilled(false);
			buttons.get(i).setBorderPainted(false);
			buttons.get(i).setFocusPainted(false);
			buttons.get(i).setFont(MainFrame.customFontM);
			buttons.get(i).setForeground(Color.WHITE);
			buttons.get(i).setBackground(Color.BLACK);
			buttons.get(i).setHorizontalAlignment(SwingConstants.LEFT);
			setBoundAndText(i);

			buttons.get(i).addKeyListener(new KeyAdapter() {

				@Override
				public void keyPressed(KeyEvent e) {

					boolean enter = false;

					if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						SoundsProvider.playBulletHit1();
						cursorPosition = 1;
						getSwitcher().showMenu();
						repaint();
					} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						((JButton) e.getComponent()).doClick();

					} else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_LEFT) {

						enter = true;

						if (curRow < 1) {

							buttons.get(buttons.size() - 1).requestFocus();
							cursorPosition = buttons.size() - 1;
							repaint();
						} else {

							buttons.get(curRow - 1).requestFocus();
							cursorPosition = curRow - 1;
							repaint();
						}
					} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_RIGHT) {

						enter = true;
						buttons.get((curRow + 1) % buttons.size()).requestFocus();
						cursorPosition = (curRow + 1) % buttons.size();
						repaint();
					}

					if (enter)
						SoundsProvider.playBulletHit1();
				}
			});

			addActionListener(i);
			this.add(buttons.get(i));
		}
		buttons.get(1).setForeground(Color.YELLOW);
	}

	public void addActionListener(int j) {

		switch (j) {
		case 0: // BACK
			buttons.get(j).addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(final ActionEvent e) {
					SoundsProvider.playBulletHit1();
					cursorPosition = 1;
					getSwitcher().showMenu();

				}
			});
			break;
		case 1: // LOBBY
			buttons.get(j).addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(final ActionEvent e) {
					SoundsProvider.playBulletHit1();
					if (nameTextField.getText().equals("")) {
						((MainFrame)getSwitcher()).setTransparent(true);
						warning = new WarningDialog("Insert name!!", ((MainFrame) getSwitcher()));
						
					} else if(nameTextField.getText().length()> lengthMaxName){ 
						((MainFrame)getSwitcher()).setTransparent(true);
						warning = new WarningDialog("Insert name min "+lengthMaxName+" !!", ((MainFrame) getSwitcher()));						
					}else {
						buttons.get(1).setEnabled(false);
		
						serverChat=new Server(portChat);
						
						new Thread(serverChat, "chat").start();
						client=new ClientChat(nameTextField.getText(), ipTextField.getText(), portChat, ((MainFrame)getSwitcher()));

						nameTextField.setText("");
						getSwitcher().showLobby(false);
						
					}
				}
			});
			break;
		default:
			break;
		}
	}


	public void setBoundAndText(int j) {

		switch (j) {
		case 0:
			buttons.get(j).setBounds(18, 12, 70, 35);
			buttons.get(j).setText("Back");
			break;
		case 1:
			buttons.get(j).setBounds(600, 570, 150, 35);
			buttons.get(j).setText("Lobby");
			break;
		default:
			break;
		}
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (((MainFrame) getSwitcher()).isTransparent()) {

			// Apply our own painting effect
			Graphics2D g2d = (Graphics2D) g;
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .2f));
			g2d.setColor(getBackground());
			g2d.fill(getBounds());
		}

		if (!((MainFrame) getSwitcher()).isTransparent())
			((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));

		g.drawImage(ImageProvider.getBackground2P(), (int) (getPreferredSize().getWidth() / 2) - 375, 0, null);

		if (!((MainFrame) getSwitcher()).isTransparent())
			((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

		if (cursorPosition == 0)
			g.drawImage(ImageProvider.getCursorLeft(), buttons.get(cursorPosition).getX() + 85,
					buttons.get(cursorPosition).getY() - 6, this);
		else
			g.drawImage(ImageProvider.getCursorRight(), buttons.get(cursorPosition).getX() - 60,
					buttons.get(cursorPosition).getY() - 8, this);
	}

	public int getCursorPosition() {
		return cursorPosition;
	}

	public void setCursorPosition(int cursorPosition) {
		this.cursorPosition = cursorPosition;
	}

	public JComponent getButton(int i) {
		return buttons.get(i);
	}

	public PanelSwitcher getSwitcher() {
		return switcher;
	}

	public void setSwitcher(PanelSwitcher switcher) {
		this.switcher = switcher;
	}
	public JTextField getIpTextField() {
		return ipTextField;
	}

	public void setIpTextField(JTextField ipTextField) {
		this.ipTextField = ipTextField;
	}

	public JTextField getNameTextField() {
		return nameTextField;
	}

	public void setNameTextField(JTextField nameTextField) {
		this.nameTextField = nameTextField;
	}

	public int getPortChat() {
		return portChat;
	}

	public void setPortChat(int portChat) {
		this.portChat = portChat;
	}
	public ClientChat getClient() {
		return client;
	}

	public void setClient(ClientChat client) {
		this.client = client;
	}
	public JTextField getPortTextField() {
		return portTextField;
	}

	public void setPortTextField(JTextField portTextField) {
		this.portTextField = portTextField;
	}
}
