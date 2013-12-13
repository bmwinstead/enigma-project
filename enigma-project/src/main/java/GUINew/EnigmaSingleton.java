package main.java.GUINew;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import main.java.enigma.EnigmaMachine;
import main.java.enigma.EnigmaSettings;

/**
 * Uses the Singleton and Observer design patterns to enable 
 * data sharing across the various modular GUI pieces. Maintains
 * state consistency for _the_ instance of EnigmaMachine and
 * provides conveniency methods for manipulating it and performing
 * encryption.
 * 
 * Is thread safe.
 * 
 * The rotor options are represented as an integer numbered 0-9. The rotors
 * are mapped as follows: 
 *            	0  - Rotor I;
 *            	1  - Rotor II;
 *            	2  - Rotor III;
 *            	3  - Rotor IV;
 *            	4  - Rotor V;
 *            	5  - Rotor VI;
 *            	6  - Rotor VII;
 *            	7  - Rotor VIII;
 *            	9  - Rotor Beta;
 *            	10 - Rotor Gamma 
 *            
 *  Reflector options are also represented as an integer. The options are
 *  numbered 0-3, and are mapped as follows:
 *  			0 - Reflector B;
 *            	1 - Reflector C;
 *            	2 - Reflector B thin;
 *            	3 - Reflector C thin
 *            
 *  Ring and rotor settings are represented using characters. 
 *  
 *  Plugboards are represented through a string that indicates their
 *  replacement mapping. Letters are swapped with their adjacent letters. For 
 *  example, a string of "ABCD" swaps A's with B's (and vice-versa) and C's
 *  with D's. 
 *  
 *  The updateType is an integer indicating what type of update or reset is to 
 *  be performed. 0 means that only the rotor indicators are to be updated. 
 *  The rotor positions change with every character encryption. 1 means that 
 *  the GUI is to be fully reset to its default position. 2 means that only the 
 *  text boxes are to be cleared. 
 *  
 *  The spacesOption is an integer used to determine the spacing in the output. 
 *  The default setting is no spaces, indicated by a 0. 1 indicates "words" of
 *  four letters, 2 indicates "words" of five letters, and 3 indicates that the
 *  original spacing should be maintained. This information is maintained here
 *  because the original spacing processing must be performed during the input
 *  processing. 
 *  
 *  The machineTypes is	an integer from 0 - 5 indicating the available machine 
 *  type options.
 * 			The machine type are mapped as follows
 * 				0 - No Restrictions,
 *				1 - Enigma I;
 *				2 - Enigma M3 Army;
 *				3 - Enigma M4 Naval; 
 *				4 - Enigma M4 R1;
 *				5 - Enigma M4 R2;    
 *  
 * @author Bryan Winstead
 * @author Team Enigma
 * @version 0.9
 * - 30 Nov 2013
 * 
 */
public class EnigmaSingleton extends Observable {
	public final static EnigmaSingleton INSTANCE = new EnigmaSingleton(); 
	public final static int INDICATORONLY = 0; // Update Type for changing only indicators on GUI
	public final static int FULLRESET = 1; // Update type for fully resetting the GUI
	public final static int CLEARTEXT = 2; // Update type for clearing the text fields on the GUI
	public final static int NOSPACES = 0; // Output type for output with no spaces. 
	public final static int FOURSPACES = 1; // Output type for output with four letter "words"
	public final static int FIVESPACES = 2; // Output type for output with five letter "words"
	public final static int ORIGINALSPACES = 3; // Output type for output that maintains its original spaces.
	private ArrayList<Observer> observers = new ArrayList<Observer>();
	private EnigmaMachine machine;
	private ConfigureOutput output = new ConfigureOutput(); //Configure
	private int updateType;
	private int spacesOption;
	private int machineType;
	public boolean machineTypeChanged;
	
	private EnigmaSingleton() {
		// No constructor for you.
	}

