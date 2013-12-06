package main.java.GUINew;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import main.java.enigma.EnigmaMachine;
import main.java.enigma.EnigmaSettings;

/**
 * Used to share the EnigmaMachine across various components of the GUI.
 * Basically just wraps around the EnigmaMachine class and allows settings to be
 * changed and stuff.
 * 
 * @author bwinstead
 * @author Team Enigma
 * @version 0.9
 * @date 30 Nov 2013
 * 
 */
public class EnigmaSingleton extends Observable {
	public final static EnigmaSingleton INSTANCE = new EnigmaSingleton();
	public final static int INDICATORONLY = 0;
	public final static int FULLRESET = 1;
	private ArrayList<Observer> observers = new ArrayList<Observer>();
	private EnigmaMachine machine;
	private ConfigureOutput output = new ConfigureOutput(); //Configure
	private int updateType;
	private EnigmaSingleton() {
		// No constructor for you.
	}

	/**
	 * Sets the full state, including plugboardMap and initialPositions.
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

		System.out.println("Changing rotors to: "
				+ Arrays.toString(rotorChoices));
		System.out.println("Changing reflector to: " + reflectorChoice);
		System.out.println("Changing ring settings to: "
				+ String.valueOf(ringSettings));
		System.out.println("Changing rotor positions to: "
				+ String.valueOf(initialPositions));
		System.out.println("Changing plugboard to: " + plugboardMap);
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
		System.out.println("Changing rotors to: "
				+ Arrays.toString(rotorChoices));
		System.out.println("Changing reflector to: " + reflectorChoice);
		System.out.println("Changing ring settings to: "
				+ String.valueOf(ringSettings));
	}

	/**
	 * 
	 * @param rotorPositions
	 */
	public void setPositions(char[] rotorPositions) {
		if(rotorPositions.length == 4 && rotorPositions[0] == '!')
			rotorPositions = new char[] {rotorPositions[1], rotorPositions[2], rotorPositions[3]};
		System.out.println("Setting rotor positions to: " + String.valueOf(rotorPositions));
		machine.setPositions(rotorPositions);
	}
	
	/**
	 * Sets the machine's current rotor positions to its "initial" positions
	 * Useful for the Indicator Reset when encrypting multiple messages of the
	 * same key and starting indicators. 
	 */
	public void setInitPositions() {
		machine.setInitPositions(machine.getPositions());
	}

	/**
	 * 
	 * @param pbMap
	 */
	public void setPlugboard(String pbMap) {
		System.out.println("Singleton: Setting plugboard to: " + pbMap);
		machine.setPlugboard(pbMap);
	}
	
	/**
	 * 
	 * @param newUpdateType
	 */
	public void setUpdateType(int newUpdateType) {
		updateType = newUpdateType;
	}
	
	public void indicatorReset() {
		machine.reset();
		notifyObservers();
	}

	/**
	 * 
	 * @param c
	 * @return encryptedChar
	 */
	public char encryptChar(char c) {
		System.out.println("Encrypting char " + c);
		c = output.configure(c); //Text error checking
		System.out.println("Text Error Checking and Conversion");
		// JLI 6Dec13 Was updating the GUI before encryption, not after.
		char result = machine.encryptChar(c);
		notifyObservers();
		return result;
	}
	
	/**
	 * 
	 * @param s
	 * @return
	 */
	public String encryptString(String s) {
		System.out.println("Encrypting string " + s);
		s = output.configure(s); //Text error checking
		System.out.println("Text Error Checking and Conversion");
		String result = machine.encryptString(s);
		// JLI 6Dec13 Was updating the GUI before enryption, not after.
		notifyObservers();
		return result;
	}
	
	@Override
	public void addObserver(Observer obs){
		observers.add(obs);
	}
	@Override
	public void notifyObservers() {
		EnigmaSettings settings = new EnigmaSettings(machine.getRotors(),
				machine.getRingSettings(), machine.getPositions(),
				machine.getReflector(), machine.getPlugboard(), updateType);
		System.out.println("Notifying, rotors are currently "
				+ String.valueOf(machine.getPositions()));
		for (Observer obs : observers) {
			// obs.update(this, String.valueOf(machine.getPositions()));
			obs.update(this, settings);
		}
		setUpdateType(INDICATORONLY);
	}
	
} // end EnigmaSingleton class
