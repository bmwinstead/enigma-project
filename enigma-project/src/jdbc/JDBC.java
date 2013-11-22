package jdbc;
import java.io.InputStream;
import java.sql.*;
import java.util.Scanner;

public class JDBC {
	private Connection sqlConnection;

	private final String DB_IP;
	private final String DB_PORT;
	private final String DB_NAME;
	private final String DB_CONNECTION;
	private final String DB_USER;
	private final String DB_PASSWORD;

	public JDBC(String IP, String port, String dbName, String userName,
			String password) {
		DB_IP = IP;
		DB_PORT = port;
		DB_NAME = dbName;
		DB_USER = userName;
		DB_PASSWORD = password;
		DB_CONNECTION = "jdbc:mysql://" + DB_IP + ":" + DB_PORT + "/" + DB_NAME;
	}

	/**
	 * Initializes the connection setup when this object was constructed.
	 * 
	 * @return true if successful, false if not.
	 */
	public boolean initializeConnection() {
		System.out.println("Establishing db connection to: " + DB_CONNECTION);
		System.out.println("Using u/n:" + DB_USER + " p/w:" + DB_PASSWORD);
		long st = System.currentTimeMillis();
		try {
			sqlConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER,
					DB_PASSWORD);
			long et = System.currentTimeMillis();
			System.out.println("Established db connection in " + (et - st)
					+ "ms");
			return true;
		} catch (SQLException e) {
			System.err.println("Could not open db connection");
			return false;
		}
	}

	public boolean setAutoCommit(boolean shouldAC) {
		try {
			sqlConnection.setAutoCommit(shouldAC);
			return true;
		} catch (SQLException e) {
			System.err.println("Couldn't set auto commit");
			return false;
		}
	}

	/**
	 * Attempts to create the necessary table using the current sqlConnection
	 * object, so obviously the method getConnection() must be called first.
	 * 
	 */
	public boolean createTables() {
		System.out.println("Creating table ngram_node and ngram_blackhole");
		long st = System.currentTimeMillis();
		try (Statement stmt = sqlConnection.createStatement()) {
			String s = "CREATE TABLE IF NOT EXISTS ngram_node "
					+ "(NGRAM_KEY CHAR(32) NOT NULL, "
					+ "NGRAM VARCHAR(255) NOT NULL,"
					+ "NGRAM_SIZE TINYINT NOT NULL,"
					+ "FREQUENCY SMALLINT DEFAULT 1 NOT NULL, "
					+ "PRIMARY KEY (NGRAM_KEY))";
			String s2 = "CREATE TABLE IF NOT EXISTS ngram_blackhole "
					+ "(NGRAM VARCHAR(255) NOT NULL, "
					+ "FREQUENCY SMALLINT DEFAULT 1 NOT NULL,"
					+ "NGRAM_SIZE TINYINT NOT NULL)" + "ENGINE=BLACKHOLE";
			String trigger = "CREATE TRIGGER populate_ngram AFTER INSERT ON ngram_blackhole FOR EACH ROW\n"
					+ "BEGIN\n"
					+ "\tINSERT INTO ngram_node VALUES (MD5(NEW.NGRAM),NEW.NGRAM,NEW.NGRAM_SIZE,NEW.FREQUENCY)\n"
					+ "ON DUPLICATE KEY UPDATE FREQUENCY=FREQUENCY+1;\n"
					+ "END;\n";
			stmt.executeUpdate(s);
			stmt.executeUpdate(s2);
			stmt.executeUpdate(trigger);
			long et = System.currentTimeMillis();
			System.out
					.println("Finished creating table in " + (et - st) + "ms");
			return true;
		} catch (SQLException e) {
			System.out.println("Failed to create table");
			return false;

		}
	}

	/**
	 * Deletes tables... duh.
	 * 
	 * @return true if dropped, false if not.
	 */
	public boolean dropTables() {
		String dropFirst = "DROP TABLE IF EXISTS ngram_node;";
		String dropSecond = "DROP TABLE IF EXISTS ngram_blackhole";
		try (Statement stmt = sqlConnection.createStatement()) {
			stmt.executeUpdate(dropFirst);
			stmt.executeUpdate(dropSecond);
			return true;
		} catch (SQLException e) {
			System.err.println("Something went wrong during table drop");
			return false;
		}
	}

	/**
	 * Breaks a string into grams and inserts the individual grams into the
	 * database. Does not have to be pre-processed, as it automatically removes
	 * punctuation and ignores capitalization. However, works best if there are
	 * no extraneous new-lines as it does not currently handle these well. It
	 * works but it slows down things a bit.
	 * 
	 * @param stringRep
	 *            the entire String to be broken up, processed, and inserted
	 *            into the database.
	 * @param batchSize
	 *            the size of the batches to be committed at once.
	 * @throws SQLException
	 */
	public void processAndBatchInsert(String stringRep, int batchSize) {
		// removes punctuation and capitalization issues.
		// will still have strings of length 0.
		String[] words = stringRep.replaceAll("\\p{P}", "").toUpperCase()
				.split("\\s");
		int length = words.length;
		String unigram = "";
		String bigram = "";
		String trigram = "";
		String quadgram = "";
		String insertTableSQL = "INSERT INTO ngram_blackhole (NGRAM,NGRAM_SIZE) VALUES(?,?)";
		try (PreparedStatement pstate = sqlConnection
				.prepareStatement(insertTableSQL)) {
			System.out.println("Inserting " + length
					+ " words(give or take) into db... this may take a while");
			long st = System.currentTimeMillis();
			int totalRows = 0;
			for (int i = 0; i < length; i++) {
				if (words[i].length() != 0) {
					unigram = words[i];
					pstate.setString(1, unigram);
					pstate.setInt(2, 1);
					pstate.addBatch();
					if (i < length - 1 && (words[i + 1].length() != 0)) {
						bigram = unigram + " " + words[i + 1];
						pstate.setString(1, bigram);
						pstate.setInt(2, 2);
						pstate.addBatch();
					}
					if (i < length - 2 && (words[i + 2].length() != 0)) {
						trigram = bigram + " " + words[i + 2];
						pstate.setString(1, trigram);
						pstate.setInt(2, 3);
						pstate.addBatch();
					}
					if (i < length - 3 && (words[i + 3].length() != 0)) {
						quadgram = trigram + " " + words[i + 3];
						pstate.setString(1, quadgram);
						pstate.setInt(2, 4);
						pstate.addBatch();
					}
					if ((i + 1) % batchSize == 0) {
						long st1 = System.currentTimeMillis();
						System.out.println("Executing 1000 items");
						int[] rval = pstate.executeBatch();
						int tempRows = 0;
						for (int j : rval) {
							if (j != Statement.SUCCESS_NO_INFO
									&& j != Statement.EXECUTE_FAILED)
								tempRows += j;
						}
						long et1 = System.currentTimeMillis();
						System.out.println("Updated " + tempRows + " rows in "
								+ (et1 - st1) + "ms");
						totalRows += tempRows;
					}
					sqlConnection.commit();
				} // end if
			} // end for
			long et = System.currentTimeMillis();
			pstate.executeBatch();
			System.out.println("Finished inserting words into db, took "
					+ (et - st) + "ms");
			System.out.println("Total rows updated:" + totalRows);
			sqlConnection.commit();
		} catch (SQLException e) {
			System.out.println("Failed inserting words into db");
			System.err.println("Insert failed somehow");
		}
	}

	/**
	 * Finds the frequency of a given gram. TODO: Insert trigger into DB so that
	 * hash takes place on DB side and you query blackhole table instead, as
	 * MYSQL hashing is way faster than Java's.
	 * 
	 * @param gram
	 *            the gram to find.
	 * @return the frequency of said gram.
	 */
	public int getFrequency(String gram) {
		String sql = "SELECT FREQUENCY FROM ngram_node WHERE NGRAM_KEY = \'"
				+ md5(gram) + "\'";
		long st = System.currentTimeMillis();
		try (Statement stmt = sqlConnection.createStatement()) {
			ResultSet rs = stmt.executeQuery(sql);
			int freq = -1;
			while (rs.next()) {
				freq = rs.getInt(1);
			}
			long et = System.currentTimeMillis();
			System.out.println("Retrieved frequency in " + (et-st) + "ms");
			return freq;
		} catch (SQLException e) {
			System.err.println("Statement failed: " + sql);
			return -1;
		}
	}

	/**
	 * Returns sorted ResultSet of entire table. Will probably be very slow.
	 * sortBy can only be NGRAM_SIZE, FREQUENCY, or NGRAM. Could also be
	 * NGRAM_KEY but your data would be sorted by hash which is silly.
	 * 
	 * @param sortBy
	 * @return
	 */
	public ResultSet getEntireTable(String sortBy) {
		String sql = "SELECT * FROM ngram_node ORDER BY " + sortBy;
		long st = System.currentTimeMillis();
		try (Statement stmt = sqlConnection.createStatement()) {
			ResultSet rs = stmt.executeQuery(sql);
			long et = System.currentTimeMillis();
			System.out.println("Retrieved entire table in " + (et-st) + "ms");
			return rs;
		} catch (SQLException e) {
			System.err.println("Statement faield: " + sql);
			return null;
		}
	}

	/**
	 * @param N
	 *            1 returns # of unigrams, 2 # of bigrams, etc.
	 * @return total number of N-grams in the database.
	 * @throws SQLException
	 */
	public int getNumberNGram(int N) {
		String sql = "SELECT COUNT(*) FROM ngram_node WHERE NGRAM_SIZE = "
				+ String.valueOf(N);
		long st = System.currentTimeMillis();
		try (Statement stmt = sqlConnection.createStatement()) {
			ResultSet rs = stmt.executeQuery(sql);
			int total = -1;
			while (rs.next()) {
				total = rs.getInt(1);
			}
			long et = System.currentTimeMillis();
			System.out.println("Retrieved " + N + " grams in " + (et - st) + "ms");
			return total;
		} catch (SQLException e) {
			System.err.println("Statement failed: " + sql);
			return -1;
		}
	}

	/**
	 * Helper method to load a resource from the classpath, instead of a
	 * filesystem. Accepts the URI of the resource to load in either absolute
	 * (ex: "\resources\Moby Dick.txt") or relative (ex: "Dracula.txt") format.
	 * Should probably be moved to a different class, but whatever.
	 * 
	 * @param name
	 *            the URI of the text resource to load.
	 * @return A string representation of the resource.
	 */
	public String readResource(String name) {
		System.out.println("loading " + name);
		long st = System.currentTimeMillis();
		InputStream io = this.getClass().getResourceAsStream(name);
		Scanner scan = new Scanner(io);
		StringBuilder stuff = new StringBuilder();
		while (scan.hasNext()) {
			stuff.append(scan.nextLine() + " ");
		}
		System.out.println("Finished loading " + name);
		long et = System.currentTimeMillis();
		System.out.println("Took: " + (et - st) + "ms");
		scan.close();
		return stuff.toString();
	}

	/**
	 * Returns String representation of md5 hash.
	 * 
	 * @param s
	 *            the String to hash.
	 * @return the hashed value.
	 */
	private String md5(String s) {
		s = s.toUpperCase();
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			byte[] array = md.digest(s.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
						.substring(1, 3));
			}
			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
		}
		return null;
	}

}
