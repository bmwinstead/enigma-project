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
 * Current implementation includes new mock ups for rotor panels and
 * plug board.
 */
public class Enigma {

    public static void main(String[] args) {
        new Enigma(); //Original GUI
        /*New Rotor Panel GUI
		RotorPanel r = new RotorPanel();
		JFrame frame = new JFrame();
		frame.add(r);
		frame.pack();
		frame.setVisible(true);
		new PlugBoardGUI().setLocation(0,200); //New PlugBoard GUI*/
    }
    
    public Enigma(){
        
        //Create the original GUI Panel      
        EnigmaGUI frame = new EnigmaGUI();
        frame.setVisible(true);

    }
    
    public void PlugBoardGUI(){
        
		//New PlugBoard GUI
		PlugBoardGUI p = new PlugBoardGUI();
		JFrame plugFrame = new JFrame();
		plugFrame.add(p);
		plugFrame.pack();
		plugFrame.setVisible(true);

    }
}
