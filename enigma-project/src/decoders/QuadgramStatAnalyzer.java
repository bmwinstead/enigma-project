/**
 * QuadgramStatAnalyzer.java
 * @author - Walter Adolph
 * @date - Nov 21, 2013
 * This attempts to decrypt an Enigma message using quadgram statistics as described at the below websites:
 * 
 * References:
 * http://practicalcryptography.com/cryptanalysis/breaking-machine-ciphers/cryptanalysis-enigma/
 * http://practicalcryptography.com/cryptanalysis/text-characterisation/quadgrams/
 * 
 * Step 1: Determine best rotor wheel order and indicator settings, saving each consecutive best result.
 * Step 2: Determine best ring setting by cycling through all rotor combinations from the candidates saved in step 1.
 * 
 * Limitations:
 * This method does not guarantee a correct result. Essentially, this algorithm is equivalent to a local maxima search in that it
 * first searches the wheel order and indicator settings and saves consecutive best matches. Once that search is exhausted, 
 * then it searches for the best ring setting using the list of best rotor settings previously constructed. This search is limited in that the 
 * ring search is restricted to the candidate rotor settings. It is possible that the correct result is a combination of suboptimal
 * rotor and ring settings, and in these cases the algorithm is expected to fail.
 */
package decoders;

import java.util.Calendar;
import java.util.Deque;
import java.util.LinkedList;

import misc.Logger;
import nlp.Corpus;
import enigma.EnigmaMachine;
import enigma.EnigmaSettings;
import enigma.Rotor;
import enigma.Rotors;

public class QuadgramStatAnalyzer {
	private Corpus database;						// Gram database to get gram counts.
	private EnigmaSettings bestResult;				// Best total result.
	
	private Deque<EnigmaSettings> resultsList;		// Used to hold list of best results.
	
	private double bestValue;						// Best fitness score.
	private String messageResult;					// Best message.
	
	private Logger log;								// Log decryption attempt.
	public QuadgramStatAnalyzer(Corpus corpus) {
		Calendar date = Calendar.getInstance();
		
		String formattedDate = "Quadgram stat attempt " + (date.get(Calendar.MONTH) + 1) + "-" + date.get(Calendar.DAY_OF_MONTH) + "-" + date.get(Calendar.YEAR)
				+ " " + date.get(Calendar.HOUR) + date.get(Calendar.MINUTE) + date.get(Calendar.SECOND) + ".txt";
		
		log = new Logger(formattedDate);
		
		database = corpus;
		
		// Init. default result.
		bestResult = new EnigmaSettings();
		resultsList = new LinkedList<EnigmaSettings>();
		
		bestValue = Double.NEGATIVE_INFINITY;
	}
	
	// Decrypt message.
	public void decryptMessage(String message) {
		log.makeEntry("Starting quadgram analysis...", true);
		long startTime = System.currentTimeMillis();
		
		// Get rotors, indicators, and reflector settings.
		determineRotorOrder(message);
		
		// Try each setting combination discovered from determineRotorOrder() to find best ring settings.
		while (!resultsList.isEmpty()) {
			EnigmaSettings candidate = resultsList.pollLast();
			
			log.makeEntry("Testing candidate:", true);
			log.makeEntry("Wheel order: " + candidate.printWheelOrder(), true);
			log.makeEntry("Best reflector: " + candidate.printReflector(), true);
			log.makeEntry("Ring settings: " + candidate.printRingSettings(), true);
			log.makeEntry("Rotor indicators: " + candidate.printIndicators(), true);
			
			determineRingSettings(candidate, message);
		}
		
		log.makeEntry("Quadgram analysis complete.", true);
		log.makeEntry("Results:", true);
		log.makeEntry("Decrypted message: " + messageResult, true);
		
		log.makeEntry("Wheel order: " + bestResult.printWheelOrder(), true);
		log.makeEntry("Best reflector: " + bestResult.printReflector(), true);
		log.makeEntry("Ring settings: " + bestResult.printRingSettings(), true);
		log.makeEntry("Rotor indicators: " + bestResult.printIndicators(), true);
		log.makeEntry("Quadgram fitness score: " + bestValue, true);
		
		long endTime = System.currentTimeMillis();
		log.makeEntry("Analysis took " + (endTime - startTime) + " milliseconds to complete.", true);
	}
	
