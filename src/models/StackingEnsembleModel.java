package models;

import dataStructures.DataStep;
import generalUtilities.CustomRandom;
import matrices.Vector;
import nonlinearityFunctions.RoughTanhUnit;

import java.util.ArrayList;

public class StackingEnsembleModel implements Model {

	private ArrayList<Model> subModels;
	private final Model combiningModel;
	private ArrayList<Vector> subModelOutputs = new ArrayList<>();
	private DataStep combiningModelInput;
	
	
	public StackingEnsembleModel(ArrayList<Model> models, int inputDimension, int outputDimension, CustomRandom util) {
		subModels = models;
		for(int i=0;i<models.size();i++) {
			subModelOutputs.add(new Vector(outputDimension));
		}
		//combiningModel = new NeuralNetworkModel(3,subModels.size()*inputDimension,100,outputDimension,util);
		combiningModel = new FeedForwardLayer(subModels.size()*inputDimension, outputDimension, new RoughTanhUnit(), util);

		combiningModelInput = new DataStep(new Vector(subModels.size() * outputDimension));
		
	}
	
	public StackingEnsembleModel(Model model, ArrayList<Model> models, int inputDimension, int outputDimension, CustomRandom util) {
		subModels = models;
		for(int i=0;i<models.size();i++) {
			subModelOutputs.add(new Vector(outputDimension));
		}
		//combiningModel = new NeuralNetworkModel(3,subModels.size()*inputDimension,100,outputDimension,util);
		combiningModel = model;

		combiningModelInput = new DataStep(new Vector(subModels.size() * outputDimension));
		
	}
	
	@Override
	public void run(DataStep input, Vector output) {
		for(int i=subModels.size()-1;i>=0;i--) {
			subModels.get(i).run(input, subModelOutputs.get(i));
		}
		Vector.concatenateVector(subModelOutputs, combiningModelInput.getInputVector());
		combiningModel.run(combiningModelInput, output);

	}

	@Override
	public void runAndDecideImprovements(DataStep input, Vector output, Vector targetOutput) {
		for(int i=subModels.size()-1;i>=0;i--) {
			subModels.get(i).runAndDecideImprovements(input, subModelOutputs.get(i), targetOutput);
		}
		Vector.concatenateVector(subModelOutputs, combiningModelInput.getInputVector());

		combiningModel.runAndDecideImprovements(combiningModelInput, output, targetOutput);
	}

	@Override
	public void updateModelParameters(double momentum, double beta1, double beta2, double alpha, double OneMinusBeta1,
									  double OneMinusBeta2) {
		for(Model model: subModels) {
			model.updateModelParameters(momentum, beta1, beta2, alpha, OneMinusBeta1, OneMinusBeta2);
		}
		combiningModel.updateModelParameters(momentum, beta1, beta2, alpha, OneMinusBeta1, OneMinusBeta2);

	}

	@Override
	public void resetState() {
		for(Model model: subModels) {
			model.resetState();
		}
		combiningModel.resetState();
		
	}

	@Override
	public String description() {
		StringBuilder stringBuilder = new StringBuilder();
		description(stringBuilder);
		return stringBuilder.toString();
	}

	@Override
	public void description(StringBuilder stringBuilder) {
		stringBuilder.append("Stacking Combining Model \n\r");
		combiningModel.description(stringBuilder);
		stringBuilder.append("\n\r");
		for (Model model : subModels) {
			model.description(stringBuilder);
			stringBuilder.append("\n\r");
		}
	}
}
