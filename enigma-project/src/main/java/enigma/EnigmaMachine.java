package main.java.enigma;

/**
 * 
 * @author Bryan Winstead
 * @author Team Enigma
 * @version 0.9
 * @date - Nov 22, 2013
 * 
 * The complete "guts" of the Enigma Machine. Includes all rotors, reflector,
 * and plugbaord. Allows for the encryption of a single character or a string.
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
	 *            to be included in the Enigma.
	 * @param reflectorChoice
	 *            An integer from 0-3 indicating which of the four available
	 *            reflectors to include.
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
	 *            to be included in the Enigma.
	 * @param reflectorChoice
	 *            An integer from 0-3 indicating which of the four available
	 *            reflectors to include.
	 * @param ringSettings
	 *            An array of 3-4 characters indicating the rotors' ring
	 *            settings.
	 * @param initialPositions
	 *            An array of 3-4 characters indicating the rotors' initial
	 *            positions.
	 * @param plugboardMap
	 *            String indicating the plugboard replacement mapping.
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
		rotors.setPositions(initPositions);
	} // end reset method
	
	/**
	 * Sets the EnigmaMachine's positions to the ones provided without
	 * changing the initial rotor position settings. 
	 * 
	 * @param newPositions
	 *            Array of 3-4 character array representing the new positions
	 *            the rotors are to be set to.
	 */
	public void setPositions(char[] newPositions) {
		rotors.setPositions(newPositions);
	} // end setPositions method
	
	/**
	 * Resets the initial positions. 
	 * 
	 * @param newInit
	 */
	public void setInitPositions(char[] newInit) {
		initPositions = newInit;
	}
	
	/**
	 * Changes the Rotor and Reflector choices of the Enigma.
	 * 
	 * @param rotorChoices
	 *            Array of 3-4 integers numbered 0-9 to determine which rotors
	 *            to be included in the Enigma.
	 * @param reflectorChoice
	 *            An integer from 0-3 indicating which of the four available
	 *            reflectors to include.
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
	 *            String indicating the plugboard replacement mapping.
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
	 * Returns Rotors so the reset panel can notify the GUI that they've
	 * changed. 
	 * 
	 * @return	Array of ints representing the rotors being used. 
	 */
	public int[] getRotors() {
		return rotorArray;
	}
	
	/**
	 * Returns ring settings for resets. 
	 * 
	 * @return Array of 3-4 chars representing the ring positions.
	 */
	public char[] getRingSettings() {
		return rotors.getRingSettings();
	}
	
	/**
	 * Returns the reflector settings for resets. 
	 * 
	 * @return int representing the reflector choice. 
	 */
	public int getReflector() {
		return rotors.getReflector();
	}
	
	public String getPlugboard() {
		if (plugboard == null) {
			return "";
		}
		else {
			return plugboard.getPlugboardMap();
		}
	}
} // end EnigmaMachine class
