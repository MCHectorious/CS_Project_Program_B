package LossFunctions;

import GeneralUtilities.Utilities;
import Matrices.Vector;

public class LossSumOfSquares implements Loss{

	public static void main(String[] args) {//for testing purposes
		double[] actualOutputArray = {0.1,-0.1, -0.9};
        System.out.println("Actual Output: " + Utilities.arrayToString(actualOutputArray));
		Vector actualOutput = new Vector(actualOutputArray );
		double[] targetOutputArray = {0.01,0, -0.89};
        System.out.println("Target Output: " + Utilities.arrayToString(targetOutputArray));
		Vector targetOutput = new Vector(targetOutputArray);
		double loss = new LossSumOfSquares().measureLoss(actualOutput,targetOutput);
		System.out.println("Loss: "+loss);
	}

	@Override
	public double measureLoss(Vector actualOutput, Vector targetOutput) {
		double output = 0;
		double difference;

		for(int i=0;i<actualOutput.getSize();i++) {
			difference = actualOutput.get(i)-targetOutput.get(i);
			output += difference*difference;//using squares means that being too large or small affect the loss the same amount
		}
		return output*0.5;//I multiply by half so that the derivative of teh loss function is just the actual values - the target values
	}

}
