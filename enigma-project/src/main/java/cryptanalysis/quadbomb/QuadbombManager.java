/**
 * QuadbombManager.java
 * @author - Walter Adolph
 * @author - Team Enigma
 * @version - 0.9
 * @date - Nov 26, 2013
 * 
 * This manages the thread work list and process flow of QuadBomb.
 * QuadBomb attempts to decrypt an Enigma message using quadgram statistics algorithm as described at the below websites:
 * 
 * References:
 * http://practicalcryptography.com/cryptanalysis/breaking-machine-ciphers/cryptanalysis-enigma/
 * http://practicalcryptography.com/cryptanalysis/text-characterisation/quadgrams/
 * 
 * Step 1: Determine best rotor wheel order and indicator settings, saving each consecutive best result.
 * Step 2: Determine best ring setting by cycling through all rotor combinations from the candidates saved in step 1.
 * Step 3: Determine plugboard connections by brute force.
 * 
 * Improvements:
 * Incorporated multiple threading to improve CPU utilization.
 * Reset best fitness score for each reflector combination to avoid biasing the analyzer to reflector B.
 * 
 * Limitations:
 * This method does not guarantee a correct result. Essentially, this algorithm is equivalent to a local maxima search in that it
 * first searches the wheel order and indicator settings and saves consecutive best matches. Once that search is exhausted, 
 * then it searches for the best ring setting using the list of best rotor settings previously constructed. This search is limited in that the 
 * ring search is restricted to the candidate rotor settings. It is possible that the correct result is a combination of suboptimal
 * rotor and ring settings, and in these cases the algorithm is expected to fail.
 */
package main.java.cryptanalysis.quadbomb;

import java.util.Calendar;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.SwingWorker;

import main.java.cryptanalysis.nlp.Corpus;
import main.java.cryptanalysis.nlp.StatisticsGenerator;
import main.java.enigma.EnigmaMachine;
import main.java.enigma.EnigmaSettings;
import misc.Logger;
import views.ResultsPanel;

public class QuadbombManager extends SwingWorker<Long, Void> {
	private static int NUM_REFLECTORS = 2;	// Debugging line to speed up testing.
	private static int NUM_ROTORS = 8;		// Debugging line to speed up testing.
	
	private final StatisticsGenerator statGenerator;
	
	private final String message;
	private String decryptedMessage;
	
	private EnigmaSettings result;
	
	private int threadSize;
	private int candidateSize;
	
	private ResultsPanel resultsPanel;
	
	private ConcurrentLinkedQueue<EnigmaSettings> resultsList;
	private PriorityQueue<EnigmaSettings> candidateList;
	
	private Logger log;
	
	// Constructor.
	public QuadbombManager(Corpus database, String message, int statTest, int threadSize, int candidateSize, ResultsPanel panel) {
		this.message = message;
		
		this.threadSize = threadSize;
		this.candidateSize = candidateSize;
		
		resultsPanel = panel;
		
		resultsList = new ConcurrentLinkedQueue<EnigmaSettings>();
		candidateList = new PriorityQueue<EnigmaSettings>();
		
		statGenerator = new StatisticsGenerator(database, statTest);
		
		// Create a log file with the timestamp.
		Calendar date = Calendar.getInstance();
		
		String formattedDate = "Quadbomb attempt " + (date.get(Calendar.MONTH) + 1) + "-" + date.get(Calendar.DAY_OF_MONTH) + "-" + date.get(Calendar.YEAR)
				+ " " + date.get(Calendar.HOUR) + date.get(Calendar.MINUTE) + date.get(Calendar.SECOND) + " " + statTest + " " + threadSize + " " + candidateSize + ".txt";
		
		log = new Logger(formattedDate);
	}
	
