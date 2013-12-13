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

import main.java.enigma.EnigmaSettings;

/**
 * Top panel of the modular GUI for the Enigma Machine. Allows setting 
 * of all Enigma settings, and helps propagate those settings
 * to other classes that need them through the use of the Enigma
 * Singleton.
 * 
 * @author Team Enigma
 * @version 0.9
 * Dec 9, 2013
 * 
 */
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
	private JLabel fourthRotorLabel;
	private JLabel leftRotorLabel;
	private JLabel middleRotorLabel;
	private JLabel rightRotorLabel;
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
	private boolean goNuts = true;
	private boolean rotorFlag = false;
	private EnigmaSingleton machine = EnigmaSingleton.INSTANCE;
	private int machineType;
	/**
	 * Constructor, initializes all relevant parts.
	 * Calls private build methods and glues them together.
	 */
	public RotorPanel() {
		machine.setState(rotors, reflector, ringSettings, rotorPositions,
				pbString);
		machine.addObserver(this);
		pbDialog = new PlugboardDialog();
		JPanel topPanel = buildTopPanel();
		JPanel plugboardPanel = buildPlugboardPanel();
		GroupLayout thisLayout = new GroupLayout(this);
		this.setLayout(thisLayout);
		this.setBackground(Color.black);
		thisLayout.setHorizontalGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(topPanel)
				.addComponent(plugboardPanel));
		thisLayout.setVerticalGroup(thisLayout.createSequentialGroup()
				.addComponent(topPanel)
				.addComponent(plugboardPanel));
	}

	/**
	 * Builds top half of the RotorPanel.
	 * This half contains all the components to allow
	 * setting the EnigmaMachine settings.
	 * @return the top panel.
	 */
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

		fourthRotorLabel = new JLabel("Rotor 4");
		fourthRotorLabel.setForeground(Color.white);
		fourthRotorLabel.setBackground(Color.black);

		leftRotorLabel = new JLabel("Rotor 3");
		leftRotorLabel.setForeground(Color.white);
		leftRotorLabel.setBackground(Color.black);

		middleRotorLabel = new JLabel("Rotor 2");
		middleRotorLabel.setForeground(Color.white);
		middleRotorLabel.setBackground(Color.black);

		rightRotorLabel = new JLabel("Rotor 1");
		rightRotorLabel.setForeground(Color.white);
		rightRotorLabel.setBackground(Color.black);

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

		int smallMin = 20;
		int smallPref = 30;
		int smallMax = 40;
		
		int medMin = 75;
		int medPref = 100;
		int medMax = 120;
		
		topLayout
				.setHorizontalGroup(topLayout
						.createSequentialGroup()
						.addGroup(
								topLayout.createParallelGroup()
										.addComponent(reflectorLabel)
										.addComponent(reflectorChoice,medMin,medPref,medMax)
										.addComponent(ringSettingsLabel)
										.addComponent(rotorPositionsLabel))
						.addGroup(
								topLayout
										.createParallelGroup(
												GroupLayout.Alignment.CENTER)
										.addComponent(fourthRotorLabel)
										.addComponent(fourthRotorChoice,medMin,medPref,medMax)
										.addComponent(fourthRotorRingSetting,
												smallMin, smallPref, smallMax)
										.addComponent(fourthRotorPosition, smallMin,
												smallPref, smallMax))
						.addGroup(
								topLayout
										.createParallelGroup(
												GroupLayout.Alignment.CENTER)
										.addComponent(leftRotorLabel)
										.addComponent(leftRotorChoice,medMin,medPref,medMax)
										.addComponent(leftRotorRingSetting,
												smallMin, smallPref, smallMax)
										.addComponent(leftRotorPosition, smallMin,
												smallPref, smallMax))
						.addGroup(
								topLayout
										.createParallelGroup(
												GroupLayout.Alignment.CENTER)
										.addComponent(middleRotorLabel)
										.addComponent(middleRotorChoice,medMin,medPref,medMax)
										.addComponent(middleRotorRingSetting,
												smallMin, smallPref, smallMax)
										.addComponent(middleRotorPosition, smallMin,
												smallPref, smallMax))
						.addGroup(
								topLayout
										.createParallelGroup(
												GroupLayout.Alignment.CENTER)
										.addComponent(rightRotorLabel)
										.addComponent(rightRotorChoice,medMin,medPref,medMax)
										.addComponent(rightRotorRingSetting,
												smallMin, smallPref, smallMax)
										.addComponent(rightRotorPosition, smallMin,
												smallPref, smallMax)));
		topLayout
				.setVerticalGroup(topLayout
						.createSequentialGroup()
						.addGroup(
								topLayout.createParallelGroup()
										.addComponent(reflectorLabel)
										.addComponent(fourthRotorLabel)
										.addComponent(leftRotorLabel)
										.addComponent(middleRotorLabel)
										.addComponent(rightRotorLabel))
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
												smallMin, smallPref, smallMax)
										.addComponent(leftRotorRingSetting,
												smallMin, smallPref, smallMax)
										.addComponent(middleRotorRingSetting,
												smallMin, smallPref, smallMax)
										.addComponent(rightRotorRingSetting,
												smallMin, smallPref, smallMax))
						.addGroup(
								topLayout
										.createParallelGroup(
												GroupLayout.Alignment.CENTER)
										.addComponent(rotorPositionsLabel)
										.addComponent(fourthRotorPosition, smallMin,
												smallPref, smallMax)
										.addComponent(leftRotorPosition, smallMin,
												smallPref, smallMax)
										.addComponent(middleRotorPosition, smallMin,
												smallPref, smallMax)
										.addComponent(rightRotorPosition, smallMin,
												smallPref, smallMax)));
		return topPanel;
	}

	/**
	 * Builds the plugboard panel, responsible for 
	 * displaying the current plugboard state,
	 * and initializing the pop-up window that will
	 * allow users to set the plugboard.
	 * @return the plugboard panel.
	 */
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
		plugboardLayout.setHorizontalGroup(plugboardLayout.createParallelGroup()
				.addComponent(pbField,400,450,500)
				.addComponent(pbButton)
				.addComponent(resetButton));
		plugboardLayout.setVerticalGroup(plugboardLayout.createSequentialGroup()
				.addComponent(pbField)
				.addComponent(pbButton)
				.addComponent(resetButton));
		plugboardLayout.linkSize(pbButton, resetButton);
		return plugboardPanel;
	}


	/**
	 * Helper method that sets the rotor positions of the GUI
	 * components. The GUI components' ActionListeners then propagate
	 * those changes to the singleton instance of the EnigmaMachine.
	 * @param positions the new positions of the rotors.
	 */
	private void setRotorPositions(char[] positions) {
		System.out.println("(RotorPanel)(setRotorPositions()) " + Arrays.toString(positions) + "\n");
		if (positions.length == 4 && positions[0] != '!') {
			fourthRotorPosition.setValue(String.valueOf(positions[0]));
			leftRotorPosition.setValue(String.valueOf(positions[1]));
			middleRotorPosition.setValue(String.valueOf(positions[2]));
			rightRotorPosition.setValue(String.valueOf(positions[3]));
		} else {
			fourthRotorPosition.setValue(" ");
			leftRotorPosition.setValue(String.valueOf(positions[0]));
			middleRotorPosition.setValue(String.valueOf(positions[1]));
			rightRotorPosition.setValue(String.valueOf(positions[2]));
		}
	}
	
	/**
	 * Helper method that sets the string that is representative
	 * of the plugboard settings. Updates the text field and sets
	 * the plugboard settings in the EnigmaSingleton.
	 * @param pbmap the new plugboard string
	 */
	private void setPlugboard(String pbmap) {
		pbField.setText(pbmap);
		machine.setPlugboard(pbmap);
		System.out.println("(RotorPanel)Changing plugboard to: " + pbmap + "\n");
	}
	
	/**
	 * Helper method that sets the state of the GUI components
	 * that represent the rotor choices. These changes then
	 * propagate down to the EnigmaSingleton through the
	 * ActionListeners of the components.
	 * @param rotors the new rotor selection.
	 */
	private void setRotorChoices(int[] rotors) {
		if (rotors.length == 4) {
			if (rotors[0] == -1) {
				fourthRotorChoice.setSelectedIndex(0);
			} 
			else {
				fourthRotorChoice.setSelectedIndex(rotors[0] - 7);
			}
			leftRotorChoice.setSelectedIndex(rotors[1]);
			middleRotorChoice.setSelectedIndex(rotors[2]);
			rightRotorChoice.setSelectedIndex(rotors[3]);
		}
		else {
			fourthRotorChoice.setSelectedIndex(0);
			leftRotorChoice.setSelectedIndex(rotors[0]);
			middleRotorChoice.setSelectedIndex(rotors[1]);
			rightRotorChoice.setSelectedIndex(rotors[2]);
		}
	}
	
	/**
	 * Helper method that sets the state of all the GUI
	 * components that represent the ring settings of the
	 * rotors. These changes then propagate down to the 
	 * EnigmaSingleton through the components' ActionListeners.
	 * @param ringSettings
	 */
	private void setRingSettings(char[] ringSettings) {
		if (ringSettings.length == 4 && ringSettings[0] != '!') {
			fourthRotorRingSetting.setSelectedIndex('A' - ringSettings[0]);
			leftRotorRingSetting.setSelectedIndex('A' - ringSettings[1]);
			middleRotorRingSetting.setSelectedIndex('A' - ringSettings[2]);
			rightRotorRingSetting.setSelectedIndex('A' - ringSettings[3]);
		} else {
			fourthRotorRingSetting.setSelectedIndex(0);
			leftRotorRingSetting.setSelectedIndex('A' - ringSettings[0]);
			middleRotorRingSetting.setSelectedIndex('A' - ringSettings[1]);
			rightRotorRingSetting.setSelectedIndex('A' - ringSettings[2]);
		}
	}
	
	/**
	 * Helper method that sets the state of the GUI 
	 * component that represents the reflector choice.
	 * These changes then propagate down to the EnigmaSingleton
	 * through the components' ActionListeners.
	 * @param reflectorNum
	 */
	private void setReflector(int reflectorNum) {
		reflectorChoice.setSelectedIndex(reflectorNum);
	}
	
	/**
	 * Implements the Observer functionality.
	 * When the method is called by the Observable
	 * (EnigmaSingleton), it triggers the panel to update
	 * its local machine state and all relevant GUI
	 * components.
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		EnigmaSettings settings = (EnigmaSettings) arg1;
		if(settings.getMachineType() == machineType)
			machine.machineTypeChanged = false;
		if(machine.machineTypeChanged)
			changeMachineType(settings.getMachineType());
		if (settings.getUpdateType() == EnigmaSingleton.FULLRESET) {
			System.out.println("(RotorPanel(update())Full Reset triggered");
			rotorFlag = true;
			System.out.println("Setting plugboard: " + settings.getPlugboardMap());
			setPlugboard(settings.getPlugboardMap());
			System.out.println("Setting rotors: " + Arrays.toString(settings.getRotors()));
			setRotorChoices(settings.getRotors());
			System.out.println("Setting ring settings: " + Arrays.toString(settings.getRingSettings()));
			setRingSettings(settings.getRingSettings());	
			System.out.println("Setting reflector: " + settings.getReflector());
			setReflector(settings.getReflector());
			pbDialog.resetPlugBoard();
			rotorFlag = false;
		}
		System.out.println("(RotorPanel)(update())Changing rotors to " + String.valueOf(settings.getIndicatorSettings()) + "\n");
		setRotorPositions(settings.getIndicatorSettings());
	}
	
	/**
	 * Changes the machine type to match the 
	 * @param newMachineType
	 */
	private void changeMachineType(int newMachineType){
		System.out.println("Changing machine type to: " + newMachineType);
		machineType = newMachineType;
		changeRotors(newMachineType);
		if(newMachineType < 4 && newMachineType != 0){
			fourthRotorChoice.setSelectedIndex(0);
			fourthRotorChoice.setEnabled(false);
			fourthRotorPosition.setEnabled(false);
			fourthRotorRingSetting.setEnabled(false);
		} else{
			fourthRotorChoice.setEnabled(true);
			fourthRotorChoice.setSelectedIndex(1);
			fourthRotorPosition.setEnabled(true);
			fourthRotorRingSetting.setEnabled(true);
		}
	}

	private void changeRotors(int newMachineType){
		String[][] machineTypeRotors = {
				{"ROTOR I","ROTOR II","ROTOR III","ROTOR IV","ROTOR V","ROTOR VI","ROTOR VII","ROTOR VIII"}, //0
				{"ROTOR I","ROTOR II","ROTOR III"}, //1
				{"ROTOR I","ROTOR II","ROTOR III","ROTOR IV","ROTOR V"}, //2
				{"ROTOR I","ROTOR II","ROTOR III","ROTOR IV","ROTOR V","ROTOR VI","ROTOR VII","ROTOR VIII"}, //3
				{"ROTOR I","ROTOR II","ROTOR III","ROTOR IV","ROTOR V","ROTOR VI","ROTOR VII","ROTOR VIII"}, //4
				{"ROTOR I","ROTOR II","ROTOR III","ROTOR IV","ROTOR V","ROTOR VI","ROTOR VII","ROTOR VIII"} //5
			};
		String[][] machineTypeReflectors = {
				{"REFLECTOR B","REFLECTOR C","REFLECTOR B THIN","REFLECTOR C THIN"}, //0
				{"REFLECTOR B","REFLECTOR C"}, //1 
				{"REFLECTOR B","REFLECTOR C"}, //2
				{"REFLECTOR B","REFLECTOR C"}, //3
				{"REFLECTOR B THIN","REFLECTOR C THIN"}, //4
				{"REFLECTOR B THIN","REFLECTOR C THIN"}  //5
		};
		rotorFlag = true;
		goNuts = true;
		leftRotorChoice.removeAllItems();
		middleRotorChoice.removeAllItems();
		rightRotorChoice.removeAllItems();
		reflectorChoice.removeAllItems();
		for(String s : machineTypeReflectors[newMachineType]){
			reflectorChoice.addItem(s);
		}
		for(String s : machineTypeRotors[newMachineType]){
			leftRotorChoice.addItem(s);
			middleRotorChoice.addItem(s);
			rightRotorChoice.addItem(s);
		}
		leftRotorChoice.setSelectedIndex(0);
		middleRotorChoice.setSelectedIndex(1);
		rightRotorChoice.setSelectedIndex(2);
		if(newMachineType != 0)
			goNuts = false;
		rotorFlag = false;
		leftRotorPosition.setValue("A");
		middleRotorPosition.setValue("B");
		rightRotorPosition.setValue("C");
		leftRotorRingSetting.setSelectedIndex(0);
		middleRotorRingSetting.setSelectedIndex(0);
		rightRotorRingSetting.setSelectedIndex(0);
	}
	
	/**
	 * Private ActionListener class that handles
	 * the events for the GUI components that represent
	 * the ring settings of the rotors.
	 * @author Bryan Winstead
	 * @author Team Enigma
	 *
	 */
	private class RingSettingsListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println("**** Ring Settings Listener Triggered ****");
			@SuppressWarnings("rawtypes")
			JComboBox temp = (JComboBox) e.getSource();
			System.out.println("(RingSettingsListener) " + e.getActionCommand() + " state " + temp.getSelectedItem());