	/**
	 * Sets the full state, including plugboardMap and initialPositions. Mostly
	 * used as an initial constructor for the EnigmaMachine. Also used for reset
	 * functionality.
	 * 
	 * @param rotorChoices
	 * 	          Array of 3-4 integers numbered 0-9 to determine which rotors
	 *            to be included in the Enigma. 
	 * @param reflectorChoice
	 * 	          An integer from 0-3 indicating which of the four available
	 *            reflectors to include. 
	 * @param ringSettings
	 *            An array of 3-4 characters indicating the rotors' ring
	 *            settings.
	 * @param initialPositions
	 *            An array of 3-4 characters indicating the rotors' initial
	 *            positions.
	 * @param plugboardMap
	 *            String indicating the plugboard replacement mapping. Letters
	 *            are swapped with their adjacant letters. For example, a 
	 *            string of "ABCD" swaps A's with B's (and vice-versa) and C's
	 *            with D's. 
	 * @param machineTypes
	 * 			A integer from 0 - 5 indicating the available machine type options.
	 *     
	 */
	public void setState(int[] rotorChoices, int reflectorChoice, char[] ringSettings, char[] initialPositions, String plugboardMap) {
		if (rotorChoices.length == 4 && rotorChoices[0] == -1){
			rotorChoices = new int[] { rotorChoices[1], rotorChoices[2],
					rotorChoices[3] };
			ringSettings = new char[] { ringSettings[1], ringSettings[2], ringSettings[3]};
			initialPositions = new char[] { initialPositions[1], initialPositions[2], initialPositions[3]};
		}
		if (plugboardMap == null)
			machine = new EnigmaMachine(rotorChoices,reflectorChoice,ringSettings, initialPositions);
		else
			machine = new EnigmaMachine(rotorChoices, reflectorChoice,
					ringSettings, initialPositions, plugboardMap);

		System.out.println("(Singleton)Changing rotors to: "
				+ Arrays.toString(rotorChoices) + "\n");
		System.out.println("(Singleton)Changing reflector to: " + reflectorChoice + "\n");
		System.out.println("(Singleton)Changing ring settings to: "
				+ String.valueOf(ringSettings) + "\n");
		System.out.println("(Singleton)Changing rotor positions to: "
				+ String.valueOf(initialPositions) + "\n");
		System.out.println("(Singleton)Changing plugboard to: " + plugboardMap + "\n");
	} // end setState

	/**
	 * Only sets the rotorChoices, reflectorChoice, and ringSettings.
	 * 
	 * @param rotorChoices
	 * 				Array of 3-4 integers.
	 * @param reflectorChoice
	 * 				Array of 3-4 letters.
	 * @param ringSettings
	 * 				Array of 3-4 letters. 
	 */
	public void setState(int[] rotorChoices, int reflectorChoice,
			char[] ringSettings) {
		if (rotorChoices.length == 4 && rotorChoices[0] == -1){
			rotorChoices = new int[] { rotorChoices[1], rotorChoices[2],
					rotorChoices[3] };
			ringSettings = new char[] { ringSettings[1], ringSettings[2], ringSettings[3]};
		}
		machine.setRotorChoices(rotorChoices, reflectorChoice);
		machine.setRingSettings(ringSettings);
		System.out.println("(Singleton)Changing rotors to: "
				+ Arrays.toString(rotorChoices) + "\n");
		System.out.println("(Singleton)Changing reflector to: " + reflectorChoice + "\n");
		System.out.println("(Singleton)Changing ring settings to: "
				+ String.valueOf(ringSettings) + "\n");
	}

	/**
	 * Sets rotor positions.
	 * 
	 * @param rotorPositions
	 * 				Array of 3-4 letters. 
	 */
	public void setPositions(char[] rotorPositions) {
		if(rotorPositions.length == 4 && rotorPositions[0] == '!')
			rotorPositions = new char[] {rotorPositions[1], rotorPositions[2], rotorPositions[3]};
		System.out.println("(Singleton)Setting rotor positions to: " + String.valueOf(rotorPositions) + "\n");
		machine.setPositions(rotorPositions);
	}
	
	/**
	 * Sets the machine's current rotor positions to its "initial" positions
	 * Useful for the Indicator Reset when encrypting/decrypting multiple messages of the
	 * same key and starting indicators. 
	 */
	public void setInitPositions() {
		machine.setInitPositions(machine.getPositions());
	}

