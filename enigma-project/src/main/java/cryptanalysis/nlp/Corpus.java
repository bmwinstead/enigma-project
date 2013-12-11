package main.java.cryptanalysis.nlp;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Implements a set of tables containing character unigrams, bigrams, trigrams, quadgrams, and whole words, with frequency counts of each.
 * 
 * After loading all the desired grams, it is recommended that trimCorpus() is called to remove very low frequency count occurrences.
 * N-grams and words are added by calling the appropriate add(ngram) method.
 * Retrieving a frequency count of a n-gram or word is done by get(ngram)Count().
 * Retrieving the total count of a n-gram is done by getTotal(ngram)Count().
 * Convenience methods to get sorted priority queues of grams is via get(ngram)TestQueue().
 * 
 * This class is not thread safe if retrieving frequency counts and/or ngram queues while adding ngrams.
 * If not adding words, then concurrent calls to get frequency counts and ngram queues is safe.
 * 
 * @author Walter Adolph
 * @author Team Enigma
 * @version 0.9
 * @date 30 Nov 2013
 * 
 */

public class Corpus implements Serializable {
	private static final long serialVersionUID = 3170587046915875517L;
	
	// Threshold setting for trimming out grams and words.
	private static final int COUNT_THRESHOLD = 1000000;
	
	// Statistic tables.
	private Map<String, Integer> unigramTable;
	private Map<String, Integer> bigramTable;
	private Map<String, Integer> trigramTable;
	private Map<String, Integer> quadgramTable;
	private Map<String, Integer> wordTable;

	// n-gram and word counters.
	private int unigramCount;
	private int bigramCount;
	private int trigramCount;
	private int quadgramCount;
	private int wordCount;
	
	public Corpus() {
		unigramTable = new HashMap<String, Integer>();
		bigramTable = new HashMap<String, Integer>();
		trigramTable = new HashMap<String, Integer>();
		quadgramTable = new HashMap<String, Integer>();
		wordTable = new HashMap<String, Integer>();
	}
	
	/**
	 * Gets the total count of added unigrams.
	 * @return total count of unigrams.
	 */
	public int getTotalUnigramCount() {
		return unigramCount;
	}
	
	/**
	 * Gets the total count of added bigrams.
	 * @return total count of bigrams.
	 */
	public int getTotalBigramCount() {
		return bigramCount;
	}
	
	/**
	 * Gets the total count of added trigrams.
	 * @return total count of trigrams.
	 */
	public int getTotalTrigramCount() {
		return trigramCount;
	}
	
	/**
	 * Gets the total count of added quadgrams.
	 * @return total count of quadgrams.
	 */
	public int getTotalQuadgramCount() {
		return quadgramCount;
	}
	
	/**
	 * Gets the total count of added words.
	 * @return total count of words.
	 */
	public int getTotalWordCount() {
		return wordCount;
	}
	
	/**
	 * Gets the frequency count of the specified unigram.
	 * @param gram - the gram to look for.
	 * @return the number of occurrences of the specified unigram, or 0 if not found.
	 */
	public int getUnigramCount(String gram) {
		if (unigramTable.containsKey(gram.toUpperCase())) {
			return unigramTable.get(gram.toUpperCase());
		}
		
		return 0;
	}
	
	/**
	 * Gets the frequency count of the specified bigram.
	 * @param gram - the gram to look for.
	 * @return the number of occurrences of the specified bigram, or 0 if not found.
	 */
	public int getBigramCount(String gram) {
		if (bigramTable.containsKey(gram.toUpperCase())) {
			return bigramTable.get(gram.toUpperCase());
		}
		
		return 0;
	}
	
	/**
	 * Gets the frequency count of the specified trigram.
	 * @param gram - the gram to look for.
	 * @return the number of occurrences of the specified trigram, or 0 if not found.
	 */
	public int getTrigramCount(String gram) {
		if (trigramTable.containsKey(gram.toUpperCase())) {
			return trigramTable.get(gram.toUpperCase());
		}
		
		return 0;
	}
	
	/**
	 * Gets the frequency count of the specified quadgram.
	 * @param gram - the gram to look for.
	 * @return the number of occurrences of the specified quadgram, or 0 if not found.
	 */
	public int getQuadgramCount(String gram) {
		if (quadgramTable.containsKey(gram.toUpperCase())) {
			return quadgramTable.get(gram.toUpperCase());
		}
		
		return 0;
	}
	
	/**
	 * Gets the frequency count of the specified word.
	 * @param gram - the word to look for.
	 * @return the number of occurrences of the specified word, or 0 if not found.
	 */
	public int getWordCount(String word) {
		if (wordTable.containsKey(word.toUpperCase())) {
			return wordTable.get(word.toUpperCase());
		}
		
		return 0;
	}
	
	/**
	 * Builds and gets an ordered queue of unigrams, sorted by descending frequency counts. 
	 * @return a new priority queue of sorted unigrams.
	 */
	public PriorityQueue<String> getUnigramTestQueue() {
		PriorityQueue<String> result = new PriorityQueue<String>(unigramTable.size(), new GramComparator(unigramTable));
		result.addAll(unigramTable.keySet());
		
		return result;
	}
	
