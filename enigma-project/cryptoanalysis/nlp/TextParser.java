package nlp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// This class takes a text file and parses into unigram, bigram, and trigram words and stores them.
public class TextParser {
	private Corpus database;
	
	public TextParser(Corpus newCorpus) {
		database = newCorpus;
	}
	
	public void parseFile(File file) {
		try {
			Scanner scanner = new Scanner(file);
			String firstWord = "";
			String secondWord = "";
			String thirdWord = "";
			
			// Parse words.
			while (scanner.hasNext()) {
				thirdWord = secondWord;
				secondWord = firstWord;
				firstWord = scanner.next().toLowerCase();
				
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
			
			scanner.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		// Parsing complete; sort for ready use.
		database.sortDatabase();
	}
}
