/**
 * WordParser.java
 * @author - walter
 * @date - Nov 21, 2013
 * This parser accumulates statistical data from a corpus on individual words.
 * 1, 2, and 3 word grams are analyzed, with numerals handled individually on a separate case.
 * Punctuation is currently ignored.
 */
package main.java.cryptanalysis.nlp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


// This class takes a text file and parses into unigram, bigram, and trigram words and stores them.
public class WordParser {
	private Corpus database;
	
	// Listed as instance members to keep track of word order.
	private String firstWord = "";
	private String secondWord = "";
	private String thirdWord = "";
	
	public WordParser(Corpus newCorpus) {
		database = newCorpus;
	}
	
	public void parseFile(File file) {
		try {
			Scanner scanner = new Scanner(file);
			Pattern nonAlphanumeric = Pattern.compile("[\\W^_]");	// Pattern to find non-alphanumeric characters.
			
			// Parse words.
			while (scanner.hasNext()) {
				String currentWord = scanner.next().toUpperCase();
				
				Matcher punctuationMatcher = nonAlphanumeric.matcher(currentWord);

				// Reject words with any punctuation for now.
				// TODO: add special processing for various punctuation.
				if (!punctuationMatcher.find()) {
					processNextWord(currentWord);
				} // End if
			} // End while
			
			scanner.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Parsing complete; sort for ready use.
		database.sortDatabase();
	}
	
	// Processes the next word.
	public void processNextWord(String word) {
		thirdWord = secondWord;
		secondWord = firstWord;
		firstWord = word;
		
		database.addUnigram(firstWord);
		
		if (!secondWord.equals("")) {
			String phrase = secondWord + " " + firstWord;
			
			database.addBigram(phrase);
			
			if (!thirdWord.equals("")) {
				phrase = thirdWord + " " + secondWord + " " + firstWord;
				
				database.addTrigram(phrase);
			}
		}
	}
}
