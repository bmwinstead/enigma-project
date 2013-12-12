package main.java.cryptanalysis.quadbomb;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;

import main.java.cryptanalysis.nlp.StatisticsGenerator;
import main.java.enigma.EnigmaMachine;
import main.java.enigma.EnigmaSettings;

/**
 * PlugboardDetector.java
 * 
 * Worker thread to determine plugboard settings. Each combination of possible plugboard settings are tested
 * and scored. If any combinations score better than the pre-tested message, then the best combination is saved
 * and then applied in the next iteration of this check. The process iterates until no improvement in score is found
 * during an iteration. The best result is returned.
 * 
 * @author - Walter Adolph
 * @author - Team Enigma
 * @version - 0.9
 * - Nov 26, 2013
 */
public class PlugboardDetector implements Callable<Boolean> {
	private StatisticsGenerator tester;
	private EnigmaSettings baseCandidate;
	private QuadBombSettings settings;
	private final String message;
	
	private ConcurrentLinkedQueue<EnigmaSettings> resultsList;
	
	/**
	 * Constructor.
	 * 
	 * @param tester
	 * 				StatisticsGenerator
	 * @param configuration
	 * 				EnigmaSettings
	 * @param settings
	 * 				QuadBombSettings
	 * @param resultsList
	 * 				CurrentLinkedQueue<EnigmaSettings>
	 * @param message
	 * 				String
	 */
	public PlugboardDetector(StatisticsGenerator tester, 
			EnigmaSettings candidate, 
			QuadBombSettings settings, 
			ConcurrentLinkedQueue<EnigmaSettings> resultsList, 
			String message) 
	{
		this.tester = tester;
		this.baseCandidate = candidate;
		this.settings = settings;
		this.resultsList = resultsList;
		this.message = message;
	}
	
	/**
	 * Required by Callable. Workhorse method. 
	 */
	public Boolean call() {
		tester.selectFitnessTest(3);
		
		// Set marker table.
		String result = settings.getPlugboardSetting();
		char[] candidates = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		
		char bestLeft;
		char bestRight;
		double controlValue;
		
		EnigmaSettings testSettings = baseCandidate.copy();
		EnigmaSettings candidate = baseCandidate.copy();
		
		testSettings.setPlugboardMap(result);
		
		// Mark out the constraints.
		for(char letter : result.toCharArray()) {
			candidates[letter - 'A'] = '!';
		}
		
		do { // while there are improvements to be found.
			bestLeft = '!';
			bestRight = '!';
			
			// Compute control probability.
			EnigmaMachine bomb = testSettings.createEnigmaMachine();
			controlValue = tester.computeFitnessScore(bomb.encryptString(message));
			
			String currentPlugboard = testSettings.getPlugboardMap();
			
			for (int left = 0; left < 26; left++) {					// Left plugboard pair.
				for (int right = left + 1; right < 26; right++) {	// Right plugboard pair.
					if (Thread.currentThread().isInterrupted()) {	// Allows interrupted thread to terminate.
						return false;
					}
					
					if (candidates[left] != '!' && candidates[right] != '!') {	// Ignore previously found steckers.
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
		
		return true;
	} // End call()
}
