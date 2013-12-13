package main.java.GUINew;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Driver for the EnigmaGUI, so that it can be used as an app in addition to
 * run as an applet. 
 * 
 * @author Team Enigma
 * @version 0.9
 * Nov 30, 2013
 */
public class EnigmaGUI {
	private static JPanel mainPanel;
	private static JFrame mainFrame;
	
	public static void main(String[] args){
		
		mainPanel = new PrimaryGUIPanel();
		mainFrame = new JFrame();
		mainFrame.add(mainPanel);
		mainFrame.setTitle("CMSC495 Enigma Machine");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.pack();
		mainFrame.setVisible(true); 
	} // end main method
} // end EnigmaGUI class
