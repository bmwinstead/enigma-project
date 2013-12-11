package main.java.cryptanalysis.quadbomb;

import java.util.LinkedList;
import java.util.Queue;

import main.java.enigma.EnigmaSettings;

/**
 * QuadBombSettings.java
 * 
 * Convenience class to control how QuadBomb will test candidates.
 * 
 * Array definitions
 * rotorSettings / ringSettings / indicatorSettings:
 * 0 - fourthRotor
 * 1 - leftRotor
 * 2 - middleRotor
 * 3 - rightRotor
 * 
 * Flag values:
 * -2 or '!' - Do not test this item.
 * -1 or '?' - Test all values.
 * Any positive value or character - test that value only.
 * 
 * @author - Walter Adolph
 * @author - Team Enigma
 * @version - 0.9
 * - Dec 6, 2013
 */
public class QuadBombSettings {
	private static int NUM_ROTORS = 8;	// Used for debugging and rapid testing only.
	
	// User-defined constraints.
	private boolean isThreeRotor;
	private int[] rotorSettings = new int[4];
	private int reflectorSetting = 0;
	private char[] ringSettings = new char[4];
	private char[] indicatorSettings = new char[4];
	private String plugboardSetting = "";
	
	private int threadCount;
	private int candidateSize;
	
	/**
	 * Constructor saving settings and determining if testing 3-rotor Enigmas only.
	 * 
	 * @param rotors
	 * 				Array of 3-4 integers (0-9)
	 * @param reflector
	 * 				Integer indicating reflector (0-3)
	 * @param rings
	 * 				Array of integers
	 * @param indicators
	 * 				Array of integers
	 * @param plugboard
	 * 				String
	 * @param threads
	 * 				Integer indicating number of threads to use
	 * @param candidates
	 * 				Integer indicating number of canidates to track. 
	 */
	public QuadBombSettings(int[] rotors, int reflector, int[] rings, int[] indicators, String plugboard, int threads, int candidates) {
		isThreeRotor = rotors[0] == 0 && (reflector == 0 || reflector == 1 || reflector == 2);
		
		// Condition Enigma machine constraints for future use.
		if (isThreeRotor) {
			// Flags to indicate not to test for fourth rotor configurations.
			rotorSettings[0] = -2;	
			ringSettings[0] = '!';
			indicatorSettings[0] = '!';
		}
		else {
			initRotor(0, rotors[0], rings[0], indicators[0]);
		}
		
		initRotor(1, rotors[1], rings[1], indicators[1]);
		initRotor(2, rotors[2], rings[2], indicators[2]);
		initRotor(3, rotors[3], rings[3], indicators[3]);
		
		reflectorSetting = reflector - 1;
		plugboardSetting = plugboard.toUpperCase().replace(" ", "");	// Strip whitespace.
		
		threadCount = threads;
		candidateSize = candidates;
	}
	
	/**
	 * Returns an integer indicating the total number of operations performed.
	 * 
	 * @return	int
	 */
	public int getTotalOperationCount() {
		return getLatchCount() + candidateSize * 2;
	}
	
	/**
	 * Gets a Queue of candidates for rotor, reflector, and indicator testing.
	 * 
	 * @return Queue<EnigmaSettings>
	 */
	public Queue<EnigmaSettings> getRotorReflectorCandidateList() {
		Queue<EnigmaSettings> result = new LinkedList<EnigmaSettings>();
		
		Queue<Integer> reflectors = getTestingReflectors();
		
		// Cycle through each possible reflector.
		while (!reflectors.isEmpty()) {
			int reflector = reflectors.poll();
			
			boolean threeRotorReflector = (reflector < 2) ? true : false;
			
			// Get rotor combinations to use.
			Queue<int[]> rotors = getTestingRotors(threeRotorReflector);
			
			// Cycle through each possible rotor.
			while (!rotors.isEmpty()) {
				int[] rotor = rotors.poll();
				char[] rings = new char[rotor.length];
				
				// Initialize ring constraints, if available.
				if (threeRotorReflector) {
					rings[0] = (ringSettings[1] == '?') ? 'A' : ringSettings[1];
					rings[1] = (ringSettings[2] == '?') ? 'A' : ringSettings[2];
					rings[2] = (ringSettings[3] == '?') ? 'A' : ringSettings[3];
				}
				else {
					rings[0] = (ringSettings[0] == '?') ? 'A' : ringSettings[0];
					rings[1] = (ringSettings[1] == '?') ? 'A' : ringSettings[1];
					rings[2] = (ringSettings[2] == '?') ? 'A' : ringSettings[2];
					rings[3] = (ringSettings[3] == '?') ? 'A' : ringSettings[3];
				}
				
				// Build candidate settings and save result.
				EnigmaSettings candidate = new EnigmaSettings(rotor, reflector, plugboardSetting);
				candidate.setRingSettings(rings);
				
				result.add(candidate);
			}
		}
		
		return result;
	}
	
