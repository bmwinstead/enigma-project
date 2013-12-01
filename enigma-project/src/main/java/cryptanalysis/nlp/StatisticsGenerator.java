/**
 * StatisticsGenerator.java
 * @author - Walter Adolph
 * @author - Team Enigma
 * @version - 0.9
 * @date - Nov 26, 2013
 * 
 * This class computes a variety of statistics, provided at the below references:
 * Each generator can be initialized to use a particular statistic.
 * 
 * List of available statistics:
 * 0 - Unigram character probability. (Sinkov's statistic)
 * 1 - Bigram character probability. (Sinkov's statistic)
 * 2 - Trigram character probability. (Sinkov's statistic)
 * 3 - Quadgram character probability. (Sinkov's statistic)
 * 4 - Unigram character probability. (Index of Coincidence)
 * 5 - Bigram character probability. (Index of Coincidence)
 * 6 - Trigram character probability. (Index of Coincidence)
 * 7 - Quadgram character probability. (Index of Coincidence)
 * 
 * References:
 * http://practicalcryptography.com/cryptanalysis/breaking-machine-ciphers/cryptanalysis-enigma/
 * http://practicalcryptography.com/cryptanalysis/text-characterisation/quadgrams/
 * 
 * 
 */
package main.java.cryptanalysis.nlp;

import java.util.HashMap;

public class StatisticsGenerator {
	private Corpus database;
	private int statIndex;		// Used to select statistic to use.
	
	public StatisticsGenerator(Corpus newDatabase, int index) {
		database = newDatabase;
		statIndex = index;
	}
	
	public void selectFitnessTest(int test) {
		statIndex = test;
	}
	
	public double computeFitnessScore(String message) {
		switch (statIndex) {
			case 0:		// Sinkov unigram character probability.
				return computeSinkovUnigramProbability(message);
			case 1:		// Sinkov bigram character probability.
				return computeSinkovBigramProbability(message);
			case 2:		// Sinkov trigram character probability.
				return computeSinkovTrigramProbability(message);
			case 3:		// Sinkov quadgram character probability.
				return computeSinkovQuadgramProbability(message);
			case 4:		// Index of Coincidence unigram character probability.
				return computeIocUnigramProbability(message);
			case 5:		// Index of Coincidence bigram character probability.
				return computeIocBigramProbability(message);
			case 6:		// Index of Coincidence trigram character probability.
				return computeIocTrigramProbability(message);
			case 7:		// Index of Coincidence quadgram character probability.
				return computeIocQuadgramProbability(message);
			case 8:		// Chi-Squared unigram probability.
				return computeChiSquaredUnigramProbability(message);
			case 9:		// Chi-Squared bigram probability.
				return computeChiSquaredBigramProbability(message);
			default:	// Error condition.
				return Double.NEGATIVE_INFINITY;
		}
	}
	
	// Compute log probability of a unigram character string compared to a corpus.
	public double computeSinkovUnigramProbability(String message) {
		double result = 0.0;
		
		long totalCount = database.getTotalUnigramCount();
		
		// Set floor value to 1 / 1000 of single instance of gram. See above references.
		double floorLog = -3.0 + Math.log10(1.0 / totalCount);
		
		char[] characters = message.toCharArray();
		
		// Compute individual log probabilites.
		// log probabilities are used to avoid numerical underflow. See above references.
		for (int index = 0; index < message.length(); index++) {
			String gram = "" + characters[index];
			long count = database.getUnigramCount(gram);
			double logProb = Math.log10((double)count / totalCount);
			
			if (count > 0)
				result += logProb;
			else
				result += floorLog;	// Prevent addition by negative infinity resulting from log(0).
		}
		
		return result;
	}
	
