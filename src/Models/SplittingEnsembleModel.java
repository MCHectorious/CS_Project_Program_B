package Models;

import DataSplitting.DataSplitOperation;
import DataStructures.DataStep;
import Matrices.Vector;

public class SplittingEnsembleModel implements Model{

	private Model modelInSplit, modelNotInSplit;//the Models to be used based on the data split operation
	private DataSplitOperation split;//decides if the data step follows a certain format

	public SplittingEnsembleModel(Model model1, Model model2, DataSplitOperation splitOp) {
		modelInSplit = model1;
		modelNotInSplit = model2;
		split = splitOp;
	}
	
	
	@Override
	public void run(DataStep input, Vector output) {
		if(split.isInSet(input)) {//decides whether the data step follows a certain format
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
	public String provideDescription() {
		return "Data Split - \t" + split.provideDescription() + "Model for split\n" +
				modelInSplit.provideDescription() + "Model not for split\n" + modelNotInSplit.provideDescription();
	}

	@Override
	public void provideDescription(StringBuilder stringBuilder) {
		stringBuilder.append("Data Split - \t");
		split.provideDescription(stringBuilder);
		stringBuilder.append("Model for split\n");
		modelInSplit.provideDescription(stringBuilder);
		stringBuilder.append("Model not for split\n");
		modelNotInSplit.provideDescription(stringBuilder);
	}
}