	/**
	 * Gets number of latches to set for rotor and reflector testing.
	 * 
	 * @return integer indicating number of latches. 
	 */
	public int getLatchCount() {
		// Get three-rotor counts.
		int rotorCount = 1;
		int offset = -2;
		
		if (rotorSettings[1] == -1) {
			rotorCount *= NUM_ROTORS + offset;
			offset++;
		}
		
		if (rotorSettings[2] == -1) {
			rotorCount *= NUM_ROTORS + offset;
			offset++;
		}
		
		if (rotorSettings[3] == -1) {
			rotorCount *= NUM_ROTORS + offset;
			offset++;
		}
		
		int threeReflector = (reflectorSetting == -1) ? 2 : (reflectorSetting == 0 || reflectorSetting == 1) ? 1 : 0;
		int fourReflector = (reflectorSetting == -1) ? 2 : (reflectorSetting == 2 || reflectorSetting == 3) ? 1 : 0;
		
		// Add four-rotor counts.
		if (isThreeRotor) {
			rotorCount *= threeReflector;
		}
		else {
			rotorCount *= fourReflector * ((rotorSettings[0] == -1) ? 2 : 1) + ((rotorSettings[0] == -1) ? threeReflector : 0);
		}
		
		return rotorCount;
	}
	
	/**
	 * @return the threadCount, an integer
	 */
	public int getThreadCount() {
		return threadCount;
	}

	/**
	 * @return the candidateSize, an integer
	 */
	public int getCandidateSize() {
		return candidateSize;
	}

	/**
	 * @return the plugboardSetting, a String
	 */
	public String getPlugboardSetting() {
		return plugboardSetting;
	}

	/**
	 * Takes settings from the spinner values and maps them to facilitate setting loop boundaries.
	 * @param rotorIndex
	 * @param rotor
	 * @param ring
	 * @param indicator
	 */
	private void initRotor(int rotorIndex, int rotor, int ring, int indicator) {
		if (rotorIndex == 0) {	// Apply fourth rotor specific settings.
			rotorSettings[rotorIndex] = (rotor == 1) ? -1 : rotor + 6;
		}
		else {	// Apply first three rotor settings.
			rotorSettings[rotorIndex] = rotor - 1;	// Index adjust.
		}
		
		// Set constraints or flags indicating to test all values.
		ringSettings[rotorIndex] = (ring == 0) ? '?' : (char) (ring - 1 + 'A');
		indicatorSettings[rotorIndex] = (indicator == 0) ? '?' : (char) (indicator - 1 + 'A');
	}
	
	/**
	 * Gets a Queue containing rotor combinations.
	 * @param threeRotorsOnly
	 * @return a Queue of int arrays containing rotor combinations.
	 */
	private Queue<int[]> getTestingRotors(boolean threeRotorsOnly) {
		Queue<int[]> result = new LinkedList<int[]>();
		
		// Compute loop boundaries.
		int fourthStart = (rotorSettings[0] == -1) ? 8 : rotorSettings[0];
		int leftStart = (rotorSettings[1] == -1) ? 0 : rotorSettings[1];
		int middleStart = (rotorSettings[2] == -1) ? 0 : rotorSettings[2];
		int rightStart = (rotorSettings[3] == -1) ? 0 : rotorSettings[3];
		
		int fourthEnd = (rotorSettings[0] == -1) ? 10 : rotorSettings[0] + 1;
		int leftEnd = (rotorSettings[1] == -1) ? NUM_ROTORS : rotorSettings[1] + 1;
		int middleEnd = (rotorSettings[2] == -1) ? NUM_ROTORS : rotorSettings[2] + 1;
		int rightEnd = (rotorSettings[3] == -1) ? NUM_ROTORS : rotorSettings[3] + 1;
		
		for (int i = leftStart; i < leftEnd; i++) { 				// Left rotor.
			for (int j = middleStart; j < middleEnd; j++) { 		// Middle rotor.
				if (i != j) { // Skip equal rotor selections.
					for (int k = rightStart; k < rightEnd; k++) { 	// Right rotor.
						if (i != k && j != k) { // Skip equal rotor selections.
							if (threeRotorsOnly) {
								int[] rotors = {i, j, k};
								
								result.add(rotors);
							}
							else {
								for (int l = fourthStart; l < fourthEnd; l++) {	// Test four-rotor configurations.
									int[] rotors = {l, i, j, k};
									
									result.add(rotors);
								} // End fourth rotor loop.
							} // End fourth rotor check.
						} // End equal rotor check.
					} // End equal right rotor check.
				} // End equal rotor check.
			} // End middle rotor check.
		} // end left rotor check.
		
		return result;
	}
	
