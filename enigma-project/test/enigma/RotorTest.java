// This is a comment by J.Ikley, to prove that I can commit.

package enigma;

import static org.junit.Assert.*;

import org.junit.Test;

public class RotorTest {

	char[] rotorI = {'E','K','M','F','L','G','D','Q','V','Z','N','T','O','W','Y','H','X','U','S','P','A','I','B','R','C','J'};
	char sCharI;
	Rotor r = new Rotor(rotorI,sCharI,1);
	char[] string = {'H','E','L','L','O'};
	char[] newString = new char[string.length];

	@Test
	public void testApplyTransformation() {
		for(int i = 0; i < string.length;i++)
			newString[i] = r.applyTransformation(string[i]);
		assertTrue("applyTransformation",String.copyValueOf(newString).equals("QLTTY"));
	}
	@Test
	public void testStep(){
		char[] expected = {'F','L','N','G','M','H','E','R','W','A','O','U','P','X','Z','I','Y','V','T','Q','B','J','C','S','D','K'};
		r.step();
		assertTrue("step",String.copyValueOf(r.getCurCharMap()).equals(String.copyValueOf(expected)));
		r.reset();
		assertTrue("reset",String.copyValueOf(r.getCurCharMap()).equals(String.copyValueOf(r.getCharMap())));
	}
}
