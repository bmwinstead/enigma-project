package main.java.enigma;

/**
 * EnigmaSettings.java
 *    
 * Wrapper class to store an Enigma machine's full settings. Used to capture a
 * candidate state for cryptanalysis testing, or other points when it is useful
 * for passing all Enigma Machine settings at once. 
 * 
 * @author - Walter Adolph
 * @author - Team Enigma
 * @version - 0.9
 * - Nov 22, 2013
 */
public class EnigmaSettings implements Comparable<EnigmaSettings> {
	private int[] rotors;
	private int reflector;
	private char[] ringSettings;
	private char[] indicatorSettings;
	private String plugboardMap;
	private int updateType;
	
	private double fitnessScore;
	
	/**
	 * Constructor that sets all possible Enigma settings.
	 * 
	 * @param newRotors
	 *            Array of 3-4 integers numbered 0-9 to determine which rotors
	 *            to be included in the Enigma. The rotors are mapped as
	 *            follows: 
	 *            	0 - Rotor I; 
	 *            	1 - Rotor II; 
	 *            	2 - Rotor III; 
	 *            	3 - Rotor IV; 
	 *            	4 - Rotor V; 
	 *            	5 - Rotor VI; 
	 *            	6 - Rotor VII; 
	 *            	7 - Rotor VIII;
	 *              9 - Rotor Beta; 
	 *              10 - Rotor Gamma
	 * @param newRingSettings
	 *            An array of 3-4 characters indicating the rotors' ring
	 *            settings.
	 * @param newIndicatorSettings
	 *            An array of 3-4 characters indicating the rotors' initial
	 *            positions.
	 * @param reflectorIndex
	 *            An integer from 0-3 indicating which of the four available
	 *            reflectors to include. The reflectors are mapped as follows: 
	 *            	0 - Reflector B; 
	 *            	1 - Reflector C; 
	 *            	2 - Reflector B thin; 
	 *            	3 - Reflector C thin
	 * @param newMap
	 *            String indicating the plugboard replacement mapping. Letters
	 *            are swapped with their adjacent letters. For example, a string
	 *            of "ABCD" swaps A's with B's (and vice-versa) and C's with
	 *            D's.
	 */
	public EnigmaSettings(int[] newRotors, char[] newRingSettings, char[] newIndicatorSettings, int reflectorIndex, String newMap) {
		rotors = newRotors.clone();
		ringSettings = newRingSettings.clone();
		indicatorSettings = newIndicatorSettings.clone();
		reflector = reflectorIndex;
		plugboardMap = newMap;
		
		fitnessScore = Double.NEGATIVE_INFINITY;
	}
	
	/**
	 * Constructor that does not include the plugboard. 
	 * 
	 * @param newRotors
	 * 			  Array of 3-4 integers numbered 0-9 to determine which rotors
	 *            to be included in the Enigma. The rotors are mapped as
	 *            follows: 
	 *            	0 - Rotor I; 
	 *            	1 - Rotor II; 
	 *            	2 - Rotor III; 
	 *            	3 - Rotor IV; 
	 *            	4 - Rotor V; 
	 *            	5 - Rotor VI; 
	 *            	6 - Rotor VII; 
	 *            	7 - Rotor VIII;
	 *              9 - Rotor Beta; 
	 *              10 - Rotor Gamma
	 * @param newRingSettings
	 *            An array of 3-4 characters indicating the rotors' ring
	 *            settings.
	 * @param newIndicatorSettings
	 *            An array of 3-4 characters indicating the rotors' initial
	 *            positions.
	 * @param reflectorIndex
	 *            An integer from 0-3 indicating which of the four available
	 *            reflectors to include. The reflectors are mapped as follows: 
	 *            	0 - Reflector B; 
	 *            	1 - Reflector C; 
	 *            	2 - Reflector B thin; 
	 *            	3 - Reflector C thin
	 */
	public EnigmaSettings(int[] newRotors, char[] newRingSettings, char[] newIndicatorSettings, int reflectorIndex) {
		this(newRotors.clone(), newRingSettings.clone(), newIndicatorSettings.clone(), reflectorIndex, "");
	}
	
