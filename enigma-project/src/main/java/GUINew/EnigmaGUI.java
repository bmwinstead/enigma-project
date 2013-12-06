package main.java.GUINew;

import java.awt.Color;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * 
 * @author Team Enigma
 * @version 0.9
 * @date Nov 30, 2013
 * 
 *       Driver/outer GUI to contain and organize the Enigma GUI elements.
 * 
 */
public class EnigmaGUI {
	private static RotorPanel rotorPanel;
	private static ResetPanel resetPanel;
	private static JPanel mainPanel;
	private static JFrame mainFrame;
	private static IOPanel ioPanel;
	private static JTabbedPane tabs;
	private static CaGuiPrototype caGUI;
	private static JPanel tabsPanel;
	
	public static void main(String[] args){
		rotorPanel = new RotorPanel();
		ioPanel = new IOPanel();
		resetPanel = new ResetPanel();
		mainPanel = new JPanel();
		mainFrame = new JFrame();
		
		GroupLayout mainPanelLayout = new GroupLayout(mainPanel);
		mainPanel.setLayout(mainPanelLayout);
		mainPanel.setBackground(Color.black);
		mainPanelLayout.setHorizontalGroup(mainPanelLayout.createSequentialGroup()
				.addGroup(mainPanelLayout.createParallelGroup()
						.addComponent(resetPanel)
				)
				.addGroup(mainPanelLayout.createParallelGroup()
						.addComponent(rotorPanel)
						.addComponent(ioPanel)
				)
		);
		mainPanelLayout.setVerticalGroup(mainPanelLayout.createParallelGroup()
				.addGroup(mainPanelLayout.createSequentialGroup()
						.addComponent(resetPanel)
				)
				.addGroup(mainPanelLayout.createSequentialGroup()
						.addComponent(rotorPanel)
						.addComponent(ioPanel)
				)
		);
		mainPanelLayout.setAutoCreateContainerGaps(true);
		mainPanelLayout.setAutoCreateGaps(true);
		
		tabs = new JTabbedPane();
		caGUI = new CaGuiPrototype();
		tabs.addTab("Main",mainPanel);
		tabs.addTab("Cryptanalysis",caGUI);
		tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tabsPanel = new JPanel();
		tabsPanel.add(tabs);
		mainFrame.add(tabsPanel);
		mainFrame.setTitle("CMSC495 Enigma Machine");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.pack();
		mainFrame.setVisible(true);
//		System.out.println(s);
	} // end main method
} // end EnigmaGUI class
