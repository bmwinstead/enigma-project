package main.java.GUINew;

import javax.swing.JApplet;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

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