	/**
	 * Constructor that does not include plugboard or ring settings. 
	 * 
	 * @param newRotors
	 *            Array of 3-4 integers numbered 0-9 to determine which rotors
	 *            to be included in the Enigma. The rotors are mapped as
	 *            follows: 
	 *            	0 - Rotor I; 
	 *            	1 - Rotor II; 
	 *            	2 - Rotor III; 
	 *            	3 - Rotor IV; 
	 *            	4 - Rotor V; 
	 *            	5 - Rotor VI; 
	 *            	6 - Rotor VII; 
	 *            	7 - Rotor VIII;
	 *              9 - Rotor Beta; 
	 *              10 - Rotor Gamma
	 * @param newIndicatorSettings
	 *            An array of 3-4 characters indicating the rotors' initial
	 *            positions.
	 * @param reflectorIndex
	 *            An integer from 0-3 indicating which of the four available
	 *            reflectors to include. The reflectors are mapped as follows: 
	 *            	0 - Reflector B; 
	 *            	1 - Reflector C; 
	 *            	2 - Reflector B thin; 
	 *            	3 - Reflector C thin
	 */
	public EnigmaSettings(int[] newRotors, char[] newIndicatorSettings, int reflectorIndex) {
		char[] defaultRings = new char[newRotors.length];

		for (int i = 0; i < defaultRings.length; i++) {
			defaultRings[i] = 'A';
		}
		
		rotors = newRotors.clone();
		ringSettings = defaultRings;
		indicatorSettings = newIndicatorSettings.clone();
		reflector = reflectorIndex;
		plugboardMap = "";
	}
	
	/**
	 * Constructor for storing only the rotors and reflector. 
	 * 
	 * @param newRotors
	 *            Array of 3-4 integers numbered 0-9 to determine which rotors
	 *            to be included in the Enigma. The rotors are mapped as
	 *            follows: 
	 *            	0 - Rotor I; 
	 *            	1 - Rotor II; 
	 *            	2 - Rotor III; 
	 *            	3 - Rotor IV; 
	 *            	4 - Rotor V; 
	 *            	5 - Rotor VI; 
	 *            	6 - Rotor VII; 
	 *            	7 - Rotor VIII;
	 *              9 - Rotor Beta; 
	 *              10 - Rotor Gamma
	 * @param reflectorIndex
	 * 	          An integer from 0-3 indicating which of the four available
	 *            reflectors to include. The reflectors are mapped as follows: 
	 *            	0 - Reflector B; 
	 *            	1 - Reflector C; 
	 *            	2 - Reflector B thin; 
	 *            	3 - Reflector C thin
	 */
	public EnigmaSettings(int[] newRotors, int reflectorIndex) {
		char[] defaultSettings = new char[newRotors.length];

		for (int i = 0; i < defaultSettings.length; i++) {
			defaultSettings[i] = 'A';
		}
		
		rotors = newRotors.clone();
		ringSettings = defaultSettings.clone();
		indicatorSettings = defaultSettings.clone();
		reflector = reflectorIndex;
		plugboardMap = "";
	}
	
	/**
	 * Constructor that includes only the rotors, reflector, and plugboard. 
	 * 
	 * @param newRotors
	 *            Array of 3-4 integers numbered 0-9 to determine which rotors
	 *            to be included in the Enigma. The rotors are mapped as
	 *            follows: 
	 *            	0 - Rotor I; 
	 *            	1 - Rotor II; 
	 *            	2 - Rotor III; 
	 *            	3 - Rotor IV; 
	 *            	4 - Rotor V; 
	 *            	5 - Rotor VI; 
	 *            	6 - Rotor VII; 
	 *            	7 - Rotor VIII;
	 *              9 - Rotor Beta; 
	 *              10 - Rotor Gamma
	 * @param reflectorIndex
	 * 	          An integer from 0-3 indicating which of the four available
	 *            reflectors to include. The reflectors are mapped as follows: 
	 *            	0 - Reflector B; 
	 *            	1 - Reflector C; 
	 *            	2 - Reflector B thin; 
	 *            	3 - Reflector C thin
	 * @param newMap
	 *            String indicating the plugboard replacement mapping. Letters
	 *            are swapped with their adjacent letters. For example, a string
	 *            of "ABCD" swaps A's with B's (and vice-versa) and C's with
	 *            D's.
	 */
	public EnigmaSettings(int[] newRotors, int reflectorIndex, String newMap) {
		this (newRotors, reflectorIndex);
		plugboardMap = newMap;
	}
	
