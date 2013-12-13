package main.java.GUINew;

import java.awt.Color;

import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * The primary panel for the EnigmaGUI. Contains and organize the Enigma GUI 
 * elements. The Enigma GUI interface allows users to encrypt and decrypt 
 * messages using a simulation of the Enigma devices used by the Germans 
 * during World War II. 
 * 
 * @author Team Enigma
 * @version 0.9
 * Dec 13, 2013
 *
 */
public class PrimaryGUIPanel extends JPanel {
	private static RotorPanel rotorPanel;
	private static ResetPanel resetPanel;
	private static JPanel mainPanel;
	private static IOPanel ioPanel;
	private static JTabbedPane tabs;
	private static CaGuiPrototype caGUI;
	private static JPanel tabsPanel;
	
	PrimaryGUIPanel(){
		rotorPanel = new RotorPanel();
		ioPanel = new IOPanel();
		resetPanel = new ResetPanel();
		mainPanel = new JPanel();
		
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
		this.add(tabsPanel);
	} // end constructor
}
