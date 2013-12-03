/**
 * WordTester.java
 * @author - Walter Adolph
 * @author - Team Enigma
 * @date - Dec 1, 2013
 * This parser searches for valid and invalid words in a string.
 * Reporting is made on both valid and invalid words, with locations kept.
 */
package main.java.cryptanalysis.nlp;

import java.util.Deque;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class WordTester {
	private Corpus database;
	private Queue<String> wordList;
	Deque<TestParserState> stack;
	
	public WordTester(Corpus corpus) {
		database = corpus;
		wordList = new LinkedList<String>();
		stack = new LinkedList<TestParserState>();
	}
	
	public PriorityQueue<TestParserState> parseString(String message) {
		PriorityQueue<TestParserState> candidateList = new PriorityQueue<TestParserState>();
		
		stack.add(new TestParserState(0, 0, wordList, message.length(), message));
		
		while (!stack.isEmpty()) {
			TestParserState candidate = findWords(stack.pop());
			
			if (candidate.getLettersRemaining() <= 0) {
				PriorityQueue<TestParserState> result = new PriorityQueue<TestParserState>();
				result.add(candidate);
				return result;
			}
			
			candidateList.add(candidate);
		}
		
		return candidateList;
	}
	
	private TestParserState findWords(TestParserState state) {
		String message = state.getMessage();
		int startPosition = state.getStartPointer();
		int wordPosition = state.getWordPointer();
		int letterCount = state.getLettersRemaining();
		
		do {
			String word = "";
			boolean wordFlag = false;

			for (int index = startPosition; index < message.length(); index++) {
				word += "" + message.charAt(index);
				
				if (database.hasWord(word)) {
					wordFlag = true;
					wordPosition = index;
				}
				else {
					if (wordFlag) {
						wordFlag = false;
						stack.addFirst(new TestParserState(wordPosition + 1, wordPosition + 1, wordList, letterCount, message));
						wordList.add(message.substring(startPosition, wordPosition + 1));
						message = removeWord(message, startPosition, wordPosition + 1);
						letterCount -= wordPosition - startPosition + 1;
						startPosition = wordPosition + 1;
						word = "";
						index--;
					}
				}
			}
			
			// Once a pass-through is complete,
			if (database.hasWord(word)) {
				wordFlag = false;
				wordList.add(message.substring(startPosition, wordPosition));
				message = removeWord(message, startPosition, wordPosition + 1);
				letterCount -= wordPosition - startPosition + 1;
				startPosition = wordPosition + 1;
			}
			
			if (letterCount > 0) {
				startPosition++;
				wordPosition = startPosition;
			}
		} while (letterCount > 0 && startPosition < message.length());
		
		return new TestParserState(startPosition, startPosition, wordList, letterCount, message);
	}
	
	private String removeWord(String string, int start, int end) {
		String result = string.substring(0, start);
		
		for (int index = 0; index < end - start; index++) {
			result += "" + "!";
		}
		
		result += string.substring(end);
		
		return result;
	}
}