	/**
	 * Constructor that includes all Enigma Settings, plus the "Update Type".
	 * Used for clearing and resetting the Enigma and the GUI. 
	 * 
	 * @param newRotors
	 *            Array of 3-4 integers numbered 0-9 to determine which rotors
	 *            to be included in the Enigma. The rotors are mapped as
	 *            follows: 
	 *            	0 - Rotor I; 
	 *            	1 - Rotor II; 
	 *            	2 - Rotor III; 
	 *            	3 - Rotor IV; 
	 *            	4 - Rotor V; 
	 *            	5 - Rotor VI; 
	 *            	6 - Rotor VII; 
	 *            	7 - Rotor VIII;
	 *              9 - Rotor Beta; 
	 *              10 - Rotor Gamma
	 * @param newRingSettings
	 *            An array of 3-4 characters indicating the rotors' ring
	 *            settings.
	 * @param newIndicatorSettings
	 *            An array of 3-4 characters indicating the rotors' initial
	 *            positions.
	 * @param reflectorIndex
	 *            An integer from 0-3 indicating which of the four available
	 *            reflectors to include. The reflectors are mapped as follows: 
	 *            	0 - Reflector B; 
	 *            	1 - Reflector C; 
	 *            	2 - Reflector B thin; 
	 *            	3 - Reflector C thin
	 * @param newMap
	 *            String indicating the plugboard replacement mapping. Letters
	 *            are swapped with their adjacent letters. For example, a string
	 *            of "ABCD" swaps A's with B's (and vice-versa) and C's with
	 *            D's.
	 * @param newUpdateType
	 *            Integer indicating what type of update or reset is to be
	 *            performed. 0 means that only the rotor indicators are to be
	 *            updated. 1 means that the GUI is to be fully reset. 2 means
	 *            that only the text boxes are to be cleared. 
	 */
	public EnigmaSettings(int[] newRotors, char[] newRingSettings,
			char[] newIndicatorSettings, int reflectorIndex, String newMap,
			int newUpdateType) {
		rotors = newRotors.clone();
		ringSettings = newRingSettings.clone();
		indicatorSettings = newIndicatorSettings.clone();
		reflector = reflectorIndex;
		plugboardMap = newMap;
		updateType = newUpdateType;

		fitnessScore = Double.NEGATIVE_INFINITY;
	}
	
	/**
	 * Constructor for default settings and no update type. 
	 */
	public EnigmaSettings() {
		int[] defaultRotors = {0, 1, 2};
		char[] defaultSettings = {'A', 'A', 'A'};
		
		rotors = defaultRotors;
		ringSettings = defaultSettings.clone();
		indicatorSettings = defaultSettings.clone();
		reflector = 0;
		plugboardMap = "";
	}
	
	/**
	 * Creates a deep-copy of the Enigma Settings, except for the updateType. 
	 * 
	 * @return	Copied EnigmaSettings. 
	 */
	public EnigmaSettings copy() {
		EnigmaSettings result = new EnigmaSettings(rotors, ringSettings, indicatorSettings, reflector, plugboardMap);
		result.setFitnessScore(fitnessScore);
		
		return result;
	}

	/**
	 * Returns the fitnessScore of the current collected EnigmaSettings.
	 * 
	 * @return the fitnessScore
	 * 			Double indicating the likelihood that this setting combination
	 * 			is a match (used for cryptanalysis functions). 
	 */
	public double getFitnessScore() {
		return fitnessScore;
	}

	/**
	 * @param fitnessScore the fitnessScore to set
	 */
	public void setFitnessScore(double fitnessScore) {
		this.fitnessScore = fitnessScore;
	}

	/**
	 * @return the reflector.
	 * 			  An integer from 0-3 indicating which of the four available
	 *            reflectors to include. The reflectors are mapped as follows: 
	 *            	0 - Reflector B; 
	 *            	1 - Reflector C; 
	 *            	2 - Reflector B thin; 
	 *            	3 - Reflector C thin
	 */
	public int getReflector() {
		return reflector;
	}

	/**
	 * @param reflector the reflector to set
	 */
	public void setReflector(int reflector) {
		this.reflector = reflector;
	}

	/**
	 * @return the rotors.
	 * 			  Array of 3-4 integers numbered 0-9 to determine which rotors
	 *            to be included in the Enigma. The rotors are mapped as
	 *            follows: 
	 *            	0 - Rotor I; 
	 *            	1 - Rotor II; 
	 *            	2 - Rotor III; 
	 *            	3 - Rotor IV; 
	 *            	4 - Rotor V; 
	 *            	5 - Rotor VI; 
	 *            	6 - Rotor VII; 
	 *            	7 - Rotor VIII;
	 *              9 - Rotor Beta; 
	 *              10 - Rotor Gamma
	 */
	public int[] getRotors() {
		return rotors.clone();
	}

	/**
	 * @return the ringSettings. Array of 3-4 characters indicating the rotors'
	 * 			ring settings. 
	 */
	public char[] getRingSettings() {
		return ringSettings.clone();
	}

	/**
	 * @return the indicatorSettings. Array of 3-4 characters indicating the
	 * 			rotors' current positions. 
	 */
	public char[] getIndicatorSettings() {
		return indicatorSettings.clone();
	}

