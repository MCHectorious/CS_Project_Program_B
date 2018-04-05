package models;

import matrix.Vector;

public class BasicCopying implements Model {

	@Override
	public void forward(Vector input, Vector output) {
		for(int i = input.getSize()-1;i>=0;i--) {
			output.set(i, input.get(i));
		}
		
	}

	@Override
	public void forwardWithBackProp(Vector input, Vector output, Vector targetOutput) {
		forward(input,output);
		
	}


	@Override
	public void getParams(StringBuilder builder) {
		builder.append("Copy Model\n");
		
	}

	@Override
	public void updateModelParams(double momentum, double beta1, double beta2, double alpha, double OneMinusBeta1,
			double OneMinusBeta2) {
		
	}

	@Override
	public void resetState() {
		
	}

}
