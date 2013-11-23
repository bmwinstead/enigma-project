package enigma;

/**
 * 
 * @author Bryan Matthew Winstead
 * @author Team Enigma
 * @version 0.9
 * 
 * Contains 3-4 instances of the Rotor class. Also acts as the reflector in an
 * Enigma machine. Fully encrypts a character (except for Plugboard
 * substititions) and handles the proper cycling of the individual Rotor
 * instances. 
 *
 */
public class Rotors {
	public final static String[] rotorWirings = { 
		 "EKMFLGDQVZNTOWYHXUSPAIBRCJ", //I     1-3 Only 0
		 "AJDKSIRUXBLHWTMCQGZNPYFVOE", //II    1-3 Only 1
		 "BDFHJLCPRTXVZNYEIWGAKMUSQO", //III   1-3 Only 2
		 "ESOVPZJAYQUIRHXLNFTGKDCMWB", //IV    1-3 Only 3
		 "VZBRGITYUPSDNHLXAWMJQOFECK", //V     1-3 Only 4
		 "JPGVOUMFYQBENHZRDKASXLICTW", //VI    1-3 Only 5
		 "NZJHGRCXMYSWBOUFAIVLPEKQDT", //VII   1-3 Only 6
		 "FKQHTLXOCBJSPDZRAMEWNIUYGV", //VIII  1-3 Only 7
		 "LEYJVCNIXWPBQMDRTAKZGFUHOS", //BETA  4 Only   8
		 "FSOKANUERHMBTIYCWLQPZXVGJD"  //GAMMA 4 Only   9
	}; 
	
	public final static char[][] rotorNotches = {
		 {'R','!'}, //I
		 {'F','!'}, //II
		 {'W','!'}, //III
		 {'K','!'}, //IV
		 {'A','!'}, //V
		 {'A','N'}, //VI
		 {'A','N'}, //VII
		 {'A','N'}, //VIII
		 {'!','!'}, //BETA, no step
		 {'!','!'}  //GAMMA, no step
	};
	
	public final static String[] reflectors = {
		"YRUHQSLDPXNGOKMIEBFZCWVJAT", //B      0
		"FVPJIAOYEDRZXWGCTKUQSBNMHL", //C      1
		"ENKQAUYWJICOPBLMDXZVFTHRGS", //B Thin 2
		"RDOBJNTKVEHMLFCWZAXGYIPSUQ", //C Thin 3
	};
	
	private Rotor left;
	private Rotor middle;
	private Rotor right;
	private Rotor fourth;  // Used only by the Navy
	private Rotor reflector;

	/**
	 * Constructor. Accepts an array representing 3-4 Engima rotor choices, plus
	 * an integer indicating the reflector. Ring settings and initial position
	 * have to be set separately. The Reflector itself is actually a Rotor that
	 * is never rotated. 
	 * 
	 * @param rotorChoices
	 *            An array of 3-4 integers numbered 0-9 corresponding to the
	 *            different rotors in use by the German military
	 * @param reflectorChoice
	 *            An integer indicating the reflector to be used. Valid options
	 *            are 0-3.
	 * 
	 */
	public Rotors(int[] rotorChoices, int reflectorChoice){
		if(rotorChoices.length == 3){
		left = new Rotor(rotorWirings[rotorChoices[0]],rotorNotches[rotorChoices[0]]);
		middle = new Rotor(rotorWirings[rotorChoices[1]],rotorNotches[rotorChoices[1]]);
		right = new Rotor(rotorWirings[rotorChoices[2]],rotorNotches[rotorChoices[2]]);
		}else{
			fourth = new Rotor(rotorWirings[rotorChoices[0]], rotorNotches[rotorChoices[0]]);
			left = new Rotor(rotorWirings[rotorChoices[1]],rotorNotches[rotorChoices[1]]);
			middle = new Rotor(rotorWirings[rotorChoices[2]],rotorNotches[rotorChoices[2]]);
			right = new Rotor(rotorWirings[rotorChoices[3]],rotorNotches[rotorChoices[3]]);
		}
		reflector = new Rotor(reflectors[reflectorChoice],new char[]{'!','!'});
	} // end Constructor
	
	/**
	 * Sets the initial starting positions of the rotors.
	 * 
	 * @param choices
	 *            Array of 3-4 characters indicating the starting positions of
	 *            the rotors. This corresponds to the letters indicated on the
	 *            top of the Enigma Machine before the operator begins encrypt
	 *            or decrypt his message.
	 */
	public void setPositions(char[] choices) {
		if(choices.length == 3){
		left.setStartPosition(choices[0]);
		middle.setStartPosition(choices[1]);
		right.setStartPosition(choices[2]);
		} else{
			fourth.setStartPosition(choices[0]);
			left.setStartPosition(choices[1]);
			middle.setStartPosition(choices[2]);
			right.setStartPosition(choices[3]);
		}
	} // end setPositions method
	
	/**
	 * Sets the initial ring settings of the rotors. This rotates the
	 * substitution mapping of the letters.
	 * 
	 * @param choices
	 *            An array of 3-4 letters indicating the ring settings. On the
	 *            Enigma Machine, these would be adjusted by aligining the
	 *            indicated letter with a mark on the rotor before the rotor is
	 *            insalled in the Enigma.
	 */
	public void setRingSettings(char[] choices) {
		if(choices.length == 3){
		left.setRingPosition(choices[0]);
		middle.setRingPosition(choices[1]);
		right.setRingPosition(choices[2]);
		} else{
			fourth.setRingPosition(choices[0]);
			left.setRingPosition(choices[1]);
			middle.setRingPosition(choices[2]);
			right.setRingPosition(choices[3]);
		}
	} // end setRingSettings method
	
	/**
	 * Encrypts an individual character. A character is first forwardEncrypted
	 * through each rotor in the Enigma, then sent through the reflector (a
	 * one-way rotor that does not cycle). Then the character is
	 * reverseEncrypted through each rotor before the final character is
	 * returned.
	 * 
	 * @param letter
	 *            Character to be encrypted.
	 * @return Final character after processing.
	 */
	public char encrypt(char letter){
		// Walter Adolph - doubleStep was init. to false; this assumed that there is no chance of a double step on initial position. Fixed 11/21/2013.
		boolean notch1Test = middle.getPosition() == middle.getNotchPosition()[0];
		boolean notch2Test = middle.getPosition() == middle.getNotchPosition()[1];
		boolean doubleStep = (notch1Test || notch2Test);
		
		if (Character.isAlphabetic(letter)) {
			if (right.cycleRotor() || doubleStep) {
				if (middle.cycleRotor()) {
					if (doubleStep) {
						left.cycleRotor();
						doubleStep = false;
					}
				} else if (middle.getPosition() == middle.getNotchPosition()[0] || middle.getPosition() == middle.getNotchPosition()[1]) {
					doubleStep = true;
				}
			}
			
			letter = right.forwardEncrypt(letter);
			letter = middle.forwardEncrypt(letter);
			letter = left.forwardEncrypt(letter);
			if(fourth != null)
				letter = fourth.forwardEncrypt(letter);
			letter = reflector.forwardEncrypt(letter);
			if(fourth != null)
				letter = fourth.reverseEncrypt(letter);
			letter = left.reverseEncrypt(letter);
			letter = middle.reverseEncrypt(letter);
			letter = right.reverseEncrypt(letter);
		}
		return letter;
	} // end encrypt method
} // end Rotors class
