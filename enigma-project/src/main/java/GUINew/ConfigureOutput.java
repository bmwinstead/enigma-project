package main.java.GUINew;

/**
 * Class to format the input for processing. Automatically replaces
 * numbers with their "code" letters, removes all other non-letter
 * characters, and converts everything to upper case. 
 * 
 * @author Ellen Ohlmacher
 * @author Team Enigma
 * @version 0.8
 * 30 Nov 2013
 *
 */
public class ConfigureOutput {
	
	public ConfigureOutput() {
	}
	
	/**
	 * Accepts a string and converts it to the proper format for encryption.
	 * Converts all digits to their corresponding letters and strips out all
	 * other nonletter characters. Converts all letters to uppercase.
	 * 
	 * @param getString
	 *            Input string to be preprocessed for encryption
	 * @return Properly processed input string.
	 */
	public String configure(String getString, int spaceOption){
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
    			if ((spaceOption == EnigmaSingleton.ORIGINALSPACES) 
    					&& (swapChar[i] == ' ')) {
    				setString += swapChar[i];
    			} // end if ((spaceOption == EnigmaSingleton.ORIGINALSPACES) 
    		} // end for(int i = 0; i < getString.length(); i++)
    		return setString.toUpperCase();
        } // end if (!getString.isEmpty())
        else{
        	return getString;
        }
    } // end configure method
	/**
	 * Accepts a character and converts it to the proper format for encryption.
	 * Converts all digits to their corresponding letters and strips out all
	 * other nonletter characters.
	 * 
	 * @param getChar
	 *            Input char to be preprocessed for encryption
	 * @return Properly processed input char.
	 */
	public char configure(char getChar, int spaceOption){
		char setChar = '!';
		if (Character.isLetter(getChar)){
			setChar = getChar;
		}
		else if(Character.isDigit(getChar)){
			if(getChar == '0'){
				setChar = 'P';
			}
			else if(getChar == '1'){
				setChar = 'Q';
			}
			else if(getChar == '2'){
			    setChar = 'W';
			 }
			 else if(getChar == '3'){
			    setChar = 'E';
			 }
			 else if(getChar == '4'){
			    setChar = 'R';
			 }
			 else if(getChar == '5'){
			    setChar = 'T';
			 }
			 else if(getChar == '6'){
			    setChar = 'Z';
			 }
			 else if(getChar == '7'){
			    setChar = 'U';
			 }
			 else if(getChar == '8'){
			    setChar = 'I';
			 }
			 else if(getChar == '9'){
			    setChar = 'O';
			 }
		} // end if(Character.isDigit(getChar))
		if ((spaceOption == EnigmaSingleton.ORIGINALSPACES)
				&& (getChar == ' ')) 
		{
			System.out.println("I am here in this if.");
			setChar = getChar;
		} // end if ((spaceOption == EnigmaSingleton.ORIGINALSPACES)... 
		return setChar;
	}// end configure method

} // end ConfigureOutput class
