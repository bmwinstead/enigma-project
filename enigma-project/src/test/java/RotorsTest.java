package test.java;

import static org.junit.Assert.*;
import main.java.enigma.Rotors;

import org.junit.Test;

/**
 * Rotors test. Verifies all methods in the Rotors class work and behave
 * as expected. 
 * 
 * @author Jessica Ikley
 * @author Team Enigma
 * @version 0.9
 * @date - Nov 22, 2013
 */

public class RotorsTest {
	
	@Test
	public void testEncrypt() {
		// Test cases include most final system test cases that do
		// not involve a plugboard. The expected results were created
		// using either hand-encryption with a spreadsheet or Enigma
		// simulators available online, and in all cases the output was 
		// verified against multiple existent Enigma Simulators, due
		// to the unlikelihood that multiple simulators "get it wrong" in the
		// exact same way. The arrays are parallel and are intended to make
		// adding new test cases easier. 
		String[] inputString = { 
				"AAAAA",
				"AAAAA",
				"ELLEN",
				"AAAAA",
				"AAAAAJESSY",
				"AAAAAWALTER",
				"AAAAAADOLPH",
				"AAAAAIKLEY",
				"AAAAAMONTANEZ",
				"AAAAALYN",
				"AAAAAMATTHEW",
				"TESTTOCHECKTHREEROTORSTURNATONCE",
				"AAAAAOHLMACHER",
				"AAAAAWINSTEAD",
				"AAAAAGENEG"
		};
		
		String[] expectedResults = {
				"BDZGO",
				"EWTYX",
				"VONDB",
				"DZGOW",
				"LFLCCOXZIS",
				"OGUYVEVJWXK",
				"PJBUZQKFHOD",
				"GJUBBPFPRS",
				"MWMJLCFGQEEQF",
				"UQOFXMBD",
				"FJBWZCCJFNOS",
				"LLXIXPACYICPKVSLPURSFBSKPPJKCZBJ",
				"BDZGOKTGOTWOLM",
				"VFITYYAYYWCZO",
				"DCTVGJVFUL"
		};
		
		int[][] rotorChoices = {
				{0, 1, 2},
				{0, 1, 2},
				{2, 1, 0},
				{0, 1, 2},
				{0, 2, 4},
				{3, 2, 4},
				{0, 1, 2},
				{5, 6, 7},
				{5, 6, 7},
				{0, 1, 2},
				{0, 1, 2},
				{0, 1, 2},
				{8, 0, 1, 2},
				{9, 7, 6, 5},
				{9, 7, 5, 6}
		};
		
		// Walter Adolph - These were misaligned; the top init. was missing a zero. Fixed 11/21/2013.
		// JLI - Now aligned vertically to make commenting out a single case easier
		int[] reflectorChoices = {
				0, 
				0, 
				0, 
				0, 
				0, 
				0, 
				1, 
				0, 
				1, 
				0, 
				0, 
				0, 
				2, 
				2, 
				3
		};
		
		char[][] positions = {
				{'A', 'A', 'A'},
				{'A', 'A', 'A'},
				{'A', 'A', 'A'},
				{'A', 'A', 'B'},
				{'A', 'A', 'A'},
				{'A', 'A', 'A'},
				{'A', 'A', 'A'},
				{'A', 'A', 'A'},
				{'A', 'A', 'A'},
				{'A', 'A', 'V'},
				{'A', 'E', 'A'},
				{'A', 'E', 'A'},
				{'A', 'A', 'A', 'A'},
				{'A', 'A', 'A', 'A'},
				{'A', 'A', 'A', 'A'},				
		};
		
		char[][] ringSettings = {
				{'A', 'A', 'A'},
				{'B', 'B', 'B'},
				{'A', 'A', 'A'},
				{'A', 'A', 'A'},
				{'A', 'A', 'A'},
				{'A', 'A', 'A'},
				{'A', 'A', 'A'},
				{'A', 'A', 'A'},
				{'A', 'A', 'A'},
				{'A', 'A', 'A'},
				{'A', 'A', 'A'},
				{'A', 'A', 'A'},
				{'A', 'A', 'A', 'A'},
				{'A', 'A', 'A', 'A'},
				{'A', 'A', 'A', 'A'},
		};
		
		Rotors[] rotors = new Rotors[reflectorChoices.length];
		
		// Create all Rotors instances.
		for (int i = 0; i < rotors.length; i++) {
			rotors[i] = new Rotors(rotorChoices[i], reflectorChoices[i]);
			rotors[i].setPositions(positions[i]);
			rotors[i].setRingSettings(ringSettings[i]);
		}
		
		// Cycle through all test cases. Turn each input string into a
		// character array to feed it to its corresponding Rotors. Turn the
		// results back into a String to compare it with expected results. 
		for (int i = 0; i < rotors.length; i++) {
			char[] inputArray = inputString[i].toCharArray();
			char[] outArray = new char[inputArray.length];
			
			for (int j = 0; j < inputArray.length; j++) {
				outArray[j] = rotors[i].encrypt(inputArray[j]);
			}
			
			String actual = String.valueOf(outArray);
			String loopName = "Loop " + String.valueOf(i);
			
			assertEquals(loopName, expectedResults[i], actual);
		} // end loop-check
		
	} // end testEncrypt method

} // end RotorsTest class
