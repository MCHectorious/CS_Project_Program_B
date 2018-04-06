package models;

import java.util.ArrayList;

import lossFunctions.Loss;
import lossFunctions.LossSumOfSquares;
import matrices.Vector;
import generalUtilities.Util;

public class AveragingEnsembleModel implements Model {

	private ArrayList<Model> subModels = new ArrayList<>();
	private ArrayList<Vector> subModelOutputs = new ArrayList<>();
	private int outputSize;
	private double reciprocalOfModelSize;
	private int modelSizeMinus1;
	private double[] weightings;
	private double[] subModelLosses;
	
	private Loss loss = new LossSumOfSquares();
	
	
	public AveragingEnsembleModel(ArrayList<Model> models, int outputDimension) {
		subModels = models;
		for(int i=0;i<models.size();i++) {
			subModelOutputs.add(new Vector(outputDimension));
		}
		reciprocalOfModelSize = 1.0/(double)models.size();
		//System.out.println(reciprocalOfModelSize+" "+subModels.size());
		modelSizeMinus1 = models.size()-1;
		outputSize = outputDimension;
		weightings = new double[models.size()];
		for(int i=0;i<models.size();i++) {
			weightings[i] = reciprocalOfModelSize;
		}
		subModelLosses = new double[models.size()];
	}
	
	@Override
	public void forward(Vector input, Vector output) {
		//System.out.println("forward");
		for(int i=subModels.size()-1;i>=0;i--) {
			subModels.get(i).forward(input, subModelOutputs.get(i) );
		}
		
		for(int i=outputSize-1;i>=0;i--) {
			double total=0.0;
			for(int j=modelSizeMinus1;j>=0;j--) {
				//System.out.println(subModelOutputs.get(j).get(i)+"*"+weightings[j]);
				total += subModelOutputs.get(j).get(i)*weightings[j];
			}
			output.set(i, total);
		}
		


	}

	@Override
	public void forwardWithBackProp(Vector input, Vector output, Vector targetOutput) {
		
		for(int i=subModels.size()-1; i >= 0; i--) {
			subModels.get(i).forwardWithBackProp(input, subModelOutputs.get(i) , targetOutput);
			subModelLosses[i] += loss.measure(subModelOutputs.get(i), targetOutput);
		}
		for(int i=outputSize-1;i>=0;i--) {
			//System.out.print(i);
			double total=0.0;
			for(int j=modelSizeMinus1;j>=0;j--) {
				total += subModelOutputs.get(j).get(i) * weightings[j];
				///System.out.print(total+" ");
			}
			//System.out.println();
			output.set(i, total );
		}
		
	}

	@Override
	public void getParams(StringBuilder builder) {
		builder.append(Util.arrayToString(weightings)).append("\n\r");
		for(Model model:subModels) {
			model.getParams(builder);
			builder.append("\n\r");
		}

	}

	@Override
	public void updateModelParams(double momentum, double beta1, double beta2, double alpha, double OneMinusBeta1,
			double OneMinusBeta2) {
		for(Model model: subModels) {
			model.updateModelParams(momentum, beta1, beta2, alpha, OneMinusBeta1, OneMinusBeta2);
		}
		double totalLoss = 0.0;
		for(int i=modelSizeMinus1;i>=0;i--) {
			//System.out.print(subModelLosses[i]+" ");
			//subModelLosses[i] *= subModelLosses[i];
			totalLoss += subModelLosses[i];
		}
		//System.out.println();
		//System.out.println(totalLoss);
		for(int i=modelSizeMinus1;i>=0;i--) {
			
			weightings[i] = (totalLoss-subModelLosses[i])/(totalLoss*modelSizeMinus1);
			//System.out.print(weightings[i]+" ");
			//System.out.println(subModelLosses[i]+" "+ weightings[i]);
			
			subModelLosses[i] *= momentum;
		}
		
		//System.out.println();

	}

	@Override
	public void resetState() {
		for(int i=modelSizeMinus1;i>=0;i--) {
			subModelLosses[i] = 0;
		}	
	}

}
