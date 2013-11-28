package main.java.GUINew;
import java.awt.GridLayout;

import javax.swing.JFrame;
public class EnigmaGUI {
	private static RotorPanel rotorPanel;
	private static PlugboardDialog plugboard;
	private static JFrame mainFrame;
	private static IOPanel ioPanel;
	public static void main(String[] args){
		mainFrame = new JFrame();
		mainFrame.setLayout(new GridLayout(0,1));
		rotorPanel = new RotorPanel();
		plugboard = new PlugboardDialog();
		ioPanel = new IOPanel();
		mainFrame.add(rotorPanel);
		mainFrame.add(ioPanel);
		mainFrame.setTitle("CMSC495 Enigma Machine");
		mainFrame.pack();
		mainFrame.setVisible(true);
		ioPanel.turnOnLight("testing");
//		String s = plugboard.displayDialog();
//		System.out.println(s);
	}
}
