/**
 * ResetPanel.java
 * @author - Jessica Ikley
 * @author - Team Enigma
 * @version - 0.9
 * @date - Dec 5, 2013
 */
package main.java.GUINew;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ResetPanel extends JPanel {
	private JButton defaultConfigButton;
	private JButton resetIndicatorsButton;
	private JButton clearTextButton;
	private EnigmaSingleton machine = EnigmaSingleton.INSTANCE;
	private char[] defaultRotorPositions = { '!', 'A', 'A', 'A' };
	private char[] defefaultRingSettings = { '!', 'A', 'A', 'A' };
	private int[] defaultRotors = { -1, 0, 1, 2 };
	private String defaultPlugboard = "";
	private int defaultReflector = 0;
	
	public ResetPanel() {
		GroupLayout mainLayout = new GroupLayout(this);
		setLayout(mainLayout);
		setBackground(Color.black);
		
		JLabel optionsLabel = new JLabel("Reset Options");
		optionsLabel.setForeground(Color.white);
		
		defaultConfigButton = new JButton("Default Configuration");
        defaultConfigButton.addActionListener(new DefaultConfigListener());
		resetIndicatorsButton = new JButton("Reset Indicators");
		clearTextButton = new JButton("Clear Text");
		
		mainLayout.setAutoCreateGaps(true);
		mainLayout.setHorizontalGroup(mainLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(optionsLabel)
				.addComponent(defaultConfigButton)
				.addComponent(resetIndicatorsButton)							
				.addComponent(clearTextButton));
		mainLayout.setVerticalGroup(mainLayout.createSequentialGroup()
				.addComponent(optionsLabel)
				.addComponent(defaultConfigButton)
				.addComponent(resetIndicatorsButton)
				.addComponent(clearTextButton));
		mainLayout.linkSize(SwingConstants.HORIZONTAL, defaultConfigButton,
				resetIndicatorsButton, clearTextButton);
	} // end constructor
	
	private class DefaultConfigListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
        	System.out.println("Look at me! I'm typing things!");
        	System.out.println("ResetPanel: Rotors: " + Arrays.toString(defaultRotors));
        	machine.setState(defaultRotors, defaultReflector, defefaultRingSettings, defaultRotorPositions, defaultPlugboard);
        	machine.notifyObservers();

        }
	}

} // end ResetPanel class
