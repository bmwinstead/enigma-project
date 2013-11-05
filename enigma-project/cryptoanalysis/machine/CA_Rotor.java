package machine;

public class CA_Rotor {
	private final char[] forwardWiring;
	private final char[] reverseWiring;
	
	private int indexOffset;
	private final char notchPosition;
	private final int size;
	
	public CA_Rotor(String code) {
		this(code, '!', 'a');
	}
	
	public CA_Rotor(String code, char newNotch, char startPosition) {
		size = code.length();
		notchPosition = Character.toUpperCase(newNotch);
		forwardWiring = new char[size];
		reverseWiring = new char[size];
		setPosition(startPosition);
		
		char[] chars = code.toUpperCase().toCharArray();
		
		for (int index = 0; index < size; index++) {
			forwardWiring[index] = chars[index];
			reverseWiring[chars[index] - 'A'] = (char)('A' + index);
		}
	}
	
	public void setPosition(char newPosition) {
		indexOffset = Character.toUpperCase(newPosition) - 'A';
	}
	
	public boolean cycleRotor() {
		indexOffset = (indexOffset + 1) % forwardWiring.length;	// Allows wrap-around.
		
		return indexOffset == (notchPosition - 'A');		// Returns if the rotor is in the notch position.
	}
	
	public char forwardEncrypt(char letter) {
		int index = (Character.toUpperCase(letter) - 'A' + indexOffset) % size;
		return forwardWiring[index];
	}
	
	public char reverseEncrypt(char letter) {
		int index = Character.toUpperCase(letter) - 'A';
		int offset = (size + reverseWiring[index] - indexOffset - 'A') % size;
		return (char)('A' + offset);
	}
}
