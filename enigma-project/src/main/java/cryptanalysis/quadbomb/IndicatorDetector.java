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
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;

import main.java.cryptanalysis.nlp.StatisticsGenerator;
import main.java.enigma.EnigmaMachine;
import main.java.enigma.EnigmaSettings;

public class IndicatorDetector implements Callable<Boolean> {
	private StatisticsGenerator tester;
	private EnigmaSettings baseCandidate;
	private QuadBombSettings settings;
	private final String message;
	
	private ConcurrentLinkedQueue<EnigmaSettings> resultsList;
	
	public IndicatorDetector(StatisticsGenerator tester, 
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
	
	public Boolean call() {
		Queue<char[]> testList = settings.getTestingIndicators(baseCandidate.isThreeRotor());
		tester.selectFitnessTest(3);
		
		while(!testList.isEmpty()) {
			if (Thread.currentThread().isInterrupted()) {
				return false;
			}
			
			EnigmaSettings candidate = baseCandidate.copy();
			candidate.setIndicatorSettings(testList.poll());
			
			EnigmaMachine bomb = candidate.createEnigmaMachine();
			
			String cipher = bomb.encryptString(message);
			double testValue = tester.computeFitnessScore(cipher);
			
			candidate.setFitnessScore(testValue);
			
			resultsList.add(candidate);
		}
		
		return true;
	} // End call()
}
