package progettoIGPE.davide.giovanni.unical2016.GUI;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class WarningDialog extends JDialog{
	JPanel p;
	JLabel l;
	
	public WarningDialog(String text, TypeMatrix[][] matrix) {
		init(text);

		Timer timer = new Timer(3000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				matrix[0][0]=matrix[0][(matrix[0].length)/2]=matrix[0][(matrix[0].length)-1]=TypeMatrix.EMPTY;
				MainFrame.transparent = false;
			}
		});
		timer.setRepeats(false);
		timer.start();
		setVisible(true);
	}
	
	public WarningDialog(String text){
		init(text);
		Timer timer = new Timer(3000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				MainFrame.transparent = false;
			}
		});
		timer.setRepeats(false);
		timer.start();
		setVisible(true);
	}
	
	private void init(String text){
		p = new JPanel();
		l = new JLabel(text);
		p.setBackground(Color.BLACK);
		p.setBorder(BorderFactory.createLineBorder(Color.RED));
		l.setForeground(Color.WHITE);
		l.setFont(MainFrame.customFontM);
		p.add(l);
		
		add(p);
		setModal(true);
		setUndecorated(true);
		setContentPane(p);
		pack();
		setLocationRelativeTo(this);
	}
}
