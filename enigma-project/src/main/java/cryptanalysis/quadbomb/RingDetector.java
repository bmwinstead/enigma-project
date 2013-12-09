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

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;

import main.java.cryptanalysis.nlp.StatisticsGenerator;
import main.java.enigma.EnigmaMachine;
import main.java.enigma.EnigmaSettings;

public class RingDetector implements Callable<Boolean> {
	private StatisticsGenerator tester;
	private EnigmaSettings configuration;
	private QuadBombSettings settings;
	private final String message;
	
	private ConcurrentLinkedQueue<EnigmaSettings> resultsList;
	
	public RingDetector(StatisticsGenerator tester, 
			EnigmaSettings configuration, 
			QuadBombSettings settings, 
			ConcurrentLinkedQueue<EnigmaSettings> resultsList, 
			String message) 
	{
		this.tester = tester;
		this.configuration = configuration;
		this.settings = settings;
		this.resultsList = resultsList;
		this.message = message;
	}
	
	public Boolean call() {
		Queue<EnigmaSettings> testList = settings.getTestingRings(configuration);
		tester.selectFitnessTest(3);
		
		while(!testList.isEmpty()) {
			if (Thread.currentThread().isInterrupted()) {
				return false;
			}
			
			EnigmaSettings candidate = testList.poll();
			
			EnigmaMachine bomb = candidate.createEnigmaMachine();
			
			String cipher = bomb.encryptString(message);
			double testValue = tester.computeFitnessScore(cipher);
			
			candidate.setFitnessScore(testValue);
			
			resultsList.add(candidate);
		}
		
		return true;
	} // End run()
}
