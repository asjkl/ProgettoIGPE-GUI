package progettoIGPE.davide.giovanni.unical2016.GUI;

import javax.swing.*;

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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

@SuppressWarnings("serial")
public class MenuPanel extends JPanel {

	private int cursorPosition;
	private final int DIM = 6;
	private final int posY = 65;
	private final int posX = 125;
	private JFileChooserClass jfilechooser;
	private PanelSwitcher switcher;
	private final ArrayList<JButton> buttons;
	private JDialog dialog;
	private JButton[] bts;
	private int cursorPositionDialog = 0;
	private boolean hide = false;
	private JLabel high;
	private JLabel player;
	private String values[];
	private int tmp = -1;

	public MenuPanel(final int w, final int h, PanelSwitcher switcher) {

		this.setPreferredSize(new Dimension(w, h));
		this.setBackground(Color.BLACK);
		this.setLayout(null);

		this.jfilechooser = new JFileChooserClass(false);
		setSwitcher(switcher);
		setCursorPosition(0);

		values = new String[DIM + 1];
		for(int i = 0; i < values.length; i++)
			values[i] = "0";
		
		high = new JLabel();
		player = new JLabel();
		this.dialog = new JDialog();
		buttons = new ArrayList<>();
		
		createButton();
		drawScore();
		
	}

