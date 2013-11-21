package decoders;

import machine.CA_Rotor;
import machine.Encryptor;

// This method conducts a simplified brute-force attack on an Enigma machine.
// This method assumes a ring setting of 'AAA' with a rotor order of 123, for validity testing.
// A potential crib is provided and is tested against each possible character position in all possible rotor offset combinations.
// Failure is indicated by an empty string return. Success is indicated by a string containing the cracked rotor order.
public class CribDetector {
	private String text;
	
	public CribDetector(String cipherText) {
		text = cipherText;
	}
	
	public String testCrib(String crib) {
		// Set up an Enigma to run.
		//Encryptor bomb = new Encryptor(
				//CA_Rotor.ROTOR1,
				//CA_Rotor.ROTOR2,
				//CA_Rotor.ROTOR3,
				//CA_Rotor.REFLECTORB);
		/*
		String formattedText = "";
		
		// Skip non-alphabetic characters.
		for (int index = 0; index < text.length(); index++) {
			if (Character.isAlphabetic(text.charAt(index)))
				formattedText += text.charAt(index);
		}
		
		// Run rotor offset combinations.
		for (int leftOffset = 0; leftOffset < 26; leftOffset++) {
			for (int middleOffset = 0; middleOffset < 26; middleOffset++) {
				for (int rightOffset = 0; rightOffset < 26; rightOffset++) {
					char leftRotorSetting = (char)('A' + leftOffset);
					char middleRotorSetting = (char)('A' + middleOffset);
					char rightRotorSetting = (char)('A' + rightOffset);
					
					//bomb.setPositions(leftRotorSetting, middleRotorSetting, rightRotorSetting);
					
					//String testString = bomb.encrypt(formattedText);
					
					// Step the encrypted crib through the message and check for a match.
					for (int index = 0; index < testString.length() - crib.length(); index++) {
						String sample = testString.substring(index, crib.length() + index);
						
						if (sample.equals(crib.toUpperCase())) {
							String result = "" + leftRotorSetting + middleRotorSetting + rightRotorSetting;
							return result; // Success; return the rotor order.
						}
					}
				}
			}
		}
		*/
		return ""; // Failure; return an empty string.
	}
}