	public Long doInBackground() {
		// Initialize thread list.
		ExecutorService threadManager = Executors.newFixedThreadPool(threadSize);
		CountDownLatch doneSignal = new CountDownLatch(NUM_REFLECTORS * (NUM_ROTORS - 2) * (NUM_ROTORS - 1) * NUM_ROTORS);
		
		log.makeEntry("Starting QuadBomb analysis...", true);
		log.makeEntry("Encrypted message: " + message, true);
		log.makeEntry("Start Fitness Score: " + statGenerator.computeFitnessScore(message), true);
		
		long startTime = System.currentTimeMillis();
		
		int operationCount = 0;
		
		// Step 1: Determine possible rotor, reflector, and indicator orders.
		// Create tasks segregated by rotor / reflector configuration and submit to threadManager.
		log.makeEntry("Determining rotors, reflectors, and indicator settings...", true);
		long startDecryptTime = System.currentTimeMillis();
		
		for (int reflector = 0; reflector < NUM_REFLECTORS; reflector++) {
			for (int i = 0; i < NUM_ROTORS; i++) { // Left rotor.
				for (int j = 0; j < NUM_ROTORS; j++) { // Middle rotor.
					if (i != j) { // Skip equal rotor selections.
						for (int k = 0; k < NUM_ROTORS; k++) { // Right rotor.
							if (i != k && j != k) { // Skip equal rotor selections.
								
								int[] rotors = {i, j, k};
								EnigmaSettings candidate = new EnigmaSettings(rotors, reflector);
								
								threadManager.execute(new IndicatorDetector(statGenerator, candidate, resultsList, message, doneSignal));
								updateProgress(++operationCount);
							} // End rotor check if
						} // End left rotor for
					} // End rotor check if
				} // End middle rotor for
			} // End right rotor for
		} // End reflector for
		
		// Wait until all tasks are complete.
		try {
			doneSignal.await();
			threadManager.shutdown();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		updateProgress(NUM_REFLECTORS * (NUM_ROTORS - 2) * (NUM_ROTORS - 1) * NUM_ROTORS);
		long endDecryptTime = System.currentTimeMillis();
		log.makeEntry("Process completed in " + (endDecryptTime - startDecryptTime) + " milliseconds.", true);
		
		// Trim candidate list.
		trimCandidateList();
		log.makeEntry("Rotor and indicator candidates:", false);
		printCandidateList();
		
		// Step 2: Determine possible ring settings.
		// Initialize thread list.
		threadManager = Executors.newFixedThreadPool(threadSize);
		doneSignal = new CountDownLatch(candidateList.size());
		
		log.makeEntry("Determining ring settings...", true);
		startDecryptTime = System.currentTimeMillis();
		
		for (EnigmaSettings candidate: candidateList) {
			threadManager.execute(new RingDetector(statGenerator, candidate, resultsList, message, doneSignal));
			updateProgress(++operationCount);
		}
		
		// Wait until all tasks are complete.
		try {
			doneSignal.await();
			threadManager.shutdown();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		updateProgress(NUM_REFLECTORS * (NUM_ROTORS - 2) * (NUM_ROTORS - 1) * NUM_ROTORS * resultsList.size());
		endDecryptTime = System.currentTimeMillis();
		log.makeEntry("Process completed in " + (endDecryptTime - startDecryptTime) + " milliseconds.", true);
		
		// Trim candidate list.
		trimCandidateList();
		log.makeEntry("Ring candidates:", false);
		printCandidateList();
		
		// Step 3: Determine possible plugboard settings.
		// Initialize thread list.
		threadManager = Executors.newFixedThreadPool(threadSize);
		doneSignal = new CountDownLatch(candidateList.size());
		
		log.makeEntry("Determining plugboard settings...", true);
		startDecryptTime = System.currentTimeMillis();
		
		for (EnigmaSettings candidate: candidateList) {
			threadManager.execute(new PlugboardDetector(statGenerator, candidate, resultsList, message, doneSignal));
			updateProgress(++operationCount);
		}
		
		// Wait until all tasks are complete.
		try {
			doneSignal.await();
			threadManager.shutdown();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		updateProgress(100);
		endDecryptTime = System.currentTimeMillis();
		log.makeEntry("Process completed in " + (endDecryptTime - startDecryptTime) + " milliseconds.", true);
		
		// Find best result and post.
		log.makeEntry("Finding best result...", true);
		
		long startSearchTime = System.currentTimeMillis();
		
		double bestScore = Double.NEGATIVE_INFINITY;

		// Trim candidate list.
		trimCandidateList();
		log.makeEntry("Plugboard candidates:", false);
		printCandidateList();
		
		while (!candidateList.isEmpty()) {
			result = candidateList.remove();
		}
		
		bestScore = result.getFitnessScore();
		
		EnigmaMachine decoder = result.createEnigmaMachine();
		decryptedMessage = decoder.encryptString(message);
		
		long endSearchTime = System.currentTimeMillis();
		log.makeEntry("Search completed in " + (endSearchTime - startSearchTime) + " milliseconds.", true);
		
		log.makeEntry("Quadgram analysis complete.", true);
		log.makeEntry("Results:", true);
		log.makeEntry("Decrypted message: " + decryptedMessage, true);
		
		log.makeEntry("Wheel order: " + result.printWheelOrder(), true);
		log.makeEntry("Best reflector: " + result.printReflector(), true);
		log.makeEntry("Ring settings: " + result.printRingSettings(), true);
		log.makeEntry("Rotor indicators: " + result.printIndicators(), true);
		log.makeEntry("Plugboard settings: " + result.printPlugboard(), true);
		log.makeEntry("Fitness score: " + bestScore, true);
		
		long endTime = System.currentTimeMillis();
		log.makeEntry("Analysis took " + (endTime - startTime) + " milliseconds to complete.", true);
		log.closeFile();
		
		return (endTime - startTime);	// return elapsed time as a default.
	}
	
	// Prints results on the Event Dispatch Thread once complete.
	protected void done() {
		resultsPanel.printSettings(result, decryptedMessage);
		
	}
	
	// Loads candidateList with the top candidates, with the list size selected by the user.
	public void trimCandidateList() {
		// Trim candidate list.
		log.makeEntry("Sorting candidate list...", true);
		long beginSortTime = System.currentTimeMillis();
		
		candidateList.clear();
		candidateList.addAll(resultsList);	// Sorts on adding.
		
		while (candidateList.size() > candidateSize) {
			candidateList.poll();
		}
		
		resultsList.clear();	// Clear out result list for next step.
		
		long endSortTime = System.currentTimeMillis();
		log.makeEntry("Sorting completed in " + (endSortTime - beginSortTime) + " milliseconds.", true);
	}
	
	public void printCandidateList() {
		int count = 1;
		double bestScore = Double.NEGATIVE_INFINITY;
		EnigmaSettings bestCandidate = candidateList.peek();
		
		for (EnigmaSettings candidate: candidateList) {
			log.makeEntry("Candidate #" + count++ + " of " + candidateList.size() + ":" + candidate.printSettings(), false);
			
			if (candidate.getFitnessScore() > bestScore) {
				bestScore = candidate.getFitnessScore();
				bestCandidate = candidate;
			}
		}
		
		log.makeEntry("Best Candidate: " + bestCandidate.printSettings(), true);
	}
	
	public void updateProgress(int progress) {
		int totalOperations = NUM_REFLECTORS * NUM_ROTORS * (NUM_ROTORS - 1) * (NUM_ROTORS - 2) * candidateSize * candidateSize;
		double percent = 100.0 * progress / totalOperations;
		setProgress((int)percent);
	}
}
