package models;

import dataSplitting.DataSplitOperation;
import dataStructures.DataStep;
import matrices.Vector;

public class SplittingEnsembleModel implements Model{

	private Model modelInSplit, modelNotInSplit;
	private DataSplitOperation split;

	public SplittingEnsembleModel(Model model1, Model model2, DataSplitOperation splitOp) {
		modelInSplit = model1;
		modelNotInSplit = model2;
		
		split = splitOp;
	}
	
	
	@Override
	public void run(DataStep input, Vector output) {
		if(split.isInSet(input)) {
			modelInSplit.run(input, output);
		}else {
			modelNotInSplit.run(input, output);
		}
	}

	@Override
	public void runAndDecideImprovements(DataStep input, Vector output, Vector targetOutput) {
		if(split.isInSet(input)) {
			modelInSplit.runAndDecideImprovements(input, output, targetOutput);
		}else {
			modelNotInSplit.runAndDecideImprovements(input, output, targetOutput);
		}		
	}

	@Override
	public void updateModelParameters(double momentum, double beta1, double beta2, double alpha, double OneMinusBeta1,
									  double OneMinusBeta2) {
		modelInSplit.updateModelParameters(momentum, beta1, beta2, alpha, OneMinusBeta1, OneMinusBeta2);
		modelNotInSplit.updateModelParameters(momentum, beta1, beta2, alpha, OneMinusBeta1, OneMinusBeta2);
	}

	@Override
	public void resetState() {
		modelInSplit.resetState();
		modelNotInSplit.resetState();
	}

	@Override
	public String description() {
		return "Data Split - \t" + split.description() + "Model for split\n" +
				modelInSplit.description() + "Model not for split\n" + modelNotInSplit.description();
	}

	@Override
	public void description(StringBuilder stringBuilder) {
		stringBuilder.append("Data Split - \t");
		split.description(stringBuilder);
		stringBuilder.append("Model for split\n");
		modelInSplit.description(stringBuilder);
		stringBuilder.append("Model not for split\n");
		modelNotInSplit.description(stringBuilder);
	}
}
