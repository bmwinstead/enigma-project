package test;

import static org.junit.Assert.*;
import org.junit.Test;
import enigma.Rotor;

/**
 * Rotor test. Verifies all methods in the Rotor class work and behave
 * as expected. 
 * 
 * @author Jessica Ikley
 * @author Team Enigma
 * @version 0.9
 */
public class RotorTest {
	
	private final char[][] rotorNotches = {
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
	
	private final String[] rotorWirings = { 
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
	
	@Test
	public void testGetNotchPosition() {

		String[] expected = { "Q ", "E ", "V ", "J ", "@ ", "@M", "@M", "@M",
				"  ", "  " };

		Rotor[] rotors = new Rotor[rotorNotches.length];

		for (int i = 0; i < rotors.length; i++) {
			rotors[i] = new Rotor("ABCDEFGHIJKLMNOPQRSTUVWXYZ", rotorNotches[i]);
		}

		for (int i = 0; i < rotors.length; i++) {
			String loopName = "getNotch Results " + String.valueOf(i);
			String actual = String.valueOf(rotors[i].getNotchPosition());

			assertEquals(loopName, expected[i], actual);
		}
	} // end testGetNotchPosition
	
	@Test
	public void testGetPosition(){
		Rotor[] rotors = new Rotor[rotorNotches.length];
		
		for (int i = 0; i < rotors.length; i++) {
			rotors[i] = new Rotor(rotorWirings[i], rotorNotches[i]);
			rotors[i].setRingPosition('A');
			rotors[i].setStartPosition('A');
		}
		
		for (int i = 0; i < rotors.length; i++) {
			for (int j = 0; j < 29; j++) {
				assertTrue((char) ('A' + (j % 26)) == 
						rotors[i].getPosition());
				rotors[i].cycleRotor();
				
			} // end letter-cycling check
		} // end all-rotors check
	} // end testGetPosition

	@Test
	public void testCycleRotor() {
		Rotor rotor = new Rotor(rotorWirings[0], rotorNotches[0]);
		rotor.setRingPosition('A');
		rotor.setStartPosition('A');
		
		for (int i = 1; i <= 26; i++) {
			if (rotor.getPosition() == 'Q') {
				assertTrue(rotor.cycleRotor());
			}
			else {
				assertFalse(rotor.cycleRotor());
			}
		}
	} // end testCycleRotor
	
	@Test
	public void testForwardEncrypt() {
		char[] toEncrypt = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
				'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
				'W', 'X', 'Y', 'Z' };

		char[] expected1 = { 'E', 'K', 'M', 'F', 'L', 'G', 'D', 'Q', 'V', 'Z',
				'N', 'T', 'O', 'W', 'Y', 'H', 'X', 'U', 'S', 'P', 'A', 'I',
				'B', 'R', 'C', 'J' };

		char[] expected2 = { 'J', 'L', 'E', 'K', 'F', 'C', 'P', 'U', 'Y', 'M',
				'S', 'N', 'V', 'X', 'G', 'W', 'T', 'R', 'O', 'Z', 'H', 'A',
				'Q', 'B', 'I', 'D' };
		
		Rotor rotor = new Rotor(rotorWirings[0], rotorNotches[0]);
		rotor.setRingPosition('A');
		rotor.setStartPosition('A');
		
		for (int i = 0; i < toEncrypt.length; i++) {
			assertTrue(rotor.forwardEncrypt(toEncrypt[i]) == expected1[i]);
		}
		
		rotor.cycleRotor();
		
		for (int i = 0; i < toEncrypt.length; i++) {
			assertTrue(rotor.forwardEncrypt(toEncrypt[i]) == expected2[i]);
		}
	}  // end testForwardEncrypt
	
	@Test
	public void testReverseEncrypt() {
		char[] encrypt1 = { 'E', 'K', 'M', 'F', 'L', 'G', 'D', 'Q', 'V', 'Z',
				'N', 'T', 'O', 'W', 'Y', 'H', 'X', 'U', 'S', 'P', 'A', 'I',
				'B', 'R', 'C', 'J' };
		
		char[] encrypt2 = { 'J', 'L', 'E', 'K', 'F', 'C', 'P', 'U', 'Y', 'M',
				'S', 'N', 'V', 'X', 'G', 'W', 'T', 'R', 'O', 'Z', 'H', 'A',
				'Q', 'B', 'I', 'D' };
		
		char[] expected = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
				'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
				'W', 'X', 'Y', 'Z' };
		
		Rotor rotor = new Rotor(rotorWirings[0], rotorNotches[0]);
		rotor.setRingPosition('A');
		rotor.setStartPosition('A');
		
		for (int i = 0; i < encrypt1.length; i++) {
			assertTrue(rotor.reverseEncrypt(encrypt1[i]) == expected[i]);
		}
		
		rotor.cycleRotor();
		
		for (int i = 0; i < encrypt2.length; i++) {
			assertTrue(rotor.reverseEncrypt(encrypt2[i]) == expected[i]);
		}
	}
}
