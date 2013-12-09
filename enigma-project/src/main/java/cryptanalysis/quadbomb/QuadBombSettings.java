/**
 * QuadBombSettings.java
 * @author - Walter Adolph
 * @author - Team Enigma
 * @version - 0.9
 * @date - Dec 6, 2013
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
 */
package main.java.cryptanalysis.quadbomb;

import java.util.LinkedList;
import java.util.Queue;

import main.java.enigma.EnigmaSettings;


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
	
	// Constructor saving settings and determining if testing 3-rotor Enigmas only.
	public QuadBombSettings(int[] rotors, int reflector, int[] rings, int[] indicators, String plugboard, int threads, int candidates) {
		isThreeRotor = rotors[0] == 0 && (reflector == 0 || reflector == 1 || reflector == 2);
		
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
	
	public int getTotalOperationCount() {
		return getLatchCount() + candidateSize * 2;
	}
	
	// Gets a Queue of candidates for rotor, reflector, and indicator testing.
	public Queue<EnigmaSettings> getRotorReflectorCandidateList() {
		Queue<EnigmaSettings> result = new LinkedList<EnigmaSettings>();
		
		Queue<Integer> reflectors = getTestingReflectors();
		
		while (!reflectors.isEmpty()) {
			int reflector = reflectors.poll();
			
			boolean threeRotorReflector = (reflector < 2) ? true : false;
			
			Queue<int[]> rotors = getTestingRotors(threeRotorReflector);
			
			while (!rotors.isEmpty()) {
				int[] rotor = rotors.poll();
				char[] rings = new char[rotor.length];
				
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
				
				EnigmaSettings candidate = new EnigmaSettings(rotor, reflector, plugboardSetting);
				candidate.setRingSettings(rings);
				
				result.add(candidate);
			}
		}
		
		return result;
	}
	
	// Gets number of latches to set for rotor and reflector testing.
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
	 * @return the threadCount
	 */
	public int getThreadCount() {
		return threadCount;
	}

	/**
	 * @return the candidateSize
	 */
	public int getCandidateSize() {
		return candidateSize;
	}

	/**
	 * @return the plugboardSetting
	 */
	public String getPlugboardSetting() {
		return plugboardSetting;
	}

	// Takes user rotor settings and init. constraint values.
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
	
	// Gets a queue of rotor values to test.
	private Queue<int[]> getTestingRotors(boolean threeRotorsOnly) {
		Queue<int[]> result = new LinkedList<int[]>();
		
		int fourthStart = (rotorSettings[0] == -1) ? 8 : rotorSettings[0];
		int leftStart = (rotorSettings[1] == -1) ? 0 : rotorSettings[1];
		int middleStart = (rotorSettings[2] == -1) ? 0 : rotorSettings[2];
		int rightStart = (rotorSettings[3] == -1) ? 0 : rotorSettings[3];
		
		int fourthEnd = (rotorSettings[0] == -1) ? 10 : rotorSettings[0] + 1;
		int leftEnd = (rotorSettings[1] == -1) ? NUM_ROTORS : rotorSettings[1] + 1;
		int middleEnd = (rotorSettings[2] == -1) ? NUM_ROTORS : rotorSettings[2] + 1;
		int rightEnd = (rotorSettings[3] == -1) ? NUM_ROTORS : rotorSettings[3] + 1;
		
		for (int i = leftStart; i < leftEnd; i++) { // Left rotor.
			for (int j = middleStart; j < middleEnd; j++) { // Middle rotor.
				if (i != j) { // Skip equal rotor selections.
					for (int k = rightStart; k < rightEnd; k++) { // Right rotor.
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
	
	// Gets a queue of reflector values to test.
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
			if (isThreeRotor) {
				result.add(0);
				result.add(1);
			}
			else {
				result.add(2);
				result.add(3);
			}
		}
		
		return result;
	}
	
	// Gets a queue of indicator values to test.
	public Queue<char[]> getTestingIndicators(boolean threeRotorsOnly) {
		Queue<char[]> result = new LinkedList<char[]>();
		
		int fourthStart = (indicatorSettings[0] == '?') ? 0 : indicatorSettings[0] - 'A';
		int leftStart = (indicatorSettings[1] == '?') ? 0 : indicatorSettings[1] - 'A';
		int middleStart = (indicatorSettings[2] == '?') ? 0 : indicatorSettings[2] - 'A';
		int rightStart = (indicatorSettings[3] == '?') ? 0 : indicatorSettings[3] - 'A';
		
		int fourthEnd = (indicatorSettings[0] == '?') ? 26 : indicatorSettings[0] + 1 - 'A';
		int leftEnd = (indicatorSettings[1] == '?') ? 26 : indicatorSettings[1] + 1 - 'A';
		int middleEnd = (indicatorSettings[2] == '?') ? 26 : indicatorSettings[2] + 1 - 'A';
		int rightEnd = (indicatorSettings[3] == '?') ? 26 : indicatorSettings[3] + 1 - 'A';
		
		for (int i = leftStart; i < leftEnd; i++) {
			for (int j = middleStart; j < middleEnd; j++) {
				for (int k = rightStart; k < rightEnd; k++) {
					if (threeRotorsOnly) {
						char[] indicators = {(char) ('A' + i), (char) ('A' + j), (char) ('A' + k)};
						
						result.add(indicators);
					}
					else {
						for (int l = fourthStart; l < fourthEnd; l++) {
							char[] indicators = {(char) ('A' + l), (char) ('A' + i), (char) ('A' + j), (char) ('A' + k)};
							
							result.add(indicators);
						}
					}
				} // End right indicator loop.
			} // End middle indicator loop.
		} // End left indicator loop.
		
		return result;
	}
	
	// Gets a queue of ring settings to test, given an existing set-up.
	public Queue<EnigmaSettings> getTestingRings(EnigmaSettings candidate) {
		Queue<EnigmaSettings> result = new LinkedList<EnigmaSettings>();
		
		int fourthStart = (ringSettings[0] == '?') ? 0 : ringSettings[0] - 'A';
		int leftStart = (ringSettings[1] == '?') ? 0 : ringSettings[1] - 'A';
		int middleStart = (ringSettings[2] == '?') ? 0 : ringSettings[2] - 'A';
		int rightStart = (ringSettings[3] == '?') ? 0 : ringSettings[3] - 'A';
		
		int fourthEnd = (ringSettings[0] == '?') ? 26 : ringSettings[0] + 1 - 'A';
		int leftEnd = (ringSettings[1] == '?') ? 26 : ringSettings[1] + 1 - 'A';
		int middleEnd = (ringSettings[2] == '?') ? 26 : ringSettings[2] + 1 - 'A';
		int rightEnd = (ringSettings[3] == '?') ? 26 : ringSettings[3] + 1 - 'A';
		
		// Short-circuit the leftmost ring cycle if no constraints are set.
		boolean ringTest = ringSettings[1] == '?' && ringSettings[2] == '?' && ringSettings[3] == '?';
		boolean indicatorTest = indicatorSettings[1] == '?' && indicatorSettings[2] == '?' && indicatorSettings[3] == '?';
		
		if (candidate.isThreeRotor()) {
			if (ringTest && indicatorTest) {
				leftStart = 0;
				leftEnd = 1;
			}
		}
		else {
			if (ringSettings[0] == '?' && indicatorSettings[0] == '?' && ringTest && indicatorTest) {
				fourthStart = 0;
				fourthEnd = 1;
			}
		}
		
		// Cycle through ring setting combinations.
		for (int i = leftStart; i < leftEnd; i++) {
			for (int j = middleStart; j < middleEnd; j++) {
				for (int k = rightStart; k < rightEnd; k++) {
					if (candidate.isThreeRotor()) {
						char[] ringTestSettings = new char[3];
						char[] indicatorTestSettings = new char[3];
						
						ringTestSettings[0] = (ringSettings[1] == '?') ? (char) ('A' + i) : ringSettings[1];
						ringTestSettings[1] = (ringSettings[2] == '?') ? (char) ('A' + j) : ringSettings[2];
						ringTestSettings[2] = (ringSettings[3] == '?') ? (char) ('A' + k) : ringSettings[3];
						
						indicatorTestSettings = candidate.getIndicatorSettings();
						
						int leftOffset = indicatorTestSettings[0] + i - 'A';
						int middleOffset = indicatorTestSettings[1] + j - 'A';
						int rightOffset = indicatorTestSettings[2] + k - 'A';
						
						char left = (char)(leftOffset % 26 + 'A');
						char middle = (char)(middleOffset % 26 + 'A');
						char right = (char)(rightOffset % 26 + 'A');
						
						indicatorTestSettings[0] = (indicatorSettings[1] == '?' && ringSettings[1] == '?') ? left : indicatorTestSettings[0];
						indicatorTestSettings[1] = (indicatorSettings[2] == '?' && ringSettings[2] == '?') ? middle : indicatorTestSettings[1];
						indicatorTestSettings[2] = (indicatorSettings[3] == '?' && ringSettings[3] == '?') ? right : indicatorTestSettings[2];
						
						EnigmaSettings test = candidate.copy();
						test.setIndicatorSettings(indicatorTestSettings);
						test.setRingSettings(ringTestSettings);
						
						result.add(test);
					}
					else {
						for (int l = fourthStart; l < fourthEnd; l++) {
							char[] ringTestSettings = new char[4];
							char[] indicatorTestSettings = new char[4];
							
							ringTestSettings[0] = (ringSettings[0] == '?') ? (char) ('A' + l) : ringSettings[0];
							ringTestSettings[1] = (ringSettings[1] == '?') ? (char) ('A' + i) : ringSettings[1];
							ringTestSettings[2] = (ringSettings[2] == '?') ? (char) ('A' + j) : ringSettings[2];
							ringTestSettings[3] = (ringSettings[3] == '?') ? (char) ('A' + k) : ringSettings[3];
							
							indicatorTestSettings = candidate.getIndicatorSettings();
							
							int fourthOffset = indicatorTestSettings[0] + l - 'A';
							int leftOffset = indicatorTestSettings[1] + i - 'A';
							int middleOffset = indicatorTestSettings[2] + j - 'A';
							int rightOffset = indicatorTestSettings[3] + k - 'A';
							
							char fourth = (char)(fourthOffset % 26 + 'A');
							char left = (char)(leftOffset % 26 + 'A');
							char middle = (char)(middleOffset % 26 + 'A');
							char right = (char)(rightOffset % 26 + 'A');
							
							indicatorTestSettings[0] = (indicatorSettings[0] == '?' && ringSettings[0] == '?') ? fourth : indicatorTestSettings[0];
							indicatorTestSettings[1] = (indicatorSettings[1] == '?' && ringSettings[1] == '?') ? left : indicatorTestSettings[1];
							indicatorTestSettings[2] = (indicatorSettings[2] == '?' && ringSettings[2] == '?') ? middle : indicatorTestSettings[2];
							indicatorTestSettings[3] = (indicatorSettings[3] == '?' && ringSettings[3] == '?') ? right : indicatorTestSettings[3];
							
							EnigmaSettings test = candidate.copy();
							test.setIndicatorSettings(indicatorTestSettings);
							test.setRingSettings(ringTestSettings);
							
							result.add(test);
						} // End left ring loop.
					} // End fourth rotor check.
				} // End right ring loop.
			} // End middle ring loop.
		} // End left ring loop.
		return result;
	}
}