	/**
	 * Builds and gets an ordered queue of bigrams, sorted by descending frequency counts. 
	 * @return a new priority queue of sorted bigrams.
	 */
	public PriorityQueue<String> getBigramTestQueue() {
		PriorityQueue<String> result = new PriorityQueue<String>(bigramTable.size(), new GramComparator(bigramTable));
		result.addAll(bigramTable.keySet());
		
		return result;
	}
	
	/**
	 * Builds and gets an ordered queue of trigrams, sorted by descending frequency counts. 
	 * @return a new priority queue of sorted trigrams.
	 */
	public PriorityQueue<String> getTrigramTestQueue() {
		PriorityQueue<String> result = new PriorityQueue<String>(trigramTable.size(), new GramComparator(trigramTable));
		result.addAll(trigramTable.keySet());
		
		return result;
	}
	
	/**
	 * Builds and gets an ordered queue of quadgrams, sorted by descending frequency counts. 
	 * @return a new priority queue of sorted quadgrams.
	 */
	public PriorityQueue<String> getQuadgramTestQueue() {
		PriorityQueue<String> result = new PriorityQueue<String>(quadgramTable.size(), new GramComparator(quadgramTable));
		result.addAll(quadgramTable.keySet());
		
		return result;
	}
	
	/**
	 * Builds and gets an ordered queue of words, sorted by descending frequency counts. 
	 * @return a new priority queue of sorted words.
	 */
	public PriorityQueue<String> getWordTestQueue() {
		PriorityQueue<String> result = new PriorityQueue<String>(wordTable.size(), new GramComparator(wordTable));
		result.addAll(wordTable.keySet());
		
		return result;
	}
	
	/**
	 * Counts and adds the specified unigram to the appropriate table. 
	 * @param a new priority queue of sorted unigrams.
	 */
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
	
	/**
	 * Counts and adds the specified bigram to the appropriate table. 
	 * @param a new priority queue of sorted bigrams.
	 */
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

	/**
	 * Counts and adds the specified trigram to the appropriate table. 
	 * @param a new priority queue of sorted trigrams.
	 */
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
	
	/**
	 * Counts and adds the specified quadgram to the appropriate table. 
	 * @param a new priority queue of sorted quadgrams.
	 */
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
	
	/**
	 * Counts and adds the specified word to the appropriate table. 
	 * @param a new priority queue of sorted words.
	 */
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
	
	/**
	 * Traverses each ngram and word table and attempts to remove likely bad grams and words. 
	 * Assumes that the majority of grams discovered are likely valid, and those that occur less than a set threshold
	 * (normally one in a million) are likely garbled.
	 * As it is expected that the only unigrams are alphanumeric, the check is not applied to the unigram table.
	 * 
	 * @param a new priority queue of sorted words.
	 */
	public void trimCorpus() {
		int countThreshold = getTotalBigramCount() / COUNT_THRESHOLD + 1;
		
		// Remove all entries with a frequency of less than 1 plus the threshold of the total gram count.
		for (String gram: getBigramTestQueue()) {	// Use the priority queue to allow modification of the underlying table.
			int count = bigramTable.get(gram);
			
			if (count < countThreshold) {
				bigramTable.remove(gram);
				bigramCount -= count;
			}
		}
		
		countThreshold = getTotalTrigramCount() / COUNT_THRESHOLD + 1;
		
		// Remove all entries with a frequency of less than 1 plus the threshold of the total gram count.
		for (String gram: getTrigramTestQueue()) {	// Use the priority queue to allow modification of the underlying table.
			int count = trigramTable.get(gram);
			
			if (count < countThreshold) {
				trigramTable.remove(gram);
				trigramCount -= count;
			}
		}
		
		countThreshold = getTotalQuadgramCount() / COUNT_THRESHOLD + 1;
		
		// Remove all entries with a frequency of less than 1 plus the threshold of the total gram count.
		for (String gram: getQuadgramTestQueue()) {	// Use the priority queue to allow modification of the underlying table.
			int count = quadgramTable.get(gram);
			
			if (count < countThreshold) {
				quadgramTable.remove(gram);
				quadgramCount -= count;
			}
		}
		
		countThreshold = getTotalWordCount() / COUNT_THRESHOLD + 1;
		
		HashMap<String, Integer> table = new HashMap<String, Integer>();
		table.putAll(wordTable);
		
		// Remove all entries with a frequency of less than 1 plus the threshold of the total gram count.
		for (String gram: table.keySet()) {	// Use a shallow copy to allow modification of the underlying table.
			int count = wordTable.get(gram);
			
			if (count < countThreshold) {
				wordTable.remove(gram);
				wordCount -= count;
			}
		}
	}
	
	/**
	 * Inner class implementing Comparator to order grams and words in descending frequency count.
	 */
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
