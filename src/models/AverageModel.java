package models;

import dataStructures.DataStep;
import generalUtilities.Utilities;
import matrices.Vector;
import training.DataProcessing;

import java.util.List;

public class AverageModel implements Model{

	private double[] averages = new double[DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR];//The average for each value
	
	public AverageModel(List<DataStep> steps) {
		double[] total = new double[DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR];
		for(DataStep step: steps) {
			double[] temp = step.getTargetOutput();
			for (int i = 0; i < DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR; i++) {
				total[i] += temp[i];
			}
		}
		for (int i = 0; i < DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR; i++) {
			averages[i] = total[i]/(double) steps.size();
		}
	}
	
	@Override
	public void run(DataStep input, Vector output) {
		output.setData(averages);
	}

	@Override
	public void runAndDecideImprovements(DataStep input, Vector output, Vector targetOutput) {
		output.setData(averages);//No improvements will be made
	}


	@Override
	public void updateModelParameters(double momentum, double beta1, double beta2, double alpha, double OneMinusBeta1,
									  double OneMinusBeta2) {
		//No changes will be made to the parameters
	}

	@Override
	public void resetState() {
		
	}

	@Override
	public String provideDescription() {
		return Utilities.arrayToString(averages);
	}


}
