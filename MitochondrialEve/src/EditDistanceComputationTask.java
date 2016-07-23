import java.util.concurrent.Callable;


public class EditDistanceComputationTask implements Callable<Integer> {

	private String stringA;
	private String stringB;
	
	public EditDistanceComputationTask(String stringA, String stringB) {
		this.stringA = stringA;
		this.stringB = stringB;
	}
	
	@Override
	public Integer call() throws Exception {
		MinimumEditDistanceFinder minimumEditDistanceFinder = new MinimumEditDistanceFinder(stringA, stringB);
		return Integer.valueOf(minimumEditDistanceFinder.getAlignmentScore());
	}

}
