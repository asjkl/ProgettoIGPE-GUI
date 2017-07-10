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
	private final int DIM = 1;
	private int cursorPosition = 0;
	private JButton arrowLeft, arrowRight;
	private final ArrayList<JButton> buttons;
	private int stageShifter = 1;
	
	public Lobby(int w, int h, PanelSwitcher switcher) {
		width=w;
		height=h;
		this.setPreferredSize(new Dimension(w, h));
		this.setBackground(Color.BLACK);
		this.setLayout(null);
		
		setSwitcher(switcher);
		
		arrowLeft = new JButton();
		arrowRight = new JButton();
		buttons = new ArrayList<>();
		
		createButton();
		
		createChat();
		
		JLabel label = new JLabel("Maps: ");
		label.setFont(MainFrame.customFontM);
		label.setForeground(Color.WHITE);
		label.setBounds(width-440, 90, 100, 40);
		add(label);
		
		createMapsPanel();
		
		JLabel online = new JLabel("Online: ");
		online.setFont(MainFrame.customFontM);
		online.setForeground(Color.WHITE);
		online.setBounds(80, 90, 100, 40);
		add(online);
		
		createOnlinePanel();
		
		JLabel difficult = new JLabel("Difficult: ");
		difficult.setFont(MainFrame.customFontM);
		difficult.setForeground(Color.WHITE);
		difficult.setBounds(520, 90, 130, 40);
		add(difficult);
		
		createDifficultPanel();
	}
	
	public void createDifficultPanel() {
		JPanel difficultPanel = new JPanel();
		difficultPanel.setLayout(null);
		difficultPanel.setBounds(520, 130, 200, 200);
		difficultPanel.setBackground(Color.GRAY);
		add(difficultPanel);
	}

	public void createOnlinePanel() {
		
		JPanel onlinePanel = new JPanel();
		onlinePanel.setLayout(null);
		onlinePanel.setBounds(80, 130, 300, 300);
		onlinePanel.setBackground(Color.GRAY);
		add(onlinePanel);
	}
	
	public void createChat() {
		
		JLabel chat = new JLabel("Chat: ");
		chat.setFont(MainFrame.customFontM);
		chat.setBackground(Color.BLACK);
		chat.setForeground(Color.WHITE);
		chat.setBounds(80, height-220, 100, 40);
		
		add(chat);
		
		
		JPanel chatPanel = new JPanel();
		chatPanel.setLayout(null);
		chatPanel.setBounds(80, height-180, 800, 150);
		chatPanel.setBackground(Color.GRAY);
		add(chatPanel);
		
		
	}
	
	public void createMapsPanel() {
		
		JPanel panel = new JPanel(){
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(ImageProvider.getMapsP2().get(stageShifter-1).getScaledInstance(230, 230, java.awt.Image.SCALE_SMOOTH),61,20,  this);
				g.setColor(Color.BLACK);
				g.setFont(MainFrame.customFontM);
				g.drawString("Stage " + stageShifter, 135, 282);
			}
		};
		
		panel.setLayout(null);
		panel.setBounds(width-440, 130, 350, 300);
		panel.setBackground(Color.GRAY);
		add(panel);
		
		arrowLeft.setBorder(null);
		arrowLeft.setIcon(new ImageIcon(ImageProvider.getArrowLeft().getScaledInstance(20, 30, java.awt.Image.SCALE_SMOOTH)));
		arrowLeft.setBounds(76, 260, 20, 30);
		arrowLeft.setContentAreaFilled(false);
		arrowLeft.setBorderPainted(false);
		arrowLeft.setFocusPainted(false);
	
		arrowLeft.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				 SoundsProvider.playBulletHit1();
				 if(stageShifter == 1)
					 stageShifter = 24;
				 else
				  stageShifter = (stageShifter - 1); 
				 repaint();
				
			}
		});	
		panel.add(arrowLeft);
	
		arrowRight.setBorder(null);
		arrowRight.setIcon(new ImageIcon(ImageProvider.getArrowRight().getScaledInstance(20, 30, java.awt.Image.SCALE_SMOOTH)));
		arrowRight.setBounds(256, 260, 20, 30);
		arrowRight.setContentAreaFilled(false);
		arrowRight.setBorderPainted(false);
		arrowRight.setFocusPainted(false);
		
		arrowRight.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				 SoundsProvider.playBulletHit1();
				   stageShifter = (stageShifter + 1)%25; 
				  repaint();
			}
		});
		
		panel.add(arrowRight);
		
	}
	
	public void createButton() {
		
		for(int i = 0; i < DIM; i++) {
			
//			final int curRow = i;
			
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
//	            	  if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_LEFT) {
//	            	  enter=true;
//	            		  if(curRow < 1) {
//	                        
//	            			  buttons.get(buttons.size() - 1).requestFocus();
//	            			  setCursorPosition(buttons.size() - 1); 
//	            			  repaint();
//		                  }        
//		                  else {
//		                    	 
//	                    	 buttons.get(curRow - 1).requestFocus();
//	                    	 setCursorPosition(curRow - 1);
//	                    	 repaint();
//		                  }
//	            	}
//	            	else 
//		                	if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_RIGHT) {
//		                		 enter=true;
//	            		  buttons.get((curRow + 1) % buttons.size()).requestFocus();
//	            		  setCursorPosition((curRow + 1) % buttons.size()); 
//	            		  repaint();
//		                	} 
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
					setCursorPosition(0);
					getSwitcher().showNetwork();
					repaint();
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


