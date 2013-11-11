package machine;

// Simulates a standard 3-rotor Enigma, for cryptoanalysis testing.
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
	public void setPositions(char left, char middle, char right) {
		this.left.setStartPosition(left);
		this.middle.setStartPosition(middle);
		this.right.setStartPosition(right);
	}
	
	// Encrypt a letter.
	public String encrypt(String text) {
		String result = "";
		boolean doubleStep = false;
		
		// Default for now; need to add method to set ring offsets (Ringstellung).
		left.setRingPosition('A');
		middle.setRingPosition('A');
		right.setRingPosition('A');
		
		for (int index = 0; index < text.length(); index++) {
			char letter = text.charAt(index);
			
			// Skip non-alphabet characters.
			if (Character.isAlphabetic(letter)) {
				if (right.cycleRotor() || doubleStep) {
					if (middle.cycleRotor()) {
						if (doubleStep) { // Cycle the left rotor when ready to double step.
							left.cycleRotor();
							doubleStep = false;
						}
					} else if (middle.getPosition() == middle.getNotchPosition()) { // Then double step on the next letter.
						doubleStep = true;
					}
				}
				
				// Encryption chain.
				letter = right.forwardEncrypt(text.charAt(index));
				letter = middle.forwardEncrypt(letter);
				letter = left.forwardEncrypt(letter);
				letter = reflector.forwardEncrypt(letter);
				letter = left.reverseEncrypt(letter);
				letter = middle.reverseEncrypt(letter);
				letter = right.reverseEncrypt(letter);
			}
				
			result = result + letter;
		}
		
		return result;
	}
}