	/**
	 * Sets the machines plugboard.
	 * 
	 * @param pbMap
	 * 				String representing the plugboard map. 
	 */
	public void setPlugboard(String pbMap) {
		System.out.println("(Singleton)Setting plugboard to: " + pbMap + "\n");
		machine.setPlugboard(pbMap);
	}
	
	/**
	 * Sets the update type.
	 * 
	 * @param newUpdateType 	 
	 *            Integer indicating what type of update or reset is to be
	 *            performed. 
	 */
	public void setUpdateType(int newUpdateType) {
		updateType = newUpdateType;
	}

	/**
	 * Sets the spaces option
	 * 
	 * @param option
	 *            int representing which space option (none, 4, 5, or original)
	 *            we are using.
	 */
	public void setSpacesOption(int option) {
		spacesOption = option;
	}
	
	public void setMachineType(int type) {
		machineTypeChanged = true;
		machineType = type;
		notifyObservers();
	}
	/**
	 * Gets the current spaces option.
	 * 
	 * @return int representing which space option (none, 4, 5, or original) we
	 *         are using.
	 */
	public int getSpacesOption() {
		return spacesOption;
	}
	
	/**
	 * Resets the Enigma's indicators, then notifies observers.
	 */
	public void indicatorReset() {
		machine.reset();
		notifyObservers();
	}

	/**
	 * Performs encryption on a character at a time using the current 
	 * machine state. Then calls notifyObservers() to pass the new machine
	 * state back to the GUI components, which then update.
	 * 
	 * @param c the char to encrypt
	 * @return encryptedChar the encrypted char
	 */
	public char encryptChar(char c) {
		System.out.println("(Singleton)Encrypting char \"" + c + "\"\n");
		System.out.println("Sending space option to config: " + getSpacesOption() + "\n");
		c = output.configure(c, getSpacesOption()); //Text error checking
		// JLI 6Dec13 Was updating the GUI before encryption, not after.
		char result = machine.encryptChar(c);
		notifyObservers();
		return result;
	}
	
	/**
	 * Performs encryption on an entire string using the current machine state.
	 * Then calls notifyObserevrs() to pass the new machine state back to the
	 * GUI components, which then update.
	 * 
	 * @param s
	 *            String to be sent to the Enigma Machine for encryption.
	 * @return String that has been encrypted.
	 */
	public String encryptString(String s) {
		System.out.println("(Singleton)Encrypting string " + s + "\n");
		s = output.configure(s, getSpacesOption()); //Text error checking
		String result = machine.encryptString(s);
		// JLI 6Dec13 Was updating the GUI before enryption, not after.
		notifyObservers();
		return result;
	}
	
	/**
	 * Adds an observer to be notified on updates.
	 */
	@Override
	public void addObserver(Observer obs){
		observers.add(obs);
	}
	
	/**
	 * Implements the message passing interface between various modular
	 * GUI components by passing the entire machine state after performing
	 * an action (encryption, changing settings, reset, etc).
	 */
	@Override
	public void notifyObservers() {
		EnigmaSettings settings = new EnigmaSettings(machine.getRotors(),
				machine.getRingSettings(), machine.getPositions(),
				machine.getReflector(), machine.getPlugboard(), updateType, machineType);
		System.out.println("(Singleton) Notifying, settings state: ");
		System.out.println("Plugboard: " + settings.getPlugboardMap());
		System.out.println("Reflector: " + settings.getReflector());
		System.out.println("Ring settings: " + Arrays.toString(settings.getRingSettings()));
		System.out.println("Rotors: " + Arrays.toString(settings.getRotors()));
		System.out.println("Rotor positions: " + Arrays.toString(settings.getIndicatorSettings()));
		System.out.println("Machine Type: " + machineType + "\n");
		for (Observer obs : observers) {
			obs.update(this, settings);
		}
		setUpdateType(INDICATORONLY);
		machineTypeChanged = false;
	}
	
} // end EnigmaSingleton class
