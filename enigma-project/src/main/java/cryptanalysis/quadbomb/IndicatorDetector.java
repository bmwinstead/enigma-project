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

import java.util.PriorityQueue;
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
	
	private PriorityQueue<EnigmaSettings> workList;
	private ConcurrentLinkedQueue<EnigmaSettings> resultsList;
	
	public IndicatorDetector(StatisticsGenerator tester, 
			EnigmaSettings candidate, 
			QuadBombSettings settings, 
			ConcurrentLinkedQueue<EnigmaSettings> resultsList, 
			String message) 
	{
		workList = new PriorityQueue<EnigmaSettings>();
		
		this.tester = tester;
		this.baseCandidate = candidate;
		this.settings = settings;
		this.resultsList = resultsList;
		this.message = message;
	}
	
	public Boolean call() {
		int[] testParameters = settings.getTestingIndicators(baseCandidate.isThreeRotor());
		tester.selectFitnessTest(3);
		
		for (int i = testParameters[2]; i < testParameters[3]; i++) {
			for (int j = testParameters[4]; j < testParameters[5]; j++) {
				for (int k = testParameters[6]; k < testParameters[7]; k++) {
					EnigmaSettings candidate = baseCandidate.copy();
					
					if (baseCandidate.isThreeRotor()) {
						char[] indicators = {(char) ('A' + i), (char) ('A' + j), (char) ('A' + k)};
						
						candidate.setIndicatorSettings(indicators);
					}
					else {
						for (int l = testParameters[0]; l < testParameters[1]; l++) {
							char[] indicators = {(char) ('A' + l), (char) ('A' + i), (char) ('A' + j), (char) ('A' + k)};
							
							candidate.setIndicatorSettings(indicators);
						}
					}
					
					EnigmaMachine bomb = candidate.createEnigmaMachine();
					
					String cipher = bomb.encryptString(message);
					double testValue = tester.computeFitnessScore(cipher);
					
					candidate.setFitnessScore(testValue);
					
					workList.add(candidate);
					
					while (workList.size() > settings.getCandidateSize()) {
						workList.poll();
					}
				} // End right indicator loop.
			} // End middle indicator loop.
		} // End left indicator loop.

		resultsList.addAll(workList);
		return true;
	} // End call()
}
