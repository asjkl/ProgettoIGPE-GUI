package progettoIGPE.davide.giovanni.unical2016.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class LoadGamePanel extends JPanel{
	private PanelSwitcher switcher;
	private JTextField filename;
	
	LoadGamePanel(final int w, final int h, PanelSwitcher switcher, JTextField filename){
		
		SoundsProvider.playStageStart();
		this.setPreferredSize(new Dimension(w, h));
		this.setBackground(Color.DARK_GRAY);
		this.setLayout(null);
		this.setFilename(filename);
		
		JLabel label = new JLabel("a brutta copia");
		label.setForeground(Color.BLACK);
		label.setFont(MainFrame.customFontM);
		label.setBounds(500, 300, 200, 40);
		this.add(label);
		setSwitcher(switcher);
		
		Timer timer = new Timer(3000, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				getSwitcher().showGame(filename);
			}
		});
		
		timer.setRepeats(false);
		timer.start();
		}

		public PanelSwitcher getSwitcher() {
			return switcher;
		}

		public void setSwitcher(PanelSwitcher switcher) {
			this.switcher = switcher;
		}

		public JTextField getFilename() {
			return filename;
		}

		public void setFilename(JTextField filename) {
			this.filename = filename;
		}

}
