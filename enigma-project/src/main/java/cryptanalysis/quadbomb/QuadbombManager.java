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
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.JTextField;
import javax.swing.SwingWorker;

import main.java.GUINew.ResultsPanel;
import main.java.cryptanalysis.nlp.Corpus;
import main.java.cryptanalysis.nlp.CribDetector;
import main.java.cryptanalysis.nlp.StatisticsGenerator;
import main.java.enigma.EnigmaMachine;
import main.java.enigma.EnigmaSettings;
import misc.Logger;

public class QuadbombManager extends SwingWorker<Long, Integer> {
	private final StatisticsGenerator statGenerator;
	private final CribDetector tester;
	private final String message;
	private String decryptedMessage;
	
	private QuadBombSettings settings;
	private EnigmaSettings result;
	
	private ResultsPanel resultsPanel;
	private JTextField statusLabel;
	
	private ConcurrentLinkedQueue<EnigmaSettings> resultsList;
	private PriorityQueue<EnigmaSettings> candidateList;
	
	private ExecutorService threadManager;
	
	private Logger log;
	
	private int operationCount;

	// Constructor.
	public QuadbombManager(Corpus database, String message, QuadBombSettings settings, JTextField label, ResultsPanel panel) {
		this.message = message;
		
		this.settings = settings;
		resultsPanel = panel;
		this.statusLabel = label;
		
		resultsList = new ConcurrentLinkedQueue<EnigmaSettings>();
		candidateList = new PriorityQueue<EnigmaSettings>();
		
		statGenerator = new StatisticsGenerator(database, 3);	// TODO: Set best statistics for individual tests.
		tester = new CribDetector(database);
		
		// Create a log file with the timestamp.
		Calendar date = Calendar.getInstance();
		
		String formattedDate = "Quadbomb attempt " + (date.get(Calendar.MONTH) + 1) + "-" + date.get(Calendar.DAY_OF_MONTH) + "-" + date.get(Calendar.YEAR)
				+ " " + date.get(Calendar.HOUR) + date.get(Calendar.MINUTE) + date.get(Calendar.SECOND) + " " + 3 + " " + settings.getThreadCount() + " " + settings.getCandidateSize() + ".txt";
		
		log = new Logger(formattedDate);
	}
	
