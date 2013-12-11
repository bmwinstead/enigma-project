package main.java.enigma;

import java.util.Arrays;

/**
 * The complete "guts" of the Enigma Machine, a simulation of the encryption
 * device used by the Germans during World War II. Includes all rotors, 
 * reflector, and plugbaord. Allows for the encryption of a single character 
 * or a string.
 * 
 * @author Bryan Winstead
 * @author Team Enigma
 * @version 0.9
 * - Nov 22, 2013
 *
 */
public class EnigmaMachine {

	private Rotors rotors;
	private int[] rotorArray;
	private Plugboard plugboard;
	private char[] initPositions;
	
	/**
	 * Constructor for creating an Enigma that has no plugboard
	 * 
	 * @param rotorChoices
	 *            Array of 3-4 integers numbered 0-9 to determine which rotors
	 *            to be included in the Enigma. The rotors are mapped as
	 *            follows: 
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
	 * @param reflectorChoice
	 *            An integer from 0-3 indicating which of the four available
	 *            reflectors to include. The reflectors are mapped as follows:
	 *            	0 - Reflector B;
	 *            	1 - Reflector C;
	 *            	2 - Reflector B thin;
	 *            	3 - Reflector C thin
	 * @param ringSettings
	 *            An array of 3-4 characters indicating the rotors' ring
	 *            settings.
	 * @param initialPositions
	 *            An array of 3-4 characters indicating the rotors' initial
	 *            positions.
	 */
	public EnigmaMachine(int[] rotorChoices, int reflectorChoice, char[] ringSettings, char[] initialPositions){
		initPositions = initialPositions;
		rotorArray = rotorChoices;
		rotors = new Rotors(rotorChoices, reflectorChoice);
		rotors.setRingSettings(ringSettings);
		rotors.setPositions(initialPositions);
		plugboard = null;
	} // end no-Plugboard constructor
	
	/**
	 * Constructor for creating an Enigma that has a plugboard
	 * 
	 * @param rotorChoices
	 *            Array of 3-4 integers numbered 0-9 to determine which rotors
	 *            to be included in the Enigma. The rotors are mapped as
	 *            follows: 
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
	 * @param reflectorChoice
	 *            An integer from 0-3 indicating which of the four available
	 *            reflectors to include. The reflectors are mapped as follows:
	 *            	0 - Reflector B;
	 *            	1 - Reflector C;
	 *            	2 - Reflector B thin;
	 *            	3 - Reflector C thin
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
	 */
	public EnigmaMachine(int[] rotorChoices, int reflectorChoice, char[] ringSettings, char[] initialPositions, String plugboardMap){
		initPositions = initialPositions;
		rotorArray = rotorChoices;
		rotors = new Rotors(rotorChoices, reflectorChoice);
		rotors.setRingSettings(ringSettings);
		rotors.setPositions(initialPositions);
		plugboard = new Plugboard(plugboardMap);
	} // end plugboard constructor
	
	/**
	 * Single-character encryption, used primarily for the lightboard on the
	 * GUI.
	 * 
	 * @param c
	 *            Character to be encrypted
	 * @return Character after encryption.
	 */
	public char encryptChar(char c){
		c = Character.toUpperCase(c);
		char a = c;
		if(plugboard != null){
			a = plugboard.matchChar(c);
		}
		a = rotors.encrypt(a);
		if(plugboard != null){
			a = plugboard.matchChar(a);
		}
		return a;
	} // end encryptChar method
	
	/**
	 * String encryption, used for processing an entire file at once.
	 * 
	 * @param s
	 *            String to be encrypted.
	 * @return Output string. Non-letter characters are preserved.
	 */
	public String encryptString(String s){
		char[] cArr = s.toCharArray();
		String rStr = "";
		for(char c : cArr){
			rStr += encryptChar(c);
		}
		return rStr;
	} // end encryptString method
	
	/**
	 * Resets the EnigmaMachine to its initial rotor positions. Useful for
	 * testing. 
	 */
	public void reset(){
		System.out.println("(EnigmaMachine) Initial Positions: " + Arrays.toString(initPositions));
		rotors.setPositions(initPositions);
	} // end reset method
	
