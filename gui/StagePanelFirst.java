package progettoIGPE.davide.giovanni.unical2016.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class StagePanelFirst extends JPanel {

	private int posY;
	private int posX;
	private final int DIM = 13;
	private int cursorPosition;
	
	private String path;
	private File file;
	private JFileChooser fileChooser;
	private JLabel labelStage;
	private JButton arrowRight;
	private ArrayList<JButton> maps;
	private PanelSwitcher switcher;
	
	public StagePanelFirst(final int w, final int h, PanelSwitcher switcher) {
	
		this.setPreferredSize(new Dimension(w, h));
		this.setBackground(Color.GRAY);
		this.setLayout(null);
		
		path = "";
		posY = 25;
		posX = 200;
		cursorPosition = 1;
		
		arrowRight = new JButton();
		maps = new ArrayList<>();
		
		file = new File(path);
		fileChooser = new JFileChooser();
		
		setSwitcher(switcher);
		createButton();
		createArrowButton();
	}
	
	public void createButton() {
		
		for(int i = 0; i < DIM; i++) {
			
			final int curRow = i;
			
			maps.add(new JButton());	

			if(i == 0) {
				
				maps.get(i).setBorder(null);
				maps.get(i).setContentAreaFilled(false);
				maps.get(i).setBorderPainted(false);
				maps.get(i).setFocusPainted(false);
				maps.get(i).setFont(MainFrame.customFontM);
				maps.get(i).setForeground(Color.BLACK);
				maps.get(i).setHorizontalAlignment( SwingConstants.LEFT);
			}
			
			setBoundAndText(i);	
			maps.get(i).addKeyListener(new KeyAdapter() {
	               
				@Override
	            public void keyPressed(KeyEvent e) {
					
					boolean enter = false;
					
					if(e.getKeyCode() == KeyEvent.VK_ENTER) {
	          	           ((JButton) e.getComponent()).doClick();
	                }
					else 
	                	if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
	                		enter = true;
	                		cursorPosition = 1;
	                		getSwitcher().showPlayer();
	                  }
	                  else 
	                	  if(e.getKeyCode() == KeyEvent.VK_LEFT) {
	                		  enter = true;
	                	  
	                		  if(curRow < 1) {
	  	                        
	                			  maps.get(maps.size() - 1).requestFocus();
	                			  cursorPosition = maps.size() - 1;
	                		
	                		  }        
	                		  else {
		                    	 
	                			  maps.get(curRow - 1).requestFocus();
	                			  cursorPosition = curRow - 1;
	                			  
	                		  }
	                   }
	                   else 
	                	   if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
	                		   enter = true;
	                		  
	                		   if(curRow == 8) {
	                		   
	                			   arrowRight.requestFocus();
	                			   cursorPosition = -1;
	                		   }
	                		   else {
	                		   
	                			   maps.get((curRow + 1) % maps.size()).requestFocus();
	                			   cursorPosition = (curRow + 1) % maps.size();
	                		   }
	                	   }   
	                	   else
	                		   if(e.getKeyCode() == KeyEvent.VK_UP) {
	                			   enter = true;
	                			   int tmp = (curRow - 4);
		                		
	                			   if(tmp < 0)
	                				   tmp = maps.size() + tmp;
		                	
	                			   if(curRow == 0)
	                				   tmp = maps.size() - 1;
	                			
		                			maps.get(tmp).requestFocus();
		                			cursorPosition = tmp;
		                		
	                	  }
	                	  else 
	                		  if(e.getKeyCode() == KeyEvent.VK_DOWN){
	                			  enter = true;
	                			int tmp = (curRow + 4) % maps.size();
		                		
	                			if(curRow + 4 > maps.size() - 1)
		                			tmp++;
		                		
	                			if(curRow == 0)
		                			tmp = curRow + 1;
	                			
									maps.get(tmp).requestFocus();
									cursorPosition = tmp;
	    	    		}
					 repaint();
					 if(enter)
						 SoundsProvider.playBulletHit1();
				}
	       });
			
		addActionListener(i);	
		this.add(maps.get(i));
		}
	}
	
	public void addActionListener(int j) {
		
		switch (j) {
		case 0:// BACK
			maps.get(j).addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					
					SoundsProvider.playBulletHit1();
					cursorPosition = 1;
					repaint();
					getSwitcher().showPlayer();
				}
			});
			break;
		default: // Tutte le mappe
			maps.get(j).addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					
					SoundsProvider.playBulletHit1();
					int tmp = 0;
					
					if(path.contains("single"))
						tmp = ((MainFrame)switcher).getUnlockedMapsP1();
					else
						tmp = ((MainFrame)switcher).getUnlockedMapsP2();
							
					if(j <= tmp) {
					
						fileChooser.setCurrentDirectory(file);
						
						JTextField fileNameMap = new JTextField();
	                    fileNameMap.setText(path + "/stage" + j + ".txt");
	                    
	                    JTextField directory = new JTextField();
	                    directory.setText(fileChooser.getCurrentDirectory().toString());
	                    cursorPosition = 1;
	                    getSwitcher().showSlide(fileNameMap);
					}
					else {
						
						cursorPosition = j;
	                    repaint();
					}
				}
			});
			break;
		}
	}
			
	public void setBoundAndText(int j) {
				
		if(j == 0) {
			
			maps.get(j).setBounds(20, 20, 70, 30);
			maps.get(j).setText("Back");
		}
		else {
		
			if(j == 5 || j == 9) {
				
				posX = 200;
				posY += 225;
			}
			setLabel(j);
			maps.get(j).setBounds(posX, posY, 
					ImageProvider.getMapsP1().get(j - 1).getWidth(null), ImageProvider.getMapsP1().get(j - 1).getHeight(null));
					
			posX += 245;
		}
	}
	
	public void setLabel(int j){

		labelStage = new JLabel();
		labelStage.setText("Stage " + j);
		labelStage.setFont(MainFrame.customFontM);
		labelStage.setBackground(Color.GRAY);
		labelStage.setForeground(Color.BLACK);
		labelStage.setBounds(posX + 35, posY + 190, 105, 25);
		this.add(labelStage);
	}
	
	public void createArrowButton() {
				
		arrowRight.setBounds((int) this.getPreferredSize().getWidth() - 80 , (int) this.getPreferredSize().getHeight() / 2 - 15,
				ImageProvider.getArrowRight().getWidth(null), ImageProvider.getArrowRight().getHeight(null));
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
				 else 
					 if(e.getKeyCode() == KeyEvent.VK_LEFT) {
						 SoundsProvider.playBulletHit1();
						 maps.get(8).requestFocus();
						 cursorPosition = 8;
			     } 
				 else 
					 if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
						 SoundsProvider.playBulletHit1();
						 maps.get(9).requestFocus();
						 cursorPosition = 9;	
				}
				repaint();
			}
		});
		
		arrowRight.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				cursorPosition = 1;
				getSwitcher().showSecondStage(path);
			}
		});
		
		this.add(arrowRight);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//cursor + select
		if(cursorPosition > 0 ) {
			g.drawImage(ImageProvider.getSelectMap(), 
					maps.get(cursorPosition).getX() - 5, maps.get(cursorPosition).getY() - 5, null);
		}
		else 
			if(cursorPosition == -1) {
			 
				g.drawImage(ImageProvider.getCursorRight(), (int) this.getPreferredSize().getWidth() - 130 , 
					(int) this.getPreferredSize().getHeight() / 2 - 8, null);
		}
		else {
			
			g.drawImage(ImageProvider.getCursorLeft(), 
					maps.get(cursorPosition).getX() + 80,maps.get(cursorPosition).getY() - 8, this);	
		}
		
	if(path.contains("single")) { 
			
			for(int i = 0;i < maps.size(); i++) {
			
				if(i != 0) {
					
					if(i <= ((MainFrame)switcher).getUnlockedMapsP1())
						maps.get(i).setIcon(new ImageIcon(ImageProvider.getMapsP1().get(i - 1)));
					
					else
						maps.get(i).setIcon(new ImageIcon(ImageProvider.getLocked()));
				}
			}
		}
		else {
			
			for(int i = 0;i < maps.size(); i++) {
				
				if(i != 0) {
					
					if(i <= ((MainFrame)switcher).getUnlockedMapsP2())
						maps.get(i).setIcon(new ImageIcon(ImageProvider.getMapsP2().get(i - 1)));
					
					else
						maps.get(i).setIcon(new ImageIcon(ImageProvider.getLocked()));
				}
			}
		}

	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public JButton getButton(int i) {
		return maps.get(i);
	}
	
	public void setSwitcher(PanelSwitcher switcher) {
		this.switcher = switcher;
	}

	public PanelSwitcher getSwitcher() {
		return switcher;
	}

	public int getCursorPosition() {
		return cursorPosition;
	}

	public void setCursorPosition(int cursorPosition) {
		this.cursorPosition = cursorPosition;
	}
}
