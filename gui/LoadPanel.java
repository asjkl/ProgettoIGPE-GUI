package progettoIGPE.davide.giovanni.unical2016.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.io.BufferedReader;
import java.io.File;
//import java.io.FileReader;
import java.io.IOException;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class LoadPanel extends JPanel {

	private PanelSwitcher switcher;
	private JProgressBar progressBar;
	public static Font customFontM;
	public static Font customFontB;
	public static Font customFontS;
	public static boolean transparent = false;
	private GraphicsEnvironment graphicscEnvironment;

	public LoadPanel(final int w, final int h, PanelSwitcher switcher) {
		
		this.setPreferredSize(new Dimension(w, h));
		this.setBackground(Color.BLACK);
		this.setLayout(null);
		
		new ImageProvider();
		new SoundsProvider();
		
		setSwitcher(switcher);
		setFont();
		setProgressBar();
		
		Timer timer = new Timer(7000, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				getSwitcher().showMenu();
			}
		});
		
		timer.setRepeats(false);
		timer.start();
	}	
	
	public void setProgressBar() {
		
		progressBar = new JProgressBar();
		progressBar.setBounds(1075, 665, 225, 50);
		progressBar.setOpaque(false);
		progressBar.setBorderPainted(false);
		this.add(progressBar, BorderLayout.NORTH);
	}
//	
//	public void readFile() {
//	    
//		try {
//	    
//	    	String s = "";
//	        File directory[] = new File[2];
//	        
//	        directory[0] = new File("./resource");
//	        directory[1] = new File("./sounds");
//	        
//	        FileReader fr = null;
//
//	        for(int i = 0; i < directory.length; i++) {
//	        
//	        	for(File fileEntry : directory[i].listFiles()) {
//		        
//		            //System.out.println(fileEntry.getName());
//		            fr = new FileReader(fileEntry);
//					BufferedReader br = new BufferedReader(fr);
//			        
//			        long readLength = 0;
//			        long totalLength = fileEntry.length();
//			        double lengthPerPercent = 100.0 / totalLength;
//			        
//			        while((s = br.readLine()) != null) {
//			       
//			            //progressBar.setValue((int) Math.round(lengthPerPercent * readLength));
//			            readLength += s.length();
//			        }
//			        
//			        Thread.sleep(20);
//			        int tmp = 100 - progressBar.getValue();
//			       // progressBar.setValue(progressBar.getValue() + tmp);
//	        	}
//	        }
//	        
//	        fr.close();
//	        
//	    }catch(Exception e){
//	    	e.printStackTrace();
//	    }
//		
//		getSwitcher().showMenu();
//	}
//	
	private void setFont(){
		
		try {
			
			customFontM = Font.createFont(Font.TRUETYPE_FONT, new File("./font/Minecraft.ttf")).deriveFont(25f);
			customFontB = Font.createFont(Font.TRUETYPE_FONT, new File("./font/Minecraft.ttf")).deriveFont(40f);
			customFontS = Font.createFont(Font.TRUETYPE_FONT, new File("./font/Minecraft.ttf")).deriveFont(16f);
			graphicscEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
			graphicscEnvironment.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("./font/Minecraft.ttf")));
		
		} catch(IOException e) {
			e.printStackTrace();
			
		} catch(FontFormatException e) {
			e.printStackTrace();
		}
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
