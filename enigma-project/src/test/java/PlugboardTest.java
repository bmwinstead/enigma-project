package test.java;

import static org.junit.Assert.*;
import main.java.enigma.Plugboard;

import org.junit.Test;

/**
 * Plugboard JUnit test. Verifies all methods in the Plugboard class work and 
 * behave as expected. 
 * 
 * @author Jessica Ikley
 * @author Team Enigma
 * @version 0.9
 * Nov 22, 2013
 */
public class PlugboardTest {

	/**
	 * Verifies operation of the matchChar method. 
	 */
	@Test
	public void testMatchChar(){
		// Strings for testing string-based Plugboard constructor
		String[] pInitStrings = { 
				"ABCDEFGHIJKLMNOPQRSTUVWXYZ", // AB CD EF GH IJ KL MN OP QR ST UV WX YZ 
				"TVEQSJPX",                   // TV EQ SJ PX
				"KALDQUROIZMNVT",             // KA LD QU RO IZ MN VT
				"ADJFLSKEURQPNZCVM",          // AD JF LS KE UR QP NZ CV, ignored M
				"aAbBcCdDeEfFgG",             // aA bB cC dD eE fF gG
				"AAABAC",                     // 'A' not substituted. 'B' and 'C' both substituted with 'A'
				"A,B.C*D ",                   // A, B. C* D
		};
		
		// Arrays for testing array-based Plugboard constructor
		char[][] pInitArray1 = {
				{'G', 'H'},
				{'P', 'D'},
				{'W', 'B'}};
		
		char[][] pInitArray2 = {
				{'G', 'H', 'Z'},
				{'P', 'D', 'Q'},
				{'W', 'B', 'T'}};
		
		// Input for all test cases
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		
		// Expected output of the different Plugboards when given the same
		// input. 
		String[] expectedResult = {
				"BADCFEHGJILKNMPORQTSVUXWZY",
				"ABCDQFGHISKLMNOXERJVUTWPYZ",
				"KBCLEFGHZJADNMRPUOSVQTWXYI",
				"DBVAKJGHIFESMZOQPULTRCWXYN",
				"ABCDEFGHIJKLMNOPQRSTUVWXYZ",
				"AAADEFGHIJKLMNOPQRSTUVWXYZ",
				",.* EFGHIJKLMNOPQRSTUVWXYZ",
				"AWCPEFHGIJKLMNODQRSTUVBXYZ",
				"AWCPEFHGIJKLMNODQRSTUVBXYZ"
		};
		
		Plugboard[] plugboard = new Plugboard[expectedResult.length];
		
		// Create first seven plugboards using the Strings
		for (int i=0; i < 7; i++) {
			plugboard[i] = new Plugboard(pInitStrings[i]);
		}
		
		// Ensure a Plugboard can be made with a char-array
		plugboard[7] = new Plugboard(pInitArray1);
		plugboard[8] = new Plugboard(pInitArray2);
		
		// matchChar takes character input
		char[] alphaArray = alphabet.toCharArray();
		
		// Loop through all test cases. 
		for (int i = 0; i < expectedResult.length; i++) {
			char[] outArray = new char[alphaArray.length];
			
			// Process array, squish back into a string to compare.
			for (int j = 0; j < alphaArray.length; j++) {
				outArray[j] = plugboard[i].matchChar(alphaArray[j]);
			}
			String actualResult = String.valueOf(outArray);
			String plugName = "Plug Results " + String.valueOf(i);
			
			assertEquals(plugName, expectedResult[i], actualResult);			
		} // end testcase loop
	} // end testMatchChar method
} // end Plugboard Test class. 
