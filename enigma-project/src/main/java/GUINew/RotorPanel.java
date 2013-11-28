package main.java.GUINew;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class RotorPanel extends JPanel implements ActionListener,
		ChangeListener {
	//Constants
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
	
	//Machine state given by current choices. Initialized to default.
	private int[] rotors = { -1, 0, 1, 2 };
	private int reflector = 0;
	private char[] ringSettings = { '!', 'A', 'A', 'A' };
	private char[] rotorPositions = { '!', 'A', 'A', 'A' };
	
	//Components
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
	private EnigmaSpinner fourthRotorPosition;
	private EnigmaSpinner leftRotorPosition;
	private EnigmaSpinner middleRotorPosition;
	private EnigmaSpinner rightRotorPosition;

	//Constructor... puts the thing together.
	//Old bounds are commented out in case we find some way to
	//re-use them.
	public RotorPanel() {
		this.setSize(300,300);
		this.setLayout(new GridLayout(4, 5));
		this.setBackground(Color.black);
		fourthRotor = new JLabel("Rotor 4");
		fourthRotor.setForeground(Color.white);
		fourthRotor.setBackground(Color.black);
		this.add(fourthRotor);
		leftRotor = new JLabel("Rotor 3");
		leftRotor.setForeground(Color.white);
		leftRotor.setBackground(Color.black);
		this.add(leftRotor);
		middleRotor = new JLabel("Rotor 2");
		middleRotor.setForeground(Color.white);
		middleRotor.setBackground(Color.black);
		this.add(middleRotor);
		rightRotor = new JLabel("Rotor 1");
		rightRotor.setForeground(Color.white);
		rightRotor.setBackground(Color.black);
		this.add(rightRotor);
		reflectorLabel = new JLabel("Reflector");
		reflectorLabel.setForeground(Color.white);
		reflectorLabel.setBackground(Color.black);
		this.add(reflectorLabel);

		fourthRotorChoice = new JComboBox<String>(fourthRotorChoices);
		// fourthRotorChoice.setBounds(19, 89, 124, 28);
		fourthRotorChoice.setSelectedIndex(0);
		fourthRotorChoice.setActionCommand("fourthRotorChoice");
		fourthRotorChoice.addActionListener(this);
		this.add(fourthRotorChoice);

		leftRotorChoice = new JComboBox<String>(rotorChoices);
		// leftRotorChoice.setBounds(148, 89, 124, 28);
		leftRotorChoice.setSelectedIndex(0);
		leftRotorChoice.setActionCommand("leftRotorChoice");
		leftRotorChoice.addActionListener(this);
		this.add(leftRotorChoice);

		middleRotorChoice = new JComboBox<String>(rotorChoices);
		// middleRotorChoice.setBounds(276, 89, 124, 28);
		middleRotorChoice.setSelectedIndex(1);
		middleRotorChoice.setActionCommand("middleRotorChoice");
		middleRotorChoice.addActionListener(this);
		this.add(middleRotorChoice);

		rightRotorChoice = new JComboBox<String>(rotorChoices);
		// rightRotorChoice.setBounds(404, 89, 124, 28);
		rightRotorChoice.setSelectedIndex(2);
		rightRotorChoice.setActionCommand("rightRotorChoice");
		rightRotorChoice.addActionListener(this);
		this.add(rightRotorChoice);

		// Reflector Box
		reflectorChoice = new JComboBox<String>(reflectorChoices);
		// reflectorChoice.setBounds(552, 89, 124, 28);
		reflectorChoice.setSelectedIndex(0);
		reflectorChoice.setActionCommand("reflectorChoice");
		reflectorChoice.addActionListener(this);
		this.add(reflectorChoice);

		ringSettingsLabel = new JLabel("Ring Settings");
		ringSettingsLabel.setForeground(Color.white);
		ringSettingsLabel.setBackground(Color.black);
		this.add(ringSettingsLabel);
		
		// Ring Setting Boxes
		fourthRotorRingSetting = new JComboBox<String>(fourthLetterChoices);
		// fourthRotorRingSetting.setBounds(55, 126, 46, 28);
		fourthRotorRingSetting.setSelectedIndex(0);
		fourthRotorRingSetting.setActionCommand("fourthRotorRingSetting");
		fourthRotorRingSetting.addActionListener(this);
		this.add(fourthRotorRingSetting);

		leftRotorRingSetting = new JComboBox<String>(letterChoices);
		// leftRotorRingSetting.setBounds(187, 126, 46, 28);
		leftRotorRingSetting.setSelectedIndex(0);
		leftRotorRingSetting.setActionCommand("leftRotorRingSetting");
		leftRotorRingSetting.addActionListener(this);
		this.add(leftRotorRingSetting);

		middleRotorRingSetting = new JComboBox<String>(letterChoices);
		// middleRotorRingSetting.setBounds(315, 126, 46, 28);
		middleRotorRingSetting.setSelectedIndex(0);
		middleRotorRingSetting.setActionCommand("middleRotorRingSetting");
		middleRotorRingSetting.addActionListener(this);
		this.add(middleRotorRingSetting);

		rightRotorRingSetting = new JComboBox<String>(letterChoices);
		// rightRotorRingSetting.setBounds(443, 126, 46, 28);
		rightRotorRingSetting.setSelectedIndex(0);
		rightRotorRingSetting.setActionCommand("rightRotorRingSetting");
		rightRotorRingSetting.addActionListener(this);
		this.add(rightRotorRingSetting);

		rotorPositionsLabel = new JLabel("Rotor Positions");
		rotorPositionsLabel.setForeground(Color.white);
		rotorPositionsLabel.setBackground(Color.black);
		this.add(rotorPositionsLabel);
		
		// Rotor Position Spinners
		fourthRotorPosition = new EnigmaSpinner();
		fourthRotorPosition.setModel(new SpinnerListModel(fourthLetterChoices));
		// fourthRotorPosition.setBounds(55, 159, 46, 28);
		fourthRotorPosition.setValue(" ");
		fourthRotorPosition.identifier = "fourthRotorPosition";
		fourthRotorPosition.addChangeListener(this);
		this.add(fourthRotorPosition);

		leftRotorPosition = new EnigmaSpinner();
		leftRotorPosition.setModel(new SpinnerListModel(letterChoices));
		// leftRotorPosition.setBounds(187, 159, 46, 28);
		leftRotorPosition.setValue("A");
		leftRotorPosition.addChangeListener(this);
		leftRotorPosition.identifier = "leftRotorPosition";
		this.add(leftRotorPosition);

		middleRotorPosition = new EnigmaSpinner();
		middleRotorPosition.setModel(new SpinnerListModel(letterChoices));
		// middleRotorPosition.setBounds(315, 159, 46, 28);
		middleRotorPosition.setValue("A");
		middleRotorPosition.addChangeListener(this);
		middleRotorPosition.identifier = "middleRotorPosition";
		this.add(middleRotorPosition);

		rightRotorPosition = new EnigmaSpinner();
		rightRotorPosition.setModel(new SpinnerListModel(letterChoices));
		// rightRotorPosition.setBounds(443, 159, 46, 28);
		rightRotorPosition.setValue("A");
		rightRotorPosition.addChangeListener(this);
		rightRotorPosition.identifier = "rightRotorPosition";
		this.add(rightRotorPosition);
	}

	//ActionListener for the ComboBoxes. Performs validation and updates machine
	//state accordingly.
	@Override
	public void actionPerformed(ActionEvent e) {
		@SuppressWarnings("rawtypes")
		JComboBox temp = (JComboBox) e.getSource();
		int leftIndex = leftRotorChoice.getSelectedIndex();
		int middleIndex = middleRotorChoice.getSelectedIndex();
		int rightIndex = rightRotorChoice.getSelectedIndex();
		JFrame tempFrame = new JFrame();
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
		case "fourthRotorRingSetting":
			if (fourthRotorChoice.getSelectedIndex() == 0) {
				// no fourth rotor
				fourthRotorRingSetting.setSelectedIndex(0);
				ringSettings[0] = '!';
			} else if (temp.getSelectedIndex() == 0) {
				fourthRotorRingSetting.setSelectedIndex(1);
				ringSettings[0] = temp.getSelectedItem().toString().charAt(0);
			} else {
				ringSettings[0] = temp.getSelectedItem().toString().charAt(0);
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
		default:
			System.out.println("WTF?");
			break;
		}
		printState();
	}

	//ActionListener for the Spinners. Performs what little validation is needed
	//and alters machine state accordingly.
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
		printState();
	}

	//methods for getting at the state in the main GUI class.
	public int[] getRotors() {
		if (rotors[0] == -1)
			return new int[] { rotors[1], rotors[2], rotors[3] };
		return new int[] { rotors[0], rotors[1], rotors[2], rotors[3] };
	}

	public int getReflector() {
		return reflector;
	}

	public char[] getRingSettings() {
		if (rotors[0] == -1)
			return new char[] { ringSettings[1], ringSettings[2],
					ringSettings[3] };
		return new char[] { ringSettings[0], ringSettings[1], ringSettings[2],
				ringSettings[3] };
	}

	public char[] getRotorPositions() {
		if (rotors[0] == -1)
			return new char[] { rotorPositions[1], rotorPositions[2],
					rotorPositions[3] };
		return new char[] { rotorPositions[0], rotorPositions[1],
				rotorPositions[2], rotorPositions[3] };
	}

	//testing method to ensure all validation works properly...
	//it will output numerous messages per action. The last one is
	//the current machine state and the only important one.
	private void printState() {
		int[] t = getRotors();
		System.out.print("Rotors: " + t[0] + "," + t[1] + "," + t[2]);
		if (t.length == 4)
			System.out.print("," + t[3] + "\n");
		else
			System.out.print("\n");
		System.out.println("Reflector: " + getReflector());
		System.out.println("Ring Settings: "
				+ String.valueOf(getRingSettings()));
		System.out.println("Rotor Positions: "
				+ String.valueOf(getRotorPositions()));
	}
	
	private class EnigmaSpinner extends JSpinner {
		public String identifier;
	}
}