	/**
	 * Sets the EnigmaMachine's positions to the ones provided without
	 * changing the "initial" rotor position settings. 
	 * 
	 * @param newPositions
	 *            Array of 3-4 character array representing the new positions
	 *            the rotors are to be set to.
	 */
	public void setPositions(char[] newPositions) {
		rotors.setPositions(newPositions);
	} // end setPositions method
	
	/**
	 * Resets the initial rotor positions.
	 * 
	 * @param newInit
	 *            Character array of 3-4 letters indicating the new initial
	 *            positions of the Enigma's rotors.
	 */
	public void setInitPositions(char[] newInit) {
		initPositions = newInit;
	}
	
	/**
	 * Changes the Rotor and Reflector choices of the Enigma.
	 * 
	 * @param rotorChoices
	 *            Array of 3-4 integers numbered 0-9 to determine which rotors
	 *            to be included in the Enigma. The rotors are mapped as
	 *            follows: 
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
	 * @param reflectorChoice
	 *            An integer from 0-3 indicating which of the four available
	 *            reflectors to include. The reflectors are mapped as follows:
	 *            	0 - Reflector B;
	 *            	1 - Reflector C;
	 *            	2 - Reflector B thin;
	 *            	3 - Reflector C thin
	 */
	public void setRotorChoices(int[] rotorChoices,int reflectorChoice){
		rotorArray = rotorChoices;
		rotors = new Rotors(rotorChoices,reflectorChoice);
	} // end setRotorChoices
	
	/**
	 * Changes the Ring Settings of the Enigma's rotors.
	 * 
	 * @param ringSettings
	 *            An array of 3-4 characters indicating the rotors' ring
	 *            settings.
	 */
	public void setRingSettings(char[] ringSettings){
		rotors.setRingSettings(ringSettings);
	} // end setRingSettings
	
	/**
	 * Sets a new Plugboard map for the Enigma Machine.
	 * 
	 * @param pbMap
	 *            String indicating the plugboard replacement mapping. Letters
	 *            are swapped with their adjacant letters. For example, a 
	 *            string of "ABCD" swaps A's with B's (and vice-versa) and C's
	 *            with D's. 
	 */
	public void setPlugboard(String pbMap){
		plugboard = new Plugboard(pbMap);
	} // end setPlugboard
	
	/**
	 * Returns the Rotor Positions to the GUI so that it will accurately
	 * reflect the current settings after encryption.
	 */
	public char[] getPositions() {
		return rotors.getPositions();
	} // end getPositions method
	
	/**
	 * Returns the rotors. Used so the reset panel can notify the GUI that
	 * they've changed.
	 * 
	 * @return Array of 3-4 integers numbered 0-9 to determine which rotors to
	 *         be included in the Enigma. The rotors are mapped as follows: 
	 *         	0 - Rotor I; 
	 *         	1 - Rotor II; 
	 *         	2 - Rotor III; 
	 *         	3 - Rotor IV; 
	 *         	4 - Rotor V; 
	 *         	5 - Rotor VI; 
	 *         	6 - Rotor VII; 
	 *         	7 - Rotor VIII; 
	 *         	9 - Rotor Beta; 
	 *         	10 - Rotor Gamma
	 */
	public int[] getRotors() {
		return rotorArray;
	}
	
	/**
	 * Returns ring settings. Used primarily for  for resets. 
	 * 
	 * @return Array of 3-4 chars representing the ring positions.
	 */
	public char[] getRingSettings() {
		return rotors.getRingSettings();
	}
	
	/**
	 * Returns the reflector settings. Used primarily for resets. 
	 * 
	 * @return An integer from 0-3 indicating which of the four available
	 *         reflectors to include. The reflectors are mapped as follows:
	 *            	0 - Reflector B;
	 *            	1 - Reflector C;
	 *            	2 - Reflector B thin;
	 *            	3 - Reflector C thin
	 */
	public int getReflector() {
		return rotors.getReflector();
	}
	
	/**
	 * Returns a string representing the plugboard mapping.
	 * 
	 * @return String representing the current plugboard map, or null if the
	 *         current Enigma has no plugboard.
	 */
	public String getPlugboard() {
		if (plugboard == null) {
			return "";
		}
		else {
			return plugboard.getPlugboardMap();
		}
	}
} // end EnigmaMachine class
