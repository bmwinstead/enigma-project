package main.java.enigma;

/**
 * This class simulates a single rotor of the Enigma encryption machine. It
 * stores both the forward and reverse wiring to allow for O(1) encryption and
 * decryption. It also stores the Ring offset (in the physical machine, this was
 * adjusted before the rotors were placed in the Enigma) and the stepOffset,
 * which indicates the current position of the rotor. It also stores the
 * positions of the carry notch(es), which impact when the rotor to the left of
 * it will rotate.
 * 
 * @author Walter Gene Adolph
 * @author Team Enigma
 * @version 0.9 Nov 22, 2013
 */
//This implementation uses two substitution databases for ensuring that both forward
//and reverse encryption is done in O(1) time.
public class Rotor {

	private final char[] forwardWiring;
	private final char[] reverseWiring;
	
	private int stepOffset;				// Rotor offset (Grundstellung)
	private int ringSetting;			// Ring offset (Ringstellung)
	private final char[] notchPositions;
	private final int size;             // Substitution alphabet size for easy reading.
	
	/**
	 * Constructor. Accepts a string representing the wiring of the rotor in the
	 * forward direction and determines the reverse wiring. The string is
	 * automatically converted to uppercase but is not otherwise checked for
	 * validity. Also accepts the location of the carry notches. Rotors have 0-2
	 * carry notches. The default notch location is '!', which represents
	 * no-notch.
	 * 
	 * @param code
	 *            A string representing the initial forward mapping of the
	 *            rotor. Characters in the string map to the letter in the
	 *            corresponding position of the actual alphabet.
	 * @param newNotch
	 *            A character array indicating the locations of the carry
	 *            notches. Arrays should always have a length of 2. '!'
	 *            indicates that carry notch is not used.
	 */
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
	} // end constructors

	/**
	 * Getter method for the current notch positions.
	 * 
	 * @return A two-character array indicating the current notch positions. '!'
	 *         indicates no notch.
	 */
	public char[] getNotchPosition() {
		char[] temp = new char[2];
		// Walter Adolph - There were some issues here; the character math is
		// not indexed to a, and there was no wrap-around. Fixed 11/21/2013.
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
	} // end getNotchPosition method
	
	/**
	 * Getter for the ringSetting.
	 * 
	 * @return Character indicating the ring setting. 
	 */
	public char getRingSetting() {
		return (char) (ringSetting + 'A');
	}
	
	/**
	 * Getter for the current position of the rotor.
	 * 
	 * @return A character representing the current position of the rotor. This
	 *         corresponds to the character that would be displayed for that
	 *         particular rotor on the top of the Enigma Machine.
	 */
	public char getPosition() {
		return (char)('A' + stepOffset);
	} // end getPosition method
	
	/**
	 * Sets the current ring setting, which rotates the rotor's replacement map
	 * relative to its default position.
	 * 
	 * @param newPosition
	 *            A character indicating the offset. In the Enigma Machine, this
	 *            would be the letter that a mark on the side of the rotor would
	 *            be aligned with.
	 */
	public void setRingPosition(char newPosition) {
		ringSetting = Character.toUpperCase(newPosition) - 'A';
	} // end setRingPosition method

	/**
	 * Sets the starting position of the rotor.
	 * 
	 * @param newPosition
	 *            A character indicating the starting position of the rotor.
	 *            This would be the letter visible on the top of the Enigma
	 *            Machine before the operator begins entering the text to be
	 *            encrypted.
	 */
	public void setStartPosition(char newPosition) {
		stepOffset = Character.toUpperCase(newPosition) - 'A';
	} // end setStartPosition method
	
	/**
	 * Rotates the current rotor and returns a boolean indicating whether the
	 * rotor to the left of it should also be rotated.
	 * 
	 * @return true if the rotor is in the carry notch position, indicating that
	 *         the rotor to the left of it should also be rotated.
	 */
	public boolean cycleRotor() {
		stepOffset = (stepOffset + 1) % size;	// Allows wrap-around.
		
		if(((notchPositions[0] - 'A') == stepOffset) || (notchPositions[1] - 'A') == stepOffset)
			return true;
		return false;	// Returns if the rotor is in the notch position.
	} // end cycleRotor method
	
	/**
	 * Returns the proper forward-encryption substitution based on the rotor's
	 * position and ring setting.
	 * 
	 * @param letter
	 *            Letter to be substituted.
	 * @return Letter to be passed to the rotor to the left of the current
	 *         rotor, or to the reflector.
	 */
	public char forwardEncrypt(char letter) {
		int letterIndex = Character.toUpperCase(letter) - 'A';
		int offset = stepOffset - ringSetting;
		int rotorAdjust = (size + letterIndex + offset) % size;
		int resultOffset = (size + forwardWiring[rotorAdjust] - 'A' - offset) % size;
		return (char)('A' + resultOffset);
	} // end forewardEncrypt method
	
	/**
	 * Returns the proper reverse-encryption substitution based on the rotor's
	 * position and ring setting.
	 * 
	 * @param letter
	 *            Letter to be substituted.
	 * @return Letter to be passed to the rotor to the right of the current
	 *         rotor, or the lightboard.
	 */
	public char reverseEncrypt(char letter) {
		int letterIndex = Character.toUpperCase(letter) - 'A';
		int offset = stepOffset - ringSetting;
		int rotorAdjust = (size + letterIndex + offset) % size;
		int resultOffset = (size + reverseWiring[rotorAdjust] - 'A' - offset) % size;
		return (char)('A' + resultOffset);
	} // end reverseEncrypt method
} // end Rotor class
