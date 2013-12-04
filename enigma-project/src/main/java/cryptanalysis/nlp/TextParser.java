/**
 * TextParser.java
 * @author - Walter Adolph
 * @author - Team Enigma
 * @date - Dec 1, 2013
 * This parser accumulates statistical data from a corpus on individual characters.
 * 1, 2, 3 and 4 character grams are analyzed.
 * A word table is also generated.
 * Punctuation is ignored.
 */

package main.java.cryptanalysis.nlp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextParser {
	private Corpus database;

	public TextParser(Corpus newCorpus) {
		database = newCorpus;
	}
	
	public void parseFile(File file) {
		try {
			Scanner scanner = new Scanner(file);
			
			char firstgram = '\0';
			char secondgram = '\0';
			char thirdgram = '\0';
			char fourthgram = '\0';
			
			// Parse words.
			while (scanner.hasNext()) {
				String word = scanner.next().trim().toUpperCase();
				String gram = "";
				
				char[] characters = word.toCharArray();
				
				for (char character: characters) {
					Pattern nonalphanumeric = Pattern.compile("[^\\w&&[^_]]");
					Matcher invalidCharacterMatcher = nonalphanumeric.matcher("" + character);
					
					if (!invalidCharacterMatcher.find()) {	// Skip punctuation.
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
		
		// Parsing complete; sort for ready use.
		database.sortDatabase();
	}
	
	// Performs a variety of tests to help ensure only properly formatted words are entered.
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
