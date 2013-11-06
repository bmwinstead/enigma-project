package machine;

public class CA_Rotor {
	private final char[] forwardWiring;
	private final char[] reverseWiring;
	
	private int stepOffset;
	private int ringOffset;
	private final char notchPosition;
	private final int size;
	
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
		setRingPosition('A');
		
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
	
	public void setRingPosition(char newPosition) {
		ringOffset = Character.toUpperCase(newPosition) - 'A';
	}
	
	public void setStartPosition(char newPosition) {
		stepOffset = Character.toUpperCase(newPosition) - 'A';
	}
	
	public boolean cycleRotor() {
		stepOffset = (stepOffset + 1) % forwardWiring.length;	// Allows wrap-around.
		
		return stepOffset == (notchPosition - 'A');		// Returns if the rotor is in the notch position.
	}
	
	public char forwardEncrypt(char letter) {
		int letterIndex = Character.toUpperCase(letter) - 'A';
		int offset = (letterIndex + stepOffset + ringOffset) % size;
		int resultOffset = (size + forwardWiring[offset] - 'A' - stepOffset) % size;
		return (char)('A' + resultOffset);
	}
	
	public char reverseEncrypt(char letter) {
		int index = Character.toUpperCase(letter) - 'A';
		int letterIndex = (size + index + stepOffset) % size;
		int offset = (2 * size + reverseWiring[letterIndex] - stepOffset - ringOffset - 'A') % size;
		return (char)('A' + offset);
	}
}
