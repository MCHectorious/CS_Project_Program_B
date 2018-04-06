package models;

import java.util.ArrayList;

import matrices.Vector;
import nonlinearityFunctions.RoughTanhUnit;
import generalUtilities.CustomRandom;

public class StackingEnsembleModel implements Model {

	private ArrayList<Model> subModels = new ArrayList<>();
	private final Model combiningModel;
	private ArrayList<Vector> subModelOutputs = new ArrayList<>();
	private Vector combiningModelInput;
	
	
	public StackingEnsembleModel(ArrayList<Model> models, int inputDimension, int outputDimension, CustomRandom util) {
		subModels = models;
		for(int i=0;i<models.size();i++) {
			subModelOutputs.add(new Vector(outputDimension));
		}
		//combiningModel = new NeuralNetwork(3,subModels.size()*inputDimension,100,outputDimension,util);
		combiningModel = new FeedForwardLayer(subModels.size()*inputDimension, outputDimension, new RoughTanhUnit(), util);
		
		combiningModelInput = new Vector(subModels.size()*outputDimension);
		
	}
	
	public StackingEnsembleModel(Model model, ArrayList<Model> models, int inputDimension, int outputDimension, CustomRandom util) {
		subModels = models;
		for(int i=0;i<models.size();i++) {
			subModelOutputs.add(new Vector(outputDimension));
		}
		//combiningModel = new NeuralNetwork(3,subModels.size()*inputDimension,100,outputDimension,util);
		combiningModel = model;
		
		combiningModelInput = new Vector(subModels.size()*outputDimension);
		
	}
	
	@Override
	public void forward(Vector input, Vector output) {
		for(int i=subModels.size()-1;i>=0;i--) {
			subModels.get(i).forward(input, subModelOutputs.get(i) );
		}
		Vector.concatenateVector(subModelOutputs, combiningModelInput);
		combiningModel.forward( combiningModelInput, output);

	}

	@Override
	public void forwardWithBackProp(Vector input, Vector output, Vector targetOutput) {
		for(int i=subModels.size()-1;i>=0;i--) {
			subModels.get(i).forwardWithBackProp(input, subModelOutputs.get(i), targetOutput);
		}
		Vector.concatenateVector(subModelOutputs, combiningModelInput);

		combiningModel.forwardWithBackProp(combiningModelInput , output, targetOutput);
	}

	@Override
	public void getParams(StringBuilder builder) {
		builder.append("Stacking Combining Model \n\r");
		combiningModel.getParams(builder);
		builder.append("\n\r");
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
		combiningModel.updateModelParams(momentum, beta1, beta2, alpha, OneMinusBeta1, OneMinusBeta2);

	}

	@Override
	public void resetState() {
		for(Model model: subModels) {
			model.resetState();
		}
		combiningModel.resetState();;
		
	}

}
