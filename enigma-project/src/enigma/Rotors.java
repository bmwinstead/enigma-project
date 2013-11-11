package enigma;

public class Rotors {
	public final static String[] rotorWirings = { 
		 "EKMFLGDQVZNTOWYHXUSPAIBRCJ", //I     1-3 Only
		 "AJDKSIRUXBLHWTMCQGZNPYFVOE", //II    1-3 Only
		 "BDFHJLCPRTXVZNYEIWGAKMUSQO", //III   1-3 Only
		 "ESOVPZJAYQUIRHXLNFTGKDCMWB", //IV    1-3 Only
		 "VZBRGITYUPSDNHLXAWMJQOFECK", //V     1-3 Only
		 "JPGVOUMFYQBENHZRDKASXLICTW", //VI    1-3 Only
		 "NZJHGRCXMYSWBOUFAIVLPEKQDT", //VII   1-3 Only
		 "FKQHTLXOCBJSPDZRAMEWNIUYGV", //VII   1-3 Only
		 "LEYJVCNIXWPBQMDRTAKZGFUHOS", //BETA  4 Only
		 "FSOKANUERHMBTIYCWLQPZXVGJD"  //GAMMA 4 Only
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
		"YRUHQSLDPXNGOKMIEBFZCWVJAT", //B
		"FVPJIAOYEDRZXWGCTKUQSBNMHL", //C
		"ENKQAUYWJICOPBLMDXZVFTHRGS", //B Thin
		"RDOBJNTKVEHMLFCWZAXGYIPSUQ", //C Thin
	};
	
	private Rotor left;
	private Rotor middle;
	private Rotor right;
	private Rotor fourth = null; //possibly not used
	private Rotor reflector;
	
	public Rotors(int[] rotorChoices, int reflectorChoice){
		left = new Rotor(rotorWirings[rotorChoices[0]],rotorNotches[rotorChoices[0]]);
		middle = new Rotor(rotorWirings[rotorChoices[1]],rotorNotches[rotorChoices[1]]);
		right = new Rotor(rotorWirings[rotorChoices[2]],rotorNotches[rotorChoices[2]]);
		if(rotorChoices.length == 4)
			fourth = new Rotor(rotorWirings[rotorChoices[3]],rotorNotches[rotorChoices[3]]);
		reflector = new Rotor(reflectors[reflectorChoice],new char[]{'!','!'});
	}
	
	public void setPositions(char[] choices) {
		left.setStartPosition(choices[0]);
		middle.setStartPosition(choices[1]);
		right.setStartPosition(choices[2]);
		if(choices.length == 4)
			fourth.setStartPosition(choices[3]);
	}
	
	public void setRingSettings(char[] choices) {
		left.setRingPosition(choices[0]);
		middle.setRingPosition(choices[1]);
		right.setRingPosition(choices[2]);
		if(choices.length == 4)
			fourth.setRingPosition(choices[3]);
	}
	
	public char encrypt(char letter){
		boolean doubleStep = false;
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
	}
}
