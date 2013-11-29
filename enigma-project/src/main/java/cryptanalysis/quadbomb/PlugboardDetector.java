/**
 * PlugboardDetector.java
 * @author - Walter Adolph
 * @author - Team Enigma
 * @version - 0.9
 * @date - Nov 26, 2013
 * Worker thread to determine plugboard settings.
 */
package main.java.cryptanalysis.quadbomb;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

import main.java.cryptanalysis.nlp.StatisticsGenerator;
import main.java.enigma.EnigmaMachine;
import main.java.enigma.EnigmaSettings;

public class PlugboardDetector implements Runnable {
	private StatisticsGenerator tester;
	private ConcurrentLinkedQueue<EnigmaSettings> resultsList;
	private EnigmaSettings settings;
	private final String message;
	
	private CountDownLatch latch;
	
	public PlugboardDetector(StatisticsGenerator tester, EnigmaSettings settings, ConcurrentLinkedQueue<EnigmaSettings> resultsList, String message, CountDownLatch latch) {
		this.tester = tester;
		this.settings = settings;
		this.resultsList = resultsList;
		this.message = message;
		
		this.latch = latch;
	}
	
	public void run() {
		String result = "";
		char[] candidates = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		
		char bestLeft;
		char bestRight;
		double controlValue;
		//EnigmaMachine bomb = settings.createEnigmaMachine();
		
		EnigmaSettings testSettings = settings.copy();
		EnigmaSettings candidate = settings.copy();
		
		if (settings.getFitnessScore() > -1000)
			testSettings = settings.copy();
		
		do { // while there are improvements to be found.
			bestLeft = '!';
			bestRight = '!';
			
			// Compute control probability.
			EnigmaMachine bomb = testSettings.createEnigmaMachine();
			controlValue = tester.computeFitnessScore(bomb.encryptString(message));
			
			String currentPlugboard = testSettings.getPlugboardMap();
			
			for (int left = 0; left < 26; left++) {
				for (int right = 0; right < 26; right++) {
					if (left != right && candidates[left] != '!' && candidates[right] != '!') {	// Ignore same letter combinations, and previously found steckers.
						char testLeft = (char) ('A' + left);
						char testRight = (char) ('A' + right);
						
						// Implement test plugboard pair.
						
						testSettings.setPlugboardMap(currentPlugboard + testLeft + testRight);
						
						bomb = testSettings.createEnigmaMachine();
						String cipher = bomb.encryptString(message);
						
						// Compute test probability.
						double testValue = tester.computeFitnessScore(cipher);
						
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
				result += "" + bestLeft + bestRight;
				candidates[bestLeft - 'A'] = '!';
				candidates[bestRight - 'A'] = '!';
				testSettings.setPlugboardMap(result);
			}
		} while (bestLeft != '!' && bestRight != '!'); // Continue until no further gain in fitness can be achieved.
		
		candidate.setPlugboardMap(result);
		candidate.setFitnessScore(controlValue);
		
		// Save best indicator result into list for further processing.
		resultsList.add(candidate);
		
		latch.countDown();
	} // End run()
}