	// Compute log probability of a bigram character string compared to a corpus.
	public double computeSinkovBigramProbability(String message) {
		double result = 0.0;
		
		long totalCount = database.getTotalBigramCount();
		
		// Set floor value to 1 / 1000 of single instance of gram. See above references.
		double floorLog = -3.0 + Math.log10(1.0 / totalCount);
		
		char[] characters = message.toCharArray();
		
		// Compute individual log probabilites.
		// log probabilities are used to avoid numerical underflow. See above references.
		for (int index = 0; index < message.length() - 1; index++) {
			String gram = "" + characters[index] + characters[index + 1];
			long count = database.getBigramCount(gram);
			double logProb = Math.log10((double)count / totalCount);
			
			if (count > 0)
				result += logProb;
			else
				result += floorLog;	// Prevent addition by negative infinity resulting from log(0).
		}
		
		return result;
	}
	
	// Compute log probability of a trigram character string compared to a corpus.
	public double computeSinkovTrigramProbability(String message) {
		double result = 0.0;
		
		long totalCount = database.getTotalTrigramCount();
		
		// Set floor value to 1 / 1000 of single instance of gram. See above references.
		double floorLog = -3.0 + Math.log10(1.0 / totalCount);
		
		char[] characters = message.toCharArray();
		
		// Compute individual log probabilites.
		// log probabilities are used to avoid numerical underflow. See above references.
		for (int index = 0; index < message.length() - 2; index++) {
			String gram = "" + characters[index] + characters[index + 1] + characters[index + 2];
			long count = database.getTrigramCount(gram);
			double logProb = Math.log10((double)count / totalCount);
			
			if (count > 0)
				result += logProb;
			else
				result += floorLog;	// Prevent addition by negative infinity resulting from log(0).
		}
		
		return result;
	}
	
	// Compute log probability of a quadgram character string compared to a corpus.
	public double computeSinkovQuadgramProbability(String message) {
		double result = 0.0;
		
		long totalCount = database.getTotalQuadgramCount();
		
		// Set floor value to 1 / 1000 of single instance of gram. See above references.
		double floorLog = -3.0 + Math.log10(1.0 / totalCount);
		
		char[] characters = message.toCharArray();
		
		// Compute individual quadgram log probabilites.
		// log probabilities are used to avoid numerical underflow. See above references.
		for (int index = 0; index < message.length() - 3; index++) {
			String gram = "" + characters[index] + characters[index + 1] + characters[index + 2] + characters[index + 3];
			long count = database.getQuadgramCount(gram);
			double logProb = Math.log10((double)count / totalCount);
			
			if (count > 0)
				result += logProb;
			else
				result += floorLog;	// Prevent addition by negative infinity resulting from log(0).
		}
		
		return result;
	}
	
	// Compute log probability of a unigram character string compared to a corpus.
	public double computeIocUnigramProbability(String message) {
		Corpus iocCounter = new Corpus();
		
		double result = 0.0;

		char[] characters = message.toCharArray();
		
		// Count gram instances.
		for (int index = 0; index < message.length(); index++) {
			String gram = "" + characters[index];
			iocCounter.addUnigram(gram);
		}
		
		// Compute Index of Coincidences.
		for (String gram: iocCounter.getUnigramTestQueue()) {
			long count = iocCounter.getUnigramCount(gram);
			
			result += count * (count - 1);
		}
		
		result /= message.length() * (message.length() - 1);
		
		return result;
	}
	
	// Compute log probability of a bigram character string compared to a corpus.
	public double computeIocBigramProbability(String message) {
		Corpus iocCounter = new Corpus();
		
		double result = 0.0;
		
		char[] characters = message.toCharArray();
		
		// Count gram instances.
		for (int index = 0; index < message.length() - 1; index++) {
			String gram = "" + characters[index] + characters[index + 1];
			iocCounter.addBigram(gram);
		}
		
		// Compute Index of Coincidences.
		for (String gram: iocCounter.getBigramTestQueue()) {
			long count = iocCounter.getBigramCount(gram);
			
			result += count * (count - 1);
		}
		
		result /= message.length() * (message.length() - 1);
		
		return result;
	}
	
