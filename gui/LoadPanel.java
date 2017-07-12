package progettoIGPE.davide.giovanni.unical2016.GUI;

import java.awt.BorderLayout;
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
		this.setLayout(new BorderLayout());
		this.setBackground(Color.BLACK);
		this.setLayout(null);
		setSwitcher(switcher);
		setProgressBar();
//		((MainFrame)switcher).setUndecorated(true);
		((MainFrame)switcher).setVisible(true);
	}	
	
	public void setProgressBar() {
		
		progressBar = new JProgressBar();
		progressBar.setBounds(1100, 700, 225, 50);
		progressBar.setOpaque(false);
		progressBar.setBorderPainted(false);
		this.add(progressBar, BorderLayout.NORTH);
	}
  	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(ImageProvider.getBattleCity(), 450, 250, null);
		g.drawImage(ImageProvider.getLoading(), progressBar.getX(), progressBar.getY(), this);
	}
	
	public PanelSwitcher getSwitcher() {
		return switcher;
	}

	public void setSwitcher(PanelSwitcher switcher) {
		this.switcher = switcher;
	}
}
