package enigma;

public class EnigmaMachine {

	private Rotors rotors;
	private Plugboard plugboard;
	//No plugboard
	public EnigmaMachine(int[] rotorChoices, int reflectorChoice, char[] ringSettings, char[] initialPositions){
		rotors = new Rotors(rotorChoices, reflectorChoice);
		rotors.setRingSettings(ringSettings);
		rotors.setPositions(initialPositions);
		plugboard = null;
	}
	
	//Plugboard
	public EnigmaMachine(int[] rotorChoices, int reflectorChoice, char[] ringSettings, char[] initialPositions, String plugboardMap){
		rotors = new Rotors(rotorChoices, reflectorChoice);
		rotors.setRingSettings(ringSettings);
		rotors.setPositions(initialPositions);
		plugboard = new Plugboard(plugboardMap);
	}
	
	//Used for real-time encryption (with lightboard, if we do that).
	public char encryptChar(char c){
		char a = c;
		if(plugboard != null){
			a = plugboard.matchChar(c);
		}
		a = rotors.encrypt(a);
		if(plugboard != null){
			a = plugboard.matchChar(a);
		}
		return a;
	}
	
	//For 'process' button/text file upload.
	public String encryptString(String s){
		char[] cArr = s.toCharArray();
		String rStr = "";
		for(char c : cArr){
			rStr += encryptChar(c);
		}
		return rStr;
	}
}
