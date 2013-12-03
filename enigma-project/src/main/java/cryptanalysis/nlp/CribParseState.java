/**
 * TestParserState.java
 * @author - Walter Adolph
 * @author - Team Enigma
 * @date - Dec 2, 2013
 * Wrapper class to contain information about a word parse state.
 */
package main.java.cryptanalysis.nlp;

import java.util.Queue;

public class TestParserState implements Comparable<TestParserState> {
	private int startPointer;
	private int wordPointer;
	private Queue<String> wordList;
	private int lettersRemaining;
	private String message;
	
	public TestParserState(int startPointer, int wordPointer, Queue<String> wordList, int lettersRemaining, String message) {
		this.startPointer = startPointer;
		this.wordPointer = wordPointer;
		this.wordList = wordList;
		this.lettersRemaining = lettersRemaining;
		this.message = message;
	}

	/**
	 * @return the startPointer
	 */
	public int getStartPointer() {
		return startPointer;
	}

	/**
	 * @return the wordPointer
	 */
	public int getWordPointer() {
		return wordPointer;
	}

	/**
	 * @return the wordList
	 */
	public Queue<String> getWordList() {
		return wordList;
	}

	/**
	 * @return the lettersRemaining
	 */
	public int getLettersRemaining() {
		return lettersRemaining;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	@Override
	public int compareTo(TestParserState parser) {
		if (lettersRemaining > parser.getLettersRemaining()) {
			return 1;
		}
		else if (lettersRemaining < parser.getLettersRemaining()) {
			return -1;
		}
		else {
			return 0;
		}
	}
}
