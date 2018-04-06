package lossFunctions;

import matrices.Vector;
import generalUtilities.Util;

public class LossSumOfSquares implements Loss{

	public static void main(String[] args) {
		double[] actualOutputArray = {0.1,-0.1, -0.9};
		System.out.println("Actual Output: "+Util.arrayToString(actualOutputArray));
		Vector actualOutput = new Vector(actualOutputArray );
		double[] targetOutputArray = {0.01,0, -0.89};
		System.out.println("Target Output: "+Util.arrayToString(targetOutputArray));		
		Vector targetOutput = new Vector(targetOutputArray);
		double loss = new LossSumOfSquares().measure(actualOutput,targetOutput);
		System.out.println("Loss: "+loss);
	}

	@Override
	public double measure(Vector actualOutput, Vector targetOutput) {
		double output = 0;
		double errorDelta;
		//System.out.println(actualOutput.getSize());
		
		//System.out.println("actual size "+actualOutput.getSize());
		for(int i=0;i<actualOutput.getSize();i++) {
			errorDelta = actualOutput.get(i)-targetOutput.get(i);
			
			//errorDelta = actualOutput.get(i);
			//errorDelta -= targetOutput.get(i);
			
			output += errorDelta*errorDelta;
			//System.out.print(output+",");
		}
		//System.out.println();
		return output*0.5;
	}

}