	/**
	 * Gets a Queue of reflector combinations.
	 * @return a Queue of reflector combinations
	 */
	private Queue<Integer> getTestingReflectors() {
		Queue<Integer> result = new LinkedList<Integer>();
		
		if (reflectorSetting > -1) {
			result.add(reflectorSetting);
		}
		else if (rotorSettings[0] == -1) {
			for (int i = 0; i < 4; i++) {
				result.add(i);
			}
		}
		else {
			result.add(isThreeRotor ? 0 : 2);
			result.add(isThreeRotor ? 1 : 3);
		}
		
		return result;
	}
	
	/**
	 * Gets a queue of indicator values to test.
	 * 
	 * @param threeRotorsOnly
	 * 				boolean. Should be true if it's a confirmed 3-rotor message
	 * @return Array of integers
	 */
	public int[] getTestingIndicators(boolean threeRotorsOnly) {
		int[] result = new int[8];
		
		result[0] = (indicatorSettings[0] == '?') ? 0 : indicatorSettings[0] - 'A';
		result[2] = (indicatorSettings[1] == '?') ? 0 : indicatorSettings[1] - 'A';
		result[4] = (indicatorSettings[2] == '?') ? 0 : indicatorSettings[2] - 'A';
		result[6] = (indicatorSettings[3] == '?') ? 0 : indicatorSettings[3] - 'A';
		
		result[1] = (indicatorSettings[0] == '?') ? 26 : indicatorSettings[0] + 1 - 'A';
		result[3] = (indicatorSettings[1] == '?') ? 26 : indicatorSettings[1] + 1 - 'A';
		result[5] = (indicatorSettings[2] == '?') ? 26 : indicatorSettings[2] + 1 - 'A';
		result[7] = (indicatorSettings[3] == '?') ? 26 : indicatorSettings[3] + 1 - 'A';
		
		if (threeRotorsOnly) {
			result[0] = -1;
			result[1] = -1;
		}
		
		return result;
	}

	/**
	 * 
	 * @param threeRotorOnly
	 * 				boolean indicating whether the machine is a confirmed
	 * 				3-rotor Enigma. 
	 * @return Array of integers. 
	 */
	public int[] getTestingRings(boolean threeRotorOnly) {
		int[] result = new int[8];
		
		result[0] = (ringSettings[0] == '?') ? 0 : ringSettings[0] - 'A';
		result[2] = (ringSettings[1] == '?') ? 0 : ringSettings[1] - 'A';
		result[4] = (ringSettings[2] == '?') ? 0 : ringSettings[2] - 'A';
		result[6] = (ringSettings[3] == '?') ? 0 : ringSettings[3] - 'A';
		
		result[1] = (ringSettings[0] == '?') ? 26 : ringSettings[0] + 1 - 'A';
		result[3] = (ringSettings[1] == '?') ? 26 : ringSettings[1] + 1 - 'A';
		result[5] = (ringSettings[2] == '?') ? 26 : ringSettings[2] + 1 - 'A';
		result[7] = (ringSettings[3] == '?') ? 26 : ringSettings[3] + 1 - 'A';
		
		// Short-circuit the leftmost ring cycle if no constraints are set.
		boolean ringTest = ringSettings[1] == '?' && ringSettings[2] == '?' && ringSettings[3] == '?';
		boolean indicatorTest = indicatorSettings[1] == '?' && indicatorSettings[2] == '?' && indicatorSettings[3] == '?';
		
		if (threeRotorOnly) {
			if (ringTest && indicatorTest) {
				result[2] = 0;
				result[3] = 1;
			}
		}
		else {
			if (ringSettings[0] == '?' && indicatorSettings[0] == '?' && ringTest && indicatorTest) {
				result[0] = 0;
				result[1] = 1;
			}
		}
		
		return result;
	}
	
	/**
	 * Gets an array of booleans, indexed by rotor position, indicating whether the rotor and ring
	 * need to be tandem incremented when testing ring combinations.
	 * @return Array of booleans
	 */
	public boolean[] getTandemStepFlags() {
		boolean[] result = new boolean[4];
		
		result[0] = indicatorSettings[0] == '?' && ringSettings[0] == '?';
		result[1] = indicatorSettings[1] == '?' && ringSettings[1] == '?';
		result[2] = indicatorSettings[2] == '?' && ringSettings[2] == '?';
		result[3] = indicatorSettings[3] == '?' && ringSettings[3] == '?';
		
		return result;
	}
}
