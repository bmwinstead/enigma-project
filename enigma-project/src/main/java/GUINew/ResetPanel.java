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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ResetPanel extends JPanel {
	private JButton defaultConfigButton;
	private JButton resetIndicatorsButton;
	private JButton clearTextButton;
	private JComboBox<String> outSpaceDropdown;
	private static final String[] outSpaceChoices = {
			"No Spaces",
			"4 Spaces", 
			"5 Spaces",
			"Original Spaces"
	};
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
		
		JLabel outSpaceLabel = new JLabel("Output Space Options");
		outSpaceLabel.setForeground(Color.white);
		JLabel optionsLabel = new JLabel("Reset Options");
		optionsLabel.setForeground(Color.white);
		
		outSpaceDropdown = new JComboBox<String>(outSpaceChoices);
		outSpaceDropdown.setSelectedIndex(0);
		outSpaceDropdown.addActionListener(new SpaceDropdownListener());
		defaultConfigButton = new JButton("Default Configuration");
        defaultConfigButton.addActionListener(new DefaultConfigListener());
		resetIndicatorsButton = new JButton("Reset Indicators");
		resetIndicatorsButton.addActionListener(new ResetIndicatorsListener());
		clearTextButton = new JButton("Clear Text");
		clearTextButton.addActionListener(new ClearTextListener());	
		
		mainLayout.setAutoCreateGaps(true);
		mainLayout.setHorizontalGroup(mainLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(outSpaceLabel)
				.addComponent(outSpaceDropdown)
				.addComponent(optionsLabel)
				.addComponent(defaultConfigButton)
				.addComponent(resetIndicatorsButton)							
				.addComponent(clearTextButton));
		mainLayout.setVerticalGroup(mainLayout.createSequentialGroup()
				.addComponent(outSpaceLabel)
				.addComponent(outSpaceDropdown)
				.addComponent(optionsLabel)
				.addComponent(defaultConfigButton)
				.addComponent(resetIndicatorsButton)
				.addComponent(clearTextButton)				);
		mainLayout.linkSize(SwingConstants.HORIZONTAL, defaultConfigButton,
				resetIndicatorsButton, clearTextButton, outSpaceDropdown);
		mainLayout.linkSize(SwingConstants.VERTICAL, defaultConfigButton,
				resetIndicatorsButton, clearTextButton, outSpaceDropdown);
	} // end constructor
	
	private class DefaultConfigListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println("ResetPanel: Rotors: "
					+ Arrays.toString(defaultRotors));
			machine.setUpdateType(EnigmaSingleton.FULLRESET);
			machine.setState(defaultRotors, defaultReflector,
					defefaultRingSettings, defaultRotorPositions,
					defaultPlugboard);
			machine.notifyObservers();

		}
	} // end DefaultConfigListener class
	
	private class ResetIndicatorsListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			machine.indicatorReset();
		}
	} // end ResetIndicatorsListener
	
	private class ClearTextListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			machine.setUpdateType(EnigmaSingleton.CLEARTEXT);
			machine.notifyObservers();
		}
	}
	
	private class SpaceDropdownListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println("Setting space option: " 
					+ outSpaceDropdown.getSelectedIndex());
			machine.setSpacesOption(outSpaceDropdown.getSelectedIndex());
		}
	}

} // end ResetPanel class
