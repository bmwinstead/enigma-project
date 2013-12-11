package main.java.cryptanalysis.nlp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TextParser.java
 * 
 * This parser takes a database reference and scans a text file (UTF-8 is proven to work, other formats are unknown) for
 * character grams and whole words. Nonalphanumeric characters are skipped for character grams, and specific rules are 
 * applied for parsing words.
 * 
 * @author - Walter Adolph
 * @author - Team Enigma
 * Dec 1, 2013
 */
public class TextParser {
	private Corpus database;

	/**
	 * Constructor providing a handle to a corpus instance.
	 * @param newCorpus
	 */
	public TextParser(Corpus newCorpus) {
		database = newCorpus;
	}
	
	/**
	 * Parses a file for character grams and words, while applying set rules for
	 * nonalphanumeric characters.
	 * 
	 * @param file
	 *            Text file to be parsed.
	 */
	public void parseFile(File file) {
		try {
			Scanner scanner = new Scanner(file);
			
			char firstgram = '\0';
			char secondgram = '\0';
			char thirdgram = '\0';
			char fourthgram = '\0';
			
			// Parse on word tokens.
			while (scanner.hasNext()) {
				String word = scanner.next().trim().toUpperCase();	// Format word.
				String gram = "";
				
				char[] characters = word.toCharArray();
				
				for (char character: characters) {
					Pattern nonalphanumeric = Pattern.compile("[^\\w&&[^_]]");					// Matches on nonalphanumeric characters.
					Matcher invalidCharacterMatcher = nonalphanumeric.matcher("" + character);
					
					if (!invalidCharacterMatcher.find() && character != '_') {	// Skip punctuation.
						fourthgram = thirdgram;
						thirdgram = secondgram;
						secondgram = firstgram;
						firstgram = character;
						
						database.addUnigram(String.valueOf(firstgram));
						
						if (secondgram != '\0') {
							gram = "" + secondgram + firstgram;
							
							database.addBigram(gram);
							
							if (thirdgram != '\0') {
								gram = "" + thirdgram + secondgram + firstgram;
								
								database.addTrigram(gram);
								
								if (fourthgram != '\0') {
									gram = "" + fourthgram + thirdgram + secondgram + firstgram;
									
									database.addQuadgram(gram);
								} // End quadgram if
							} // End trigram if
						} // End bigram if
					} // End punctuation if
				} // End character for
				
				addWord(word);
			} // End while
			
			scanner.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds a word, applying set rules for specific punctuation.
	 * First, check for words with both numbers and letters, or words with no letters or numbers. If found, reject the word.
	 * Next, split hyphenated words into word pieces and continue parsing.
	 * Next, remove all invalid punctuation.
	 * Next, check for valid single character words (A, I, and digits). Otherwise, reject the word.
	 * Next, if a number, split the number into individual digits and add.
	 * Finally, if not an empty string add the word.
	 * 
	 * @param word to be parsed.
	 */
	private void addWord(String word) {
		Pattern nonalphanumeric = Pattern.compile("[^\\w&&[^_]]");
		
		Pattern alphabetic = Pattern.compile("[A-Z]");
		Pattern numeric = Pattern.compile("\\d");
		
		Matcher alphabetMatcher = alphabetic.matcher(word);
		Matcher digitMatcher = numeric.matcher(word);
		
		boolean isAlphabetic = alphabetMatcher.find();
		boolean isNumeric = digitMatcher.find();
		
		// Check for digits and letters in the same word. Also, reject any words with no valid letters or numbers.
		if ((!isAlphabetic || !isNumeric) && (isAlphabetic || isNumeric)) {
			// If there is a hyphen split the words.
			String[] parts = word.split("-");
			
			for (String part : parts) {
				// Remove extraneous characters.
				Matcher invalidCharacterMatcher = nonalphanumeric.matcher(part);
				String formattedWord = invalidCharacterMatcher.replaceAll("");
				
				// Add a single-character letter only if it is an A or I.
				if (formattedWord.length() == 1 && (!formattedWord.equals("A") && !formattedWord.equals("I") && !Character.isDigit(formattedWord.charAt(0)))) {
					formattedWord = "";
				}
				
				// If a number only then split the digits and add separately.
				if (isNumeric) {
					for (int index = 0; index < formattedWord.length(); index++) {
						database.addWord("" + formattedWord.charAt(index));
					}
					
					formattedWord = "";
				}
				
				if (!formattedWord.equals("")) {
					database.addWord(formattedWord);
				}
			}
		}
	}
}
