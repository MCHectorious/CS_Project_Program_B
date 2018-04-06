package models;

import java.util.ArrayList;
import java.util.List;

import dataStructures.DataStep;
import matrices.Vector;
import training.DataPreparation;
import generalUtilities.Util;

public class AverageModel implements Model{

	double[] average = new double[DataPreparation.FIXED_VECTOR_SIZE];
	
	public AverageModel(List<DataStep> steps) {
		double[] total = new double[DataPreparation.FIXED_VECTOR_SIZE];
		for(DataStep step: steps) {
			double[] temp = step.getTargetOutput();
			for(int i=0;i<DataPreparation.FIXED_VECTOR_SIZE;i++) {
				total[i] += temp[i];
			}
		}
		for(int i=0;i<DataPreparation.FIXED_VECTOR_SIZE;i++) {
			average[i] = total[i]/(double) steps.size();
		}
	}
	
	@Override
	public void forward(Vector input, Vector output) {
		output.setData(average);
	}

	@Override
	public void forwardWithBackProp(Vector input, Vector output, Vector targetOutput) {
		output.setData(average);
	}

	@Override
	public void getParams(StringBuilder builder) {
		builder.append(Util.arrayToString(average));
	}

	@Override
	public void updateModelParams(double momentum, double beta1, double beta2, double alpha, double OneMinusBeta1,
			double OneMinusBeta2) {
		
	}

	@Override
	public void resetState() {
		
	}

}
