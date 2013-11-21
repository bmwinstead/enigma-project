package test;

import static org.junit.Assert.*;
import org.junit.Test;
import enigma.Rotors;

/**
 * Rotors test. Verifies all methods in the Rotors class work and behave
 * as expected. 
 * 
 * @author Jessica Ikley
 * @author Team Enigma
 * @version 0.9
 */

public class RotorsTest {
	
	@Test
	public void testEncrypt() {
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
				// "AAAAAMATTHEW",
				// "TESTTOCHECKTHREEROTORSTURNATONCE",
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
				// "FJBWZCCJFNOS",
				// "LLXIXPACYICPKVSLPURSFBSKPPJKCZBJ",
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
				// {0, 1, 2},
				// {0, 1, 2},
				{8, 0, 1, 2},
				{9, 7, 6, 5},
				{9, 7, 5, 6}
		};
		
		// int[] reflectorChoices = {0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 2, 2, 3};
		int[] reflectorChoices = {0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 2, 2, 3};
		
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
				// {'A', 'E', 'A'},
				// {'A', 'E', 'A'},
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
				// {'A', 'A', 'A'},
				// {'A', 'A', 'A'},
				{'A', 'A', 'A', 'A'},
				{'A', 'A', 'A', 'A'},
				{'A', 'A', 'A', 'A'},
		};
		
		Rotors[] rotors = new Rotors[reflectorChoices.length];
		
		for (int i = 0; i < rotors.length; i++) {
			rotors[i] = new Rotors(rotorChoices[i], reflectorChoices[i]);
			rotors[i].setPositions(positions[i]);
			rotors[i].setRingSettings(ringSettings[i]);
		}
		
		for (int i = 0; i < rotors.length; i++) {
			char[] inputArray = inputString[i].toCharArray();
			char[] outArray = new char[inputArray.length];
			
			for (int j = 0; j < inputArray.length; j++) {
				outArray[j] = rotors[i].encrypt(inputArray[j]);
			}
			
			String actual = String.valueOf(outArray);
			String loopName = "Loop " + String.valueOf(i);
			
			assertEquals(loopName, expectedResults[i], actual);
		}
		
	}

}