	// Compute log probability of a trigram character string compared to a corpus.
	public double computeIocTrigramProbability(String message) {
		Corpus iocCounter = new Corpus();
		
		double result = 0.0;
		
		char[] characters = message.toCharArray();
		
		// Count gram instances.
		for (int index = 0; index < message.length() - 2; index++) {
			String gram = "" + characters[index] + characters[index + 1] + characters[index + 2];
			iocCounter.addTrigram(gram);
		}
		
		// Compute Index of Coincidences.
		for (String gram: iocCounter.getTrigramTestQueue()) {
			long count = iocCounter.getTrigramCount(gram);
			
			result += count * (count - 1);
		}
		
		result /= message.length() * (message.length() - 1);
		
		return result;
	}
	
	// Compute log probability of a quadgram character string compared to a corpus.
	public double computeIocQuadgramProbability(String message) {
		Corpus iocCounter = new Corpus();
		
		double result = 0.0;
		
		char[] characters = message.toCharArray();
		
		// Count gram instances.
		for (int index = 0; index < message.length() - 3; index++) {
			String gram = "" + characters[index] + characters[index + 1] + characters[index + 2] + characters[index + 3];
			iocCounter.addQuadgram(gram);
		}
		
		// Compute Index of Coincidences.
		for (String gram: iocCounter.getQuadgramTestQueue()) {
			long count = iocCounter.getQuadgramCount(gram);
			
			result += count * (count - 1);
		}
		
		result /= message.length() * (message.length() - 1);
		
		return result;
	}
	
	public double computeChiSquaredUnigramProbability(String message) {
		Corpus gramCounter = new Corpus();
		
		double result = 0.0;
		
		char[] characters = message.toCharArray();
		
		// Count gram instances.
		for (int index = 0; index < message.length(); index++) {
			String gram = "" + characters[index];
			gramCounter.addUnigram(gram);
		}
		
		// Count gram instances.
		for (String gram: gramCounter.getUnigramTestQueue()) {
			double count = gramCounter.getUnigramCount(gram);
			double unigramCount = database.getUnigramCount(gram);
			long totalCount = database.getTotalUnigramCount();
			
			double relativeFreq = count / message.length();
			double absoluteFreq = unigramCount / totalCount;
			
			result += -(relativeFreq - absoluteFreq) * (relativeFreq - absoluteFreq) / absoluteFreq;
		}
		
		return result;
	}
	
	public double computeChiSquaredBigramProbability(String message) {
		Corpus gramCounter = new Corpus();
		
		double result = 0.0;
		
		char[] characters = message.toCharArray();
		
		HashMap<Character, Integer> bigramFirstLetterCount = new HashMap<Character, Integer>();
		
		for (int index = 0; index < 26; index++) {
			bigramFirstLetterCount.put((char)('A' + index), 0);
		}
		
		// Count gram instances.
		for (int index = 0; index < message.length() - 1; index++) {
			String gram = "" + characters[index] + characters[index + 1];

			int count = bigramFirstLetterCount.get(characters[index]) + 1;
			bigramFirstLetterCount.put(characters[index], count);
			
			gramCounter.addUnigram("" + characters[index]);
			gramCounter.addBigram(gram);
		}
		
		gramCounter.addUnigram("" + characters[message.length() - 1]);
		
		// Count gram instances.
		for (int i = 0; i < 26; i++) {
			for (int j = 0; j < 26; j++) {
				char first = (char)(i + 'A');
				char second = (char)(j + 'A');
				String gram = "" + first + second;
				
				int unigramCount = gramCounter.getUnigramCount("" + first);
				int bigramCount = gramCounter.getBigramCount(gram);
				double bigramLetterCount = bigramFirstLetterCount.get(first);
				double bigramTotalCount = database.getBigramCount(gram);
				int totalCount = database.getTotalBigramCount();
				
				if (bigramLetterCount > 0 && bigramTotalCount > 0) {
					double relativeFreq = bigramCount / bigramLetterCount;
					double absoluteFreq = bigramTotalCount / totalCount;
					
					result += -unigramCount * (relativeFreq - absoluteFreq) * (relativeFreq - absoluteFreq) / absoluteFreq;
				}
			}
		}
		
		return result;
	}
	
