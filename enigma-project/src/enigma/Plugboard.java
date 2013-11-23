package enigma;

/**
 * 
 * @author Brian Matthew Winstead
 * @author Team Enigma
 * @version 0.9
 * 
 * This class simulates the plugboard in the Enigma encryption machine. The
 * plugboard was a simple two-way replacement cipher that was available
 * on some Enigmas. The substitions were performed both during initial
 * letter input (before processing by rotors and reflectors) and output (after
 * processing). 
 *
 */
public class Plugboard {

	private char[][] plugboard;
	private int numPairs;
	
	/**
	 * Constructor. Accepts a string of characters and creates a simple two-way
	 * replacement cipher. The string should be formatted in pairs with no
	 * spaces or punctuation. For example, initializing with a string of "ABCD"
	 * would swap all 'A's with 'B's, all 'B's with 'A's, all 'C's with 'D's,
	 * and all 'D's with 'C's.
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
