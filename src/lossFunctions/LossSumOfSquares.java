package lossFunctions;

import generalUtilities.Utilities;
import matrices.Vector;

public class LossSumOfSquares implements Loss{

	public static void main(String[] args) {
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
		//System.out.println(actualOutput.getSize());
		
		//System.out.println("actual size "+actualOutput.getSize());
		for(int i=0;i<actualOutput.getSize();i++) {
			difference = actualOutput.get(i)-targetOutput.get(i);
			
			//difference = actualOutput.get(i);
			//difference -= targetOutput.get(i);
			
			output += difference*difference;
			//System.out.print(output+",");
		}
		//System.out.println();
		return output*0.5;
	}

}
