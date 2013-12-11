package main.java.cryptanalysis.nlp;

import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;

/**
 * Analyzes a decrypted message and attempts to identify words and add spaces.
 * Calling parseMessage recursively splits the message by words in the corpus, starting with the most common word ("THE" in most cases).
 * For each piece of the message, a word probability and a count of unidentified characters is done, and compared for best fit.
 * Once a list of word matches is found, spaces are added, starting with the lowest probability word.
 * 
 * This class is not thread safe, as adding items to the corpus at the same time as accessing state values can lead to inconsistent state.
 * However, this class is designed to be called by a single thread.
 * 
 * @author Walter Adolph
 * @author Team Enigma
 * @version 0.9
 * @date 1 Dec 2013
 * 
 */

public class CribDetector {
	private Corpus database;
	private HashMap<String, WordCount> wordList;
	
	/** 
	 * Constructor requiring a completed corpus.
	 * @param corpus
	 */
	public CribDetector(Corpus corpus) {
		database = corpus;
		wordList = new HashMap<String, WordCount>();
	}
	
	/**
	 * Parses a message and adds spaces at word detections, based on the corpus table entries.
	 * @param message to be parsed.
	 * @return the same message with spaces
	 */
	public String parseMessage(String message) {
		String result = message;			// String to return.
		String messageMarker = message;		// Work string to 'mark out' found words.
		
		PriorityQueue<String> words = database.getWordTestQueue();
		List<String> refList = new LinkedList<String>();			// List of found words.
		
		// Find all possible word matches. If a word can be found in the whole string, then it exists
		// in some subset of the message. Inversely, if a word is not found in the whole string, it does
		// not exist in any contiguous subset of the message.
		//
		// This way, only the found words are needed to parse the string, and not the whole corpus word list.
		while (!words.isEmpty()) {
			String word = words.poll();
			if (message.contains(word)) {
				refList.add(word);
			}
		}
		
		// Call recursive method to parse the message for best word matches.
		WordCount results = splitWord(message, refList);
		
		// Remove duplicate instances of words.
		Set<String> wordResults = new HashSet<String>();
		wordResults.addAll(results.words);
		
		// Add the corpus word list to a deque to facilitate a reverse sort.
		Deque<String> reversedWords = new LinkedList<String>();
		reversedWords.addAll(database.getWordTestQueue());
		
		// Holds space locations in ascending order.
		TreeSet<Integer> wordStarts = new TreeSet<Integer>();
		
		// Traverse the corpus word order from least likely to most likely.
		while (!reversedWords.isEmpty()) {
			String word = reversedWords.removeLast();
			
			// Find a word match.
			if (wordResults.contains(word)) {
				// This is used to 'mark out' a found word, by replacing it with a nonalphannumeric character.
				// This prevents previously found words from returning future matches.
				// i.e. if INCREDULITY is found, a later IT search doesn't give a false spacing.
				String blackout = word;
				blackout = blackout.replaceAll(".", "!");
				
				// Find all instances of the matched word.
				for (int start = -1; start < message.length(); start += word.length()) {
					start = messageMarker.indexOf(word, start);
					
					// Check for no match.
					if (start != -1) {
						wordStarts.add(start);	// Add word index to list.
					}
					else {
						break;
					}
				}
				
				// Mark out all instances of the word.
				messageMarker = messageMarker.replaceAll(word, blackout);
				
				wordResults.remove(word);
			}
		}
		
		// Starting with the end of the string, add spaces in.
		// Starting at the end of the string avoids moving index issues.
		while (!wordStarts.isEmpty()) {
			int index = wordStarts.pollLast();
			
			String first = result.substring(0, index);
			
			result = first + " " + result.substring(index);
		}
		
		return result;
	}
	
	/**
	 * Recursively finds the best phrase split of a specified message.
	 * More likely phrases and phrases with fewer unknown characters are ranked higher.
	 * As the method works through the parse, discovered phrases are saved in a static word list
	 * to facilitate later lookups, saving rework.
	 * 
	 * @param phrase to be parsed
	 * @param referenceList to be used as a reference.
	 * @return the best phrase split
	 */
	private WordCount splitWord(String message, List<String> referenceList) {
		// Base case - Phrase already found.
		if (wordList.containsKey(message)) {
			return wordList.get(message);
		}
		
		// Base case - no split is possible.
		double bestScore = getProbability(message);
		int bestLetterCount = message.length();
		
		WordCount bestWord = new WordCount(message, bestScore, bestLetterCount);
		
		// Find word matches.
		for (String word : referenceList) {
			if (message.contains(word)) {
				String[] pieces = message.split(word);
				
				int missingLetters = 0;
				double totalScore = 0.0;
				
				// Compute each piece.
				for (String piece : pieces) {
					if (piece.length() > 0) {
						WordCount testWord = splitWord(piece, referenceList);
						
						missingLetters += testWord.letters;
						totalScore += testWord.score;
					}
				}
				
				// Add split word probability to candidate.
				double probability = getProbability(word);
				
				// Multiply split word probability by number of matches.
				// A word matched n times makes n + 1 splits.
				if (pieces.length > 2) {
					probability *= (pieces.length - 1);
				}
				
				// Add split word to table if not already there.
				if (!wordList.containsKey(word)) {
					WordCount foundWord = new WordCount(word, probability, 0);
					wordList.put(word, foundWord);
				}
				
				totalScore += probability;
				
				// Compare results and save the best.
				if (totalScore > bestScore) {
					bestWord = new WordCount(pieces, totalScore, missingLetters);
					bestWord.words.add(word);
					bestScore = totalScore;
					bestLetterCount = missingLetters;
				}
				else if (missingLetters < bestLetterCount) {
					bestWord = new WordCount(pieces, totalScore, missingLetters);
					bestWord.words.add(word);
					bestScore = totalScore;
					bestLetterCount = missingLetters;
				}
			}
		}
		
		wordList.put(message, bestWord);
		return bestWord;
	}
	
	/**
	 * Computes log 10 probability of a word frequency.
	 * @param word to analyze
	 * @return log probability
	 */
	private double getProbability(String word) {
		int count = database.getWordCount(word);
		int total = database.getTotalWordCount();
		
		if (count == 0) {
			return -3.0 + Math.log10(1.0 / total);	// If no word floor by 1 / 1000 of a single instance.
		}
		else {
			return Math.log10((double)count / total);
		}
	}
	
	/**
	 * Inner class to contain statistics on word splits.
	 * @author Walter
	 *
	 */
	public class WordCount {
		private int letters = 0;
		private double score = 0.0;
		
		private List<String> words;
		
		public WordCount(String[] words, double score, int letters) {
			this();
			
			for (String word : words) {
				if (word.length() > 0) {
					this.words.addAll(wordList.get(word).words);
				}
			}
			
			this.score = score;
			this.letters = letters;
		}
		
		public WordCount(String word, double score, int letters) {
			this();
			
			this.words.add(word);
			this.score = score;
			this.letters = letters;
		}
		
		public WordCount() {
			words = new LinkedList<String>();
		}
		
		public String toString() {
			String result = "[";
			
			for (String value : words) {
				result += value + " ";
			}
			
			result += "] - " + letters + " - " + score;
			
			return result;
		}
	}
}
