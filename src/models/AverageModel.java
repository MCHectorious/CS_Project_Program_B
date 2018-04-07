package models;

import dataStructures.DataStep;
import generalUtilities.Utilities;
import matrices.Vector;
import training.DataProcessing;

import java.util.List;

public class AverageModel implements Model{

	private double[] average = new double[DataProcessing.FIXED_VECTOR_SIZE];
	
	public AverageModel(List<DataStep> steps) {
		double[] total = new double[DataProcessing.FIXED_VECTOR_SIZE];
		for(DataStep step: steps) {
			double[] temp = step.getTargetOutput();
			for (int i = 0; i < DataProcessing.FIXED_VECTOR_SIZE; i++) {
				total[i] += temp[i];
			}
		}
		for (int i = 0; i < DataProcessing.FIXED_VECTOR_SIZE; i++) {
			average[i] = total[i]/(double) steps.size();
		}
	}
	
	@Override
	public void run(DataStep input, Vector output) {
		output.setData(average);
	}

	@Override
	public void runAndDecideImprovements(DataStep input, Vector output, Vector targetOutput) {
		output.setData(average);
	}


	@Override
	public void updateModelParameters(double momentum, double beta1, double beta2, double alpha, double OneMinusBeta1,
									  double OneMinusBeta2) {
		
	}

	@Override
	public void resetState() {
		
	}

	@Override
	public String description() {
		return Utilities.arrayToString(average);
	}


}
