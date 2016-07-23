import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class MinimumEditDistanceClient {
	
	public static final char[] validBases = {'A', 'C', 'G', 'T'};
	public static final int MAXIMUM_NUCLEOTIDE_STRING_LENGTH = 16569;
	
	private Random randomNumberGenerator;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		MinimumEditDistanceClient minimumEditDistanceClient = new MinimumEditDistanceClient(args);
		minimumEditDistanceClient.showStringAlignment();

	}
	
	/**
	 * Constructor
	 */
	public MinimumEditDistanceClient(String[] args) {
		
		this.randomNumberGenerator = new Random(System.currentTimeMillis());
		
	}
	
	/**
	 * Show the difference between the strings
	 */
	private void showStringAlignment() {

		MinimumEditDistanceFinder minimumEditDistanceFinder = null;
		
		//minimumEditDistance = new MinimumEditDistance(stringA, stringB);
		//editDistance1 = minimumEditDistance.getAlignmentScoreByDynamicProgramming();
		//System.out.println("\nAlignment score by DP: " + editDistance1 + '\n');
		
		List<StringPair> mitochondrialDnaPairs = new ArrayList<StringPair>();
		StringPair mitochondrialDnaForTwoPersons = null;
		for (int counter = 0; counter < 4; ++counter) {
			mitochondrialDnaForTwoPersons = new StringPair(getRandomNucleotideString(), getRandomNucleotideString());
			mitochondrialDnaPairs.add(mitochondrialDnaForTwoPersons);
		}
		
		long startTime = System.currentTimeMillis();
		for (StringPair dnaPair : mitochondrialDnaPairs) {
			minimumEditDistanceFinder = new MinimumEditDistanceFinder(dnaPair.getStringA(), dnaPair.getStringB());
			System.out.println("Alignment score serial: " + minimumEditDistanceFinder.getAlignmentScore());
		}
		
		System.out.println("Serial alignment score for 4 pairs took " + (System.currentTimeMillis() - startTime) + " (ms).");
		
		startTime = System.currentTimeMillis();
		ExecutorService editDistanceComputationPool = Executors.newFixedThreadPool(4);
		List<Future<Integer>> alignmentScoreFutures = new ArrayList<Future<Integer>>();
		Future<Integer> alignmentResult = null;
		for (StringPair dnaPair : mitochondrialDnaPairs) {
			alignmentResult = editDistanceComputationPool.submit(new EditDistanceComputationTask(dnaPair.getStringA(), dnaPair.getStringB()));
			alignmentScoreFutures.add(alignmentResult);
		}
		
		for (Future<Integer> future : alignmentScoreFutures) {
			
			try {
				System.out.println("Alignment score: " + future.get().intValue());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			
			
		}
		
		System.out.println("Parallel alignment score for 4 pairs took " + (System.currentTimeMillis() - startTime) + " (ms).");
		
		editDistanceComputationPool.shutdown();
	}
	
	/**
	 * @return a random nucleotide string
	 */
	private String getRandomNucleotideString() {
		
		StringBuffer randomNucleotideString = new StringBuffer();
		//int randomNucleotideStringLength = this.randomNumberGenerator.nextInt(MAXIMUM_NUCLEOTIDE_STRING_LENGTH + 1);
		int randomNucleotideStringLength = MAXIMUM_NUCLEOTIDE_STRING_LENGTH + 1;
		int numberOfValidBases = validBases.length;
		
		for (int nucleotideCounter = 0; nucleotideCounter < randomNucleotideStringLength; ++nucleotideCounter) {
			randomNucleotideString.append(validBases[this.randomNumberGenerator.nextInt(numberOfValidBases)]);
		}
		
		return randomNucleotideString.toString();
		
	}
	
}