//			System.out.println("Action registered on ring settings combo boxes, dumping state before");
//			printState();
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
//			System.out.println("All changes performed, dumping state");
//			printState();
			if(!rotorFlag)
				machine.setState(rotors, mapReflector(reflector), ringSettings);
		}
	}

	/**
	 * Private ActionListener class that handles the 
	 * events for the GUI components that represent
	 * the rotor choices of the machine.
	 * @author Bryan Winstead
	 * @author Team Enigma
	 */
	private class RotorListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("**** Rotor Listener Triggered ****");
			@SuppressWarnings("rawtypes")
			JComboBox temp = (JComboBox) e.getSource();
			System.out.println("(RotorListener) " + e.getActionCommand() + " state " + temp.getSelectedItem());
			int leftIndex = leftRotorChoice.getSelectedIndex();
			int middleIndex = middleRotorChoice.getSelectedIndex();
			int rightIndex = rightRotorChoice.getSelectedIndex();
			JFrame tempFrame = new JFrame();
			switch (e.getActionCommand()) {
			case "fourthRotorChoice":
				if(machineType > 3 && temp.getSelectedIndex() == 0){
					fourthRotorChoice.setSelectedIndex(1);
				}
				if (temp.getSelectedIndex() == 0) {
					fourthRotorRingSetting.setSelectedIndex(0);
					rotors[0] = -1;
					fourthRotorPosition.setValue(" ");
				} else {
					rotors[0] = temp.getSelectedIndex() + 7;
					fourthRotorRingSetting.setSelectedIndex(1);
					fourthRotorPosition.setValue("A");
				}
				break;
			case "leftRotorChoice":
				if (!goNuts && (leftIndex == middleIndex || leftIndex == rightIndex)) {
					JOptionPane.showMessageDialog(tempFrame,
							"Error 101: Selected rotor is already in use.");
					leftRotorChoice.setSelectedIndex(rotors[1]);
				} else
					rotors[1] = temp.getSelectedIndex();
				break;
			case "middleRotorChoice":
				if (!goNuts && (leftIndex == middleIndex || middleIndex == rightIndex)) {
					JOptionPane.showMessageDialog(tempFrame,
							"Error 101: Selected rotor is already in use.");
					middleRotorChoice.setSelectedIndex(rotors[2]);
				} else
					rotors[2] = temp.getSelectedIndex();
				break;
			case "rightRotorChoice":
				if (!goNuts && (rightIndex == middleIndex || leftIndex == rightIndex)) {
					JOptionPane.showMessageDialog(tempFrame,
							"Error 101: Selected rotor is already in use.");
					rightRotorChoice.setSelectedIndex(rotors[3]);
				} else
					rotors[3] = temp.getSelectedIndex();
				break;
			case "reflectorChoice":
				reflector = temp.getSelectedIndex();
				break;
			}
			if(!rotorFlag)
				machine.setState(rotors, mapReflector(reflector), ringSettings);
		}
	}

	/**
	 * Private ActionListener class that handles
	 * the events for the GUI components that represent
	 * the rotor positions of the Enigma machine.
	 * @author Bryan Winstead
	 * @author Team Enigma
	 *
	 */
	private class PositionsListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent arg0) {
			System.out.println("**** Positions Listener Triggered ****");
			EnigmaSpinner js = (EnigmaSpinner) arg0.getSource();
			System.out.println("(PositionsListener) " + js.identifier + " state " + js.getValue());
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
			if(!rotorFlag)
				machine.setPositions(rotorPositions);
		}
	}

	private int mapReflector(int i){
		if(machineType > 3)
			return i + 2;
		return i;
	}
	/**
	 * Probably unnecessary inner class that
	 * gives us an easy way to identify different 
	 * JSpinner objects.
	 * @author Bryan Winstead
	 * @author Team Enigma
	 *
	 */
	private class EnigmaSpinner extends JSpinner {
		public String identifier;
	}

	/**
	 * Private ActionListener that handles the events
	 * for the GUI components that handle the plugboard
	 * settings.
	 * @author Bryan Winstead
	 * @author Team Enigma
	 */
	private class ButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String message = arg0.getActionCommand();
			if (message.equals("Reset")) {
				System.out.println("**** Plugboard Reset ****");
				pbDialog.resetPlugBoard();
				pbString = null;
				pbField.setText("");
				System.out.println("(RotorPanel)Resetting plugboard\n");
			} else if (message.equals("Plugboard")) {
				System.out.println("**** Plugboard Changed ****");
				pbString = pbDialog.displayDialog();
				setPlugboard(pbString); 
				System.out.println("(RotorPanel)Changing plugboard to: " + pbString +"\n");
			}
		}
	}

}
