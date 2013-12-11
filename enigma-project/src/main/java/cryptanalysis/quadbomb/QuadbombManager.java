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

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import main.java.GUINew.ResultsPanel;
import main.java.cryptanalysis.nlp.Corpus;
import main.java.cryptanalysis.nlp.CribDetector;
import main.java.cryptanalysis.nlp.StatisticsGenerator;
import main.java.enigma.EnigmaMachine;
import main.java.enigma.EnigmaSettings;

public class QuadbombManager extends SwingWorker<Boolean, Integer> {
	private final StatisticsGenerator statGenerator;
	private final CribDetector tester;
	private final String message;
	private String decryptedMessage;
	
	private QuadBombSettings settings;
	private EnigmaSettings result;
	
	private ResultsPanel resultsPanel;
	private JTextField statusLabel;
	private JButton encryptButton;
	
	private ConcurrentLinkedQueue<EnigmaSettings> resultsList;
	private PriorityQueue<EnigmaSettings> candidateList;
	
	private ExecutorService threadManager;

	private int operationCount;

	// Constructor.
	public QuadbombManager(Corpus database, String message, QuadBombSettings settings, JTextField label, JButton button, ResultsPanel panel) {
		this.message = message;
		
		this.settings = settings;
		this.resultsPanel = panel;
		this.statusLabel = label;
		this.encryptButton = button;
		
		resultsList = new ConcurrentLinkedQueue<EnigmaSettings>();
		candidateList = new PriorityQueue<EnigmaSettings>();
		
		statGenerator = new StatisticsGenerator(database, 3);	// TODO: Set best statistics for individual tests.
		tester = new CribDetector(database);
	}
	
	public Boolean doInBackground() {
		// Initialize thread list.
		threadManager = Executors.newFixedThreadPool(settings.getThreadCount());
		
		setProgress(0);
		operationCount = 0;
		
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
					return false;
				}
			}
			
			threadList.removeAll(removeList);
		}
		
		threadManager.shutdown();
		
		// Trim candidate list.
		trimCandidateList();
		
		// Step 2: Determine possible ring settings.
		// Initialize thread list.
		threadManager = Executors.newFixedThreadPool(settings.getThreadCount());
		
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
					return false;
				}
			}
			
			threadList.removeAll(removeList);
		}
			
		threadManager.shutdown();
		
		// Trim candidate list.
		trimCandidateList();
		
		// Step 3: Determine possible plugboard settings.
		// Initialize thread list.
		threadManager = Executors.newFixedThreadPool(settings.getThreadCount());
		
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
					return false;
				}
			}
			
			threadList.removeAll(removeList);
		}
				
		threadManager.shutdown();
		
		// Trim candidate list.
		trimCandidateList();
		
		while (!candidateList.isEmpty()) {
			result = candidateList.remove();
		}
		
		EnigmaMachine decoder = result.createEnigmaMachine();
		decryptedMessage = decoder.encryptString(message);
		
		decryptedMessage = tester.parseMessage(decryptedMessage);

		return true;	// return elapsed time as a default.
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
		encryptButton.setEnabled(true);
		
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
		candidateList.clear();
		candidateList.addAll(resultsList);	// Sorts on adding.
		
		while (candidateList.size() > settings.getCandidateSize()) {
			candidateList.poll();
		}
		
		resultsList.clear();	// Clear out result list for next step.
	}
}
