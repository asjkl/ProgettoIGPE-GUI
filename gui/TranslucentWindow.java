package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import core.GameManager;

@SuppressWarnings("serial")
public class TranslucentWindow extends JDialog {

	private Image image;
	private Timer timer;
	private JTextField filename;
	private JTextField directory;
	private PanelSwitcher switcher;
	
    public TranslucentWindow(PanelSwitcher switcher, JTextField filename, Image image) {
    	
    	this.setSwitcher(switcher) ;
    	this.setFilename(filename);
    	this.setDirectory(directory);
    	this.image = image;

    	setWindow();
    }
    
    public void setWindow() {

		this.setAlwaysOnTop(true);
		
		timer = new Timer(3000, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				dispose();
				((MainFrame)getSwitcher()).setTransparent(false);
				
				if(GameManager.offline) 
					getSwitcher().showScores(filename);
				else
					getSwitcher().showNetwork();
			}
		});
		
		timer.setRepeats(false);
		timer.start();
		
		this.setModal(true);
		this.setUndecorated(true);
		this.setBackground(new Color(0,0,0,0));
		this.setContentPane(new TranslucentPane());
		JLabel j=new JLabel(new ImageIcon(image));
		j.setLocation(getWidth()/2-(image.getWidth(null)/2), getHeight()/2-(image.getHeight(null)/2));
		this.add(j);
		this.pack();
		this.setLocationRelativeTo((MainFrame)switcher);
		this.setVisible(true);
    }
    
    private class TranslucentPane extends JPanel {

        public TranslucentPane() {
            this.setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); 

            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(AlphaComposite.SrcOver.derive(.0f));
            g2d.setColor(getBackground());
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
   
	public JTextField getFilename() {
		return filename;
	}

	public void setFilename(JTextField filename) {
		this.filename = filename;
	}

	public JTextField getDirectory() {
		return directory;
	}

	public void setDirectory(JTextField directory) {
		this.directory = directory;
	}
	
    public PanelSwitcher getSwitcher() {
		return switcher;
	}
    
    public void setSwitcher(PanelSwitcher switcher) {
    	this.switcher = switcher;
    }

}