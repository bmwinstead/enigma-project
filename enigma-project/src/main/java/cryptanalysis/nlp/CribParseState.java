/**
 * TestParserState.java
 * @author - Walter Adolph
 * @author - Team Enigma
 * @date - Dec 2, 2013
 * Wrapper class to contain information about a word parse state.
 */
package main.java.cryptanalysis.nlp;

import java.util.LinkedList;
import java.util.Queue;

public class CribParseState implements Comparable<CribParseState> {
	private int startPointer;
	private int wordPointer;
	private Queue<Crib> wordList;
	private int lettersRemaining;
	private String message;
	
	public CribParseState(int startPointer, int wordPointer, Queue<Crib> wordList, int lettersRemaining, String message) {
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
	public Queue<Crib> getWordList() {
		Queue<Crib> result = new LinkedList<Crib>();
		result.addAll(wordList);
		
		return result;
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
	
	public String toString() {
		return lettersRemaining + " encrypted letters.";
	}

	@Override
	public int compareTo(CribParseState parser) {
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
	
	// Two states are equal if they have the same message state.
	public boolean equals(CribParseState state) {
		if (message.equals(state.getMessage())) {
			return true;
		}
		
		return false;
	}
}
