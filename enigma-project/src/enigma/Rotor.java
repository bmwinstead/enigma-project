package enigma;

//This implementation uses two substitution databases for ensuring that both forward
//and reverse encryption is done in O(1) time.
public class Rotor {

	private final char[] forwardWiring;
	private final char[] reverseWiring;
	
	private int stepOffset;				// Rotor offset (Grundstellung)
	private int ringSetting;			// Ring offset (Ringstellung)
	private final char[] notchPositions;
	private final int size;             // Substitution alphabet size for easy reading.
	
	public Rotor(String code, char[] newNotch) {
		size = code.length();
		notchPositions = newNotch;
		forwardWiring = new char[size];
		reverseWiring = new char[size];
		setRingPosition('A');
		
		char[] chars = code.toUpperCase().toCharArray();
		
		for (int index = 0; index < size; index++) {
			forwardWiring[index] = chars[index];
			reverseWiring[chars[index] - 'A'] = (char)('A' + index);
		}
	}

	public char[] getNotchPosition() {
		char[] temp = new char[2];
			// Walter Adolph - There were some issues here; the character math is not indexed to a, and there was no wrap-around. Fixed 11/21/2013.
			if (notchPositions[0] != '!') {
				int offset = (notchPositions[0] - 'A' - 1) % size;
				temp[0] = (char)(offset + 'A');
			}
			else {
				temp[0] = '!';	// Preserve no notch indication.
			}
			
			if (notchPositions[1] != '!') {
				int offset = (notchPositions[1] - 'A' - 1) % size;
				temp[1] = (char)(offset + 'A');
			}
			else {
				temp[1] = '!';	// Preserve no notch indication.
			}

		return temp;
	}
	
	public char getPosition() {
		return (char)('A' + stepOffset);
	}
	
	public void setRingPosition(char newPosition) {
		ringSetting = Character.toUpperCase(newPosition) - 'A';
	}
	
	public void setStartPosition(char newPosition) {
		stepOffset = Character.toUpperCase(newPosition) - 'A';
	}
	
	public boolean cycleRotor() {
		stepOffset = (stepOffset + 1) % size;	// Allows wrap-around.
		
		if(((notchPositions[0] - 'A') == stepOffset) || (notchPositions[1] - 'A') == stepOffset)
			return true;
		return false;	// Returns if the rotor is in the notch position.
	}
	
	public char forwardEncrypt(char letter) {
		int letterIndex = Character.toUpperCase(letter) - 'A';
		int offset = stepOffset - ringSetting;
		int rotorAdjust = (size + letterIndex + offset) % size;
		int resultOffset = (size + forwardWiring[rotorAdjust] - 'A' - offset) % size;
		return (char)('A' + resultOffset);
	}
	
	public char reverseEncrypt(char letter) {
		int letterIndex = Character.toUpperCase(letter) - 'A';
		int offset = stepOffset - ringSetting;
		int rotorAdjust = (size + letterIndex + offset) % size;
		int resultOffset = (size + reverseWiring[rotorAdjust] - 'A' - offset) % size;
		return (char)('A' + resultOffset);
	}
}
