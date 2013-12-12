package main.java.enigma;

/**
 * This class simulates the plugboard in the Enigma encryption machine. The
 * plugboard was a simple two-way replacement cipher that was available
 * on some Enigmas. The substitions were performed both during initial
 * letter input (before processing by rotors and reflectors) and output (after
 * processing). 
 * 
 * Plugboards are represented through a string that indicates their
 * replacement mapping. Letters are swapped with their adjacent letters. For 
 * example, a string of "ABCD" swaps A's with B's (and vice-versa) and C's
 * with D's. 
 * 
 * @author Brian Matthew Winstead
 * @author Team Enigma
 * @version 0.9
 * - Nov 22, 2013
 */
public class Plugboard {

	private char[][] plugboard;
	private int numPairs;
	
	/**
	 * Constructor. Accepts a string of characters and creates a simple two-way
	 * replacement cipher. The string should be formatted in pairs with no
	 * spaces or punctuation. 
	 * 
	 * The constructor does not check the capitalization of the input and does
	 * not remove punctuation, spaces, or invalid characters. The constructor
	 * does not prevent double-mapping, although the first match will always be
	 * the one returned.
	 * 
	 * @param mapping
	 *            String to be used to create the two-way substitution. Strings
	 *            are not checked for validity. 
	 * 
	 */
	public Plugboard(String mapping){
		char[] cArr = mapping.toUpperCase().toCharArray();
		numPairs = (int) Math.floor(cArr.length/2);
		plugboard = new char[numPairs][2];
		for(int i = 0; i < (numPairs * 2); i++){
			int j = (int) Math.floor(i/2);
			plugboard[j][i%2]=cArr[i];
		}
		//System.out.println("Plugboard: created using map " + mapping ); - Walter: Commented this line due to interference in my testing.
	}
	
	/**
	 * Constructor. Builds a two-way substitution cipher out of a
	 * two-dimensional array. Characters in the first row will be swapped with
	 * their corresponding characters in the second row, and vice-versa. For
	 * example, the character in newpb[0][0] will get swapped with newpb[0][1],
	 * and vice- versa.
	 * 
	 * @param newpb
	 *            Two-dimensional array of characters representing the plugboard
	 *            configuration. Input is not checked for validity.
	 */
	public Plugboard(char[][] newpb){
		plugboard = newpb;
	}
	
	/**
	 * Returns the current plugboard map to the machine, which then passes it
	 * where needed. 
	 * 
	 * @return String representing the current plugboard map. 
	 */
	public String getPlugboardMap() {
		String map = "";
		for (int i = 0; i < plugboard.length; i++) {
			map += String.valueOf(plugboard[i]);
		}
		return map;
	}
	
	/**
	 * Checks a character to see if it's a swap-character. If so, it returns the
	 * substitution character. If not, it returns the initial character.
	 * 
	 * @param c
	 *            Character to be checked for substitution.
	 * @return The substitution character if there is a match. If not, return is
	 *         the original character.
	 */
	public char matchChar(char c){
		for(int i = 0; i < plugboard.length;i++){
			if(plugboard[i][0] == c)
				return plugboard[i][1];
			if(plugboard[i][1] == c)
				return plugboard[i][0];
		}
		return c;
	}
}
