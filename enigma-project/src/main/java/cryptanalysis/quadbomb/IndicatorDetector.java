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

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

import main.java.cryptanalysis.nlp.StatisticsGenerator;
import main.java.enigma.EnigmaMachine;
import main.java.enigma.EnigmaSettings;

public class IndicatorDetector implements Runnable {
	private StatisticsGenerator tester;
	private EnigmaSettings configuration;
	private QuadBombSettings settings;
	private final String message;
	
	private ConcurrentLinkedQueue<EnigmaSettings> resultsList;
	
	private CountDownLatch latch;
	
	public IndicatorDetector(StatisticsGenerator tester, 
			EnigmaSettings configuration, 
			QuadBombSettings settings, 
			ConcurrentLinkedQueue<EnigmaSettings> resultsList, 
			String message, 
			CountDownLatch latch) 
	{
		this.tester = tester;
		this.configuration = configuration;
		this.settings = settings;
		this.resultsList = resultsList;
		this.message = message;
		
		this.latch = latch;
	}
	
	public void run() {
		Queue<char[]> testList = settings.getTestingIndicators(configuration.isThreeRotor());
		tester.selectFitnessTest(3);
		
		while(!testList.isEmpty()) {
			EnigmaSettings candidate = configuration.copy();
			candidate.setIndicatorSettings(testList.poll());
			
			EnigmaMachine bomb = candidate.createEnigmaMachine();
			
			String cipher = bomb.encryptString(message);
			double testValue = tester.computeFitnessScore(cipher);
			
			candidate.setFitnessScore(testValue);
			
			resultsList.add(candidate);
		}
		
		latch.countDown();
	} // End run()
}