	// Test generator used to compare individual probabilities in a single pass.
	public double[] computeAllScores(String message) {
		double[] result = new double[8];
		Corpus iocCounter = new Corpus();
		
		int[] totalCount = {database.getTotalUnigramCount(),
				database.getTotalBigramCount(),
				database.getTotalTrigramCount(),
				database.getTotalQuadgramCount()
		};
		
		double[] floorLog = new double[result.length];
		
		// Set floor value to 1 / 1000 of single instance of gram. See above references.
		for (int index = 0; index < 4; index++) {
			floorLog[index] = -3.0 + Math.log10(1.0 / totalCount[index]);
		}
		
		char[] characters = message.toCharArray();
		
		// Compute individual probabilites.
		// log probabilities are used to avoid numerical underflow. See above references.
		for (int index = 0; index < message.length(); index++) {
			// Compute unigram character statistics.
			String gram = "" + characters[index];
			long count = database.getUnigramCount(gram);
			double logProb = Math.log10((double)count / totalCount[0]);
			
			if (count > 0)
				result[0] += logProb;
			else
				result[0] += floorLog[0];	// Prevent addition by negative infinity resulting from log(0).
			
			iocCounter.addUnigram(gram);
			
			if (message.length() - index > 1) {
				// Compute bigram character statistic.
				gram = "" + characters[index] + characters[index + 1];
				count = database.getBigramCount(gram);
				logProb = Math.log10((double)count / totalCount[1]);
				
				if (count > 0)
					result[1] += logProb;
				else
					result[1] += floorLog[1];	// Prevent addition by negative infinity resulting from log(0).
				
				iocCounter.addBigram(gram);
			}
			
			if (message.length() - index > 2) {
				// Compute trigram character statistic.
				gram = "" + characters[index] + characters[index + 1] + characters[index + 2];
				count = database.getTrigramCount(gram);
				logProb = Math.log10((double)count / totalCount[2]);
				
				if (count > 0)
					result[2] += logProb;
				else
					result[2] += floorLog[2];	// Prevent addition by negative infinity resulting from log(0).
				
				iocCounter.addTrigram(gram);
			}
			
			if (message.length() - index > 3) {
				// Compute quadgram character statistic.
				gram = "" + characters[index] + characters[index + 1] + characters[index + 2] + characters[index + 3];
				count = database.getQuadgramCount(gram);
				logProb = Math.log10((double)count / totalCount[3]);
				
				if (count > 0)
					result[3] += logProb;
				else
					result[3] += floorLog[3];	// Prevent addition by negative infinity resulting from log(0).
				
				iocCounter.addQuadgram(gram);
			}
		}
		
		iocCounter.sortDatabase();
		
		// Compute Index of Coincidences.
		for (String gram: iocCounter.getUnigramTestQueue()) {
			long count = iocCounter.getUnigramCount(gram);
			result[4] += count * (count - 1);
		}
		
		for (String gram: iocCounter.getBigramTestQueue()) {
			long count = iocCounter.getBigramCount(gram);
			result[5] += count * (count - 1);
		}
		
		for (String gram: iocCounter.getTrigramTestQueue()) {
			long count = iocCounter.getTrigramCount(gram);
			result[6] += count * (count - 1);
		}
		
		for (String gram: iocCounter.getQuadgramTestQueue()) {
			long count = iocCounter.getQuadgramCount(gram);
			result[7] += count * (count - 1);
		}
		
		result[4] /= iocCounter.getTotalUnigramCount() * (iocCounter.getTotalUnigramCount() - 1);
		result[5] /= iocCounter.getTotalBigramCount() * (iocCounter.getTotalBigramCount() - 1);
		result[6] /= iocCounter.getTotalTrigramCount() * (iocCounter.getTotalTrigramCount() - 1);
		result[7] /= iocCounter.getTotalQuadgramCount() * (iocCounter.getTotalQuadgramCount() - 1);
		
		return result;
	}
}