	public Long doInBackground() {
		// Initialize thread list.
		threadManager = Executors.newFixedThreadPool(settings.getThreadCount());
		
		log.makeEntry("Starting QuadBomb analysis...", true);
		log.makeEntry("Encrypted message: " + message, true);
		log.makeEntry("Start Fitness Score: " + statGenerator.computeFitnessScore(message), true);
		
		setProgress(0);
		operationCount = 0;
		
		long startTime = System.currentTimeMillis();
		
		// Step 1: Determine possible rotor, reflector, and indicator orders.
		// Create tasks segregated by rotor / reflector configuration and submit to threadManager.
		log.makeEntry("Determining rotors, reflectors, and indicator settings...", true);
		long startDecryptTime = System.currentTimeMillis();
		
		Queue<EnigmaSettings> testList = settings.getRotorReflectorCandidateList();
		LinkedList<Future<Boolean>> threadList = new LinkedList<Future<Boolean>>();
		
		while (!testList.isEmpty()) {
			threadList.add(threadManager.submit(new IndicatorDetector(statGenerator, testList.poll(), settings, resultsList, message)));
		}
		
		// Wait until all tasks are complete.
		while (!threadList.isEmpty()) {
			List<Future<Boolean>> removeList = new LinkedList<Future<Boolean>>();
			
			for (Future<Boolean> thread : threadList) {
				if (thread.isDone() && !thread.isCancelled()) {
					operationCount++;
					updateProgress(operationCount);
					publish(operationCount);
					removeList.add(thread);
				}
				else if (Thread.currentThread().isInterrupted()) {
					updateProgress(0);
					return -1L;
				}
			}
			
			threadList.removeAll(removeList);
		}
		
		threadManager.shutdown();

		long endDecryptTime = System.currentTimeMillis();
		log.makeEntry("Process completed in " + (endDecryptTime - startDecryptTime) + " milliseconds.", true);
		
		// Trim candidate list.
		trimCandidateList();
		log.makeEntry("Rotor and indicator candidates:", false);
		printCandidateList();
		
		// Step 2: Determine possible ring settings.
		// Initialize thread list.
		threadManager = Executors.newFixedThreadPool(settings.getThreadCount());

		log.makeEntry("Determining ring settings...", true);
		startDecryptTime = System.currentTimeMillis();
		
		for (EnigmaSettings candidate: candidateList) {
			threadList.add(threadManager.submit(new RingDetector(statGenerator, candidate, settings, resultsList, message)));
		}
		
		// Wait until all tasks are complete.
		while (!threadList.isEmpty()) {
			List<Future<Boolean>> removeList = new LinkedList<Future<Boolean>>();
			
			for (Future<Boolean> thread : threadList) {
				if (thread.isDone() && !thread.isCancelled()) {
					operationCount++;
					updateProgress(operationCount);
					publish(operationCount);
					removeList.add(thread);
				}
				else if (Thread.currentThread().isInterrupted()) {
					updateProgress(0);
					return -1L;
				}
			}
			
			threadList.removeAll(removeList);
		}
			
		threadManager.shutdown();
		
		endDecryptTime = System.currentTimeMillis();
		log.makeEntry("Process completed in " + (endDecryptTime - startDecryptTime) + " milliseconds.", true);
		
		// Trim candidate list.
		trimCandidateList();
		log.makeEntry("Ring candidates:", false);
		printCandidateList();
		
		// Step 3: Determine possible plugboard settings.
		// Initialize thread list.
		threadManager = Executors.newFixedThreadPool(settings.getThreadCount());
		
		log.makeEntry("Determining plugboard settings...", true);
		startDecryptTime = System.currentTimeMillis();
		
		for (EnigmaSettings candidate: candidateList) {
			threadList.add(threadManager.submit(new PlugboardDetector(statGenerator, candidate, settings, resultsList, message)));
		}
		
		// Wait until all tasks are complete.
		while (!threadList.isEmpty()) {
			List<Future<Boolean>> removeList = new LinkedList<Future<Boolean>>();
			
			for (Future<Boolean> thread : threadList) {
				if (thread.isDone() && !thread.isCancelled()) {
					operationCount++;
					updateProgress(operationCount);
					publish(operationCount);
					removeList.add(thread);
				}
				else if (Thread.currentThread().isInterrupted()) {
					updateProgress(0);
					return -1L;
				}
			}
			
			threadList.removeAll(removeList);
		}
				
		threadManager.shutdown();
		
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
		
		decryptedMessage = tester.parseMessage(decryptedMessage);
		
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
	
	// Updates the status label and the progressbar on the Event Dispatch Thread.
	protected void process(List<Integer> list) {
		if (!threadManager.isTerminated()) {
			for (Integer count : list) {
				statusLabel.setText("Completed operation " + count + " of " + settings.getTotalOperationCount());
			}
		}
	}
	
	// Prints results on the Event Dispatch Thread once complete.
	protected void done() {
		if (result != null && decryptedMessage != null) {
			resultsPanel.printSolution(result, decryptedMessage);
			statusLabel.setText("Completed");
		}
	}
	
	// Stops all work on the worker threads.
	public void abort() {
		threadManager.shutdownNow();
	}
	
	private void updateProgress(int count) {
		int percent = (int)(100 * (double)count / settings.getTotalOperationCount());
		setProgress(percent);
	}
	
	// Loads candidateList with the top candidates, with the list size selected by the user.
	private void trimCandidateList() {
		// Trim candidate list.
		log.makeEntry("Sorting candidate list...", true);
		long beginSortTime = System.currentTimeMillis();
		
		candidateList.clear();
		candidateList.addAll(resultsList);	// Sorts on adding.
		
		while (candidateList.size() > settings.getCandidateSize()) {
			candidateList.poll();
		}
		
		resultsList.clear();	// Clear out result list for next step.
		
		long endSortTime = System.currentTimeMillis();
		log.makeEntry("Sorting completed in " + (endSortTime - beginSortTime) + " milliseconds.", true);
	}
	
	private void printCandidateList() {
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
}
