package main.java.GUINew;

/**
 * 
 * @author Team Enigma
 * @version 0.8
 * @date - 30 Nov 2013
 * 
 * Class to format the input for processing. Automatically replaces
 * numbers with their "code" letters, removes all other non-letter
 * characters, and converts everything to upper case. 
 *
 */
public class ConfigureOutput {
	
	public ConfigureOutput() {
	}
	
	/**
	 * Accepts a string and converts it to the proper format for encryption.
	 * Converts all digits to their corresponding letters and strips out all
	 * other non-letter characters. Converts all letters to uppercase.
	 * 
	 * @param getString
	 *            Input string to be pre-processed for encryption
	 * @return Properly processed input string.
	 */
	public String configure(String getString){
    	String setString = "";
    	char[] swapChar = getString.toCharArray();
    	if (!getString.isEmpty()){
    		for(int i = 0; i < getString.length(); i++){
    			if (Character.isLetter(swapChar[i])){
    				setString += swapChar[i];
    			}
    			else if(Character.isDigit(swapChar[i])){
    				if(swapChar[i] == '0'){
    					swapChar[i] = 'P';
    					setString += swapChar[i];
    				}
    				else if(swapChar[i] == '1'){
    					swapChar[i] = 'Q';
    					setString += swapChar[i];
    				}
    				else if(swapChar[i] == '2'){
    					swapChar[i] = 'W';
    					setString += swapChar[i];
    				}
    				else if(swapChar[i] == '3'){
    					swapChar[i] = 'E';
    					setString += swapChar[i];
    				}
    				else if(swapChar[i] == '4'){
    					swapChar[i] = 'R';
    					setString += swapChar[i];
    				}
    				else if(swapChar[i] == '5'){
    					swapChar[i] = 'T';
    					setString += swapChar[i];
    				}
    				else if(swapChar[i] == '6'){
    					swapChar[i] = 'Z';
    					setString += swapChar[i];
    				}
    				else if(swapChar[i] == '7'){
    					swapChar[i] = 'U';
    					setString += swapChar[i];
    				}
    				else if(swapChar[i] == '8'){
    					swapChar[i] = 'I';
    					setString += swapChar[i];
    				}
    				else if(swapChar[i] == '9'){
    					swapChar[i] = 'O';
    					setString += swapChar[i];
    				}
    			} // end if(Character.isDigit(swapChar[i]))
    		} // end for(int i = 0; i < getString.length(); i++)
    		return setString.toUpperCase();
        } // end if (!getString.isEmpty())
        else{
        	return getString;
        }
    } // end configure method
} // end ConfigureOutput class
