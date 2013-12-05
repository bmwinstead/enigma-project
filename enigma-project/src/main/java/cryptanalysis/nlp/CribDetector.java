/**
 * WordTester.java
 * @author - Walter Adolph
 * @author - Team Enigma
 * @date - Dec 1, 2013
 * 
 * TODO: Add attribution here.
 * http://stackoverflow.com/questions/4580877/text-segmentation-dictionary-based-word-splitting
 * https://docs.google.com/viewer?a=v&pid=sites&srcid=ZGVmYXVsdGRvbWFpbnxkanBkZnN0b3JlfGd4OjQ1YmFiZTNhODVjMzY2MmY
 * 
 */
package main.java.cryptanalysis.nlp;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;


public class CribDetector {
	private Corpus database;
	private HashMap<String, IndexScorePair> probList;
	
	public CribDetector(Corpus corpus) {
		database = corpus;
		probList = new HashMap<String, IndexScorePair>();
	}
	
	public String parseMessage(String message) {
		Deque<Integer> breaks = splitWord(message, 0).index;
		breaks.removeLast();	// Remove the zero index break.
		
		String result = "";
		int startIndex = 0;
		
		while (!breaks.isEmpty()) {
			int endIndex = breaks.removeLast();
			result += message.substring(startIndex, endIndex) + " ";
			
			startIndex = endIndex;
			
			if (breaks.isEmpty()) {
				result += message.substring(startIndex);
			}
		}
		
		return result;
	}
	
	private IndexScorePair splitWord(String message, int beginIndex) {
		// Base case.
		if (message.length() == 0) {
			return new IndexScorePair(0.0);
		}
		
		// If the message section has already been computed just return the probability value.
		if (probList.containsKey(message)) {
			return probList.get(message);
		}
		
		IndexScorePair bestIndex = new IndexScorePair(Double.NEGATIVE_INFINITY);
		
		for (int index = 1; index <= message.length(); index++) {
			String testFirstString = message.substring(0, index);
			int count = database.getWordCount(message.substring(0, index));
			double probability;
			
			probability = Math.log10((double)count / database.getTotalWordCount());
			
			// Recursive case.
			String testSecondString = message.substring(index);
			IndexScorePair testValue = splitWord(message.substring(index), beginIndex + index);
			
			if (probability + testValue.score > bestIndex.score) {
				bestIndex = new IndexScorePair(testValue);
				bestIndex.index.add(beginIndex);
				bestIndex.score = probability + testValue.score;
			} // End best case test.
		} // Message traverse.
		
		probList.put(message, bestIndex);
		return bestIndex;
	}
	
	private class IndexScorePair {
		private double score;
		private Deque<Integer> index;
		
		private IndexScorePair(IndexScorePair pair) {
			this(pair.score, pair.index);
		}
		
		private IndexScorePair(double score, Queue<Integer> index) {
			this(score);
			
			this.index.addAll(index);
		}
		
		private IndexScorePair(double score) {
			this.score = score;
			this.index = new LinkedList<Integer>();
		}
		
		public String toString() {
			String result = "[";
			
			for (Integer value : index) {
				result += value + " ";
			}
			
			result += "] - " + score;
			
			return result;
		}
	}
}
