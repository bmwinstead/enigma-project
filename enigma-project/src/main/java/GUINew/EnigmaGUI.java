package main.java.GUINew;

import javax.swing.GroupLayout;
import javax.swing.JFrame;

public class EnigmaGUI {
	private static RotorPanel rotorPanel;
	private static JFrame mainFrame;
	private static IOPanel ioPanel;
	public static void main(String[] args){
		mainFrame = new JFrame();
		GroupLayout frameLayout = new GroupLayout(mainFrame.getContentPane());
		mainFrame.getContentPane().setLayout(frameLayout);
		
		rotorPanel = new RotorPanel();
		ioPanel = new IOPanel();
		frameLayout.setHorizontalGroup(frameLayout.createParallelGroup()
				.addComponent(rotorPanel)
				.addComponent(ioPanel));
		frameLayout.setVerticalGroup(frameLayout.createSequentialGroup()
				.addComponent(rotorPanel)
				.addComponent(ioPanel));
		mainFrame.setTitle("CMSC495 Enigma Machine");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.pack();
		mainFrame.setVisible(true);
//		System.out.println(s);
	}
}
