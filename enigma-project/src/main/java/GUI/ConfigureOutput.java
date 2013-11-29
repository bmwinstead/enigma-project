package main.java.GUI;

public class ConfigureOutput {
	
    //Change input to correct case & remove special characters
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
    			}
    		}
    		return setString.toUpperCase();
        }
        else{
        	return getString;
        }
    }
}
