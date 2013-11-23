/**
 * EnigmaSettings.java
 * @author - Walter Adolph
 * @author - Team Enigma
 * @version - 0.9
 * @date - Nov 22, 2013
 * 
 * Stores an Enigma's full settings. 
 */
package enigma;

public class EnigmaSettings {
	private int[] rotors;
	private int reflector;
	private char[] ringSettings;
	private char[] indicatorSettings;
	private String plugboardMap;
	
	// Constructor with all settings.
	public EnigmaSettings(int[] newRotors, char[] newRingSettings, char[] newIndicatorSettings, int reflectorIndex, String newMap) {
		rotors = newRotors.clone();
		ringSettings = newRingSettings.clone();
		indicatorSettings = newIndicatorSettings.clone();
		reflector = reflectorIndex;
		plugboardMap = newMap;
	}
	
	// Constructor with no plugboard.
	public EnigmaSettings(int[] newRotors, char[] newRingSettings, char[] newIndicatorSettings, int reflectorIndex) {
		this(newRotors.clone(), newRingSettings.clone(), newIndicatorSettings.clone(), reflectorIndex, "");
	}
	
	// Constructor with no plugboard and default ring settings.
	public EnigmaSettings(int[] newRotors, char[] newIndicatorSettings, int reflectorIndex) {
		char[] defaultRings = {'A', 'A', 'A'};

		rotors = newRotors.clone();
		ringSettings = defaultRings;
		indicatorSettings = newIndicatorSettings.clone();
		reflector = reflectorIndex;
		plugboardMap = "";
	}
	
	// Constructor with no plugboard and default indicator and ring settings.
	public EnigmaSettings(int[] newRotors, int reflectorIndex) {
		char[] defaultSettings = {'A', 'A', 'A'};
		
		rotors = newRotors.clone();
		ringSettings = defaultSettings.clone();
		indicatorSettings = defaultSettings.clone();
		reflector = reflectorIndex;
		plugboardMap = "";
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
		return new EnigmaMachine(rotors.clone(), reflector, ringSettings.clone(), indicatorSettings.clone());
	}
	
	// Prints out the rotor order in a String.
	public String printWheelOrder() {
		return "" + rotors[0] + rotors[1] + rotors[2];
	}
	
	// Prints out the ring settings in a String.
	public String printRingSettings() {
		return "" + ringSettings[0] + ringSettings[1] + ringSettings[2];
	}
	
	// Prints out the rotor indicator offset in a String.
	public String printIndicators() {
		return "" + indicatorSettings[0] + indicatorSettings[1] + indicatorSettings[2];
	}
	
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
}
