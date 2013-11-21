package machine;

import misc.Logger;

// Simulates a standard 3-rotor Enigma, for cryptanalysis testing.
public class Encryptor {
	private CA_Rotor left;
	private CA_Rotor middle;
	private CA_Rotor right;
	private CA_Rotor reflector;
	
	// Constructor with defined wheel order.
	public Encryptor(CA_Rotor left, CA_Rotor middle, CA_Rotor right, CA_Rotor reflector) {
		this.left = left;
		this.middle = middle;
		this.right = right;
		this.reflector = reflector;
	}
	
	// Set rotor offset for rotors.
	public void setRotorPositions(char left, char middle, char right) {
		this.left.setStartPosition(left);
		this.middle.setStartPosition(middle);
		this.right.setStartPosition(right);
	}
	
	// Encrypt a letter.
	public String encrypt(String text) {
		String result = "";
		char middlePosition = middle.getPosition();
		char notchPosition = middle.getNotchPosition();
		boolean doubleStep = (middlePosition == notchPosition);
		
		for (int index = 0; index < text.length(); index++) {
			char letter = text.charAt(index);
			
			Logger.makeEntry("Character to encrypt: " + letter, true);
			
			// Skip non-alphabet characters.
			if (Character.isAlphabetic(letter)) {
				boolean middleStep = right.cycleRotor() || doubleStep;
				Logger.makeEntry("Stepped right rotor to: " + right.getPosition(), true);
				
				if (middleStep) {
					boolean leftStep = middle.cycleRotor();
					Logger.makeEntry("Stepped middle rotor to: " + middle.getPosition(), true);
					
					if (leftStep) {
						if (doubleStep) { // Cycle the left rotor when ready to double step.
							left.cycleRotor();
							Logger.makeEntry("Stepped left rotor to: " + left.getPosition(), true);
							
							doubleStep = false;
							Logger.makeEntry("Double Step set to " + doubleStep, true);
						}
					} else if (middle.getPosition() == middle.getNotchPosition()) { // Then double step on the next letter.
						Logger.makeEntry("Double Step set to " + doubleStep, true);
						doubleStep = true;
					}
				}
				
				// Encryption chain.
				letter = right.forwardEncrypt(text.charAt(index));
				Logger.makeEntry("Right Rotor Encryption: " + letter, true);
				
				letter = middle.forwardEncrypt(letter);
				Logger.makeEntry("Middle Rotor Encryption: " + letter, true);
				
				letter = left.forwardEncrypt(letter);
				Logger.makeEntry("Left Rotor Encryption: " + letter, true);
				
				letter = reflector.forwardEncrypt(letter);
				Logger.makeEntry("Reflector Encryption: " + letter, true);
				
				letter = left.reverseEncrypt(letter);
				Logger.makeEntry("Left Rotor Inverse Encryption: " + letter, true);
				
				letter = middle.reverseEncrypt(letter);
				Logger.makeEntry("Middle Rotor Inverse Encryption: " + letter, true);
				
				letter = right.reverseEncrypt(letter);
				Logger.makeEntry("Right Rotor Inverse Encryption: " + letter, true);
			}
				
			result = result + letter;
		}
		
		return result;
	}
}
