package progettoIGPE.davide.giovanni.unical2016.GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class SlideContainer extends JLayeredPane {

	private final int DELAY = 5;
	private final int DELTA_X = 2;
	private Component oldComponent;
	private static boolean ready;

	public SlideContainer(final int w, final int h) {
		
		this.setBackground(Color.BLACK);
		this.setSize(new Dimension(w, h));
		this.setLayout(null);
		this.setOpaque(true);
		setReady(false);
	}

	@Override
	public Component add(Component component) {

		Component[] components = this.getComponents();

		if(components.length > 0) {
			oldComponent = components[0];
		} 
		else 
			if(oldComponent == component) {
				return super.add(component);
		} 
		else 
			if(oldComponent != null) {
				SlideContainer.putLayer((JComponent) oldComponent, JLayeredPane.DEFAULT_LAYER);
		}

			 Component returnResult = super.add(component);

		SlideContainer.putLayer((JComponent) component, JLayeredPane.DRAG_LAYER);

		component.setSize(getPreferredSize());
		component.setLocation(0, getPreferredSize().height);
		slideFromBottom(component);
		
		return returnResult;
	}

	public void slideFromBottom(final Component component) {

		new Timer(DELAY, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				int y = component.getY();

				if (y <= 0) {

					component.setLocation(0, 0);
					putLayer((JComponent) component, JLayeredPane.DEFAULT_LAYER);

					if(oldComponent != null)
						remove(oldComponent);
						
						setReady(true);
						((Timer) e.getSource()).stop();
					
				} else {

					y -= DELTA_X;
					component.setLocation(0, y);
				}

				repaint();
			}
		}).start();
	}

	public static boolean isReady() {
		return ready;
	}

	public static void setReady(boolean ready) {
		SlideContainer.ready = ready;
	}
	
//	@Override
//	protected void paintComponent(Graphics g) {
//		// TODO Auto-generated method stub
//		super.paintComponent(g);
//	}
}
