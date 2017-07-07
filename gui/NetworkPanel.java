package progettoIGPE.davide.giovanni.unical2016.GUI;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import net.ConnectionManager;
import progettoIGPE.davide.giovanni.unical2016.GUI.PanelSwitcher;
import progettoIGPE.davide.giovanni.unical2016.GUI.SoundsProvider;

@SuppressWarnings("serial")
public class NetworkPanel extends JPanel {

	private boolean difficult = false;
	private boolean map = false;
	
	private PanelSwitcher switcher;
	private JTextField ipTextField;
	private JTextField nameTextField;
	private JTextField portTextField;
	private int cursorPosition;

	private ArrayList<JButton> buttons;
	private JDialog dialog;
	private int DIM = 3;
	private JDialog dialogRoom;
	private JButton[] buttonsRoom;
	private int cursorPositionRoom;
	private WarningDialog warning;


	public NetworkPanel(int w, int h, PanelSwitcher switcher) {

		
		this.dialogRoom = new JDialog();
		this.setBackground(Color.BLACK);
		this.setPreferredSize(new Dimension(w, h));
		this.setSwitcher(switcher);
		this.setLayout(null);
		dialog = new JDialog(dialog, "ERROR");
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
		label.setBounds(440,320,200,40);
		this.add(label);
		
		ipTextField = new JTextField(10);
		ipTextField.setText("127.0.0.1");
		ipTextField.setBackground(Color.BLACK);
		ipTextField.setForeground(Color.WHITE);
		ipTextField.setFont(MainFrame.customFontM);
		ipTextField.setBounds(640,320,200,40);
		this.add(ipTextField);
	
		//----
		
		JLabel label2 = new JLabel("Server Port");
		label2.setBackground(Color.BLACK);
		label2.setForeground(Color.WHITE);
		label2.setFont(MainFrame.customFontM);
		label2.setBounds(440,380,200,40);
		this.add(label2);
		portTextField = new JTextField(10);
		portTextField.setText("1234");
		portTextField.setBackground(Color.BLACK);
		portTextField.setForeground(Color.WHITE);
		portTextField.setFont(MainFrame.customFontM);
		portTextField.setBounds(640,380,200,40);
		this.add(portTextField);
		
		//-----
		
		JLabel label3 = new JLabel("Player Name");
		label3.setBackground(Color.BLACK);
		label3.setForeground(Color.WHITE);
		label3.setFont(MainFrame.customFontM);
		label3.setBounds(440,440,200,40);
		this.add(label3);
		
		nameTextField = new JTextField(10);
		nameTextField.setText("P1");
		nameTextField.setBackground(Color.BLACK);
		nameTextField.setForeground(Color.WHITE);
		nameTextField.setFont(MainFrame.customFontM);
		nameTextField.setBounds(640,440,200,40);
		this.add(nameTextField);
		

	}
	
