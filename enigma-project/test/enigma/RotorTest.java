package enigma;

import static org.junit.Assert.*;

import org.junit.Test;

public class RotorTest {

	char[] rotorI = {'E','K','M','F','L','G','D','Q','V','Z','N','T','O','W','Y','H','X','U','S','P','A','I','B','R','C','J'};
	char[] rotorII = {'A','J','D','K','S','I','R','U','X','B','L','H','W','T','M','C','Q','G','Z','N','P','Y','F','V','O','E'};
	char sCharI;
	Rotor r1 = new Rotor(rotorI,sCharI,1);
	Rotor r2 = new Rotor(rotorII,sCharI,1);
	char[] string = {'H','E','L','L','O'};
	char[] newString = new char[string.length];

	@Test
	public void testApplyTransformation() {
		for(int i = 0; i < string.length;i++) {
			newString[i] = r1.applyTransformation(string[i]);
		}
		assertTrue("applyTransformation",String.copyValueOf(newString).equals("QLTTY"));
		
		for(int i = 0; i < string.length;i++) {
			newString[i] = r2.applyTransformation(string[i]);
		}
		assertTrue("applyTransformation",String.copyValueOf(newString).equals("USHHM"));
	}
	@Test
	public void testStep(){
		char[] expected = {'F','L','N','G','M','H','E','R','W','A','O','U','P','X','Z','I','Y','V','T','Q','B','J','C','S','D','K'};
		r1.step();
		assertTrue("step",String.copyValueOf(r1.getCurCharMap()).equals(String.copyValueOf(expected)));
		r1.reset();
		assertTrue("reset",String.copyValueOf(r1.getCurCharMap()).equals(String.copyValueOf(r1.getCharMap())));
	}
}
