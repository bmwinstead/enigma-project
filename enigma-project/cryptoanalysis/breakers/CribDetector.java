package breakers;

import machine.CA_Rotor;
import machine.Encryptor;

public class CribDetector {
	private String text;
	
	public CribDetector(String cipherText) {
		text = cipherText;
	}
	
	public String testCrib(String crib) {
		Encryptor bomb = new Encryptor(
				CA_Rotor.ROTOR1,
				CA_Rotor.ROTOR2,
				CA_Rotor.ROTOR3,
				CA_Rotor.REFLECTORB);
		
		String formattedText = "";
		
		for (int index = 0; index < text.length(); index++) {
			if (Character.isAlphabetic(text.charAt(index)))
				formattedText += text.charAt(index);
		}
		
		for (int leftOffset = 0; leftOffset < 26; leftOffset++) {
			for (int middleOffset = 0; middleOffset < 26; middleOffset++) {
				for (int rightOffset = 0; rightOffset < 26; rightOffset++) {
					char leftRotorSetting = (char)('A' + leftOffset);
					char middleRotorSetting = (char)('A' + middleOffset);
					char rightRotorSetting = (char)('A' + rightOffset);
					
					bomb.setPositions(leftRotorSetting, middleRotorSetting, rightRotorSetting);
					
					String testString = bomb.encrypt(formattedText);
					
					for (int index = 0; index < testString.length() - crib.length(); index++) {
						String sample = testString.substring(index, crib.length() + index);
						
						if (sample.equals(crib.toUpperCase())) {
							String result = "" + leftRotorSetting + (char)middleRotorSetting + rightRotorSetting;
							return result;
						}
					}
				}
			}
		}
		
		return "";
	}
}
