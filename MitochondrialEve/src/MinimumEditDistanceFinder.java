
public class MinimumEditDistanceFinder {
	
	private String stringA;
	private String stringB;
	private final int stringALength;
	private final int stringBLength;
	
	/**
	 * Constructor
	 * 
	 * @param stringA
	 * @param stringB
	 */
	public MinimumEditDistanceFinder(String stringA, String stringB) {
		this.stringA = stringA;
		this.stringB = stringB;
		this.stringALength = stringA.length();
		this.stringBLength = stringB.length();
	}

	
	/**
	 * @return the number of substitutions, insertions or deletions needed to convert one string to another
	 */
	public int getAlignmentScore() {
		
		//Create a strip of memory two columns wide that will be slid along from beginning to end of string A
		int [][] twoColumnScoreMatrix = new int [this.stringBLength + 1][2];
		
		for (int columnCounter = 0; columnCounter <= this.stringALength; ++columnCounter) {
			
			//When column counter is zero, fill in the alignment with an empty string A
			if (columnCounter == 0) {
				for (int emptyStringAlignmentCounter = 0; emptyStringAlignmentCounter <= this.stringBLength; ++emptyStringAlignmentCounter) {
					twoColumnScoreMatrix[emptyStringAlignmentCounter][0] = emptyStringAlignmentCounter; 
				}
				continue;
			}

			//Fill in the alignment scores one column at a time
			for (int rowCounter = 0; rowCounter <= this.stringBLength; ++rowCounter) {
				
				if (rowCounter == 0) {
					twoColumnScoreMatrix[rowCounter][columnCounter % 2] = columnCounter;
				} else {
					twoColumnScoreMatrix[rowCounter][columnCounter % 2] = getMinimumScore(twoColumnScoreMatrix[rowCounter - 1][columnCounter % 2] + 1, 
																						  twoColumnScoreMatrix[rowCounter][(columnCounter - 1) % 2] + 1, 
																						  twoColumnScoreMatrix[rowCounter - 1][(columnCounter - 1) % 2] + 
																					   	 (this.stringA.charAt(columnCounter - 1) == this.stringB.charAt(rowCounter - 1) ? 0 : 1));
				}
				
			}
		}
		
		return twoColumnScoreMatrix[this.stringBLength][this.stringALength % 2];

	}
	
	/**
	 * Find the string alignment score using dynamic programming
	 */
	
	/**
	 * @return the number of substitutions, insertions or deletions needed to convert one string to another
	 */
	public int getAlignmentScoreByDynamicProgramming() {
		
		int[][] stringAlignmentCache = new int[this.stringBLength + 1][this.stringALength + 1];

		
		//Initialize bottom row of cache
		for (int columnCounter = 0; columnCounter <= this.stringALength; ++columnCounter) {
			stringAlignmentCache[0][columnCounter] = columnCounter;
		}
		
		//Initialize left column of cache
		for (int rowCounter = 0; rowCounter <= this.stringBLength; ++rowCounter) {
			stringAlignmentCache[rowCounter][0] = rowCounter;
		}
		
		//Fill in the remaining cache entries row by row from bottom to top till you reach the
		//top right entry, which will contain the best alignment score
		for (int columnCounter = 1; columnCounter <= this.stringALength; ++columnCounter) {
			for (int rowCounter = 1; rowCounter <= this.stringBLength; ++rowCounter) {
				
				stringAlignmentCache[rowCounter][columnCounter] = getMinimumScore(stringAlignmentCache[rowCounter - 1][columnCounter] + 1, 
																				  stringAlignmentCache[rowCounter][columnCounter - 1] + 1, 
																				  stringAlignmentCache[rowCounter - 1][columnCounter - 1] + 
																				  (this.stringA.charAt(columnCounter - 1) == this.stringB.charAt(rowCounter - 1) ? 0 : 1));
				
			}
		}
		
		return stringAlignmentCache[this.stringB.length()][this.stringA.length()];
		
	}
	
	/**
	 * @param integer1
	 * @param integer2
	 * @param integer3
	 * @return the least of the three parameters
	 */
	private int getMinimumScore(int integer1, int integer2, int integer3) {
		
		return Math.min(Math.min(integer1, integer2), integer3);
		
	}

}
