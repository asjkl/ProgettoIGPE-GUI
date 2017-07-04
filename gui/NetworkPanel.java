package progettoIGPE.davide.giovanni.unical2016.GUI;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.ConnectionManager;
import progettoIGPE.davide.giovanni.unical2016.GUI.ImageProvider;
import progettoIGPE.davide.giovanni.unical2016.GUI.MainFrame;
import progettoIGPE.davide.giovanni.unical2016.GUI.PanelSwitcher;
import progettoIGPE.davide.giovanni.unical2016.GUI.SoundsProvider;

@SuppressWarnings("serial")
public class NetworkPanel extends JPanel {

	private final JTextField ipTextField;
//	private final MainFrame mainFrame;
	private final JTextField nameTextField;
	private final JTextField portTextField;
	private JLabel labels[];
	private JButton back;
	private JButton connect;
	private JDialog dialog;
	private PanelSwitcher switcher;

	public NetworkPanel(final int w, final int h, PanelSwitcher switcher) {
		
		this.setBackground(Color.BLACK);
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(w, h));
		this.switcher = switcher;
//		mainFrame = ((MainFrame)switcher);
	
		setLabelsAndBack();		
		//final JPanel content = new JPanel(new GridBagLayout());
		final GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(20, 20, 20, 20);
		constraints.gridx = 0;
		constraints.gridy = 0;
		
		this.add(labels[0], constraints);
		ipTextField = new JTextField(20);
		ipTextField.setText("127.0.0.1");
		constraints.gridx++;
		this.add(ipTextField, constraints);
		constraints.gridy++;
		constraints.gridx = 0;
		this.add(labels[1], constraints);
		portTextField = new JTextField(20);
		portTextField.setText("1234");
		constraints.gridx++;
		this.add(portTextField, constraints);
		constraints.gridy++;
		constraints.gridx = 0;
		this.add(labels[2], constraints);
		nameTextField = new JTextField(20);
		nameTextField.setText("");
		constraints.gridx++;
		this.add(nameTextField, constraints);
		constraints.gridy++;
		constraints.gridx = 0;

		connect = new JButton("Connect");
		connect.setFont(MainFrame.customFontS);
		connect.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(final ActionEvent e) {
				
				SoundsProvider.playBulletHit1();
				connect.setEnabled(false);
				
				new Thread() {
					
					@Override
					public void run() {
						
						try {
							connectoToServer();
						
						} catch(Exception e) {
							showDialog();
						}
					};
				}.start();
			}
		});
		
		this.add(connect, constraints);
	}

	private void showDialog() {
		
		dialog = new JDialog(dialog, "ERROR");
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
	
	private void setLabelsAndBack() {

		labels = new JLabel[3];
		
		for(int i = 0; i < labels.length; i++) {
			
			labels[i] = new JLabel();
			labels[i].setFont(MainFrame.customFontM);
			labels[i].setBackground(Color.BLACK);
			labels[i].setForeground(Color.WHITE);
			labels[i].setPreferredSize(new Dimension(150, 50));
			
			if(i == 0)
				labels[i].setText("Server IP");
			else
				if(i == 1)
					labels[i].setText("Port");
				else
					labels[i].setText("Name");
		}
		
		back = new JButton("Back");
		
		//TODO NON RIESCO A POSIZIONARLO IN ALTO A SINISTRA A CAUSA DEL GRID BAG GRANATA BASTARDO LAYOUT
//		back.setFont(MainFrame.customFontM);
//		back.setBorder(null);
//		back.setContentAreaFilled(false);
//		back.setBorderPainted(false);
//		back.setFocusPainted(false);
//		back.setBackground(Color.BLACK);
//		back.setForeground(Color.WHITE);
//		back.setBounds(20, 20, 70, 30);
//		back.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {	
//			
//				SoundsProvider.playBulletHit1();
//				repaint();
//				getSwitcher().showMenu();
//			}
//		});
//		back.addKeyListener(new KeyAdapter() {
//		
//			@Override
//			public void keyPressed(KeyEvent e) {
//			
//				if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
//					
//					SoundsProvider.playBulletHit1();
//					getSwitcher().showMenu();
//				}
//			}
//		});
//		
//		this.add(back);
	}
	
	protected void connectoToServer() throws Exception {
		final Socket socket = new Socket(ipTextField.getText(), Integer.parseInt(portTextField.getText()));
		final ConnectionManager connectionManager = new ConnectionManager(socket, nameTextField.getText(), (MainFrame)switcher);
		new Thread(connectionManager, "Connection Manager").start();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f));
		g.drawImage(ImageProvider.getBackground2P(), 
				(int) (getPreferredSize().getWidth() / 2) - (ImageProvider.getBackground2P().getWidth(null) / 2), 0, null);
		
		
		((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		
		if(back.isSelected())
			g.drawImage(ImageProvider.getCursorLeft(), back.getX() + 90, back.getY() - 8, this);	
	}
	
	public PanelSwitcher getSwitcher() {
		return switcher;
	}
	
	public void setSwitcher(PanelSwitcher switcher) {
		this.switcher = switcher;
	}
}
