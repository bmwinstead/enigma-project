package enigma;

public class TestEnigma {
	public static void main(String[] args) {
		int[] rotorChoices = {1,2,3,9};
		char[] ringSettings = {'A','B','C','D'};
		char[] initialPositions = {'A','A','A','A'};
		int[] rotorChoices2 = {1,2,3};
		char[] ringSettings2 = {'A','B','C'};
		char[] initialPositions2 = {'A','B','C'};
		String pbMap = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		
		EnigmaMachine em = new EnigmaMachine(rotorChoices,3,ringSettings,initialPositions);
		EnigmaMachine em2 = new EnigmaMachine(rotorChoices2,1,ringSettings2,initialPositions2,pbMap);
		EnigmaMachine em3 = new EnigmaMachine(rotorChoices,3,ringSettings,initialPositions);
		EnigmaMachine em4 = new EnigmaMachine(rotorChoices2,1,ringSettings2,initialPositions2,pbMap);
		
		String testString1 = "HELLO EVERYONE THIS IS A SUBSTANTIAL LENGTH TEST ENCRYPTION JUST TO ENSURE THAT EVERYTHING IS WORKING PROPERLY YOU KNOW POTATOES ARE TASTY SOMETHING SOMETHING "
				+ "BLAH BLAH BLAH BLAH BLAH AAAAAAAAAAAAAAAAAAAAAAAAAAA BBBBBBBBBBBBBBBBBBBBBBBBBBBBBB CCCCCCCCCCCCCCCCCCCCCCCCCCC DDDDDDDDDDDDDDDDDDDDDDDDDDDDD EEEEEEEEEEEEEEEEEEEEEEEEEEEEE";
		System.out.println("testString1: " + testString1);
		String result = em.encryptString(testString1);
		String result2 = em2.encryptString(testString1);
		
		System.out.println("em1 encryption: " + result);
		System.out.println("em2 encryption: " + result2);
		String result3 = em3.encryptString(result);
		String result4 = em4.encryptString(result2);
		System.out.println("em3 decryption: " + result3);
		System.out.println("em4 decryption: " + result4);
		
	}
}
