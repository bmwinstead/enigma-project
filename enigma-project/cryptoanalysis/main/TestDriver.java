package main;

import machine.CA_Rotor;
import views.TestPanel;

public class TestDriver {

	public static void main(String[] args) {
		/*
		CA_Rotor rotor1 = new CA_Rotor("EKMFLGDQVZNTOWYHXUSPAIBRCJ", 'R', 'A');
		CA_Rotor rotor2 = new CA_Rotor("AJDKSIRUXBLHWTMCQGZNPYFVOE", 'F', 'A');
		CA_Rotor rotor3 = new CA_Rotor("BDFHJLCPRTXVZNYEIWGAKMUSQO", 'W', 'A');
		CA_Rotor reflector1 = new CA_Rotor("YRUHQSLDPXNGOKMIEBFZCWVJAT");
		
		String text = "The quick brown fox jumped over the lazy dogs.";
		String cipher = "";
		
		for (int index = 0; index < text.length(); index++) {
			char letter = text.charAt(index);
			
			if (Character.isAlphabetic(letter)) {
				if (rotor3.cycleRotor())
					if (rotor2.cycleRotor())
						rotor1.cycleRotor();
				
				letter = rotor3.forwardEncrypt(text.charAt(index));
				letter = rotor2.forwardEncrypt(letter);
				letter = rotor1.forwardEncrypt(letter);
				letter = reflector1.forwardEncrypt(letter);
				letter = rotor1.reverseEncrypt(letter);
				letter = rotor2.reverseEncrypt(letter);
				letter = rotor3.reverseEncrypt(letter);
			}
				
			cipher = cipher + letter;
		}
		
		System.out.println("Encrypted: " + cipher);
		String plaintext = "";
		rotor1.setPosition('A');
		rotor2.setPosition('A');
		rotor3.setPosition('A');
		
		for (int index = 0; index < text.length(); index++) {
			char letter = cipher.charAt(index);
			
			if (Character.isAlphabetic(letter)) {
				if (rotor3.cycleRotor())
					if (rotor2.cycleRotor())
						rotor1.cycleRotor();
				
				letter = rotor3.forwardEncrypt(cipher.charAt(index));
				letter = rotor2.forwardEncrypt(letter);
				letter = rotor1.forwardEncrypt(letter);
				letter = reflector1.forwardEncrypt(letter);
				letter = rotor1.reverseEncrypt(letter);
				letter = rotor2.reverseEncrypt(letter);
				letter = rotor3.reverseEncrypt(letter);
			}
			
			plaintext = plaintext + letter;
		}
		
		System.out.println("Decrypted: " + plaintext);
		*/
		TestPanel frame = new TestPanel();
		
		frame.setVisible(true);
	}

}
