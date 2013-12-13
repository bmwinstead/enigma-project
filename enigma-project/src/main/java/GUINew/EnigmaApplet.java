package main.java.GUINew;

import javax.swing.JApplet;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * The applet driver for the Enigma GUI, for running it as an applet instead
 * of as an app. 
 * 
 * @author Jessica Ikley
 * @author Team Enigma
 * @version 0.9
 * Dec 13, 2013
 *
 */
public class EnigmaApplet extends JApplet {
	
	public void init() {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					JPanel primary = new PrimaryGUIPanel();
					add(primary);
				}
			});
		} 
		catch (Exception e) {
			System.err.println("GUI creation failed.");
		}
	}

}
