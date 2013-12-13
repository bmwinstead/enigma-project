package main.java.cryptanalysis.quadbomb;

import java.util.PriorityQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;

import main.java.cryptanalysis.nlp.StatisticsGenerator;
import main.java.enigma.EnigmaMachine;
import main.java.enigma.EnigmaSettings;

/**
 * Worker thread to determine best indicator settings for a given rotor / reflector configuration.
 * This thread performs an exhaustive search of all possible indicator settings within defined constraints.
 * Each candidate indicator setting is applied to the provided machine settings, and scored with a defined statistic.
 * The best number of candidates (number specified by the user) is saved in resultsList for further use.
 * Return values indicate if the thread completed it's computation.
 * 
 * IndicatorDetector.java
 * @author - Walter Adolph
 * @author - Team Enigma
 * @version - 0.9
 * Nov 26, 2013
 */
public class IndicatorDetector implements Callable<Boolean> {
	private StatisticsGenerator tester;
	private EnigmaSettings baseCandidate;
	private QuadBombSettings settings;
	private final String message;
	
	private PriorityQueue<EnigmaSettings> workList;
	private ConcurrentLinkedQueue<EnigmaSettings> resultsList;
	
	/**
	 * Constructor.
	 * 
	 * @param tester
	 * 			StatisticsGenerator
	 * @param candidate
	 * 			EnigmaSettings
	 * @param settings
	 * 			QuadBombSettings
	 * @param resultsList
	 * 			CurrentLinkedQueue<EnigmaSettings>
	 * @param message
	 * 			String
	 */
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
	
	/**
	 * Workhorse method. 
	 */
	public Boolean call() {
		int[] testParameters = settings.getTestingIndicators(baseCandidate.isThreeRotor());
		tester.selectFitnessTest(3);
		
		// Cycle through each combination of three rotor settings, taking in account set constraints.
		for (int i = testParameters[2]; i < testParameters[3]; i++) {			// Left rotor loop.
			for (int j = testParameters[4]; j < testParameters[5]; j++) {		// Middle rotor loop.
				for (int k = testParameters[6]; k < testParameters[7]; k++) {	// Right rotor loop.
					if (baseCandidate.isThreeRotor()) {
						// Copy the rotor and reflector settings and generate indicator settings.
						EnigmaSettings candidate = baseCandidate.copy();
						char[] indicators = {(char) ('A' + i), (char) ('A' + j), (char) ('A' + k)};
						
						candidate.setIndicatorSettings(indicators);
						
						// Test the candidate and score.
						EnigmaMachine bomb = candidate.createEnigmaMachine();
						String cipher = bomb.encryptString(message);
						double testValue = tester.computeFitnessScore(cipher);
						
						// Save results.
						candidate.setFitnessScore(testValue);
						workList.add(candidate);
					}
					else {	// Is four-rotor.
						for (int l = testParameters[0]; l < testParameters[1]; l++) {	// Fourth rotor loop.
							// Copy the rotor and reflector settings and generate indicator settings.
							EnigmaSettings candidate = baseCandidate.copy();
							char[] indicators = {(char) ('A' + l), (char) ('A' + i), (char) ('A' + j), (char) ('A' + k)};
							
							candidate.setIndicatorSettings(indicators);
							
							// Test the candidate and score.
							EnigmaMachine bomb = candidate.createEnigmaMachine();
							String cipher = bomb.encryptString(message);
							double testValue = tester.computeFitnessScore(cipher);
							
							// Save results.
							candidate.setFitnessScore(testValue);
							workList.add(candidate);
							
							// Trim saved results to candidate size to save on memory.
							while (workList.size() > settings.getCandidateSize()) {
								workList.poll();
							}
						}
					}
					
					// Trim saved results to candidate size to save on memory.
					while (workList.size() > settings.getCandidateSize()) {
						workList.poll();
					}
					
					// Allows interrupted thread to terminate.
					if (Thread.currentThread().isInterrupted()) {
						return false;
					}
				} // End right indicator loop.
			} // End middle indicator loop.
		} // End left indicator loop.

		// Save results and exit.
		resultsList.addAll(workList);
		return true;
	} // End call()
}
