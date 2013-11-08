package machine;

public class Encryptor {
	private CA_Rotor left;
	private CA_Rotor middle;
	private CA_Rotor right;
	private CA_Rotor reflector;
	
	public Encryptor(CA_Rotor left, CA_Rotor middle, CA_Rotor right, CA_Rotor reflector) {
		this.left = left;
		this.middle = middle;
		this.right = right;
		this.reflector = reflector;
	}
	
	public void setPositions(char left, char middle, char right) {
		this.left.setStartPosition(left);
		this.middle.setStartPosition(middle);
		this.right.setStartPosition(right);
	}
	
	public String encrypt(String text) {
		String result = "";
		boolean doubleStep = false;
		
		left.setRingPosition('B');
		middle.setRingPosition('B');
		right.setRingPosition('B');
		
		for (int index = 0; index < text.length(); index++) {
			char letter = text.charAt(index);
			
			if (Character.isAlphabetic(letter)) {
				if (right.cycleRotor() || doubleStep) {
					if (middle.cycleRotor()) {
						if (doubleStep) {
							left.cycleRotor();
							doubleStep = false;
						}
					} else if (middle.getPosition() == middle.getNotchPosition()) {
						doubleStep = true;
					}
				}
				
				letter = right.forwardEncrypt(text.charAt(index));
				letter = middle.forwardEncrypt(letter);
				letter = left.forwardEncrypt(letter);
				letter = reflector.forwardEncrypt(letter);
				letter = left.reverseEncrypt(letter);
				letter = middle.reverseEncrypt(letter);
				letter = right.reverseEncrypt(letter);
			}
				
			result = result + letter;
		}
		
		return result;
	}
}
