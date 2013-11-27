package main.java.GUI;

import javax.swing.JFrame;

/**
 * 
 * @author Ellen Ohlmacher
 * @author Team Enigma
 * @version 0.7
 * 
 * A driver to create the Enigma GUI panel. Currently this works as an app,
 * but the team intends to have both app and applet options in the future. 
 *
 */
public class Enigma {

    public static void main(String[] args) {
        new Enigma();
		RotorPanel r = new RotorPanel();
		JFrame frame = new JFrame();
		frame.add(r);
		frame.pack();
		frame.setVisible(true);
    }
    
    public Enigma(){
        
        //Create the GUI Panel      
        EnigmaGUI frame = new EnigmaGUI();
        frame.setVisible(true);

    }
}
