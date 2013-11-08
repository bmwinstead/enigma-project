package nlp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		database.sortDatabase();
	}
}