	/**
	 * @return the plugboardMap
	 * 			  String indicating the plugboard replacement mapping. Letters
	 *            are swapped with their adjacent letters. For example, a string
	 *            of "ABCD" swaps A's with B's (and vice-versa) and C's with
	 *            D's.
	 */
	public String getPlugboardMap() {
		return plugboardMap;
	}
	
	/**
	 * 
	 * @return updateType. Used for passing settings among GUI components. 
	 * 			  Integer indicating what type of update or reset is to be
	 *            performed. 0 means that only the rotor indicators are to be
	 *            updated. 1 means that the GUI is to be fully reset. 2 means
	 *            that only the text boxes are to be cleared. 
	 */
	public int getUpdateType() {
		return updateType;
	}

	/**
	 * @param rotors the rotors to set
	 */
	public void setRotors(int[] rotors) {
		this.rotors = rotors.clone();
	}

	/**
	 * @param ringSettings the ringSettings to set
	 */
	public void setRingSettings(char[] ringSettings) {
		this.ringSettings = ringSettings.clone();
	}

	/**
	 * @param indicatorSettings the indicatorSettings to set
	 */
	public void setIndicatorSettings(char[] indicatorSettings) {
		this.indicatorSettings = indicatorSettings.clone();
	}

	/**
	 * @param plugboardMap the plugboardMap to set
	 */
	public void setPlugboardMap(String plugboardMap) {
		this.plugboardMap = plugboardMap;
	}

	/**
	 * Creates a new EnigmaMachine from the saved settings. 
	 * 
	 * @return	Newly created EnigmaMachine
	 */
	public EnigmaMachine createEnigmaMachine() {
		return new EnigmaMachine(rotors.clone(), reflector, ringSettings.clone(), indicatorSettings.clone(), plugboardMap);
	}
	
	/**
	 * Returns "true" if the settings saved are for a 3-rotor machine, false
	 * otherwise. 
	 * 
	 * @return	"true" if the settings saved are for a 3-rotor machine, false
	 * 			if for a 4-rotor machine. 
	 */
	public boolean isThreeRotor() {
		return (rotors.length == 3) ? true : false;
	}
	
	/**
	 * Returns a string indicating the current rotor order. 
	 * 
	 * @return	String indicating current rotor order. 
	 */
	public String printWheelOrder() {
		String result = "";
		
		for (int i = 0; i < rotors.length; i++) {
			result += (rotors[i] + 1);
		}
		return result + " ";
	}
	
	/**
	 * Returns a string indicating the current Ring Settings.
	 * 
	 * @return	String indicating the current Ring Settings. 
	 */
	public String printRingSettings() {
		String result = "";
		
		for (int i = 0; i < ringSettings.length; i++) {
			result += ringSettings[i];
		}
		return result + " ";
	}
	
	/**
	 * Returns a string indicating the initial rotor indicators. 
	 * 
	 * @return	String indicating the initial rotor indicators. 
	 */
	public String printIndicators() {
		String result = "";
		
		for (int i = 0; i < indicatorSettings.length; i++) {
			result += indicatorSettings[i];
		}
		return result + " ";
	}
	
	/**
	 * Returns a string indicating which Reflector is being used.
	 * 
	 * @return	String indicating which reflector is being used. 
	 */
	public String printReflector() {
		switch(reflector) {
		case 0:
			return "B";
		case 1:
			return "C";
		case 2:
			return "B Thin";
		case 3:
			return "C Thin";
		default:
			return "";
		}
	}
	
	/**
	 * A String with the plugboard pairs with spaces in between.
	 * 
	 * @return	String with the plugboard pairs with spaces in between.
	 */
	public String printPlugboard() {
		String result = "";
		char[] value = plugboardMap.toCharArray();
		
		for (int index = 0; index < plugboardMap.length(); index++) {
			result += "" + value[index];
			
			if (index % 2 != 0) {
				result += " ";
			}
		}
		
		return result;
	}
	
	/**
	 * Returns a string of all settings. Used for logging purposes. 
	 * 
	 * @return	String of all settings. 
	 */
	public String printSettings() {
		return printWheelOrder() + printReflector() + " " + printRingSettings() + printIndicators() + "[" + printPlugboard() + "] - " + fitnessScore;
	}

	/**
	 * Allows the EnigmaSettings to be sorted according to their fitness 
	 * scores.
	 */
	public int compareTo(EnigmaSettings settings) {
		if (fitnessScore > settings.getFitnessScore())
			return 1;
		else if (fitnessScore < settings.getFitnessScore())
			return -1;
		else
			return 0;
	}
}
