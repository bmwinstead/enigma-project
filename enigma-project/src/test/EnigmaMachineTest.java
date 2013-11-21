package test;

import static org.junit.Assert.*;

import org.junit.Test;
import enigma.EnigmaMachine;

/**
 * EnigmaMachine test. Verifies all methods in the EnigmaMachine class work 
 * and behave as expected. 
 * 
 * @author Jessica Ikley
 * @author Team Enigma
 * @version 0.9
 */
public class EnigmaMachineTest {
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
			"aaaaalyn",
			// "aaaaamatthew",
			// "TESTTOCHECKTHREEROTORSTURNATONCE",
			"AAAAAOHLMACHER",
			"AAAAAWINSTEAD",
			"AAAAAGENEG",
			"ROSANA"
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
			"DCTVGJVFUL",
			"KDBMWK"
	};
	
	int[][] nonPBRotorChoices = {
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
			{9, 7, 5, 6},
	};
	
	int[][] pbRotorChoices = {
			{0, 1, 2}, 
	};
	
	// int[] nonPBReflectorChoices = {0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 2, 2, 3};
	int[] nonPBReflectorChoices = {0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 2, 2, 3};
	int[] pbReflectorChoices = {0};
	
	char[][] nonPBRingSettings = {
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
	
	char[][] pbRingSettings = {
			{'A', 'A', 'A'}
	};
	
	char[][] nonPBPositions = {
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
	
	char[][] pbPositions = {
			{'A', 'A', 'A'}
	};
	
	String[] plugboardMaps = {
			"RWOASLNE"
	};
	
	@Test
	public void testEncryptChar() {
		int PBStart = nonPBReflectorChoices.length;
		int fullLength = inputString.length;
		EnigmaMachine[] enigmaMachines = new EnigmaMachine[fullLength];
		
		for (int i = 0; i < PBStart; i++) {
			enigmaMachines[i] = new EnigmaMachine(nonPBRotorChoices[i], 
					nonPBReflectorChoices[i], nonPBRingSettings[i], 
					nonPBPositions[i]);
		}
		
		for (int i = PBStart; i < fullLength; i++) {
			int pbi = i - PBStart;
			enigmaMachines[i] = new EnigmaMachine(pbRotorChoices[pbi],
					pbReflectorChoices[pbi], pbRingSettings[pbi],
					pbPositions[pbi], plugboardMaps[pbi]);
		}
		
		for (int i = 0; i < fullLength; i++) {
			char[] inputArray = inputString[i].toCharArray();
			char[] outArray = new char[inputArray.length];
			
			for (int j = 0; j < inputArray.length; j++) {
				outArray[j] = enigmaMachines[i].encryptChar(inputArray[i]);
			}
			
			String actual = String.valueOf(outArray);
			String loopName = "Loop " + String.valueOf(i);
			
			assertEquals(loopName, expectedResults[i], actual);
		}
	}
	
	@Test
	public void testEncryptString() {
		int PBStart = nonPBReflectorChoices.length;
		int fullLength = inputString.length;
		EnigmaMachine[] enigmaMachines = new EnigmaMachine[fullLength];
		
		for (int i = 0; i < PBStart; i++) {
			enigmaMachines[i] = new EnigmaMachine(nonPBRotorChoices[i], 
					nonPBReflectorChoices[i], nonPBRingSettings[i], 
					nonPBPositions[i]);
		}
		
		for (int i = PBStart; i < fullLength; i++) {
			int pbi = i - PBStart;
			enigmaMachines[i] = new EnigmaMachine(pbRotorChoices[pbi],
					pbReflectorChoices[pbi], pbRingSettings[pbi],
					pbPositions[pbi], plugboardMaps[pbi]);
		}
		
		for (int i = 0; i < fullLength; i++) {
			
			String actual = enigmaMachines[i].encryptString(inputString[i]);
			String loopName = "Loop " + String.valueOf(i);
			
			assertEquals(loopName, expectedResults[i], actual);
		}
	}

}
