package machine;

// This implementation uses two substitution databases for ensuring that both forward
// and reverse encryption is done in O(1) time.
public class CA_Rotor {
	private final char[] forwardWiring;
	private final char[] reverseWiring;
	
	private int stepOffset;				// Rotor offset (Grundstellung)
	private int ringSetting;			// Ring offset (Ringstellung)
	private final char notchPosition;
	private final int size;				// Substitution alphabet size for easy reading.
	
	// Standard Enigma rotor definitions.
	public static final CA_Rotor ROTOR1 = new CA_Rotor("EKMFLGDQVZNTOWYHXUSPAIBRCJ", 'R');
	public static final CA_Rotor ROTOR2 = new CA_Rotor("AJDKSIRUXBLHWTMCQGZNPYFVOE", 'F');
	public static final CA_Rotor ROTOR3 = new CA_Rotor("BDFHJLCPRTXVZNYEIWGAKMUSQO", 'W');
	public static final CA_Rotor REFLECTORB = new CA_Rotor("YRUHQSLDPXNGOKMIEBFZCWVJAT");
	
	public CA_Rotor(String code) {
		this(code, '!');
	}
	
	public CA_Rotor(String code, char newNotch) {
		size = code.length();
		notchPosition = Character.toUpperCase(newNotch);
		forwardWiring = new char[size];
		reverseWiring = new char[size];
		setRingPosition('A');			// Default for now; Setting the ring requires a call to setRingPosition().
		
		char[] chars = code.toUpperCase().toCharArray();
		
		for (int index = 0; index < size; index++) {
			forwardWiring[index] = chars[index];
			reverseWiring[chars[index] - 'A'] = (char)('A' + index);
		}
	}
	
	public char getNotchPosition() {
		return (char)(notchPosition - 1);
	}
	
	public char getPosition() {
		return (char)('A' + stepOffset);
	}
	
	// Sets ring setting (Ringstellung)
	public void setRingPosition(char newPosition) {
		ringSetting = Character.toUpperCase(newPosition) - 'A';
	}
	
	// Sets rotor offset (Grundstellung)
	public void setStartPosition(char newPosition) {
		stepOffset = Character.toUpperCase(newPosition) - 'A';
	}
	
	// Steps the rotor 1 position.
	public boolean cycleRotor() {
		stepOffset = (stepOffset + 1) % forwardWiring.length;	// Allows wrap-around.
		
		return stepOffset == (notchPosition - 'A');		// Returns if the rotor is in the notch position.
	}
	
	public char forwardEncrypt(char letter) {
		int letterIndex = Character.toUpperCase(letter) - 'A';
		int offset = stepOffset - ringSetting;
		int rotorAdjust = (size + letterIndex + offset) % size;
		int resultOffset = (size + forwardWiring[rotorAdjust] - 'A' - offset) % size;
		return (char)('A' + resultOffset);
	}
	
	// Encrypts on the reverse path from the reflector.
	public char reverseEncrypt(char letter) {
		int letterIndex = Character.toUpperCase(letter) - 'A';
		int offset = stepOffset - ringSetting;
		int rotorAdjust = (size + letterIndex + offset) % size;
		int resultOffset = (size + reverseWiring[rotorAdjust] - 'A' - offset) % size;
		return (char)('A' + resultOffset);
	}
}
