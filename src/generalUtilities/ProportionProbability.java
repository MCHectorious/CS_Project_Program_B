package generalUtilities;

public class ProportionProbability {

	private double[] valuesForProbs;
	private char[] outputs;
	
	public ProportionProbability(double[] probs, char[] out) {
		valuesForProbs = probs;
		outputs = out;
	}
	
	public char execute(CustomRandom random) {
		double value = random.randomDouble();
		for(int i=valuesForProbs.length-1;i>=0;i--) {
			if(value<valuesForProbs[i]) {
				return outputs[i];
			}
		}
		return ' ';
	}
}
