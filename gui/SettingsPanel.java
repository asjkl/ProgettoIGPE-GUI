package progettoIGPE.davide.giovanni.unical2016.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;

@SuppressWarnings("serial")
public class SettingsPanel extends JPanel {

	private boolean hide;
	protected static float soundValue=0.0f;
	public static boolean easy=true, normal=false, hard=false;
	
	private float currValue;
	private int cursorPosition;
	private final int DIM = 3;
	private PanelSwitcher switcher;
	private JSlider sound;
	private ArrayList<JRadioButton> level;
	private ArrayList<JButton> buttons;
	private ButtonGroup group;

	public SettingsPanel(final int w, final int h, PanelSwitcher switcher) {

		this.setPreferredSize(new Dimension(w, h));
		this.setBackground(Color.BLACK);
		this.setLayout(null);
		
		hide = false;
		cursorPosition = 1;
		buttons = new ArrayList<>();
		level = new ArrayList<>();
		group =  new ButtonGroup();
		setSwitcher(switcher);
		setButtons();
		setSlider();
		createRadioButtons();
	}
	
	public void setButtons() {
		
		for(int i = 0; i < DIM; i++) {
		
			final int curRow = i;
			
			buttons.add(new JButton());
			
			if(i == 0)
				buttons.get(i).setFont(MainFrame.customFontM);
			else
				buttons.get(i).setFont(MainFrame.customFontB);
				
			buttons.get(i).setContentAreaFilled(false);
			buttons.get(i).setBorderPainted(false);
			buttons.get(i).setFocusPainted(false);
			buttons.get(i).setBackground(Color.BLACK);
			buttons.get(i).setForeground(Color.WHITE);
			setBoundsAndText(i);
			addActionListener(i);
			buttons.get(i).addMouseListener(new MouseInputAdapter() {
				
				@Override
				public void mousePressed(MouseEvent e) {
				
					if(e.getComponent().getY() == buttons.get(curRow).getY()) {
						cursorPosition = curRow;
						repaint();
					}
				}
			});
			buttons.get(i).addKeyListener(new KeyAdapter() {
			
				@Override
				public void keyPressed(KeyEvent e) {
					
					boolean enter = false;
					
					if(e.getKeyCode() == KeyEvent.VK_ENTER) {
	         	        
						hide = false; 
	         	        repaint();
						((JButton) e.getComponent()).doClick();
	                }
					else 
	              	  if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
	              		
	              		SoundsProvider.playBulletHit1();
	              		hide = false;
	              		repaint();
	              		cursorPosition = 1;
	              		getSwitcher().showMenu();
	              	}
	              	else 
	              		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_LEFT) {

	              			enter = true;
	              			hide = false;

						if(curRow < 1) {

							buttons.get(buttons.size() - 1).requestFocus();
							cursorPosition = buttons.size() - 1;
							repaint();
							
						} else {

							buttons.get(curRow - 1).requestFocus();
							cursorPosition = curRow - 1;
							repaint();
						}
					} else 
						if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_RIGHT) {

							enter = true;
							hide = false;
							buttons.get((curRow + 1) % buttons.size()).requestFocus();
							cursorPosition = (curRow + 1) % buttons.size();
							repaint();
					}
					
					if(enter) 
						SoundsProvider.playBulletHit1();
				}
			});
			
				this.add(buttons.get(i));
			}
	}
	
	public void setBoundsAndText(int j) {
		
		switch(j) {
		case 0:
			buttons.get(j).setBounds(20, 20, 100, 30);
			buttons.get(j).setText("Back");
			break;
		case 1:
			buttons.get(j).setBounds(265, 270, 200, 100);
			buttons.get(j).setText("Sound");
			break;
		case 2:
			buttons.get(j).setBounds(265, 370, 200, 100);
			buttons.get(j).setText("Level");
			break;
		default:
			break;
		}
	}
	
	public void addActionListener(int j) {
		
		switch(j) {
		case 0:
			buttons.get(j).addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {	
					
					SoundsProvider.playBulletHit1();
					hide = false;
					cursorPosition = j;
					repaint();
					cursorPosition = 1;
					getSwitcher().showMenu();
				}
			});
			break;
		case 1:
			buttons.get(j).addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {	
					
					SoundsProvider.playBulletHit1();
					hide = false;
					cursorPosition = j;
					repaint();
				}
			});
			break;
		case 2:
			buttons.get(j).addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {	
					
					SoundsProvider.playBulletHit1();
					hide = false;
					cursorPosition = j;
					repaint();
				}
			});
			break;
		default:
			break;
		}
	}
	
	public void createRadioButtons() {

		for(int i = 0; i < DIM; i++) {
			
			level.add(new JRadioButton());
			level.get(i).setBackground(null);
			
			if(i == 0)
				level.get(i).setSelected(easy);
			
			level.get(i).addChangeListener(new ChangeListener() {
				
				@Override
				public void stateChanged(ChangeEvent e) {
					
					hide = true;
					
					if(level.get(0).isSelected()) {						
						
						easy = true;
						normal = hard = false;
					}
					else
						if(level.get(1).isSelected()) {
							
							normal = true;
							easy = hard = false;						
						}
						else
							if(level.get(2).isSelected()) {
								
								hard = true;
								easy = normal = false;
							}
					
					repaint();
					}
			});	
			
			setBoundRadioButton(i);
			group.add(level.get(i));
			this.add(level.get(i));
	    }
	}

	public void setBoundRadioButton(int j) {
		
		switch (j) {
		case 0:
			level.get(j).setBounds((int) (this.getPreferredSize().getWidth() / 2 - 100),
					(int) (this.getPreferredSize().getHeight() / 2 + 45), 20, 20);
			break;
		case 1:
			level.get(j).setBounds((int) (this.getPreferredSize().getWidth() / 2 + 35),
					(int) (this.getPreferredSize().getHeight() / 2 + 45), 20, 20);
			break;
		case 2:
			level.get(j).setBounds((int) (this.getPreferredSize().getWidth() / 2 + 170),
					(int) (this.getPreferredSize().getHeight() / 2 + 45), 20, 20);
			break;
		default:
			break;
		}
	}
	
	public void setSlider() {

		sound = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
		sound.setBounds((int) (this.getPreferredSize().getWidth() / 2 - 105),
				(int) (this.getPreferredSize().getHeight() / 2 - 65), 300, 50);
		sound.setBackground(null);
		sound.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				
				hide = true;
				repaint();
				
				if(sound.getValue() != 0) {
				
					currValue = (sound.getMaximum() - sound.getValue()) / 2;
					soundValue = currValue / 2;
				}
				else
					soundValue = currValue;
			}
		});

		this.add(sound);
	}

	@Override
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);

		if(!hide) {
		
			if(cursorPosition == 0)
				g.drawImage(ImageProvider.getCursorLeft(), buttons.get(cursorPosition).getX() + 100,buttons.get(cursorPosition).getY() - 8, this);
			else
				g.drawImage(ImageProvider.getCursorRight(), buttons.get(cursorPosition).getX() - 35, buttons.get(cursorPosition).getY() + 25, this);
		}
		
		g.drawImage(ImageProvider.getEasy(), level.get(0).getX() - 17, level.get(0).getY() + 25, null);
		g.drawImage(ImageProvider.getNormal(),level.get(1).getX() - 27, level.get(1).getY() + 25, null);
		g.drawImage(ImageProvider.getHard(), level.get(2).getX() - 17, level.get(2).getY() + 25, null);
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
	
	public int getCursorPosition() {
		return cursorPosition;
	}
}
