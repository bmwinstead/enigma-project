package main.java.GUINew;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class RotorPanel extends JPanel implements Observer {
	// Constants
	private static final String[] rotorChoices = { "ROTOR I", "ROTOR II",
			"ROTOR III", "ROTOR IV", "ROTOR V", "ROTOR VI", "ROTOR VII",
			"ROTOR VIII" };
	private static final String[] fourthRotorChoices = { " ", "BETA", "GAMMA" };
	private static final String[] reflectorChoices = { "REFLECTOR B",
			"REFLECTOR C", "REFLECTOR B THIN", "REFLECTOR C THIN" };
	private static final String[] letterChoices = { "A", "B", "C", "D", "E",
			"F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
			"S", "T", "U", "V", "W", "X", "Y", "Z" };
	private static final String[] fourthLetterChoices = { " ", "A", "B", "C",
			"D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
			"Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

	// Machine state given by current choices. Initialized to default.
	private int[] rotors = { -1, 0, 1, 2 };
	private int reflector = 0;
	private char[] ringSettings = { '!', 'A', 'A', 'A' };
	private char[] rotorPositions = { '!', 'A', 'A', 'A' };
	private String pbString = null;
	// Components
	private JLabel fourthRotor;
	private JLabel leftRotor;
	private JLabel middleRotor;
	private JLabel rightRotor;
	private JLabel reflectorLabel;
	private JLabel ringSettingsLabel;
	private JLabel rotorPositionsLabel;
	private JComboBox<String> fourthRotorChoice;
	private JComboBox<String> leftRotorChoice;
	private JComboBox<String> middleRotorChoice;
	private JComboBox<String> rightRotorChoice;
	private JComboBox<String> reflectorChoice;
	private JComboBox<String> fourthRotorRingSetting;
	private JComboBox<String> leftRotorRingSetting;
	private JComboBox<String> middleRotorRingSetting;
	private JComboBox<String> rightRotorRingSetting;
	private JTextField pbField;
	private JButton pbButton;
	private JButton resetButton;
	private EnigmaSpinner fourthRotorPosition;
	private EnigmaSpinner leftRotorPosition;
	private EnigmaSpinner middleRotorPosition;
	private EnigmaSpinner rightRotorPosition;
	private PlugboardDialog pbDialog;
	private EnigmaSingleton machine = EnigmaSingleton.INSTANCE;

	// Constructor... puts the thing together.
	public RotorPanel() {
		machine.setState(rotors, reflector, ringSettings, rotorPositions,
				pbString);
		machine.addObserver(this);
		pbDialog = new PlugboardDialog();
		JPanel topPanel = buildTopPanel();
		JPanel plugboardPanel = buildPlugboardPanel();
		GroupLayout thisLayout = new GroupLayout(this);
		this.setLayout(thisLayout);
		thisLayout.setHorizontalGroup(thisLayout.createParallelGroup()
				.addComponent(topPanel).addComponent(plugboardPanel));
		thisLayout.setVerticalGroup(thisLayout.createSequentialGroup()
				.addComponent(topPanel).addComponent(plugboardPanel));
	}

	private JPanel buildTopPanel() {
		JPanel topPanel = new JPanel();
		GroupLayout topLayout = new GroupLayout(topPanel);
		topLayout.setAutoCreateGaps(true);
		topLayout.setAutoCreateContainerGaps(true);
		topPanel.setLayout(topLayout);
		topPanel.setBackground(Color.black);

		reflectorLabel = new JLabel("Reflector");
		reflectorLabel.setForeground(Color.white);
		reflectorLabel.setBackground(Color.black);

		fourthRotor = new JLabel("Rotor 4");
		fourthRotor.setForeground(Color.white);
		fourthRotor.setBackground(Color.black);

		leftRotor = new JLabel("Rotor 3");
		leftRotor.setForeground(Color.white);
		leftRotor.setBackground(Color.black);

		middleRotor = new JLabel("Rotor 2");
		middleRotor.setForeground(Color.white);
		middleRotor.setBackground(Color.black);

		rightRotor = new JLabel("Rotor 1");
		rightRotor.setForeground(Color.white);
		rightRotor.setBackground(Color.black);

		// Reflector Box
		reflectorChoice = new JComboBox<String>(reflectorChoices);
		reflectorChoice.setSelectedIndex(0);
		reflectorChoice.setActionCommand("reflectorChoice");
		reflectorChoice.addActionListener(new RotorListener());

		fourthRotorChoice = new JComboBox<String>(fourthRotorChoices);
		fourthRotorChoice.setSelectedIndex(0);
		fourthRotorChoice.setActionCommand("fourthRotorChoice");
		fourthRotorChoice.addActionListener(new RotorListener());

		leftRotorChoice = new JComboBox<String>(rotorChoices);
		leftRotorChoice.setSelectedIndex(0);
		leftRotorChoice.setActionCommand("leftRotorChoice");
		leftRotorChoice.addActionListener(new RotorListener());

		middleRotorChoice = new JComboBox<String>(rotorChoices);
		middleRotorChoice.setSelectedIndex(1);
		middleRotorChoice.setActionCommand("middleRotorChoice");
		middleRotorChoice.addActionListener(new RotorListener());

		rightRotorChoice = new JComboBox<String>(rotorChoices);
		rightRotorChoice.setSelectedIndex(2);
		rightRotorChoice.setActionCommand("rightRotorChoice");
		rightRotorChoice.addActionListener(new RotorListener());

		ringSettingsLabel = new JLabel("Ring Settings");
		ringSettingsLabel.setForeground(Color.white);
		ringSettingsLabel.setBackground(Color.black);

		// Ring Setting Boxes
		fourthRotorRingSetting = new JComboBox<String>(fourthLetterChoices);
		fourthRotorRingSetting.setSelectedIndex(0);
		fourthRotorRingSetting.setActionCommand("fourthRotorRingSetting");
		fourthRotorRingSetting.addActionListener(new RingSettingsListener());

		leftRotorRingSetting = new JComboBox<String>(letterChoices);
		leftRotorRingSetting.setSelectedIndex(0);
		leftRotorRingSetting.setActionCommand("leftRotorRingSetting");
		leftRotorRingSetting.addActionListener(new RingSettingsListener());

		middleRotorRingSetting = new JComboBox<String>(letterChoices);
		middleRotorRingSetting.setSelectedIndex(0);
		middleRotorRingSetting.setActionCommand("middleRotorRingSetting");
		middleRotorRingSetting.addActionListener(new RingSettingsListener());

		rightRotorRingSetting = new JComboBox<String>(letterChoices);
		rightRotorRingSetting.setSelectedIndex(0);
		rightRotorRingSetting.setActionCommand("rightRotorRingSetting");
		rightRotorRingSetting.addActionListener(new RingSettingsListener());

		rotorPositionsLabel = new JLabel("Rotor Positions");
		rotorPositionsLabel.setForeground(Color.white);
		rotorPositionsLabel.setBackground(Color.black);

		// Rotor Position Spinners
		fourthRotorPosition = new EnigmaSpinner();
		fourthRotorPosition.setModel(new SpinnerListModel(fourthLetterChoices));
		fourthRotorPosition.setValue(" ");
		fourthRotorPosition.identifier = "fourthRotorPosition";
		fourthRotorPosition.addChangeListener(new PositionsListener());

		leftRotorPosition = new EnigmaSpinner();
		leftRotorPosition.setModel(new SpinnerListModel(letterChoices));
		leftRotorPosition.setValue("A");
		leftRotorPosition.addChangeListener(new PositionsListener());
		leftRotorPosition.identifier = "leftRotorPosition";

		middleRotorPosition = new EnigmaSpinner();
		middleRotorPosition.setModel(new SpinnerListModel(letterChoices));
		middleRotorPosition.setValue("A");
		middleRotorPosition.addChangeListener(new PositionsListener());
		middleRotorPosition.identifier = "middleRotorPosition";

		rightRotorPosition = new EnigmaSpinner();
		rightRotorPosition.setModel(new SpinnerListModel(letterChoices));
		rightRotorPosition.setValue("A");
		rightRotorPosition.addChangeListener(new PositionsListener());
		rightRotorPosition.identifier = "rightRotorPosition";

		int min = 20;
		int pref = 30;
		int max = 40;
		// LAYOUT CODE AWWW YEAH
		topLayout
				.setHorizontalGroup(topLayout
						.createSequentialGroup()
						.addGroup(
								topLayout.createParallelGroup()
										.addComponent(reflectorLabel)
										.addComponent(reflectorChoice)
										.addComponent(ringSettingsLabel)
										.addComponent(rotorPositionsLabel))
						.addGroup(
								topLayout
										.createParallelGroup(
												GroupLayout.Alignment.CENTER)
										.addComponent(fourthRotor)
										.addComponent(fourthRotorChoice)
										.addComponent(fourthRotorRingSetting,
												min, pref, max)
										.addComponent(fourthRotorPosition, min,
												pref, max))
						.addGroup(
								topLayout
										.createParallelGroup(
												GroupLayout.Alignment.CENTER)
										.addComponent(leftRotor)
										.addComponent(leftRotorChoice)
										.addComponent(leftRotorRingSetting,
												min, pref, max)
										.addComponent(leftRotorPosition, min,
												pref, max))
						.addGroup(
								topLayout
										.createParallelGroup(
												GroupLayout.Alignment.CENTER)
										.addComponent(middleRotor)
										.addComponent(middleRotorChoice)
										.addComponent(middleRotorRingSetting,
												min, pref, max)
										.addComponent(middleRotorPosition, min,
												pref, max))
						.addGroup(
								topLayout
										.createParallelGroup(
												GroupLayout.Alignment.CENTER)
										.addComponent(rightRotor)
										.addComponent(rightRotorChoice)
										.addComponent(rightRotorRingSetting,
												min, pref, max)
										.addComponent(rightRotorPosition, min,
												pref, max)));
		topLayout
				.setVerticalGroup(topLayout
						.createSequentialGroup()
						.addGroup(
								topLayout.createParallelGroup()
										.addComponent(reflectorLabel)
										.addComponent(fourthRotor)
										.addComponent(leftRotor)
										.addComponent(middleRotor)
										.addComponent(rightRotor))
						.addGroup(
								topLayout.createParallelGroup()
										.addComponent(reflectorChoice)
										.addComponent(fourthRotorChoice)
										.addComponent(leftRotorChoice)
										.addComponent(middleRotorChoice)
										.addComponent(rightRotorChoice))
						.addGroup(
								topLayout
										.createParallelGroup(
												GroupLayout.Alignment.CENTER)
										.addComponent(ringSettingsLabel)
										.addComponent(fourthRotorRingSetting,
												min, pref, max)
										.addComponent(leftRotorRingSetting,
												min, pref, max)
										.addComponent(middleRotorRingSetting,
												min, pref, max)
										.addComponent(rightRotorRingSetting,
												min, pref, max))
						.addGroup(
								topLayout
										.createParallelGroup(
												GroupLayout.Alignment.CENTER)
										.addComponent(rotorPositionsLabel)
										.addComponent(fourthRotorPosition, min,
												pref, max)
										.addComponent(leftRotorPosition, min,
												pref, max)
										.addComponent(middleRotorPosition, min,
												pref, max)
										.addComponent(rightRotorPosition, min,
												pref, max)));
		return topPanel;
	}

	private JPanel buildPlugboardPanel() {
		JPanel plugboardPanel = new JPanel();
		plugboardPanel.setBackground(Color.black);
		pbField = new JTextField(30);
		pbField.setEditable(false);
		pbButton = new JButton("Plugboard Settings");
		pbButton.addActionListener(new ButtonListener());
		pbButton.setActionCommand("Plugboard");
		resetButton = new JButton("Reset Plugboard");
		resetButton.addActionListener(new ButtonListener());
		resetButton.setActionCommand("Reset");
		GroupLayout plugboardLayout = new GroupLayout(plugboardPanel);

		plugboardLayout.setHorizontalGroup(plugboardLayout
				.createParallelGroup().addComponent(pbField)
				.addComponent(pbButton).addComponent(resetButton));
		plugboardLayout.setVerticalGroup(plugboardLayout
				.createSequentialGroup().addComponent(pbField)
				.addComponent(pbButton).addComponent(resetButton));
		plugboardLayout.linkSize(pbButton, resetButton);
		return plugboardPanel;
	}



	private void printState() {
		System.out.println("Printing state of RotorPanel.java");
		System.out.println("Rotors: " + Arrays.toString(rotors));
		System.out.println("Reflector: " + reflector);
		System.out.println("Ring settings: " + Arrays.toString(ringSettings));
		System.out.println("Rotor positions: "
				+ Arrays.toString(rotorPositions));
		System.out.println("Plugboard: " + pbString);
	}

	public void setRotorPositions(char[] positions) {
		System.out.println(String.valueOf(positions));
		if (positions.length == 4 && positions[0] != '!') {
			System.out.println("4");
			fourthRotorPosition.setValue(String.valueOf(positions[0]));
			leftRotorPosition.setValue(String.valueOf(positions[1]));
			middleRotorPosition.setValue(String.valueOf(positions[2]));
			rightRotorPosition.setValue(String.valueOf(positions[3]));
		} else {
			System.out.println("3");
			leftRotorPosition.setValue(String.valueOf(positions[0]));
			middleRotorPosition.setValue(String.valueOf(positions[1]));
			rightRotorPosition.setValue(String.valueOf(positions[2]));
		}
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		String s = (String) arg1;
		System.out.println("Changing rotors to " + s);
		setRotorPositions(s.toCharArray());
	}
	
	private class RingSettingsListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			@SuppressWarnings("rawtypes")
			JComboBox temp = (JComboBox) e.getSource();
			System.out
					.println("Action registered on ring settings combo boxes, dumping state before");
			printState();
			switch (temp.getActionCommand()) {
			case "fourthRotorRingSetting":
				if (fourthRotorChoice.getSelectedIndex() == 0) {
					// no fourth rotor
					fourthRotorRingSetting.setSelectedIndex(0);
					ringSettings[0] = '!';
				} else if (temp.getSelectedIndex() == 0) {
					fourthRotorRingSetting.setSelectedIndex(1);
					ringSettings[0] = temp.getSelectedItem().toString()
							.charAt(0);
				} else {
					ringSettings[0] = temp.getSelectedItem().toString()
							.charAt(0);
				}
				break;
			case "leftRotorRingSetting":
				ringSettings[1] = temp.getSelectedItem().toString().charAt(0);
				break;
			case "middleRotorRingSetting":
				ringSettings[2] = temp.getSelectedItem().toString().charAt(0);
				break;
			case "rightRotorRingSetting":
				ringSettings[3] = temp.getSelectedItem().toString().charAt(0);
				break;
			}
			System.out.println("All changes performed, dumping state");
			printState();
			machine.setState(rotors, reflector, ringSettings);
		}
	}

	private class RotorListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			@SuppressWarnings("rawtypes")
			JComboBox temp = (JComboBox) e.getSource();
			int leftIndex = leftRotorChoice.getSelectedIndex();
			int middleIndex = middleRotorChoice.getSelectedIndex();
			int rightIndex = rightRotorChoice.getSelectedIndex();
			JFrame tempFrame = new JFrame();
			System.out
					.println("Action registered on rotor combo boxes, dumping state before");
			printState();
			switch (e.getActionCommand()) {
			case "fourthRotorChoice":
				if (temp.getSelectedIndex() == 0) {
					fourthRotorRingSetting.setSelectedIndex(0);
					rotors[0] = -1;
					fourthRotorPosition.setValue(" ");
					reflectorChoice.setSelectedIndex(0);
				} else {
					rotors[0] = temp.getSelectedIndex() + 7;
					fourthRotorRingSetting.setSelectedIndex(1);
					fourthRotorPosition.setValue("A");
					reflectorChoice.setSelectedIndex(2);
				}
				break;
			case "leftRotorChoice":
				if (leftIndex == middleIndex || leftIndex == rightIndex) {
					JOptionPane.showMessageDialog(tempFrame,
							"You cannot reuse rotor choices");
					leftRotorChoice.setSelectedIndex(rotors[1]);
				} else
					rotors[1] = temp.getSelectedIndex();
				break;
			case "middleRotorChoice":
				if (leftIndex == middleIndex || middleIndex == rightIndex) {
					JOptionPane.showMessageDialog(tempFrame,
							"You cannot reuse rotor choices");
					middleRotorChoice.setSelectedIndex(rotors[2]);
				} else
					rotors[2] = temp.getSelectedIndex();
				break;
			case "rightRotorChoice":
				if (rightIndex == middleIndex || leftIndex == rightIndex) {
					JOptionPane.showMessageDialog(tempFrame,
							"You cannot reuse rotor choices");
					rightRotorChoice.setSelectedIndex(rotors[3]);
				} else
					rotors[3] = temp.getSelectedIndex();
				break;
			case "reflectorChoice":
				if (fourthRotorChoice.getSelectedIndex() == 0) {
					// no fourth rotor, need 0 or 1.
					if (temp.getSelectedIndex() == 2
							|| temp.getSelectedIndex() == 3) {
						JOptionPane
								.showMessageDialog(tempFrame,
										"With the fourth rotor inactive, you can only choose reflector B or C");
						reflectorChoice.setSelectedIndex(0); // default
					} // end if
					else {
						reflector = temp.getSelectedIndex();
					} // end else
				} // end if
				else {
					// fourth rotor, need 2 or 3
					if (temp.getSelectedIndex() == 0
							|| temp.getSelectedIndex() == 1) {
						JOptionPane
								.showMessageDialog(tempFrame,
										"With the fourth rotor active, you can only choose reflector B thin or C thin");
						reflectorChoice.setSelectedIndex(2);
					} // end if
					else {
						reflector = temp.getSelectedIndex();
					} // end else
				} // end else
				break;
			}
			System.out.println("All changes performed. Dumping state after");
			printState();
			machine.setState(rotors, reflector, ringSettings);
		}
	}

	private class PositionsListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent arg0) {
			EnigmaSpinner js = (EnigmaSpinner) arg0.getSource();
			switch (js.identifier) {
			case "fourthRotorPosition":
				if (fourthRotorChoice.getSelectedIndex() == 0) {
					js.setValue(" ");
					rotorPositions[0] = '!';
				} else if (js.getValue().toString().equals(" ")) {
					js.setValue("A");
					rotorPositions[0] = js.getValue().toString().toCharArray()[0];
				} else {
					rotorPositions[0] = js.getValue().toString().toCharArray()[0];
				}
				break;
			case "leftRotorPosition":
				rotorPositions[1] = js.getValue().toString().toCharArray()[0];
				break;
			case "middleRotorPosition":
				rotorPositions[2] = js.getValue().toString().toCharArray()[0];
				break;
			case "rightRotorPosition":
				rotorPositions[3] = js.getValue().toString().toCharArray()[0];
				break;
			}
			machine.setPositions(rotorPositions);
		}
	}

	private class EnigmaSpinner extends JSpinner {
		public String identifier;
	}

	private class ButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String message = arg0.getActionCommand();
			if (message.equals("Reset")) {
				pbDialog.resetPlugBoard();
				pbString = null;
				pbField.setText("");
				System.out.println("Changing plugboard to: ");
			} else if (message.equals("Plugboard")) {
				pbString = pbDialog.displayDialog();
				pbField.setText(pbString);
				System.out.println("Changing plugboard to: " + pbString);
			}
		}
	}

}
