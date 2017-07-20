package progettoIGPE.davide.giovanni.unical2016.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

@SuppressWarnings("serial")
public class LoadPanel extends JPanel {

	private JProgressBar progressBar;
	private PanelSwitcher switcher;
	
	public LoadPanel(final int w, final int h, PanelSwitcher switcher) {
		
		this.setPreferredSize(new Dimension(w, h));
		this.setBackground(Color.BLACK);
		this.setLayout(null);
		setSwitcher(switcher);
		setProgressBar(w, h);
	} 
	 
	public void setProgressBar(int w, int h) {
		
		progressBar = new JProgressBar();
		progressBar.setBounds(w - 190, h - 50,
				ImageProvider.getLoading().getWidth(null), 
				ImageProvider.getLoading().getHeight(null));
		progressBar.setOpaque(false);
		progressBar.setBorderPainted(false);
		this.add(progressBar);
	}
  	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawImage(ImageProvider.getBattleCity(), (this.getWidth() / 2) - (ImageProvider.getBattleCity().getWidth(null) / 2),
				(this.getHeight() / 2) - (ImageProvider.getBattleCity().getHeight(null) / 2), null);
		g.drawImage(ImageProvider.getLoading(), progressBar.getX(), progressBar.getY(), this);
	}
	
	public PanelSwitcher getSwitcher() {
		return switcher;
	}

	public void setSwitcher(PanelSwitcher switcher) {
		this.switcher = switcher;
	}

}
