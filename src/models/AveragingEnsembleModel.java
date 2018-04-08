package models;

import dataStructures.DataStep;
import generalUtilities.Utilities;
import lossFunctions.Loss;
import lossFunctions.LossSumOfSquares;
import matrices.Vector;

import java.util.ArrayList;

public class AveragingEnsembleModel implements Model {

	private ArrayList<Model> subModels;
	private ArrayList<Vector> subModelOutputs = new ArrayList<>();
	private int outputSize;
	private int modelSizeMinus1;
	private double[] weights;
	private double[] subModelLosses;
	
	private Loss loss = new LossSumOfSquares();
	
	
	public AveragingEnsembleModel(ArrayList<Model> models, int outputDimension) {
		subModels = models;
		for(int i=0;i<models.size();i++) {
			subModelOutputs.add(new Vector(outputDimension));
		}
		double reciprocalOfModelSize = 1.0 / (double) models.size();
		modelSizeMinus1 = models.size()-1;
		outputSize = outputDimension;
		weights = new double[models.size()];
		for(int i=0;i<models.size();i++) {
			weights[i] = reciprocalOfModelSize;
		}
		subModelLosses = new double[models.size()];
	}
	
	@Override
	public void run(DataStep input, Vector output) {
		for(int i=subModels.size()-1;i>=0;i--) {
			subModels.get(i).run(input, subModelOutputs.get(i));
		}
		
		for(int i=outputSize-1;i>=0;i--) {
			double total=0.0;
			for(int j=modelSizeMinus1;j>=0;j--) {
				total += subModelOutputs.get(j).get(i)* weights[j];
			}
			output.set(i, total);
		}
		


	}

	@Override
	public void runAndDecideImprovements(DataStep input, Vector output, Vector targetOutput) {
		
		for(int i=subModels.size()-1; i >= 0; i--) {
			subModels.get(i).runAndDecideImprovements(input, subModelOutputs.get(i), targetOutput);
			subModelLosses[i] += loss.measureLoss(subModelOutputs.get(i), targetOutput);
		}
		for(int i=outputSize-1;i>=0;i--) {
			double total=0.0;
			for(int j=modelSizeMinus1;j>=0;j--) {
				total += subModelOutputs.get(j).get(i) * weights[j];
			}
			output.set(i, total );
		}
		
	}

	@Override
	public void updateModelParameters(double momentum, double beta1, double beta2, double alpha, double OneMinusBeta1,
									  double OneMinusBeta2) {
		for(Model subModel: subModels) {
			subModel.updateModelParameters(momentum, beta1, beta2, alpha, OneMinusBeta1, OneMinusBeta2);
		}
		double totalLoss = 0.0;
		for(int i=modelSizeMinus1;i>=0;i--) {
			totalLoss += subModelLosses[i];
		}

		for(int i=modelSizeMinus1;i>=0;i--) {
			
			weights[i] = (totalLoss-subModelLosses[i])/(totalLoss*modelSizeMinus1);
			subModelLosses[i] *= momentum;
		}

	}

	@Override
	public void resetState() {
		for(int i=modelSizeMinus1;i>=0;i--) {
			subModelLosses[i] = 0;
		}	
	}

	@Override
	public String provideDescription() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(Utilities.arrayToString(weights)).append("\n\r");
		for (Model subModel : subModels) {
			subModel.provideDescription(stringBuilder);
			stringBuilder.append("\n\r");
		}
		return stringBuilder.toString();
	}

	@Override
	public void provideDescription(StringBuilder stringBuilder) {
		stringBuilder.append(Utilities.arrayToString(weights)).append("\n\r");
		for (Model subModel : subModels) {
			subModel.provideDescription(stringBuilder);
			stringBuilder.append("\n");
		}
	}
}
