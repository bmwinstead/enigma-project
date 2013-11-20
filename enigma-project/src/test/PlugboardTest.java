package test;

import static org.junit.Assert.*;
import org.junit.Test;
import enigma.Plugboard;

/**
 * Plugboard test. Verifies all methods in the Plugboard class work and behave
 * as expected. 
 * 
 * @author Jessica Ikley
 * @author Team Enigma
 * @version 0.9
 */
public class PlugboardTest {

	@Test
	public void testMatchChar(){
		String[] pInitStrings = { 
				"ABCDEFGHIJKLMNOPQRSTUVWXYZ", // AB CD EF GH IJ KL MN OP QR ST UV WX YZ 
				"TVEQSJPX",                   // TV EQ SJ PX
				"KALDQUROIZMNVT",             // KA LD QU RO IZ MN VT
				"ADJFLSKEURQPNZCVM",          // AD JF LS KE UR QP NZ CV, ignored M
				"aAbBcCdDeEfFgG",             // aA bB cC dD eE fF gG
				"AAABAC",                     // 'A' not substituted. 'B' and 'C' both substituted with 'A'
				"A,B.C*D ",                   // A, B. C* D
		};
		
		char[][] pInitArray1 = {
				{'G', 'H'},
				{'P', 'D'},
				{'W', 'B'}};
		
		char[][] pInitArray2 = {
				{'G', 'H', 'Z'},
				{'P', 'D', 'Q'},
				{'W', 'B', 'T'}};
		
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		
		String[] expectedResult = {
				"BADCFEHGJILKNMPORQTSVUXWZY",
				"ABCDQFGHISKLMNOXERJVUTWPYZ",
				"KBCLEFGHZJADNMRPUOSVQTWXYI",
				"DBVAKJGHIFESMZOQPULTRCWXYN",
				"abcdefgHIJKLMNOPQRSTUVWXYZ",
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
		
		// Plugboard works on a character basis
		char[] alphaArray = alphabet.toCharArray();
		
		for (int i = 0; i < expectedResult.length; i++) {
			char[] outArray = new char[alphaArray.length];
			
			// Process array, squish back into a string to compare.
			for (int j = 0; j < alphaArray.length; j++) {
				outArray[j] = plugboard[i].matchChar(alphaArray[j]);
			}
			String actualResult = String.valueOf(outArray);
			String plugName = "Plug Results " + String.valueOf(i);
			
			assertEquals(plugName, expectedResult[i], actualResult);			
		}
	}

}
