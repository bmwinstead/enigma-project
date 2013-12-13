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
public class EnigmaGUI extends JFrame {
	private static JPanel mainPanel;
	
	public static void main(String[] args){
		EnigmaGUI app = new EnigmaGUI();
		app.createGUI();
	} // end main method
	
	/**
	 * GUI design moved here in order to allow for Webstart. 
	 */
	private void createGUI() {
		// Create and set up the content pane.
		mainPanel = new PrimaryGUIPanel();
		this.add(mainPanel);
		this.setTitle("CMSC495 Enigma Machine");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true); 
		
	}
} // end EnigmaGUI class
