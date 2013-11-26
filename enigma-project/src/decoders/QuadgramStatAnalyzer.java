/**
 * QuadgramStatAnalyzer.java
 * @author - Walter Adolph
 * @date - Nov 21, 2013
 * This attempts to decrypt an Enigma message using quadgram statistics algorithm as described at the below websites:
 * 
 * References:
 * http://practicalcryptography.com/cryptanalysis/breaking-machine-ciphers/cryptanalysis-enigma/
 * http://practicalcryptography.com/cryptanalysis/text-characterisation/quadgrams/
 * 
 * Step 1: Determine best rotor wheel order and indicator settings, saving each consecutive best result.
 * Step 2: Determine best ring setting by cycling through all rotor combinations from the candidates saved in step 1.
 * Step 3: Determine plugboard connections by brute force.
 * 
 * Improvements:
 * Reset best fitness score for each reflector combination to avoid biasing the analyzer to reflector B.
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
import enigma.Plugboard;
import enigma.Rotor;
import enigma.Rotors;

public class QuadgramStatAnalyzer {
	private Corpus database;						// Gram database to get gram counts.
	private EnigmaSettings bestResult;				// Best total result.
	
	private Deque<EnigmaSettings> resultsList;		// Used to hold list of best results.
	
	private double bestScore;						// Best fitness score.
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
		
		bestScore = Double.NEGATIVE_INFINITY;
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
			//determinePlugboardSettings(candidate, message); Disabled pending testing.
		}
		
		log.makeEntry("Quadgram analysis complete.", true);
		log.makeEntry("Results:", true);
		log.makeEntry("Decrypted message: " + messageResult, true);
		
		log.makeEntry("Wheel order: " + bestResult.printWheelOrder(), true);
		log.makeEntry("Best reflector: " + bestResult.printReflector(), true);
		log.makeEntry("Ring settings: " + bestResult.printRingSettings(), true);
		log.makeEntry("Rotor indicators: " + bestResult.printIndicators(), true);
		log.makeEntry("Plugboard settings: " + bestResult.printPlugboard(), true);
		log.makeEntry("Quadgram fitness score: " + bestScore, true);
		
		long endTime = System.currentTimeMillis();
		log.makeEntry("Analysis took " + (endTime - startTime) + " milliseconds to complete.", true);
		log.closeFile();
	}
	
	// Step 1: Determine best possible rotor and reflector order.
	public void determineRotorOrder(String message) {
		log.makeEntry("Determining rotor order...", false);
		long startTime = System.currentTimeMillis();
		
		// TODO: Chenge reflector counts back once complete with plugboard testing.
		for (int reflector = 0; reflector < 1; reflector++) {
			log.makeEntry("Testing Reflector: " + reflector, true);
			
			// Reset best score to catch more candidates for each reflector.
			bestScore = Double.NEGATIVE_INFINITY;
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
					
					if (testValue > bestScore) {
						result = true;
						bestScore = testValue;
						
						resultsList.add(new EnigmaSettings(rotors, rotorTestSettings, reflector));
						
						bestResult.setRotors(rotors);
						bestResult.setIndicatorSettings(rotorTestSettings);
						bestResult.setReflector(reflector);
						//bestResult.setPlugboardMap(settings.getPlugboardMap());
						
						messageResult = cipher;
						
						log.makeEntry("Best rotor, reflector, and indicator fit value: " + bestScore, true);
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
					
					if (testValue > bestScore) {
						result = true;
						
						bestScore = testValue;
					
						bestResult.setRotors(settings.getRotors());
						bestResult.setRingSettings(ringTestSettings);
						bestResult.setIndicatorSettings(rotorTestSettings);
						bestResult.setReflector(settings.getReflector());
						bestResult.setPlugboardMap(settings.getPlugboardMap());
						
						messageResult = cipher;
						
						settings.setRingSettings(ringTestSettings);
						settings.setIndicatorSettings(rotorTestSettings);
						
						log.makeEntry("Best ring setting fit value: " + bestScore, true);
						log.makeEntry("Best ring settings: " + bestResult.printRingSettings(), true);
						log.makeEntry("Best rotor indicators: " + bestResult.printIndicators(), true);
					} // End best result if
				} // End right ring for
			} // End middle ring for
		} // End left ring for
		
		log.makeEntry("Completed ring setting search.", true);
		long endTime = System.currentTimeMillis();
		log.makeEntry("Search took " + (endTime - startTime) + " milliseconds to complete.", false);
		
		return result;
	}
	
	// Step 3: Determine plugboard settings.
	public void determinePlugboardSettings(EnigmaSettings settings, String message) {
		String result = "";
		char[] candidates = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		
		char bestLeft;
		char bestRight;
		double controlValue;
		
		EnigmaMachine bomb = settings.createEnigmaMachine();
		
		log.makeEntry("Determining plugboard settings...", true);
		
		do { // while there are improvements to be found.
			bestLeft = '!';
			bestRight = '!';

			// Compute control probability.
			controlValue = computeProbability(bomb.encryptString(message));
			bomb.reset();	// Reset for subsequent encryption.
			
			for (int left = 0; left < 26; left++) {
				for (int right = 0; right < 26; right++) {
					if (left != right && candidates[left] != '!' && candidates[right] != '!') {	// Ignore same letter combinations, and previously found steckers.
						char testLeft = (char) ('A' + left);
						char testRight = (char) ('A' + right);
						
						Plugboard testBoard = new Plugboard("" + testLeft + testRight);
						
						// Implement test plugboard pair.
						String testMessage = "";
						
						for (char character: message.toCharArray()) {
							testMessage += testBoard.matchChar(character);
						}
						
						String cipher = bomb.encryptString(testMessage);
						bomb.reset();
						testMessage = "";
						
						// Reverse test plugboard pair.
						for (char character: cipher.toCharArray()) {
							testMessage += testBoard.matchChar(character);
						}
						
						// Compute test probability.
						double testValue = computeProbability(testMessage);
						
						// Find best plugboard pair.
						if (testValue > controlValue) {
							controlValue = testValue;
							bestLeft = testLeft;
							bestRight = testRight;
						} // End best value saving if
					} // End invalid combination rejection if
				} // End right letter for
			} // End left letter for
			
			// If improvements are found over the original decryption save the best result of the pass and remove the candidates.
			if (bestLeft != '!' && bestRight != '!') {
				log.makeEntry("Found plugboard pair: " + bestLeft + bestRight, true);
				result += "" + bestLeft + bestRight;
				candidates[bestLeft - 'A'] = '!';
				candidates[bestRight - 'A'] = '!';
				settings.setPlugboardMap(result);
				
				bomb = settings.createEnigmaMachine();
			}
		} while (bestLeft != '!' && bestRight != '!'); // Continue until no further gain in fitness can be achieved.
		
		if (controlValue > bestScore) {
			bestScore = controlValue;
		
			bestResult.setRotors(settings.getRotors());
			bestResult.setRingSettings(settings.getRingSettings());
			bestResult.setIndicatorSettings(settings.getIndicatorSettings());
			bestResult.setReflector(settings.getReflector());
			bestResult.setPlugboardMap(settings.getPlugboardMap());
			
			messageResult = bomb.encryptString(message);
			
			log.makeEntry("Best plugboard setting fit value: " + bestScore, true);
			log.makeEntry("Best plugboard settings: " + bestResult.getPlugboardMap(), true);
		} // End best result if
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
	
	public String getPlugboard() {
		return bestResult.getPlugboardMap();
	}
	
	public String getDecryptedMessage() {
		return messageResult;
	}
	
	public Rotor getRotor(int index) {
		return new Rotor(Rotors.rotorWirings[index], Rotors.rotorNotches[index]);
	}
}
