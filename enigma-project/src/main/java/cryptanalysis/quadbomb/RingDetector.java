package main.java.cryptanalysis.quadbomb;

import java.util.PriorityQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;

import main.java.cryptanalysis.nlp.StatisticsGenerator;
import main.java.enigma.EnigmaMachine;
import main.java.enigma.EnigmaSettings;

/**
 * Worker thread to determine best ring settings for given rotor / reflector / indicator configurations.
 * This thread performs an exhaustive search of all possible ring settings within defined constraints.
 * As rings are tested, the indicators are offset to keep alignment. Because of this, if a full test is done, the leftmost
 * ring does not need to be stepped.
 * 
 * Each candidate ring setting is applied to the provided machine settings, and scored with a defined statistic.
 * The best number of candidates (number specified by the user) is saved in resultsList for further use.
 * Return values indicate if the thread completed it's computation.
 * 
 * RingDetector.java
 * @author - Walter Adolph
 * @author - Team Enigma
 * @version - 0.9
 * - Nov 26, 2013
 */
public class RingDetector implements Callable<Boolean> {
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
	 * 				StatisticsGenerator
	 * @param baseCandidate
	 * 				EnigmaSettings
	 * @param settings
	 * 				QuadBombSettings
	 * @param resultsList
	 * 				ConcurrentLinkedQueue<EnigmaSettings>
	 * @param message
	 * 				String
	 */
	public RingDetector(StatisticsGenerator tester, 
			EnigmaSettings baseCandidate, 
			QuadBombSettings settings, 
			ConcurrentLinkedQueue<EnigmaSettings> resultsList, 
			String message) 
	{
		workList = new PriorityQueue<EnigmaSettings>();
		
		this.tester = tester;
		this.baseCandidate = baseCandidate;
		this.settings = settings;
		this.resultsList = resultsList;
		this.message = message;
	}
	
	/**
	 * Worker method. Specified by Callable interface. 
	 */
	public Boolean call() {
		int[] testParameters = settings.getTestingRings(baseCandidate.isThreeRotor());
		tester.selectFitnessTest(3);
		
		boolean[] tandemCycle = settings.getTandemStepFlags();
		
		// Cycle through ring setting combinations.
		for (int i = testParameters[2]; i < testParameters[3]; i++) {			// Left ring loop.
			for (int j = testParameters[4]; j < testParameters[5]; j++) {		// Middle ring loop.
				for (int k = testParameters[6]; k < testParameters[7]; k++) {	// Right ring loop.
					if (baseCandidate.isThreeRotor()) {
						// Copy settings and generate ring settings, offsetting indicator settings if necessary.
						EnigmaSettings candidate = baseCandidate.copy();
						char[] ringTestSettings = new char[3];
						char[] indicatorTestSettings = new char[3];
						
						ringTestSettings[0] = (char) ('A' + i);
						ringTestSettings[1] = (char) ('A' + j);
						ringTestSettings[2] = (char) ('A' + k);
						
						// Compute indicator offsets.
						indicatorTestSettings = candidate.getIndicatorSettings();
						
						int leftOffset = indicatorTestSettings[0] + i - 'A';
						int middleOffset = indicatorTestSettings[1] + j - 'A';
						int rightOffset = indicatorTestSettings[2] + k - 'A';
						
						char left = (char)(leftOffset % 26 + 'A');
						char middle = (char)(middleOffset % 26 + 'A');
						char right = (char)(rightOffset % 26 + 'A');
						
						indicatorTestSettings[0] = tandemCycle[1] ? left : indicatorTestSettings[0];
						indicatorTestSettings[1] = tandemCycle[2] ? middle : indicatorTestSettings[1];
						indicatorTestSettings[2] = tandemCycle[3] ? right : indicatorTestSettings[2];
						
						candidate.setIndicatorSettings(indicatorTestSettings);
						candidate.setRingSettings(ringTestSettings);
						
						// Test the candidate and score.
						EnigmaMachine bomb = candidate.createEnigmaMachine();
						String cipher = bomb.encryptString(message);
						double testValue = tester.computeFitnessScore(cipher);
						
						// Save results.
						candidate.setFitnessScore(testValue);
						workList.add(candidate);
						
						if (Thread.currentThread().isInterrupted()) {	// Allows interrupted thread to terminate.
							return false;
						}
					}
					else { // Is four-rotor.
						for (int l = testParameters[0]; l < testParameters[1]; l++) {	// Fourth ring loop.
							// Copy settings and generate ring settings, offsetting indicator settings if necessary.
							EnigmaSettings candidate = baseCandidate.copy();
							char[] ringTestSettings = new char[4];
							char[] indicatorTestSettings = new char[4];
							
							ringTestSettings[0] = (char) ('A' + l);
							ringTestSettings[1] = (char) ('A' + i);
							ringTestSettings[2] = (char) ('A' + j);
							ringTestSettings[3] = (char) ('A' + k);
							
							// Compute indicator offsets.
							indicatorTestSettings = candidate.getIndicatorSettings();
							
							int fourthOffset = indicatorTestSettings[0] + l - 'A';
							int leftOffset = indicatorTestSettings[1] + i - 'A';
							int middleOffset = indicatorTestSettings[2] + j - 'A';
							int rightOffset = indicatorTestSettings[3] + k - 'A';
							
							char fourth = (char)(fourthOffset % 26 + 'A');
							char left = (char)(leftOffset % 26 + 'A');
							char middle = (char)(middleOffset % 26 + 'A');
							char right = (char)(rightOffset % 26 + 'A');
							
							indicatorTestSettings[0] = (tandemCycle[0]) ? fourth : indicatorTestSettings[0];
							indicatorTestSettings[1] = (tandemCycle[1]) ? left : indicatorTestSettings[1];
							indicatorTestSettings[2] = (tandemCycle[2]) ? middle : indicatorTestSettings[2];
							indicatorTestSettings[3] = (tandemCycle[3]) ? right : indicatorTestSettings[3];
							
							candidate.setIndicatorSettings(indicatorTestSettings);
							candidate.setRingSettings(ringTestSettings);
							
							// Test the candidate and score.
							EnigmaMachine bomb = candidate.createEnigmaMachine();
							String cipher = bomb.encryptString(message);
							double testValue = tester.computeFitnessScore(cipher);
							
							// Save results.
							candidate.setFitnessScore(testValue);
							workList.add(candidate);
							
							if (Thread.currentThread().isInterrupted()) {	// Allows interrupted thread to terminate.
								return false;
							}
						} // End left ring loop.
					} // End fourth rotor check.
					
					// Trim saved results to candidate size to save on memory.
					while (workList.size() > settings.getCandidateSize()) {
						workList.poll();
					}
				} // End right ring loop.
			} // End middle ring loop.
		} // End left ring loop.
		
		// Save results and exit.
		resultsList.addAll(workList);
		
		return true;
	} // End run()
}
