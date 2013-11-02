package enigma;

public class Rotor {
	/**
	 * Essentially the wiring of the Rotor.
	 */
	private char[] charMap;
	/**
	 * The current charMap that takes into account the stepping function. Only
	 * different from charMap to facilitate encrypting multiple messages without
	 * having to re-initialize the machine.
	 */
	private char[] curCharMap;
	/**
	 * The current alphabet.
	 */
	private static char[] alphabet = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
			'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
			'V', 'W', 'X', 'Y', 'Z' };
	/**
	 * The character at which it will cause the next Rotor to step.
	 */
	public char stepChar;
	/**
	 * Defines the Rotor's position in the Enigma machine.
	 */
	public int position;

	public Rotor(char[] cMap, char sChar, int position) {
		charMap = cMap;
		stepChar = sChar;
		curCharMap = charMap;
	}

	/**
	 * Applies the current transformation.
	 * 
	 * @param c
	 *            the character to be transformed
	 * @return the transformed character
	 */
	public char applyTransformation(char c) {
		int pos = mapCharToInt(c);
		if (pos != -1)
			return curCharMap[pos];
		return '-';
	}

	/**
	 * Helper function, used to map a character to its array position in the
	 * alphabet array.
	 * 
	 * @param c
	 *            the character to be found in the alphabet array.
	 * @return the position of the character in the alphabet array.
	 */
	private static int mapCharToInt(char c) {
		int toReturn = -1;

		for (int i = 0; i < alphabet.length; i++)
			if (alphabet[i] == Character.toUpperCase(c))
				toReturn = i;
		return toReturn;
	}

	/**
	 * Helper function, used to increment one individual character, taking into
	 * account rollover.
	 * 
	 * @param c
	 *            - the character to increment
	 * @return the incremented character
	 */
	private char step(char c) {
		int a = mapCharToInt(c);
		if (a == 25)
			return alphabet[0];
		return alphabet[a + 1];
	}

	/**
	 * Causes the Rotor to "step", by advancing each entry in curCharMap by one,
	 * with rollover so Z becomes A.
	 */
	public void step() {
		char[] temp = new char[26];
		for (int i = 0; i < curCharMap.length; i++) {
			temp[i] = step(curCharMap[i]);
		}
		curCharMap = temp;
	}

	/**
	 * Causes the Rotor to reset, to facilitate encrypting multiple messages
	 * with the same key.
	 */
	public void reset() {
		curCharMap = charMap;
	}

	/**
	 * Returns a copy of the curCharMap. Currently only used for testing
	 * purposes.
	 * 
	 * @return a copy of the curCharMap
	 */
	public char[] getCurCharMap() {
		char[] temp = new char[curCharMap.length];
		for (int i = 0; i < temp.length; i++)
			temp[i] = curCharMap[i];
		return temp;
	}

	/**
	 * Returns a copy of the charMap. Currently only used for testing purposes.
	 * 
	 * @return a copy of the charMap
	 */
	public char[] getCharMap() {
		char[] temp = new char[charMap.length];
		for (int i = 0; i < temp.length; i++)
			temp[i] = charMap[i];
		return temp;
	}
}
