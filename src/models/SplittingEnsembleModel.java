package models;

import dataSplitting.DataSplitOp;
import matrix.Vector;

public class SplittingEnsembleModel implements Model{

	Model modelInSplit, modelNotInSplit;
	DataSplitOp split;
	
	public SplittingEnsembleModel(Model model1, Model model2,  DataSplitOp splitOp) {
		modelInSplit = model1;
		modelNotInSplit = model2;
		
		split = splitOp;
	}
	
	
	@Override
	public void forward(Vector input, Vector output) {
		if(split.isInSet(input)) {
			modelInSplit.forward(input, output);
		}else {
			modelNotInSplit.forward(input, output);
		}
	}

	@Override
	public void forwardWithBackProp(Vector input, Vector output, Vector targetOutput) {
		if(split.isInSet(input)) {
			modelInSplit.forwardWithBackProp(input, output,targetOutput);
		}else {
			modelNotInSplit.forwardWithBackProp(input, output,targetOutput);
		}		
	}

	@Override
	public void getParams(StringBuilder builder) {
		builder.append("Data Split - \t");
		split.toString(builder);
		builder.append("Model for split\n");
		modelInSplit.getParams(builder);
		builder.append("Model not for split\n");
		modelNotInSplit.getParams(builder);
	}

	@Override
	public void updateModelParams(double momentum, double beta1, double beta2, double alpha, double OneMinusBeta1,
			double OneMinusBeta2) {
		modelInSplit.updateModelParams(momentum, beta1, beta2, alpha, OneMinusBeta1, OneMinusBeta2);
		modelNotInSplit.updateModelParams(momentum, beta1, beta2, alpha, OneMinusBeta1, OneMinusBeta2);
	}

	@Override
	public void resetState() {
		modelInSplit.resetState();
		modelNotInSplit.resetState();
	}

}
