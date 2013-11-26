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

import main.java.enigma.EnigmaMachine;
import main.java.enigma.EnigmaSettings;
import main.java.enigma.Plugboard;

public class PlugboardDetector implements Runnable {
	private QuadbombManager manager;
	private ConcurrentLinkedQueue<EnigmaSettings> resultsList;
	private EnigmaSettings settings;
	private final String message;
	
	private CountDownLatch latch;
	
	public PlugboardDetector(QuadbombManager manager, EnigmaSettings settings, ConcurrentLinkedQueue<EnigmaSettings> resultsList, String message, CountDownLatch latch) {
		this.manager = manager;
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
		double bestScore = Double.NEGATIVE_INFINITY;
		//EnigmaMachine bomb = settings.createEnigmaMachine();
		
		do { // while there are improvements to be found.
			bestLeft = '!';
			bestRight = '!';
			
			// Compute control probability.
			EnigmaMachine bomb = settings.createEnigmaMachine();
			controlValue = manager.computeQuadgramProbability(bomb.encryptString(message));
			//bomb.reset();	// Reset for subsequent encryption.
			
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
						
						bomb = settings.createEnigmaMachine();
						String cipher = bomb.encryptString(testMessage);
						//bomb.reset();
						testMessage = "";
						
						// Reverse test plugboard pair.
						for (char character: cipher.toCharArray()) {
							testMessage += testBoard.matchChar(character);
						}
						
						// Compute test probability.
						double testValue = manager.computeQuadgramProbability(testMessage);
						
						// Find best plugboard pair.
						if (testValue > controlValue) {
							controlValue = testValue;
							bestScore = testValue;
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
				settings.setPlugboardMap(result);
				
				//bomb = settings.createEnigmaMachine();
			}
		} while (bestLeft != '!' && bestRight != '!'); // Continue until no further gain in fitness can be achieved.
		
		EnigmaSettings candidate = settings.copy();
		candidate.setFitnessScore(bestScore);
		
		// Save best indicator result into list for further processing.
		resultsList.add(candidate);
		
		latch.countDown();
	} // End run()
}
