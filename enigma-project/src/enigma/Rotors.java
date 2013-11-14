package enigma;

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
	private Rotor fourth;
	private Rotor reflector;

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
	}
	
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
	}
	
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
