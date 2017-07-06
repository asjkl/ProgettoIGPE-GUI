package progettoIGPE.davide.giovanni.unical2016.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class LoadGamePanel extends JLayeredPane {

	private final int DELAY = 5;
	private final int DELTA_Y = 2;
	private Component oldComponent;
	boolean flag = false;
	private JLabel label;
	private PanelSwitcher panelSwitcher;
	private JTextField filename;
	private ArrayList<JPanel> panels = new ArrayList<>();

	public LoadGamePanel(final int w, final int h, PanelSwitcher panelSwitcher, JTextField filename) {
		
		SoundsProvider.playStageStart();
		this.setBackground(Color.BLACK);
		this.setPreferredSize(new Dimension(w, h));
		this.setLayout(null);
		this.setOpaque(true);
		
		label = new JLabel();
		this.add(label);
		this.filename = filename;
		this.setSwitcher(panelSwitcher);
		
		label.setFont(MainFrame.customFontB);
		System.out.println(filename.getText());
		label.setText("Stage " + filename.getText().subSequence(filename.getText().length()-5, filename.getText().length() - 4));
		label.setBounds((int) (getPreferredSize().getWidth() / 2) - 70,
				(int) getPreferredSize().getHeight() / 2-50, 300, 100);
		label.setBackground(Color.GRAY);
		label.setForeground(Color.BLACK);
		
		for(int i = 0; i < 2; i++) {
			
			panels.add(new JPanel());
			panels.get(i).setPreferredSize(new Dimension(w, h));
			panels.get(i).setBackground(Color.GRAY);
		}
		
		this.add(panels.get(0), panels.get(1));

	}

	public void add(Component component1, Component component2) {

		this.add(component1);
		this.add(component2);

		LoadGamePanel.putLayer((JComponent) component1, JLayeredPane.DRAG_LAYER);
		LoadGamePanel.putLayer((JComponent) component2, JLayeredPane.DRAG_LAYER);

		component1.setSize(getPreferredSize());
		component1.setLocation(0, -getPreferredSize().height);
		component2.setSize(getPreferredSize());
		component2.setLocation(0, getPreferredSize().height);
		
		slideTopAndBottom(component1, component2);
	}

	public void slideTopAndBottom(final Component component1,final Component component2) {

		final int tmp = -(getPreferredSize().height / 2);
	
		new Timer(DELAY, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				int y1 = component1.getY();
				int y2 = component2.getY();
				
				if (y1 >= tmp) {

					component1.setLocation(0, y1);
					component2.setLocation(0, y2);
					putLayer((JComponent) component1, JLayeredPane.DEFAULT_LAYER);
					putLayer((JComponent) component2, JLayeredPane.DEFAULT_LAYER);
					
					if(oldComponent != null)
						remove(oldComponent);
						
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						
						((Timer) e.getSource()).stop();
					
						getSwitcher().showGame(filename);
						
				} else {

					y1 += DELTA_Y;
					y2 -= DELTA_Y;
					component1.setLocation(0, y1);
					component2.setLocation(0, y2);
				}

				repaint();
			}
		}).start();
	}

	public PanelSwitcher getSwitcher() {
		return panelSwitcher;
	}

	public void setSwitcher(PanelSwitcher panelSwitcher) {
		this.panelSwitcher = panelSwitcher;
	}	
}