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
 * @author Bryan Winstead
 * @author Team Enigma
 * @version 0.9
 * Date: 30 Nov 2013
 * 
 */
public class EnigmaSingleton extends Observable {
	public final static EnigmaSingleton INSTANCE = new EnigmaSingleton();
	public final static int INDICATORONLY = 0;
	public final static int FULLRESET = 1;
	public final static int CLEARTEXT = 2;
	public final static int NOSPACES = 0;
	public final static int FOURSPACES = 1;
	public final static int FIVESPACES = 2;
	public final static int ORIGINALSPACES = 3;
	private ArrayList<Observer> observers = new ArrayList<Observer>();
	private EnigmaMachine machine;
	private ConfigureOutput output = new ConfigureOutput(); //Configure
	private int updateType;
	private int spacesOption;
	private EnigmaSingleton() {
		// No constructor for you.
	}

	/**
	 * Sets the full state, including plugboardMap and initialPositions. Mostly
	 * used as an initial constructor for the EnigmaMachine. Also used for reset
	 * functionality.
	 * 
	 * @param rotorChoices
	 * @param reflectorChoice
	 * @param ringSettings
	 * @param initialPositions
	 * @param plugboardMap
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
	 * @param reflectorChoice
	 * @param ringSettings
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
	 * @param rotorPositions
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
	 * @param pbMap
	 */
	public void setPlugboard(String pbMap) {
		System.out.println("(Singleton)Setting plugboard to: " + pbMap + "\n");
		machine.setPlugboard(pbMap);
	}
	
	/**
	 * Sets the update type.
	 * @param newUpdateType
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
				machine.getReflector(), machine.getPlugboard(), updateType);
		System.out.println("(Singleton) Notifying, settings state: ");
		System.out.println("Plugboard: " + settings.getPlugboardMap());
		System.out.println("Reflector: " + settings.getReflector());
		System.out.println("Ring settings: " + Arrays.toString(settings.getRingSettings()));
		System.out.println("Rotors: " + Arrays.toString(settings.getRotors()));
		System.out.println("Rotor positions: " + Arrays.toString(settings.getIndicatorSettings()) + "\n");
		for (Observer obs : observers) {
			// obs.update(this, String.valueOf(machine.getPositions()));
			obs.update(this, settings);
		}
		setUpdateType(INDICATORONLY);
	}
	
} // end EnigmaSingleton class
