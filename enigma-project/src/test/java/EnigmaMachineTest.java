package test.java;

import static org.junit.Assert.*;
import main.java.enigma.EnigmaMachine;

import org.junit.Test;

/**
 * EnigmaMachine test. Verifies all methods in the EnigmaMachine class work 
 * and behave as expected. 
 * 
 * @author Jessica Ikley
 * @author Team Enigma
 * @version 0.9
 * Nov 22, 2013
 */
public class EnigmaMachineTest {
	// Test cases include most final system test cases. The expected results
	// were created using either hand-encryption with a spreadsheet or Enigma
	// simulators available online, and in all cases the output was
	// verified against multiple existent Enigma Simulators, due
	// to the unlikelihood that multiple simulators "get it wrong" in the
	// exact same way. The arrays are parallel and are intended to make
	// adding new test cases easier. Plugboard related input strings and
	// expected results should be included at the end of the arrays. 
	String[] inputString = { 
			"INTELLIGENCEPOINTSTOATTACKONTHEEASTWALLOFTHECASTLEATDAWN",
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
			"aaaaamatthew",
			"TESTTOCHECKTHREEROTORSTURNATONCE",
			"AAAAAOHLMACHER",
			"AAAAAWINSTEAD",
			"AAAAAGENEG",
			"ROSANA"
	};
	
	String[] expectedResults = {
			"NPNKANVHWKPXORCDDTRJRXSJFLCIUAIIBUNQIUQFTHLOZOIMENDNGPCB",
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
			"DCTVGJVFUL",
			"KDBMWK"
	};
	
	// Information for building the EnigmaMachine instances. Plugboard
	// and non-Plugboard cases are held in different arrays. 
	int[][] nonPBRotorChoices = {
			{1, 4, 3},
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
			{9, 7, 5, 6},
	};
	
	int[][] pbRotorChoices = {
			{0, 1, 2}, 
	};
	
	// Reflector array stored vertically to make commenting out individual
	// cases easier. 
	int[] nonPBReflectorChoices = {
			0, 
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
	int[] pbReflectorChoices = {
			0
	};
	
	char[][] nonPBRingSettings = {
			{'P', 'M', 'P'},
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
	
	char[][] pbRingSettings = {
			{'A', 'A', 'A'}
	};
	
	char[][] nonPBPositions = {
			{'S', 'I', 'G'},
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
	
	char[][] pbPositions = {
			{'A', 'A', 'A'}
	};
	
	String[] plugboardMaps = {
			"RWOASLNE"
	};
	
	/**
	 * Validates the encryptChar method. 
	 */
	@Test
	public void testEncryptChar() {
		int PBStart = nonPBReflectorChoices.length;
		int fullLength = inputString.length;
		EnigmaMachine[] enigmaMachines = new EnigmaMachine[fullLength];
		
		// Make all the non-plugboard machines
		for (int i = 0; i < PBStart; i++) {
			enigmaMachines[i] = new EnigmaMachine(nonPBRotorChoices[i], 
					nonPBReflectorChoices[i], nonPBRingSettings[i], 
					nonPBPositions[i]);
		}
		
		// Make the plugboard machines. 
		for (int i = PBStart; i < fullLength; i++) {
			int pbi = i - PBStart;
			enigmaMachines[i] = new EnigmaMachine(pbRotorChoices[pbi],
					pbReflectorChoices[pbi], pbRingSettings[pbi],
					pbPositions[pbi], plugboardMaps[pbi]);
		}
		
		// Test all the machines, character-by-character. 
		for (int i = 0; i < fullLength; i++) {
			char[] inputArray = inputString[i].toCharArray();
			char[] outArray = new char[inputArray.length];
			
			for (int j = 0; j < inputArray.length; j++) {
				outArray[j] = enigmaMachines[i].encryptChar(inputArray[j]);
			}
			
			String actual = String.valueOf(outArray);
			String loopName = "Loop " + String.valueOf(i);
			
			assertEquals(loopName, expectedResults[i], actual);
		}
	} // end testEncryptChar
	
	/**
	 * Validates the encryptString method
	 */
	@Test
	public void testEncryptString() {
		int PBStart = nonPBReflectorChoices.length;
		int fullLength = inputString.length;
		EnigmaMachine[] enigmaMachines = new EnigmaMachine[fullLength];
		
		// Make all the non-plugboard machines. 
		for (int i = 0; i < PBStart; i++) {
			enigmaMachines[i] = new EnigmaMachine(nonPBRotorChoices[i], 
					nonPBReflectorChoices[i], nonPBRingSettings[i], 
					nonPBPositions[i]);
		}
		
		// Make all the plugboard machines.
		for (int i = PBStart; i < fullLength; i++) {
			int pbi = i - PBStart;
			enigmaMachines[i] = new EnigmaMachine(pbRotorChoices[pbi],
					pbReflectorChoices[pbi], pbRingSettings[pbi],
					pbPositions[pbi], plugboardMaps[pbi]);
		}
		
		// Test all the machines.
		for (int i = 0; i < fullLength; i++) {
			
			String actual = enigmaMachines[i].encryptString(inputString[i]);
			String loopName = "Loop " + String.valueOf(i);
			
			assertEquals(loopName, expectedResults[i], actual);
		}
	} // end testEncryptChar method

} // end EnigmaMachineTest class
