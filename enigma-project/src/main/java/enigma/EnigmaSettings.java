/**
 * EnigmaSettings.java
 * @author - Walter Adolph
 * @author - Team Enigma
 * @version - 0.9
 * @date - Nov 22, 2013
 * 
 * Wrapper class to store an Enigma machine's full settings.
 * Also, this can be used to capture a candidate state for cryptanalysis testing.
 */
package main.java.enigma;


public class EnigmaSettings implements Comparable<EnigmaSettings> {
	private int[] rotors;
	private int reflector;
	private char[] ringSettings;
	private char[] indicatorSettings;
	private String plugboardMap;
	private int updateType;
	
	private double fitnessScore;
	
	// Constructor with all settings.
	public EnigmaSettings(int[] newRotors, char[] newRingSettings, char[] newIndicatorSettings, int reflectorIndex, String newMap) {
		rotors = newRotors.clone();
		ringSettings = newRingSettings.clone();
		indicatorSettings = newIndicatorSettings.clone();
		reflector = reflectorIndex;
		plugboardMap = newMap;
		
		fitnessScore = Double.NEGATIVE_INFINITY;
	}
	
	// Constructor with no plugboard.
	public EnigmaSettings(int[] newRotors, char[] newRingSettings, char[] newIndicatorSettings, int reflectorIndex) {
		this(newRotors.clone(), newRingSettings.clone(), newIndicatorSettings.clone(), reflectorIndex, "");
	}
	
	// Constructor with no plugboard and default ring settings.
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
	
	// Constructor with no plugboard and default indicator and ring settings.
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
	
	// Constructor with default ring and indicator settings.
	public EnigmaSettings(int[] newRotors, int reflectorIndex, String newMap) {
		this (newRotors, reflectorIndex);
		plugboardMap = newMap;
	}
	
	// Constructor that includes updateType (for setting passing among GUI)
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
	
	// Default settings.
	public EnigmaSettings() {
		int[] defaultRotors = {0, 1, 2};
		char[] defaultSettings = {'A', 'A', 'A'};
		
		rotors = defaultRotors;
		ringSettings = defaultSettings.clone();
		indicatorSettings = defaultSettings.clone();
		reflector = 0;
		plugboardMap = "";
	}
	
	// Performs a deep copy of this object.
	public EnigmaSettings copy() {
		EnigmaSettings result = new EnigmaSettings(rotors, ringSettings, indicatorSettings, reflector, plugboardMap);
		result.setFitnessScore(fitnessScore);
		
		return result;
	}

	/**
	 * @return the fitnessScore
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
	 * @return the reflector
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
	 * @return the rotors
	 */
	public int[] getRotors() {
		return rotors.clone();
	}

	/**
	 * @return the ringSettings
	 */
	public char[] getRingSettings() {
		return ringSettings.clone();
	}

	/**
	 * @return the indicatorSettings
	 */
	public char[] getIndicatorSettings() {
		return indicatorSettings.clone();
	}

	/**
	 * @return the plugboardMap
	 */
	public String getPlugboardMap() {
		return plugboardMap;
	}
	
	/**
	 * 
	 * @return updateType. Used for passing settings among GUI components.
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

	// Creates a new EnigmaMachine from the saved settings.
	public EnigmaMachine createEnigmaMachine() {
		return new EnigmaMachine(rotors.clone(), reflector, ringSettings.clone(), indicatorSettings.clone(), plugboardMap);
	}
	
	public boolean isThreeRotor() {
		return (rotors.length == 3) ? true : false;
	}
	
	// Prints out the rotor order in a String.
	public String printWheelOrder() {
		String result = "";
		
		for (int i = 0; i < rotors.length; i++) {
			result += (rotors[i] + 1);
		}
		return result + " ";
	}
	
	// Prints out the ring settings in a String.
	public String printRingSettings() {
		String result = "";
		
		for (int i = 0; i < ringSettings.length; i++) {
			result += ringSettings[i];
		}
		return result + " ";
	}
	
	// Prints out the rotor indicator offset in a String.
	public String printIndicators() {
		String result = "";
		
		for (int i = 0; i < indicatorSettings.length; i++) {
			result += indicatorSettings[i];
		}
		return result + " ";
	}
	
	// Prints the reflector setting.
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
	
	// Prints the plugboard pairs with spaces.
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
	
	// Prints a string representing the settings, for logging purposes.
	public String printSettings() {
		return printWheelOrder() + printReflector() + " " + printRingSettings() + printIndicators() + "[" + printPlugboard() + "] - " + fitnessScore;
	}

	// Allows sorting by fitness score.
	public int compareTo(EnigmaSettings settings) {
		if (fitnessScore > settings.getFitnessScore())
			return 1;
		else if (fitnessScore < settings.getFitnessScore())
			return -1;
		else
			return 0;
	}
}
