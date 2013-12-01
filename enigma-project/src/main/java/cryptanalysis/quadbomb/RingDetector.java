/**
 * RingDetector.java
 * @author - Walter Adolph
 * @author - Team Enigma
 * @version - 0.9
 * @date - Nov 26, 2013
 * 
 * Worker thread to determine possible ring settings.
 */
package main.java.cryptanalysis.quadbomb;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

import main.java.cryptanalysis.nlp.StatisticsGenerator;
import main.java.enigma.EnigmaMachine;
import main.java.enigma.EnigmaSettings;

public class RingDetector implements Runnable {
	private StatisticsGenerator tester;
	private ConcurrentLinkedQueue<EnigmaSettings> resultsList;
	private EnigmaSettings settings;
	private final String message;
	
	private CountDownLatch latch;
	
	public RingDetector(StatisticsGenerator tester, EnigmaSettings settings, ConcurrentLinkedQueue<EnigmaSettings> resultsList, String message, CountDownLatch latch) {
		this.tester = tester;
		this.settings = settings;
		this.resultsList = resultsList;
		this.message = message;
		
		this.latch = latch;
	}
	
	public void run() {
		char[] ringTestSettings = new char[3];
		char[] rotorTestSettings = new char[3];
		char[] baseRotorSettings = settings.getIndicatorSettings();
		
		// Cycle through ring setting combinations.
		for (int j = 0; j < 26; j++) {
			for (int k = 0; k < 26; k++) {
				ringTestSettings[0] = 'A';
				ringTestSettings[1] = (char) ('A' + j);
				ringTestSettings[2] = (char) ('A' + k);
				
				// Offset the indicators the same as the ring offset. See references above.	
				//int leftOffset = baseRotorSettings[0] - 'A';
				int middleOffset = baseRotorSettings[1] + j - 'A';
				int rightOffset = baseRotorSettings[2] + k - 'A';
				
				//char left = //(char)(leftOffset % 26 + 'A');
				char middle = (char)(middleOffset % 26 + 'A');
				char right = (char)(rightOffset % 26 + 'A');
				
				rotorTestSettings[0] = baseRotorSettings[0];
				rotorTestSettings[1] = middle;
				rotorTestSettings[2] = right;
				
				EnigmaMachine bomb = new EnigmaMachine(settings.getRotors(), settings.getReflector(), ringTestSettings, rotorTestSettings);
				
				String cipher = bomb.encryptString(message);
				
				double testValue = tester.computeFitnessScore(cipher);

				if (rotorTestSettings[0] == 'R' && rotorTestSettings[2] == 'Q') {
					rotorTestSettings[0] = 'R';
				}
				
				EnigmaSettings candidate = settings.copy();
				
				candidate.setRingSettings(ringTestSettings);
				candidate.setIndicatorSettings(rotorTestSettings);
				candidate.setFitnessScore(testValue);
				
				// Save best indicator result into list for further processing.
				resultsList.add(candidate);
			} // End right ring for
		} // End middle ring for
		
		latch.countDown();
	} // End run()
}
