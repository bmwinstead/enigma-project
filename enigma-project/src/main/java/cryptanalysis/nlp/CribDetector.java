/**
 * WordTester.java
 * @author - Walter Adolph
 * @author - Team Enigma
 * @date - Dec 1, 2013
 * 
 * 
 */
package main.java.cryptanalysis.nlp;

import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;


public class CribDetector {
	private Corpus database;
	private HashMap<String, WordCount> wordList;
	
	public CribDetector(Corpus corpus) {
		database = corpus;
		wordList = new HashMap<String, WordCount>();
	}
	
	public String parseMessage(String message) {
		String result = message;
		/*
		PriorityQueue<String> words = database.getWordTestQueue();
		List<String> refList = new LinkedList<String>();
		
		// Find all possible word matches.
		while (!words.isEmpty()) {
			String word = words.poll();
			if (message.contains(word)) {
				refList.add(word);
			}
		}
		
		WordCount results = splitWord(message, refList);
		
		Set<String> wordResults = new HashSet<String>();
		wordResults.addAll(results.words);
		
		Deque<String> reversedWords = new LinkedList<String>();
		reversedWords.addAll(database.getWordTestQueue());
		
		while (!reversedWords.isEmpty()) {
			String word = reversedWords.removeLast();
			
			if (wordResults.contains(word)) {
				result = result.replace(word, word + " ");
				wordResults.remove(word);
			}
		}
		*/
		return result;
	}
	
	private WordCount splitWord(String message, List<String> referenceList) {
		if (wordList.containsKey(message)) {
			return wordList.get(message);
		}
		
		double bestScore = getProbability(message);
		int bestLetterCount = message.length();
		
		WordCount bestWord = new WordCount(message, bestScore, bestLetterCount);
		
		for (String word : referenceList) {
			if (message.contains(word)) {
				String[] pieces = message.split(word);
				
				int missingLetters = 0;
				double totalScore = 0.0;
				
				for (String piece : pieces) {
					if (piece.length() > 0) {
						WordCount testWord = splitWord(piece, referenceList);
						
						missingLetters += testWord.letters;
						totalScore += testWord.score;
					}
				}
				
				double probability = getProbability(word);
				
				if (!wordList.containsKey(word)) {
					WordCount foundWord = new WordCount(word, probability, 0);
					wordList.put(word, foundWord);
				}
				
				if (pieces.length > 2) {
					probability *= (pieces.length - 1);
				}
				
				totalScore += probability;
				
				if (totalScore > bestScore) {
					bestScore = totalScore;
					bestLetterCount = missingLetters;
					bestWord = new WordCount(pieces, totalScore, missingLetters);
					bestWord.words.add(word);
				}
				else if (missingLetters < bestLetterCount) {
					bestScore = totalScore;
					bestLetterCount = missingLetters;
					bestWord = new WordCount(pieces, totalScore, missingLetters);
					bestWord.words.add(word);
				}
			}
		}
		
		wordList.put(message, bestWord);
		return bestWord;
	}
	
	private double getProbability(String word) {
		int count = database.getWordCount(word);
		int total = database.getTotalWordCount();
		
		if (count == 0) {
			return -3.0 + Math.log10(1.0 / total);
		}
		else {
			return Math.log10((double)count / total);
		}
	}
	
	private class WordCount {
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