	public void createButton() {

		for (int i = 0; i < DIM; i++) {

			final int curRow = i;

			buttons.add(new JButton("fef"));
			buttons.get(i).setBorder(null);
			buttons.get(i).setOpaque(false);
			buttons.get(i).setContentAreaFilled(false);
			buttons.get(i).setBorderPainted(false);
			buttons.get(i).setFocusPainted(false);
			buttons.get(i).setFont(MainFrame.customFontB);
			buttons.get(i).setForeground(Color.WHITE);
			buttons.get(i).setBackground(Color.BLACK);
			buttons.get(i).setHorizontalAlignment(SwingConstants.LEFT);

			setBoundAndText(i);

			buttons.get(i).addKeyListener(new KeyAdapter() {

				@Override
				public void keyPressed(KeyEvent e) {

					if(SlideContainer.isReady()) {

					boolean enter = false;

					if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {

						enter = true;

						buttons.get(buttons.size() - 1).requestFocus();
						cursorPosition = buttons.size() - 1 ;
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
				}
			});

			addActionListener(i);
			this.add(buttons.get(i));
		}
	}

	public void addActionListener(int j) {

		switch (j) {
		case 0:
			buttons.get(j).addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					
					SoundsProvider.playBulletHit1();
					setCursorPosition(j);
					getSwitcher().showPlayer();
				}
			});
			break;
		case 1:
			buttons.get(j).addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					
					setCursorPosition(j);
					SoundsProvider.playBulletHit1();
					getSwitcher().showNetwork();
				}
			});
			break;
		case 2:
			buttons.get(j).addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					
					SoundsProvider.playBulletHit1();
					setCursorPosition(j);
					
					
					if(jfilechooser.functionLoadFile()) {
						setCursorPosition(0);
						jfilechooser.getFilename().setText(jfilechooser.getFile().toString()+"/"+jfilechooser.getFilename().getText() + ".txt");
						  getSwitcher().showLoading(jfilechooser.getFilename());
					}
				}
			});
			break;
		case 3:
			buttons.get(j).addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					setCursorPosition(j);
					SoundsProvider.playBulletHit1();
					getSwitcher().showConstruction();
				}
			});
			break;
		case 4:
			buttons.get(j).addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					SoundsProvider.playBulletHit1();
					setCursorPosition(j);
					getSwitcher().showSettings();
				}
			});
			break;
		case 5:
			buttons.get(j).addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					SoundsProvider.playBulletHit1();
					hide = true;
					setCursorPosition(j);
					MainFrame.transparent = true;
					exitDialog();
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
			buttons.get(j).setBounds((int) (this.getPreferredSize().getWidth()) / 2 - posX,
					(int) (this.getPreferredSize().getHeight()) / 2 - posY + (posY * j), 255, 40);
			buttons.get(j).setText("Play");
			break;
		case 1:
			buttons.get(j).setBounds((int) (this.getPreferredSize().getWidth()) / 2 - posX,
					(int) (this.getPreferredSize().getHeight()) / 2 - posY + (posY * j), 270, 40);
			buttons.get(j).setText("Network");
			break;
		case 2:
			buttons.get(j).setBounds((int) (this.getPreferredSize().getWidth()) / 2 - posX,
					(int) (this.getPreferredSize().getHeight()) / 2 - posY + (posY * j), 380, 40);
			buttons.get(j).setText("Custom Maps");
			break;
		case 3:
			buttons.get(j).setBounds((int) (this.getPreferredSize().getWidth()) / 2 - posX,
					(int) (this.getPreferredSize().getHeight()) / 2 - posY + (posY * j), 380, 40);
			buttons.get(j).setText("Construction");
			break;
		case 4:
			buttons.get(j).setBounds((int) (this.getPreferredSize().getWidth()) / 2 - posX,
					(int) (this.getPreferredSize().getHeight()) / 2 - posY + (posY * j), 250, 40);
			buttons.get(j).setText("Settings");
			break;
		case 5:
			buttons.get(j).setBounds((int) (this.getPreferredSize().getWidth()) / 2 - posX,
					(int) (this.getPreferredSize().getHeight()) / 2 - posY + (posY * j), 135, 40);
			buttons.get(j).setText("Exit");
		default:
			break;
		}
	}

	public void exitDialog() {
		
		dialog.setPreferredSize(new Dimension(300, 150));
		JPanel fullpanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (cursorPositionDialog == 0) {
					g.drawImage(ImageProvider.getCursorRight(), 75, 58, this);
				} else {
					g.drawImage(ImageProvider.getCursorRight(), 75, 106, this);
				}
			}
		};
		JLabel label = new JLabel("Do you want exit?");
		JPanel buttons = new JPanel(new GridLayout(2, 1, 0, 10));
		JPanel text = new JPanel(new GridLayout(1,1,0,10));
		String[] buttonTxt = { "No", "Si" };
		fullpanel.setPreferredSize(new Dimension(300, 150));
		fullpanel.setBorder(BorderFactory.createLineBorder(Color.RED));
		fullpanel.setBackground(Color.BLACK);
		fullpanel.setLayout(new BoxLayout(fullpanel, BoxLayout.Y_AXIS));
		label.setFont(MainFrame.customFontM);
		label.setForeground(Color.WHITE);
		label.setHorizontalAlignment(JLabel.CENTER);
		text.setAlignmentX(Component.CENTER_ALIGNMENT);
		text.setPreferredSize(new Dimension(300, 70));
		text.setMaximumSize(new Dimension(300, 70)); // set max = pref
		text.setBackground(Color.BLACK);
		text.add(label);
		bts = new JButton[buttonTxt.length];
		for (int i = 0; i < buttonTxt.length; i++) {

			final int curRow = i;
			bts[i] = new JButton(buttonTxt[i]);
			bts[i].setFont(MainFrame.customFontM);
			bts[i].setBackground(Color.BLACK);
			bts[i].setForeground(Color.WHITE);
			bts[i].setBorder(null);
			bts[i].setContentAreaFilled(false);
			bts[i].setBorderPainted(false);
			bts[i].setFocusPainted(false);
			bts[i].addKeyListener(new KeyAdapter() {

				@Override
				public void keyPressed(KeyEvent e) {

					boolean enter = false;
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						((JButton) e.getComponent()).doClick();
					} else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_LEFT) {
						enter = true;

						if (curRow < 1) {
							bts[bts.length - 1].requestFocus();
							cursorPositionDialog = bts.length - 1;

						} else {
							bts[curRow - 1].requestFocus();
							cursorPositionDialog = curRow - 1;
						}
					} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_RIGHT) {
						enter = true;
						bts[(curRow + 1) % bts.length].requestFocus();
						cursorPositionDialog = (curRow + 1) % bts.length;
					}
					fullpanel.repaint();
					if (enter)
						 SoundsProvider.playBulletHit1();
				}
			});
			buttons.add(bts[i]);
			exitDialogListener(i);
		}

		buttons.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttons.setPreferredSize(new Dimension(70, 100));
		buttons.setMaximumSize(new Dimension(70, 100));
		buttons.setBackground(Color.BLACK);
		fullpanel.add(text);
		fullpanel.add(buttons);
		dialog.setContentPane(fullpanel);
		dialog.setUndecorated(true);
		dialog.setModal(true);
		dialog.pack();
		dialog.setLocationRelativeTo(this);
		dialog.setVisible(true);
	}

	public void exitDialogListener(int j) {

		switch (j) {
		case 0: // NO
			bts[j].addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					SoundsProvider.playBulletHit1();
					MainFrame.transparent = false;
					cursorPositionDialog = 0;
					hide = false;
					repaint();
					dialog.dispose();
					
				}
			});
			break;
		case 1: // SI
			bts[j].addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					SoundsProvider.playBulletHit1();
					cursorPositionDialog = 1;
					repaint();
					resetScore();
					System.exit(0);
				}
			});
			break;
		default:
			break;
		}
	}
	
	public void drawScore() {
		
		loadScore();
		
		if(tmp < Integer.parseInt(values[1])) {
			
			tmp = Integer.parseInt(values[1]);
			
			high.setFont(MainFrame.customFontB);
			high.setBackground(Color.BLACK);
			high.setForeground(Color.WHITE);
			high.setText("Hi - " + values[1]);
			high.setBounds(500, 0, 500, 100);
		}
		
		player.setFont(MainFrame.customFontB);
		player.setBackground(Color.BLACK);
		player.setForeground(Color.WHITE);
		player.setText("I - " + values[0]);
		player.setBounds(190, 0, 500, 100);
		
		this.add(player);
		this.add(high);
	}	
	
	public void loadScore() {
		
		BufferedReader reader = null;
		String line = null;
		
		try {
			
			reader = new BufferedReader(new FileReader("./values.txt"));
			line = reader.readLine();
			
			int i = 0;
			
			while(line != null) {
				
				StringTokenizer st = new StringTokenizer(line, "");
				String tmp = null;
				
				while(st.hasMoreTokens()) {
					
					tmp = st.nextToken();
					
					if(tmp.matches("[0-9]+") && i < values.length) {
						values[i++] = tmp;
					}
				}
				
				line = reader.readLine();
			}
			
			((MainFrame)switcher).setCurrentResume(Integer.parseInt(values[values.length - 3]));
			((MainFrame)switcher).setUnlockedMaps1P(Integer.parseInt(values[values.length - 2]));
			((MainFrame)switcher).setUnlockedMaps2P(Integer.parseInt(values[values.length - 1]));
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void resetScore() {
		
		BufferedWriter b = null;
		PrintWriter w = null;
		
		int p1 = ((MainFrame)switcher).getUnlockedMaps1P();
		int p2 = ((MainFrame)switcher).getUnlockedMaps2P();
		int r = ((MainFrame)switcher).getCurrentResume();
		
		try {
			
			w = new PrintWriter("./values.txt");
			b = new BufferedWriter(w);
			
			b.write("P1:\n");
			b.write("0\n");
			b.write(String.valueOf(values[1] + "\n"));
			b.write("P2:\n");
			b.write("0\n");
			b.write("0\n");
			b.write("LIVES:\n");
			b.write(String.valueOf(r) + "\n");
			b.write("MAPS:\n");
			b.write(String.valueOf(p1) + "\n");
			b.write(String.valueOf(p2));
			b.flush();
			b.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (MainFrame.transparent) {

			Graphics2D g2d = (Graphics2D) g;
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .2f));
			g2d.setColor(getBackground());
			g2d.fill(getBounds());
		}

		g.drawImage(ImageProvider.getTitle(), 175, 75, null);
		
		if(!hide)
			g.drawImage(ImageProvider.getCursorRight(),
					buttons.get(cursorPosition).getX() - 70, buttons.get(cursorPosition).getY() - 4, this);
	}

	public int getCursorPosition() {
		return cursorPosition;
	}

	public void setCursorPosition(int cursorPosition) {
		this.cursorPosition = cursorPosition;
	}

	public JButton getButton(int i) {
		return buttons.get(i);
	}

	public PanelSwitcher getSwitcher() {
		return switcher;
	}

	public void setSwitcher(PanelSwitcher switcher) {
		this.switcher = switcher;
	}
}