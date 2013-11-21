/**
 * Logger.java
 * @author - Walter
 * @date - Nov 21, 2013
 * Static class to dump events to a text file / console output.
 */
package misc;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class Logger {
	private static PrintWriter output;
	
	// Create a new text file if one does not exist.
	public static void createFile() {
		try {
			output = new PrintWriter(new BufferedWriter(new FileWriter("C:\\Users\\Walter\\Documents\\Enigma Text Files\\enigma-debug.txt", true)));
			makeEntry("Logger initialized.", true);
		} catch (IOException e) {
			if (output != null) {
				output.close();
			}
			
			e.printStackTrace();
		}
	}
	
	// Close a file.
	public static void closeFile() {
		makeEntry("Logger closed.\r\n", true);	// Double spacing to separate logger instances.
		output.close();
	}
	
	// Adds a new time-stamped entry to the log, with an option to print to console.
	public static void makeEntry(String phrase, boolean outputConsole) {
		Date timestamp = new Date(); // Init. to current time and date.
		
		String entry = "[" + timestamp + "] - " + phrase + "\r\n";	// Windows text editors require a carriage return, should be fine also on other systems.
		
		output.append(entry);
		
		if (outputConsole) {
			System.out.print(entry);
		}
	}
}
