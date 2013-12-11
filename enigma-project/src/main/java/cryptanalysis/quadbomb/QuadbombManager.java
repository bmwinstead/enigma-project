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

/**
 * QuadbombManager.java
 *  
 * This manages the thread work list and process flow of QuadBomb.
 * QuadBomb attempts to decrypt an Enigma message using quadgram statistics algorithm as described at the linked websites:
 * 
 * Step 1: Determine best rotor wheel order and indicator settings, saving each consecutive best result.
 * Step 2: Determine best ring setting by cycling through rotor combinations from the top number of candidates saved in step 1.
 * Step 3: Determine plugboard connections a pair at a time, saving best results of each pair combo, until no further improvement can be found.
 * 
 * Improvements:
 * Incorporated multiple threading to improve CPU utilization.
 * 
 * Limitations:
 * This method does not guarantee a correct result. Essentially, this algorithm is equivalent to a ensemble local maxima search in that it
 * first searches the wheel order and indicator settings and saves a set number of best matches. Once that search is exhausted, 
 * then it searches for the best ring setting using the list of best rotor settings previously constructed. This search is limited in that the 
 * ring search is restricted to the candidate rotor settings. It is possible that the correct result is a combination of suboptimal
 * rotor and ring settings, and in these cases the algorithm is expected to fail. Furthermore, in cases of messages encrypted with a large
 * number of plugboard pairs, the search space scores are very similar until most of the correct settings are recovered. In these cases, it is very
 * likely that the correct settings are lost in a given step due to a large number of slightly better scoring 'incorrect' setting combinations clogging
 * the candidate lists.
 * 
 * @see <a href="http://practicalcryptography.com/cryptanalysis/breaking-machine-ciphers/cryptanalysis-enigma/">Practical Cryptography: Cryptanalysis of Enigma</a>
 * @see <a href="http://practicalcryptography.com/cryptanalysis/text-characterisation/quadgrams/">Practical Cryptography: Quadram Statistics as a Fitness Measure</a>
 *
 * @author - Walter Adolph
 * @author - Team Enigma
 * @version - 0.9
 * - Nov 26, 2013
 */
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

	/**
	 * Constructor
	 * 
	 * @param database
	 * 				Corpus
	 * @param message
	 * 				String
	 * @param settings
	 * 				QuadBombSettings
	 * @param label
	 * 				JTextField
	 * @param button
	 * 				JButton
	 * @param panel
	 * 				ResultsPanel
	 */
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
	
	/**
	 * Workhorse method. Specified by SwingWorker. 
	 */
	public Boolean doInBackground() {
		// Initialize thread list.
		threadManager = Executors.newFixedThreadPool(settings.getThreadCount());
		
		operationCount = 0;
		
		// Get rotors and reflectors to test.
		Queue<EnigmaSettings> testList = settings.getRotorReflectorCandidateList();
		LinkedList<Future<Boolean>> threadList = new LinkedList<Future<Boolean>>();
		
		updateProgress(0);	// Reset progress bar and status text.
		
		// Step 1: Test indicators for each rotor and reflector combo.
		while (!testList.isEmpty()) {
			threadList.add(threadManager.submit(new IndicatorDetector(statGenerator, testList.poll(), settings, resultsList, message)));
		}
		
		// Spinlock this thread, updating progress and status, until all threads are complete.
		while (!threadList.isEmpty()) {
			List<Future<Boolean>> removeList = new LinkedList<Future<Boolean>>();
			
			for (Future<Boolean> thread : threadList) {
				if (thread.isDone() && !thread.isCancelled()) {
					operationCount++;
					updateProgress(operationCount);
					publish(operationCount);
					removeList.add(thread);
				}
				else if (Thread.currentThread().isInterrupted()) {	// Allow thread cancellation.
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
		
		// Spinlock this thread, updating progress and status, until all threads are complete.
		while (!threadList.isEmpty()) {
			List<Future<Boolean>> removeList = new LinkedList<Future<Boolean>>();
			
			for (Future<Boolean> thread : threadList) {
				if (thread.isDone() && !thread.isCancelled()) {
					operationCount++;
					updateProgress(operationCount);
					publish(operationCount);
					removeList.add(thread);
				}
				else if (Thread.currentThread().isInterrupted()) {	// Allow thread cancellation.
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
		
		// Spinlock this thread, updating progress and status, until all threads are complete.
		while (!threadList.isEmpty()) {
			List<Future<Boolean>> removeList = new LinkedList<Future<Boolean>>();
			
			for (Future<Boolean> thread : threadList) {
				if (thread.isDone() && !thread.isCancelled()) {
					operationCount++;
					updateProgress(operationCount);
					publish(operationCount);
					removeList.add(thread);
				}
				else if (Thread.currentThread().isInterrupted()) {	// Allow thread cancellation.
					updateProgress(0);
					return false;
				}
			}
			
			threadList.removeAll(removeList);
		}
				
		threadManager.shutdown();
		
		// Trim candidate list.
		trimCandidateList();
		
		// Get best result.
		while (!candidateList.isEmpty()) {
			result = candidateList.remove();
		}
		
		EnigmaMachine decoder = result.createEnigmaMachine();
		decryptedMessage = decoder.encryptString(message);
		
		decryptedMessage = tester.parseMessage(decryptedMessage);

		return true;	// Return success flag.
	}
	
	/**
	 * Updates the status label and the progressbar on the Event Dispatch Thread.
	 * Specified by SwingWorker.
	 */
	protected void process(List<Integer> list) {
		for (Integer count : list) {
			statusLabel.setText("Completed operation " + count + " of " + settings.getTotalOperationCount());
		}
	}
	
	/**
	 * Prints results on the Event Dispatch Thread once complete.
	 * Specified by SwingWorker.
	 */
	protected void done() {
		encryptButton.setEnabled(true);
		
		if (result != null && decryptedMessage != null) {
			resultsPanel.printSolution(result, decryptedMessage);
			statusLabel.setText("Completed");
		}
		else if (threadManager.isShutdown()) {
			statusLabel.setText("Aborted...");
		}
	}
	
	/**
	 * Stops work on all worker threads, in case the user wishes to cancel
	 * prematurely. 
	 */
	public void abort() {
		threadManager.shutdownNow();
	}
	
	/**
	 * Computes a percentage and updates the progress bar.
	 * @param count
	 */
	private void updateProgress(int count) {
		int percent = (int)(100 * (double)count / settings.getTotalOperationCount());
		setProgress(percent);
	}
	
	/**
	 * Loads candidateList with the top candidates, with the list size specified by the user.
	 */
	private void trimCandidateList() {
		candidateList.clear();
		candidateList.addAll(resultsList);	// Sorts on adding.
		
		while (candidateList.size() > settings.getCandidateSize()) {
			candidateList.poll();
		}
		
		resultsList.clear();	// Clear out result list for next step.
	}
}
