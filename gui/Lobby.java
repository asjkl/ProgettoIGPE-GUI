package progettoIGPE.davide.giovanni.unical2016.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class Lobby extends JPanel{
	
	private int width;
	private int height;
	private PanelSwitcher switcher;
	private final int DIM = 3;
	private int cursorPosition;
	private JButton arrowLeft, arrowRight;
	private final ArrayList<JButton> buttons;
	
	public Lobby(int w, int h, PanelSwitcher switcher) {
		width=w;
		height=h;
		this.setPreferredSize(new Dimension(w, h));
		this.setBackground(Color.BLACK);
		this.setLayout(null);
		
		setSwitcher(switcher);
		setCursorPosition(1);
		
		arrowLeft = new JButton();
		arrowRight = new JButton();
		buttons = new ArrayList<>();
		createButton();
		createChat();
		createMapsChooser();
		
	}
	
	public void createChat() {
		JLabel chat = new JLabel("Chat: ");
		chat.setFont(MainFrame.customFontM);
		chat.setBackground(Color.BLACK);
		chat.setForeground(Color.WHITE);
		chat.setBounds(80, height-200, 100, 40);
		
		add(chat);
	}
	
	public void createMapsChooser() {
		
		JPanel panel = new JPanel();
		panel.setBounds(width-400, 100, 350, 300);
		panel.setBackground(Color.GRAY);
		add(panel);
		
		JLabel label = new JLabel("Maps: ");
		label.setFont(MainFrame.customFontM);
		label.setBackground(Color.BLACK);
		label.setForeground(Color.WHITE);
		label.setBounds(width-400, 60, 100, 40);
		add(label);
		
		arrowLeft.setBounds(100, 80, 50, 30);
		arrowLeft.setBorder(null);
		arrowLeft.setIcon(new ImageIcon(ImageProvider.getArrowLeft()));
		arrowLeft.setContentAreaFilled(false);
		arrowLeft.setBorderPainted(false);
		arrowLeft.setFocusPainted(false);
		arrowLeft.addKeyListener(new KeyAdapter() {
            
			@Override
            public void keyPressed(KeyEvent e) {
				
				 if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					 SoundsProvider.playBulletHit1();
          	           ((JButton) e.getComponent()).doClick();
                 }
			 repaint();
			 
			}
		});
		
		arrowLeft.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cursorPosition = 0;
				
			}
		});	
		panel.add(arrowLeft);
		
		arrowRight.setBounds(100, 80, 50, 30);
		arrowRight.setBorder(null);
		arrowRight.setIcon(new ImageIcon(ImageProvider.getArrowRight()));
		arrowRight.setContentAreaFilled(false);
		arrowRight.setBorderPainted(false);
		arrowRight.setFocusPainted(false);
		arrowRight.addKeyListener(new KeyAdapter() {
            
			@Override
            public void keyPressed(KeyEvent e) {
				
				 if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					 SoundsProvider.playBulletHit1();
          	           ((JButton) e.getComponent()).doClick();
                 }
				repaint();
			}
		});
		
		arrowRight.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				cursorPosition = 1;
				
			}
		});
		
		panel.add(arrowRight);
		
		
	}
	
	public void createButton() {
		
		for(int i = 0; i < DIM; i++) {
			
			final int curRow = i;
			
			buttons.add(new JButton());
			buttons.get(i).setBorder(null);
			buttons.get(i).setContentAreaFilled(false);
			buttons.get(i).setBorderPainted(false);
			buttons.get(i).setFocusPainted(false);
			buttons.get(i).setForeground(Color.WHITE);
			buttons.get(i).setBackground(Color.BLACK);
			buttons.get(i).setHorizontalAlignment( SwingConstants.LEFT );
			
			if(i == 0)
				buttons.get(i).setFont(MainFrame.customFontM);
			else
				buttons.get(i).setFont(MainFrame.customFontB);
			
			setBoundAndText(i);
			buttons.get(i).addKeyListener(new KeyAdapter() {
	               
			@Override
	        public void keyPressed(KeyEvent e) {
				
			boolean enter = false;
				
				 if(e.getKeyCode() == KeyEvent.VK_ENTER) {
	      	         ((JButton) e.getComponent()).doClick();
	              }
	              else 
	            	  if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
	            		  enter=true;
	            		  setCursorPosition(1);
	            		  getSwitcher().showNetwork();
	              }
	              else 
	            	  if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_LEFT) {
	            	  enter=true;
	            		  if(curRow < 1) {
	                        
	            			  buttons.get(buttons.size() - 1).requestFocus();
	            			  setCursorPosition(buttons.size() - 1); 
	            			  repaint();
		                  }        
		                  else {
		                    	 
	                    	 buttons.get(curRow - 1).requestFocus();
	                    	 setCursorPosition(curRow - 1);
	                    	 repaint();
		                  }
	            	}
	            	else 
		                	if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_RIGHT) {
		                		 enter=true;
	            		  buttons.get((curRow + 1) % buttons.size()).requestFocus();
	            		  setCursorPosition((curRow + 1) % buttons.size()); 
	            		  repaint();
		                	} 
				 if(enter)
					 SoundsProvider.playBulletHit1();
				}   
			});	
			
			addActionListener(i);	
			add(buttons.get(i));
	    }
	}

	public void addActionListener(int j) {
		
		switch (j) {
		case 0:
			buttons.get(j).addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {	
					SoundsProvider.playBulletHit1();
					setCursorPosition(1);
					getSwitcher().showNetwork();
					repaint();
				}
			});
			break;
//		case 1:
//			buttons.get(j).addActionListener(new ActionListener() {
//
//				@Override
//				public void actionPerformed(ActionEvent e) {
//					SoundsProvider.playBulletHit1();
//					setCursorPosition(1);
//					getSwitcher().showFirstStage("./maps/career/singleplayer");
//					repaint();
//				}
//			});
//			break;
//		case 2:
//			buttons.get(j).addActionListener(new ActionListener() {
//
//				@Override
//				public void actionPerformed(ActionEvent e) {
//				
//					SoundsProvider.playBulletHit1();
//					setCursorPosition(2);
//					repaint();
//					getSwitcher().showFirstStage("./maps/career/multiplayer");
//				}				
//			});
//			break;
		default:
			break;
		}
	}
					
	public void setBoundAndText(int j) {

		switch (j) {
		case 0:
			buttons.get(j).setBounds(20, 20, 70, 30);
			buttons.get(j).setText("Back");
			break;
//		case 1:
//			buttons.get(j).setBounds((int) (this.getPreferredSize().getWidth()) / 2 - posX,
//					(int) (this.getPreferredSize().getHeight()) / 2 - posY, 260, 40);
//			buttons.get(j).setText("Singleplayer");
//			break;
//		case 2:
//			buttons.get(j).setBounds((int) (this.getPreferredSize().getWidth()) / 2 - posX,
//					(int) (this.getPreferredSize().getHeight()) / 2 , 260, 40);
//			buttons.get(j).setText("Multiplayer");
//			break;
		default:
			break;
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		if(getCursorPosition() == 0)
			g.drawImage(ImageProvider.getCursorLeft(), 
					buttons.get(cursorPosition).getX() + 90,buttons.get(cursorPosition).getY() - 8, this);
		else
			g.drawImage(ImageProvider.getCursorRight(), 
					buttons.get(cursorPosition).getX() - 65,buttons.get(cursorPosition).getY() - 5, this);
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


