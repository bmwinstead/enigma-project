package enigma;

public class Plugboard {

	private char[][] plugboard;
	private int numPairs;
	
	public Plugboard(String mapping){
		char[] cArr = mapping.toCharArray();
		numPairs = (int) Math.floor(cArr.length/2);
		plugboard = new char[numPairs][2];
		for(int i = 0; i < cArr.length; i++){
			int j = (int) Math.floor(i/2);
			plugboard[j][i%2]=cArr[i];
		}
	}
	
	public Plugboard(char[][] newpb){
		plugboard = newpb;
	}
	
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