	private void createButtons() {
		
		for(int i = 0; i < DIM; i++) {
			
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

					if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						SoundsProvider.playBulletHit1();
						cursorPosition = 1;
               		  	getSwitcher().showMenu();
						repaint();
					} else 
						if(e.getKeyCode() == KeyEvent.VK_ENTER) {
							((JButton) e.getComponent()).doClick();

					} else 
						if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_LEFT) {

						enter = true;

						if(curRow < 1) {

							buttons.get(buttons.size() - 1).requestFocus();
							cursorPosition = buttons.size() - 1;
							repaint();
						} 
						else {

							buttons.get(curRow - 1).requestFocus();
							cursorPosition = curRow - 1;
							repaint();
						}
					} else 
						if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_RIGHT) {

							enter = true;
							buttons.get((curRow + 1) % buttons.size()).requestFocus();
							cursorPosition = (curRow + 1) % buttons.size();
							repaint();
					}

					if(enter)
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
	case 0: //BACK
		buttons.get(j).addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				SoundsProvider.playBulletHit1();
				cursorPosition = 1;
				difficult = false;
				map = false;
				getSwitcher().showMenu();

			}
		});
		break;
	case 1: //CONNECT
		buttons.get(j).addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				SoundsProvider.playBulletHit1();
				if(difficult && map) {
					buttons.get(1).setEnabled(false);
					new Thread() {
						@Override
						public void run() {
								try {
									connectoToServer();
								} catch (final Exception e) {
									showDialog();
								}
							};
						}.start();
	
				}
				else
					warning = new WarningDialog("Create room first!");
			}
			});
			break;
	case 2: // CREATE ROOM
		buttons.get(j).addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				Room();
			}
		});
			break;
		default:
			break;
		}
	}
	
	private void showDialog() {
		
		JLabel label = new JLabel("Impossible to connect to " + ipTextField.getText() + ":" + portTextField.getText());
		
		label.setFont(MainFrame.customFontS);
		label.setBackground(Color.BLACK);
		label.setForeground(Color.RED);
		label.setHorizontalAlignment(JLabel.CENTER);
		
		JPanel panel = new JPanel(new GridLayout(2,0));
		
		panel.setBackground(Color.BLACK);
		panel.setBorder(BorderFactory.createLineBorder(Color.RED));
		
		JButton ok = new JButton("OK");
		
		ok.setBorder(null);
		ok.setContentAreaFilled(false);
		ok.setBorderPainted(false);
		ok.setFocusPainted(false);
		ok.setFont(MainFrame.customFontS);
		ok.setBackground(Color.BLACK);
		ok.setForeground(Color.WHITE);
		ok.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				SoundsProvider.playBulletHit1();
				dialog.dispose();
			}
		});
		
		panel.add(label);
		panel.add(ok);
		panel.setPreferredSize(new Dimension(300,100));
		dialog.setContentPane(panel);
		dialog.setUndecorated(true);
		dialog.setModal(true);
		dialog.pack();
		dialog.setLocationRelativeTo(this);
		dialog.setVisible(true);
	}

	public void setBoundAndText(int j) {

		switch (j) {
		case 0:
			buttons.get(j).setBounds(18, 12, 70, 35);
			buttons.get(j).setText("Back");
			break;
		case 1:
			buttons.get(j).setBounds(600, 570, 150, 35);
			buttons.get(j).setText("Connect");
			break;
		case 2:
			buttons.get(j).setBounds(580, 650, 200, 35);
			buttons.get(j).setText("Create Room");
			break;
		default:
			break;
		}
	}

	// <<<<<<   Roooooom   >>>>>>>
	
	public void Room() {

		dialogRoom.setSize(new Dimension(250, 250));
		JPanel fullpanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (cursorPositionRoom == 0) {
					g.drawImage(ImageProvider.getCursorRight(), 30, 80, this);
				} else if (cursorPositionRoom == 1) {
					g.drawImage(ImageProvider.getCursorRight(), 30, 133, this);
				} else {
					g.drawImage(ImageProvider.getCursorRight(), 30, 185, this);
				}
			}
		};
		JPanel text = new JPanel();
		JPanel buttonspanel = new JPanel(new GridLayout(3, 1, 0, 10));

		JLabel label = new JLabel("Room");
		String[] buttonTxt = { "Maps", "Difficult", "Back"};

		fullpanel.setPreferredSize(new Dimension(250, 270));
		fullpanel.setBorder(BorderFactory.createLineBorder(Color.RED));
		fullpanel.setBackground(Color.BLACK);
		buttonsRoom = new JButton[buttonTxt.length];
		label.setFont(MainFrame.customFontB);
		label.setForeground(Color.RED);
		label.setBorder(null);
		text.add(label);
		text.setPreferredSize(new Dimension(200, 70));
		text.setMaximumSize(new Dimension(200, 70)); // set max = pref
		text.setBackground(Color.BLACK);
		text.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonspanel.setBackground(Color.BLACK);

		for (int i = 0; i < buttonTxt.length; i++) {

			final int curRow = i;
			buttonsRoom[i] = new JButton(buttonTxt[i]);
			buttonsRoom[i].setFont(MainFrame.customFontM);
			buttonsRoom[i].setBackground(Color.BLACK);
			buttonsRoom[i].setForeground(Color.WHITE);
			buttonsRoom[i].setBorder(null);
			buttonsRoom[i].setFocusPainted(false);
			buttonsRoom[i].setContentAreaFilled(false);
			buttonsRoom[i].setBorderPainted(false);
			buttonsRoom[i].setFocusPainted(false);
			buttonsRoom[i].addKeyListener(new KeyAdapter() {

				@Override
				public void keyPressed(KeyEvent e) {

					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					
							((JButton) e.getComponent()).doClick();
							
								dialogRoom.dispose();
						
					} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						
						dialogRoom.dispose();
					}

					else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_LEFT) {
						SoundsProvider.playBulletHit1();
						if (curRow < 1) {
							buttonsRoom[buttonsRoom.length - 1].requestFocus();
							cursorPositionRoom = buttonsRoom.length - 1;

						} else {
							buttonsRoom[curRow - 1].requestFocus();
							cursorPositionRoom = curRow - 1;

						}
					} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_RIGHT) {
						SoundsProvider.playBulletHit1();
						buttonsRoom[(curRow + 1) % buttonsRoom.length].requestFocus();
						cursorPositionRoom = (curRow + 1) % buttonsRoom.length;
					}
				}
			});

			buttonspanel.add(buttonsRoom[i]);
			buttonspanel.setBackground(Color.BLACK);
			RoomActionListener(i);
		}

		buttonspanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonspanel.setPreferredSize(new Dimension(100, 150));
		buttonspanel.setMaximumSize(new Dimension(100, 150));
		buttonspanel.setBackground(Color.BLACK);
		fullpanel.add(text);
		fullpanel.add(buttonspanel);
		dialogRoom.setContentPane(fullpanel);
		dialogRoom.setUndecorated(true);
		dialogRoom.setModal(true);
		dialogRoom.pack();
		dialogRoom.setLocationRelativeTo(this);
		dialogRoom.setVisible(true);
	
	}

	public void RoomActionListener(int j) {

		switch (j) {
		case 0: 
			buttonsRoom[j].addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					SoundsProvider.playBulletHit1();
		
					dialogRoom.dispose();
				}
			});
			break;
		case 1:
			buttonsRoom[j].addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					SoundsProvider.playBulletHit1();
				
						dialogRoom.dispose();
					
				}
			});
		case 2:
			buttonsRoom[j].addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					SoundsProvider.playBulletHit1();
				
						dialogRoom.dispose();
					
				}
			});
			break;		
		default:
			break;
		}
	}

	
	protected void connectoToServer() throws Exception {
		final Socket socket = new Socket(ipTextField.getText(), Integer.parseInt(portTextField.getText()));
		final ConnectionManager connectionManager = new ConnectionManager(socket, nameTextField.getText(), ((MainFrame)getSwitcher()) );
		new Thread(connectionManager, "Connection Manager").start();
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
		g.drawImage(ImageProvider.getBackground2P(), (int) (getPreferredSize().getWidth() / 2)-375,0, null);
		
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
}

