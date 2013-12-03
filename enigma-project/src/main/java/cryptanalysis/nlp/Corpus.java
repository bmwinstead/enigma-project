package main.java.cryptanalysis.nlp;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

// This class represents a corpus (character database) containing unigrams, bigrams, trigrams, and quadgrams.
// Counts of each type of gram are kept for future statistical use.
// PriorityQueues are also provided with sorts by gram count.
// It is intended that all words are first loaded into HashMaps (fast lookup with counts),
// and then sortDatabase() is called to load priority queues with the entered words in descending word count order.
// Then each call to get(n-gram)TestQueue() returns a shallow copy of each queue for word processing.

public class Corpus implements Serializable {
	private static final long serialVersionUID = -6995615626048870170L;
	//private static final long serialVersionUID = -2096305067100712292L;
	// Statistic databases.
	private Map<String, Integer> unigramTable;
	private Map<String, Integer> bigramTable;
	private Map<String, Integer> trigramTable;
	private Map<String, Integer> quadgramTable;
	private Map<String, Integer> wordTable;
	
	// Sorted databases.
	private PriorityQueue<String> unigramQueue;
	private PriorityQueue<String> bigramQueue;
	private PriorityQueue<String> trigramQueue;
	private PriorityQueue<String> quadgramQueue;

	private int unigramCount;
	private int bigramCount;
	private int trigramCount;
	private int quadgramCount;
	private int wordCount;
	
	// Constructor.
	public Corpus() {
		unigramTable = new HashMap<String, Integer>();
		bigramTable = new HashMap<String, Integer>();
		trigramTable = new HashMap<String, Integer>();
		quadgramTable = new HashMap<String, Integer>();
		wordTable = new HashMap<String, Integer>();
		
		unigramQueue = new PriorityQueue<String>(11, new GramComparator(unigramTable));
		bigramQueue = new PriorityQueue<String>(11, new GramComparator(bigramTable));
		trigramQueue = new PriorityQueue<String>(11, new GramComparator(trigramTable));
		quadgramQueue = new PriorityQueue<String>(11, new GramComparator(quadgramTable));
	}
	
	public int getTotalUnigramCount() {
		return unigramCount;
	}
	
	public int getTotalBigramCount() {
		return bigramCount;
	}
	
	public int getTotalTrigramCount() {
		return trigramCount;
	}
	
	public int getTotalQuadgramCount() {
		return quadgramCount;
	}
	
	public int getTotalWordCount() {
		return wordCount;
	}
	
	public int getUnigramCount(String gram) {
		if (unigramTable.containsKey(gram.toUpperCase())) {
			return unigramTable.get(gram.toUpperCase());
		}
		
		return 0;
	}
	
	public int getBigramCount(String gram) {
		if (bigramTable.containsKey(gram.toUpperCase())) {
			return bigramTable.get(gram.toUpperCase());
		}
		
		return 0;
	}
	
	public int getTrigramCount(String gram) {
		if (trigramTable.containsKey(gram.toUpperCase())) {
			return trigramTable.get(gram.toUpperCase());
		}
		
		return 0;
	}
	
	public int getQuadgramCount(String gram) {
		if (quadgramTable.containsKey(gram.toUpperCase())) {
			return quadgramTable.get(gram.toUpperCase());
		}
		
		return 0;
	}
	
	public int getWordCount(String word) {
		if (quadgramTable.containsKey(word.toUpperCase())) {
			return quadgramTable.get(word.toUpperCase());
		}
		
		return 0;
	}
	
	// Returns shallow copy of test queue.
	public PriorityQueue<String> getUnigramTestQueue() {
		PriorityQueue<String> result = new PriorityQueue<String>(unigramTable.size(), new GramComparator(unigramTable));
		result.addAll(unigramTable.keySet());
		
		return result;
	}
	
	// Returns shallow copy of test queue.
	public PriorityQueue<String> getBigramTestQueue() {
		PriorityQueue<String> result = new PriorityQueue<String>(bigramTable.size(), new GramComparator(bigramTable));
		result.addAll(bigramTable.keySet());
		
		return result;
	}
	
	// Returns shallow copy of test queue.
	public PriorityQueue<String> getTrigramTestQueue() {
		PriorityQueue<String> result = new PriorityQueue<String>(trigramTable.size(), new GramComparator(trigramTable));
		result.addAll(trigramTable.keySet());
		
		return result;
	}
	
	// Returns shallow copy of test queue.
	public PriorityQueue<String> getQuadgramTestQueue() {
		PriorityQueue<String> result = new PriorityQueue<String>(quadgramTable.size(), new GramComparator(quadgramTable));
		result.addAll(quadgramTable.keySet());
		
		return result;
	}
	
	public void addUnigram(String word) {
		if (unigramTable.containsKey(word)) {
			int count = unigramTable.get(word);
			unigramTable.put(word, count + 1);
		}
		else {
			unigramTable.put(word, 1);
		}
		
		unigramCount++;
	}
	
	public void addBigram(String phrase) {
		if (bigramTable.containsKey(phrase)) {
			int count = bigramTable.get(phrase);
			bigramTable.put(phrase, count + 1);
		}
		else {
			bigramTable.put(phrase, 1);
		}
		
		bigramCount++;
	}

	public void addTrigram(String phrase) {
		if (trigramTable.containsKey(phrase)) {
			int count = trigramTable.get(phrase);
			trigramTable.put(phrase, count + 1);
		}
		else {
			trigramTable.put(phrase, 1);
		}
		
		trigramCount++;
	}
	
	public void addQuadgram(String phrase) {
		if (quadgramTable.containsKey(phrase)) {
			int count = quadgramTable.get(phrase);
			quadgramTable.put(phrase, count + 1);
		}
		else {
			quadgramTable.put(phrase, 1);
		}
		
		quadgramCount++;
	}
	
	public void addWord(String word) {
		if (wordTable.containsKey(word)) {
			int count = wordTable.get(word);
			wordTable.put(word, count + 1);
		}
		else {
			wordTable.put(word, 1);
		}
		
		wordCount++;
	}
	
	public boolean hasWord(String word) {
		return wordTable.containsKey(word);
	}
	
	// This method is intended to be called after all words are sorted.
	public void sortDatabase() {
		unigramQueue.clear();
		bigramQueue.clear();
		trigramQueue.clear();
		quadgramQueue.clear();
		
		for (String unigram : unigramTable.keySet()) {
			unigramQueue.add(unigram);
		}
		
		for (String bigram : bigramTable.keySet()) {
			bigramQueue.add(bigram);
		}
		
		for (String trigram : trigramTable.keySet()) {
			trigramQueue.add(trigram);
		}
		
		for (String quadgram : quadgramTable.keySet()) {
			quadgramQueue.add(quadgram);
		}
	}
	
	// Inner class to sort database words in descending count order.
	public class GramComparator implements Comparator<String>, Serializable {
		private static final long serialVersionUID = -245563502404239581L;
		private Map<String, Integer> table;
		
		public GramComparator(Map<String, Integer> database) {
			table = database;
		}
		
		public int compare(String arg0, String arg1) {
			if (table.get(arg0) < table.get(arg1))
				return 1;
			else if (table.get(arg0) > table.get(arg1))
				return -1; 
			else
				return 0;
		}
	}
}
