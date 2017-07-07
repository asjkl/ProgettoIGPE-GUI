package progettoIGPE.davide.giovanni.unical2016.GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

@SuppressWarnings("serial")
public class TranslucentWindow extends JDialog {

	private JTextField filename;
	private JTextField directory;
	private PanelSwitcher switcher;
	private String value;
	private Image image;
	
    public TranslucentWindow(PanelSwitcher switcher, JTextField filename, String value, Image image) {
    	
    	this.setSwitcher(switcher) ;
    	this.setFilename(filename);
    	this.setDirectory(directory);
    	this.value = value;
    	this.image = image;

    	setWindow();
    }
    
    public void setWindow() {

		this.setAlwaysOnTop(true);
		
		Timer timer = new Timer(3000, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				dispose();
				((MainFrame)getSwitcher()).setTransparent(false);
				getSwitcher().showScores(value);
			}
		});
		
		timer.setRepeats(false);
		timer.start();
		this.setModal(true);
		this.setUndecorated(true);
		this.setBackground(new Color(0,0,0,0));
		this.setContentPane(new TranslucentPane());
		this.add(new JLabel(new ImageIcon(image)));
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