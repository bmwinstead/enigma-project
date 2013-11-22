package jdbc;

public class TestJDBC {
	private static final String IP = "localhost";
	private static final String DB_NAME = "test";
	private static final String PORT = "3306";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "password";
	private static JDBC jdbc;
	public static void main(String[] args){
		jdbc = new JDBC(IP,PORT,DB_NAME,USERNAME,PASSWORD);
		jdbc.initializeConnection();
		jdbc.createTables();
		jdbc.processAndBatchInsert("Hello, I am a dog and I like to play outside in the summer the the the the a a a a a a a a the the summer",10);
		System.out.println("Frequency of the: " + jdbc.getFrequency("the"));
		System.out.println("Frequency of a: " + jdbc.getFrequency("a"));
		System.out.println("Frequency of summer: " + jdbc.getFrequency("summer"));
		System.out.println("Number of unigrams: " + jdbc.getNumberNGram(1));
		System.out.println("Number of bigrams: " + jdbc.getNumberNGram(2));
		System.out.println("Number of trigrams: " + jdbc.getNumberNGram(3));
		System.out.println("Number of quadgrams: " + jdbc.getNumberNGram(4));
	}
}