	// Step 1: Determine best possible rotor and reflector order.
	public void determineRotorOrder(String message) {
		log.makeEntry("Determining rotor order...", false);
		long startTime = System.currentTimeMillis();
		
		for (int reflector = 0; reflector < 4; reflector++) {
			log.makeEntry("Testing Reflector: " + reflector, true);
			
			for (int i = 0; i < 5; i++) { // Left rotor.
				for (int j = 0; j < 5; j++) { // Middle rotor.
					if (i != j) { // Skip equal rotor selections.
						for (int k = 0; k < 5; k++) { // Right rotor.
							if (i != k && j != k) { // Skip equal rotor selections.
								
								int[] rotors = {i, j, k};
	
								log.makeEntry("Testing Rotor: " + i + j + k, false);
								determineIndicatorSettings(message, rotors, reflector);
							} // End rotor check if
						} // End left rotor for
					} // End rotor check if
				} // End middle rotor for
			} // End right rotor for
		} // End reflector for
		log.makeEntry("Completed rotor order search.", false);
		long endTime = System.currentTimeMillis();
		log.makeEntry("Search took " + (endTime - startTime) + " milliseconds to complete.", false);
	}
	
	// Step 1: Find best indicator settings.
	public boolean determineIndicatorSettings(String message, int[] rotors, int reflector) {
		log.makeEntry("Determining indicator settings...", false);
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

					EnigmaMachine bomb = new EnigmaMachine(rotors, reflector, bestResult.getRingSettings(), rotorTestSettings);
					
					String cipher = bomb.encryptString(message);
					double testValue = computeProbability(cipher);
					
					if (testValue > bestValue) {
						result = true;
						bestValue = testValue;
						
						resultsList.add(new EnigmaSettings(rotors, rotorTestSettings, reflector));
						
						bestResult.setRotors(rotors);
						bestResult.setIndicatorSettings(rotorTestSettings);
						bestResult.setReflector(reflector);
						messageResult = cipher;
						
						log.makeEntry("Best rotor, reflector, and indicator fit value: " + bestValue, true);
						log.makeEntry("Best wheel order: " + bestResult.printWheelOrder(), true);
						log.makeEntry("Best reflector: " + bestResult.printReflector(), true);
						log.makeEntry("Best rotor indicators: " + bestResult.printIndicators(), true);
					} // End best result if
				} // End right indicator for
			} // End middle indicator for
		} // End left indicator for
		
		log.makeEntry("Completed rotor indicator search.", false);
		long endTime = System.currentTimeMillis();
		log.makeEntry("Search took " + (endTime - startTime) + " milliseconds to complete.", false);
		
		return result;
	}
	
	// Step 2: Determine best ring setting.
	public boolean determineRingSettings(EnigmaSettings settings, String message) {
		log.makeEntry("Determining ring settings...", true);
		long startTime = System.currentTimeMillis();
		
		boolean result = false;
		
		char[] ringTestSettings = new char[3];
		char[] rotorTestSettings = new char[3];
		char[] baseRotorSettings = settings.getIndicatorSettings();
		
		// Cycle through ring setting combinations.
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
					
					EnigmaMachine bomb = new EnigmaMachine(settings.getRotors(), settings.getReflector(), ringTestSettings, rotorTestSettings);
					
					String cipher = bomb.encryptString(message);
					double testValue = computeProbability(cipher);
					
					if (testValue > bestValue) {
						result = true;
						
						bestValue = testValue;
						
						bestResult.setRingSettings(ringTestSettings);
						bestResult.setIndicatorSettings(rotorTestSettings);
						bestResult.setReflector(settings.getReflector());
						messageResult = cipher;
						
						log.makeEntry("Best ring setting fit value: " + bestValue, true);
						log.makeEntry("Best ring settings: " + bestResult.printRingSettings(), true);
						log.makeEntry("Best rotor indicators: " + bestResult.printIndicators(), true);
					} // End best result if
				} // End right ring for
			} // End middle ring for
		} // End left ring for
		
		log.makeEntry("Completed ring setting search.", true);
		long endTime = System.currentTimeMillis();
		log.makeEntry("Search took " + (endTime - startTime) + " milliseconds to complete.", false);
		log.closeFile();
		
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
		return bestResult.getRotors();
	}
	
	public int getReflector() {
		return bestResult.getReflector();
	}
	
	public char[] getRingSettings() {
		return bestResult.getRingSettings();
	}
	
	public char[] getRotorSettings() {
		return bestResult.getIndicatorSettings();
	}
	
	public String getDecryptedMessage() {
		return messageResult;
	}
	
	public Rotor getRotor(int index) {
		return new Rotor(Rotors.rotorWirings[index], Rotors.rotorNotches[index]);
	}
}
