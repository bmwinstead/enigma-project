/**
 * IndicatorDetector.java
 * @author - Walter Adolph
 * @author - Team Enigma
 * @version - 0.9
 * @date - Nov 26, 2013
 * 
 * Worker thread to determine best indicator settings for a given rotor / reflector configuration.
 */
package main.java.cryptanalysis.quadbomb;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

import main.java.cryptanalysis.nlp.StatisticsGenerator;
import main.java.enigma.EnigmaMachine;
import main.java.enigma.EnigmaSettings;

public class IndicatorDetector implements Runnable {
	private StatisticsGenerator tester;
	private ConcurrentLinkedQueue<EnigmaSettings> resultsList;
	private EnigmaSettings settings;
	private final String message;
	
	private CountDownLatch latch;
	
	public IndicatorDetector(StatisticsGenerator tester, EnigmaSettings settings, ConcurrentLinkedQueue<EnigmaSettings> resultsList, String message, CountDownLatch latch) {
		this.tester = tester;
		this.settings = settings;
		this.resultsList = resultsList;
		this.message = message;
		
		this.latch = latch;
	}
	
	public void run() {
		char[] rotorTestSettings = new char[3];
		
		// Cycle through rotor indicator combinations.
		for (int i = 0; i < 26; i++) {
			for (int j = 0; j < 26; j++) {
				for (int k = 0; k < 26; k++) {
					rotorTestSettings[0] = (char) ('A' + i);
					rotorTestSettings[1] = (char) ('A' + j);
					rotorTestSettings[2] = (char) ('A' + k);

					EnigmaMachine bomb = new EnigmaMachine(settings.getRotors(), settings.getReflector(), settings.getRingSettings(), rotorTestSettings);
					
					String cipher = bomb.encryptString(message);
					double testValue = tester.computeFitnessScore(cipher);

					EnigmaSettings candidate = settings.copy();
					candidate.setIndicatorSettings(rotorTestSettings);
					candidate.setFitnessScore(testValue);
					
					// Save best indicator result into list for further processing.
					resultsList.add(candidate);
				} // End right indicator for
			} // End middle indicator for
		} // End left indicator for
		
		latch.countDown();
	} // End run()
}
