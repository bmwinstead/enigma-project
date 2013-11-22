/**
 * QuadgramStatAnalyzer.java
 * @author - Walter Adolph
 * @date - Nov 21, 2013
 * This attempts to decrypt an Enigma message using quadgram statistics as described at the below websites:
 * http://practicalcryptography.com/cryptanalysis/breaking-machine-ciphers/cryptanalysis-enigma/
 * http://practicalcryptography.com/cryptanalysis/text-characterisation/quadgrams/
 * 
 * Limitations:
 * 
 */
package decoders;

import misc.Logger;
import nlp.Corpus;
import enigma.EnigmaMachine;
import enigma.Plugboard;
import enigma.Rotor;
import enigma.Rotors;

public class QuadgramStatAnalyzer {
	private Corpus database;
	private int[] rotorTypeResults;
	private char[] ringSettingResults;
	private char[] rotorOffsetResults;
	private Plugboard plugboardResults = null;
	
	public QuadgramStatAnalyzer(Corpus corpus) {
		database = corpus;
		
		rotorTypeResults = new int[3];
		ringSettingResults = new char[3];
		rotorOffsetResults = new char[3];
		
		ringSettingResults[0] = 'A';
		ringSettingResults[1] = 'A';
		ringSettingResults[2] = 'A';
		
		rotorOffsetResults[0] = 'A';
		rotorOffsetResults[1] = 'A';
		rotorOffsetResults[2] = 'A';
	}
	
	public String decryptMessage(String message) {
		Logger.makeEntry("Starting quadgram analysis...", true);
		long startTime = System.currentTimeMillis();
		
		String result = determineRotorOrder(message);
		
		Logger.makeEntry("Quadgram analysis complete.", true);
		long endTime = System.currentTimeMillis();
		Logger.makeEntry("Analysis took " + (endTime - startTime) + " milliseconds to complete.", true);
		
		return result;
	}
	
	// Step 1: Determine best possible rotor order.
	public String determineRotorOrder(String message) {
		double bestValue = Double.NEGATIVE_INFINITY;
		String result = message;
		
		Logger.makeEntry("Determining rotor order...", true);
		long startTime = System.currentTimeMillis();
		
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (i != j) { // Skip equal rotor selections.
					for (int k = 0; k < 5; k++) {
						if (i != k && j != k) { // Skip equal rotor selections.
							// Build machine using default settings.
							int[] rotors = {i, j, k};

							EnigmaMachine bomb = new EnigmaMachine(rotors, 0, ringSettingResults, rotorOffsetResults);
							
							String cipher = bomb.encryptString(message);
							double testValue = computeProbability(cipher);
							
							if (testValue > bestValue) {
								bestValue = testValue;
								
								rotorTypeResults[0] = i;
								rotorTypeResults[1] = j;
								rotorTypeResults[2] = k;
								
								result = cipher;
								
								Logger.makeEntry("Best rotor selection fit value: " + bestValue, true);
								Logger.makeEntry("Best rotors: " + i + j + k, true);
							} // End best result if
						} // End rotor check if
					} // End left rotor increment for
				} // End rotor check if
			} // End middle rotor increment for
		} // End right rotor increment for
		
		Logger.makeEntry("Completed rotor order search.", true);
		long endTime = System.currentTimeMillis();
		Logger.makeEntry("Search took " + (endTime - startTime) + " milliseconds to complete.", true);
		
		return result;
	}
	
	public double computeProbability(String message) {
		double result = 0.0;
		double floorLog = -3.0 + Math.log10(1.0 / database.getTotalQuadgramCount());	// Floor value for no match.
		int totalCount = database.getTotalQuadgramCount();
		char[] characters = message.toCharArray();
		
		// Compute individual quadgram probabilites.
		for (int index = 0; index < message.length() - 4; index++) {
			String gram = "" + characters[index] + characters[index + 1] + characters[index + 2] + characters[index + 3];
			int count = database.getQuadgramCount(gram);
			double logProb = Math.log10((double)count / totalCount);
			
			if (count > 0)
				result += logProb;
			else
				result += floorLog;	// Prevent addition by negative infinity resulting from log(0).
		}
		
		return result;
	}
	
	public int[] getRotorOrder() {
		return rotorTypeResults;
	}
	
	public char[] getRingSettings() {
		return ringSettingResults;
	}
	
	public char[] getRotorSettings() {
		return rotorOffsetResults;
	}
	
	public Rotor getRotor(int index) {
		return new Rotor(Rotors.rotorWirings[index], Rotors.rotorNotches[index]);
	}
}
