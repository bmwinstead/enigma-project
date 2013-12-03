
package main.java.cryptanalysis.nlp;


public class Crib {
	private String crib;
	private int start;
	private int end;
	
	/**
	 * @return the crib
	 */
	public String getCrib() {
		return crib;
	}

	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @return the end
	 */
	public int getEnd() {
		return end;
	}

	public Crib(String crib, int start, int end) {
		super();
		this.crib = crib;
		this.start = start;
		this.end = end;
	}
	

}
