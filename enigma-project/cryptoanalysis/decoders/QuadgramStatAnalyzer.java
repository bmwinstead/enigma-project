/**
 * QuadgramStatAnalyzer.java
 * @author - Walter Adolph
 * @date - Nov 21, 2013
 * This attempts to decrypt an Enigma message using quadgram statistics as described at the below websites:
 * http://practicalcryptography.com/cryptanalysis/breaking-machine-ciphers/cryptanalysis-enigma/
 * http://practicalcryptography.com/cryptanalysis/text-characterisation/quadgrams/
 * 
 * Step 1: Determine best rotor wheel order and indicator settings.
 * Step 2: Determine best ring setting.
 * 
 * Limitations:
 * 
 */
package decoders;

import java.util.Random;

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
	
	private double bestValue;
	private String messageResult;
	
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
		
		bestValue = Double.NEGATIVE_INFINITY;
	}
	
	// Decrypt message.
	public void decryptMessage(String message) {
		Logger.makeEntry("Starting quadgram analysis...", true);
		long startTime = System.currentTimeMillis();
		
		// Initial try.
		determineRotorOrder(message);
		determineRingSettings(message);
		
		Random rng = new Random();
		
		// Attempt simulated annealing.
		for (int i = 1; i < 10; i++) {
			for (int j = 0; j < 3; j++) {
				int offset = rng.nextInt(i) - i / 2;
				rotorOffsetResults[j] = (char) ((rotorOffsetResults[j] + offset + 26 - 'A') % 26 + 'A');
			}
			
			for (int j = 0; j < 3; j++) {
				int offset = rng.nextInt(i) - i / 2;
				ringSettingResults[j] = (char) ((ringSettingResults[j] + offset + 26 - 'A') % 26 + 'A');
			}
			
			determineIndicatorSettings(message, rotorTypeResults);
			
			determineRingSettings(message);
		}
		
		Logger.makeEntry("Quadgram analysis complete.", true);
		Logger.makeEntry("Results:", true);
		Logger.makeEntry("Decrypted message: " + messageResult, true);
		Logger.makeEntry("Wheel order: " + rotorTypeResults[0] + rotorTypeResults[1] + rotorTypeResults[2], true);
		Logger.makeEntry("Ring settings: " + ringSettingResults[0] + ringSettingResults[1] + ringSettingResults[2], true);
		Logger.makeEntry("Rotor indicators: " + rotorOffsetResults[0] + rotorOffsetResults[1] + rotorOffsetResults[2], true);
		Logger.makeEntry("Quadgram fitness score: " + bestValue, true);
		
		long endTime = System.currentTimeMillis();
		Logger.makeEntry("Analysis took " + (endTime - startTime) + " milliseconds to complete.", true);
	}
	
	// Step 1: Determine best possible rotor order.
	public void determineRotorOrder(String message) {
		Logger.makeEntry("Determining rotor order...", false);
		long startTime = System.currentTimeMillis();
		
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (i != j) { // Skip equal rotor selections.
					for (int k = 0; k < 5; k++) {
						if (i != k && j != k) { // Skip equal rotor selections.
							// Build machine using default settings.
							int[] rotors = {i, j, k};

							determineIndicatorSettings(message, rotors);
						} // End rotor check if
					} // End left rotor increment for
				} // End rotor check if
			} // End middle rotor increment for
		} // End right rotor increment for
		
		Logger.makeEntry("Completed rotor order search.", false);
		long endTime = System.currentTimeMillis();
		Logger.makeEntry("Search took " + (endTime - startTime) + " milliseconds to complete.", false);
	}
	
	// Step 1: Find best indicator settings.
	public boolean determineIndicatorSettings(String message, int[] rotors) {
		Logger.makeEntry("Determining ring settings...", false);
		long startTime = System.currentTimeMillis();
		
		boolean result = false;
		char[] rotorTestSettings = new char[3];
		
		// Cycle through rotor indicator combinations.
		for (int i = 0; i < 26; i++) {
			for (int j = 0; j < 26; j++) {
				for (int k = 0; k < 26; k++) {

					rotorTestSettings[0] = (char) ('A' + i);
					rotorTestSettings[1] = (char) ('A' + j);
					rotorTestSettings[2] = (char) ('A' + k);
					
					EnigmaMachine bomb = new EnigmaMachine(rotors, 0, ringSettingResults, rotorTestSettings);
					
					String cipher = bomb.encryptString(message);
					double testValue = computeProbability(cipher);
					
					if (testValue > bestValue) {
						result = true;
						bestValue = testValue;
						
						rotorTypeResults[0] = rotors[0];
						rotorTypeResults[1] = rotors[1];
						rotorTypeResults[2] = rotors[2];
						
						rotorOffsetResults[0] = rotorTestSettings[0];
						rotorOffsetResults[1] = rotorTestSettings[1];
						rotorOffsetResults[2] = rotorTestSettings[2];
						
						messageResult = cipher;
						
						Logger.makeEntry("Best rotor and indicator fit value: " + bestValue, true);
						Logger.makeEntry("Best wheel order: " + rotorTypeResults[0] + rotorTypeResults[1] + rotorTypeResults[2], true);
						Logger.makeEntry("Best rotor indicators: " + rotorOffsetResults[0] + rotorOffsetResults[1] + rotorOffsetResults[2], true);
					} // End best result if
				} // End right indicator for
			} // End middle indicator for
		} // End left indicator for
		
		Logger.makeEntry("Completed rotor indicator search.", false);
		long endTime = System.currentTimeMillis();
		Logger.makeEntry("Search took " + (endTime - startTime) + " milliseconds to complete.", false);
		
		return result;
	}
	
	// Step 2: Determine best ring setting.
	public boolean determineRingSettings(String message) {
		Logger.makeEntry("Determining ring settings...", false);
		long startTime = System.currentTimeMillis();
		
		boolean result = false;
		
		char[] ringTestSettings = new char[3];
		char[] rotorTestSettings = new char[3];
		char[] baseRotorSettings = new char[3];
		
		baseRotorSettings[0] = rotorOffsetResults[0];
		baseRotorSettings[1] = rotorOffsetResults[1];
		baseRotorSettings[2] = rotorOffsetResults[2];
		
		// Cycle through rotor indicator combinations.
		for (int i = 0; i < 26; i++) {
			for (int j = 0; j < 26; j++) {
				for (int k = 0; k < 26; k++) {
					ringTestSettings[0] = (char) ('A' + i);
					ringTestSettings[1] = (char) ('A' + j);
					ringTestSettings[2] = (char) ('A' + k);
					
					// Offset the indicators the same as the ring offset. See references above.	
					int leftOffset = baseRotorSettings[0] + i - 'A';
					int middleOffset = baseRotorSettings[1] + j - 'A';
					int rightOffset = baseRotorSettings[2] + k - 'A';
					
					char left = (char)(leftOffset % 26 + 'A');
					char middle = (char)(middleOffset % 26 + 'A');
					char right = (char)(rightOffset % 26 + 'A');
					
					rotorTestSettings[0] = left;
					rotorTestSettings[1] = middle;
					rotorTestSettings[2] = right;
					
					EnigmaMachine bomb = new EnigmaMachine(rotorTypeResults, 0, ringTestSettings, rotorTestSettings);
					
					String cipher = bomb.encryptString(message);
					double testValue = computeProbability(cipher);
					
					if (testValue > bestValue) {
						result = true;
						
						bestValue = testValue;
						
						ringSettingResults[0] = ringTestSettings[0];
						ringSettingResults[1] = ringTestSettings[1];
						ringSettingResults[2] = ringTestSettings[2];
						
						rotorOffsetResults[0] = rotorTestSettings[0];
						rotorOffsetResults[1] = rotorTestSettings[1];
						rotorOffsetResults[2] = rotorTestSettings[2];
						
						messageResult = cipher;
						
						Logger.makeEntry("Best ring setting fit value: " + bestValue, true);
						Logger.makeEntry("Best ring settings: " + ringSettingResults[0] + ringSettingResults[1] + ringSettingResults[2], true);
					} // End best result if
				} // End right ring for
			} // End middle ring for
		} // End left ring for
		
		Logger.makeEntry("Completed ring setting search.", false);
		long endTime = System.currentTimeMillis();
		Logger.makeEntry("Search took " + (endTime - startTime) + " milliseconds to complete.", false);
		
		return result;
	}
	
	// Compute log probability of a message compared to a corpus.
	public double computeProbability(String message) {
		double result = 0.0;
		
		// Set floor value to 1 / 1000 of single instance of gram. See above references.
		double floorLog = -3.0 + Math.log10(1.0 / database.getTotalQuadgramCount());
		
		int totalCount = database.getTotalQuadgramCount();
		char[] characters = message.toCharArray();
		
		// Compute individual quadgram log probabilites.
		// log probabilities are used to avoid numerical underflow. See above references.
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
	
	public String getDecryptedMessage() {
		return messageResult;
	}
	
	public Rotor getRotor(int index) {
		return new Rotor(Rotors.rotorWirings[index], Rotors.rotorNotches[index]);
	}
}
