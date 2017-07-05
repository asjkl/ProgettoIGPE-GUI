package progettoIGPE.davide.giovanni.unical2016.GUI;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import net.ConnectionManager;

@SuppressWarnings("serial")
public class NetworkPanel extends JPanel {

	private PanelSwitcher switcher;
	private JTextField ipTextField;
	private JTextField nameTextField;
	private JTextField portTextField;
	private JPanel content;
	private GridBagConstraints constraints;

	private int cursorPosition;
	private ArrayList<JButton> buttons;
	private int DIM = 2;

	public NetworkPanel(int w, int h, PanelSwitcher switcher) {

		this.setPreferredSize(new Dimension(w, h));
		this.setSwitcher(switcher);
		this.setLayout(new BorderLayout());
		content = new JPanel(new GridBagLayout()) {
			
			@Override
			public void setPreferredSize(Dimension preferredSize) {
				preferredSize.setSize(w, h);
			}
			
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f));
				g.drawImage(ImageProvider.getBackground2P(), 
						(int) (content.getPreferredSize().getWidth() / 2),0, null);
				
				((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

					if (cursorPosition == 0)
						g.drawImage(ImageProvider.getCursorLeft(), buttons.get(cursorPosition).getX() + 85,
								buttons.get(cursorPosition).getY() - 6, this);
					else
						g.drawImage(ImageProvider.getCursorRight(), buttons.get(cursorPosition).getX() - 65,
								buttons.get(cursorPosition).getY() - 8, this);
			}
		};
		content.setBackground(Color.BLACK);
		constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(10, 10, 10, 10);
		constraints.gridx = 0;
		constraints.gridy = 0;

		cursorPosition = 1;
		buttons = new ArrayList<>();
	
		createLabels();
		createButtons();
		
		add(content, BorderLayout.CENTER);
	}
	
	private void createLabels() {

		JLabel label = new JLabel();
		label.setText("Server IP");
		label.setBackground(Color.BLACK);
		label.setForeground(Color.WHITE);
		label.setFont(MainFrame.customFontM);
		content.add(label, constraints);
		ipTextField = new JTextField(10);
		ipTextField.setText("127.0.0.1");
		ipTextField.setBackground(Color.BLACK);
		ipTextField.setForeground(Color.WHITE);
		ipTextField.setFont(MainFrame.customFontM);
		constraints.gridx++;
		content.add(ipTextField, constraints);
		
		constraints.gridy++;
		constraints.gridx = 0;
		
		JLabel label2 = new JLabel();
		label2.setText("Server Port");
		label2.setBackground(Color.BLACK);
		label2.setForeground(Color.WHITE);
		label2.setFont(MainFrame.customFontM);
		content.add(label2, constraints);
		portTextField = new JTextField(10);
		portTextField.setText("1234");
		portTextField.setBackground(Color.BLACK);
		portTextField.setForeground(Color.WHITE);
		portTextField.setFont(MainFrame.customFontM);
		constraints.gridx++;
		content.add(portTextField, constraints);
		
		constraints.gridy++;
		constraints.gridx = 0;
		
		JLabel label3 = new JLabel();
		label3.setText("Player Name");
		label3.setBackground(Color.BLACK);
		label3.setForeground(Color.WHITE);
		label3.setFont(MainFrame.customFontM);
		content.add(label3, constraints);
		nameTextField = new JTextField(10);
		nameTextField.setText("P1");
		nameTextField.setBackground(Color.BLACK);
		nameTextField.setForeground(Color.WHITE);
		nameTextField.setFont(MainFrame.customFontM);
		constraints.gridx++;
		content.add(nameTextField, constraints);
		
		constraints.gridy++;
		constraints.gridx = 0;
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

					if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						SoundsProvider.playBulletHit1();
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
			content.add(buttons.get(i),constraints);
			constraints.gridx++;
		}
		buttons.get(1).setForeground(Color.YELLOW);
	}
	
	public void addActionListener(int j) {

	switch (j) {
	case 0:
		buttons.get(j).addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				SoundsProvider.playBulletHit1();
				cursorPosition = j;
				getSwitcher().showMenu();

			}
		});
		break;
	case 1:
		buttons.get(j).addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				buttons.get(1).setEnabled(false);
				new Thread() {
					@Override
					public void run() {
						try {
							connectoToServer();
						} catch (final Exception e) {
							JOptionPane.showMessageDialog(buttons.get(1),
									"Impossible to connect to " + ipTextField.getText() + ":" + portTextField.getText(),
									"Network Error", JOptionPane.ERROR_MESSAGE);
						}
					};
				}.start();

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
			buttons.get(j).setBounds((int) ((getPreferredSize().getWidth() / 2)-75), 600, 150, 35);
			buttons.get(j).setText("Connect");
			break;
		default:
			break;
		}
	}

	protected void connectoToServer() throws Exception {
		final Socket socket = new Socket(ipTextField.getText(), Integer.parseInt(portTextField.getText()));
		final ConnectionManager connectionManager = new ConnectionManager(socket, nameTextField.getText(), ((MainFrame)switcher));
		new Thread(connectionManager, "Connection Manager").start();
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