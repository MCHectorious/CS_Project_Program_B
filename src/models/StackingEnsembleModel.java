package models;

import dataStructures.DataStep;
import generalUtilities.CustomRandom;
import matrices.Vector;
import nonlinearityFunctions.RoughTanhUnit;

import java.util.ArrayList;

public class StackingEnsembleModel implements Model {

	private ArrayList<Model> subModels;// the models which are combined
	private final Model combiningModel;// the model which combine the sub-models
	private ArrayList<Vector> subModelOutputs = new ArrayList<>();// created here to avoid creating new objects in a loop
	private DataStep combiningModelInput;// to avoid creating new objects in a loop
	
	
	public StackingEnsembleModel(ArrayList<Model> models, int inputDimension, int outputDimension, CustomRandom util) {
		subModels = models;
		for(int i=0;i<models.size();i++) {
			subModelOutputs.add(new Vector(outputDimension));
		}
		combiningModel = new FeedForwardLayer(subModels.size()*inputDimension, outputDimension, new RoughTanhUnit(), util);//defaults to using a feed-forward layers to ombine them

		combiningModelInput = new DataStep(new Vector(subModels.size() * outputDimension));
		
	}
	
	public StackingEnsembleModel(Model model, ArrayList<Model> models, int inputDimension, int outputDimension, CustomRandom util) {
		subModels = models;
		for(int i=0;i<models.size();i++) {
			subModelOutputs.add(new Vector(outputDimension));
		}
		combiningModel = model;

		combiningModelInput = new DataStep(new Vector(subModels.size() * outputDimension));
		
	}
	
	@Override
	public void run(DataStep input, Vector output) {
		for(int i=subModels.size()-1;i>=0;i--) {
			subModels.get(i).run(input, subModelOutputs.get(i));
		}
		Vector.concatenateVector(subModelOutputs, combiningModelInput.getInputVector());// puts all the intermediate outputs in one vector
		combiningModel.run(combiningModelInput, output);

	}

	@Override
	public void runAndDecideImprovements(DataStep input, Vector output, Vector targetOutput) {
		for(int i=subModels.size()-1;i>=0;i--) {
			subModels.get(i).runAndDecideImprovements(input, subModelOutputs.get(i), targetOutput);
		}
		Vector.concatenateVector(subModelOutputs, combiningModelInput.getInputVector());// puts all the intermediate outputs in one vector

		combiningModel.runAndDecideImprovements(combiningModelInput, output, targetOutput);
	}

	@Override
	public void updateModelParameters(double momentum, double beta1, double beta2, double alpha, double OneMinusBeta1,
									  double OneMinusBeta2) {
		for(Model subModel: subModels) {
			subModel.updateModelParameters(momentum, beta1, beta2, alpha, OneMinusBeta1, OneMinusBeta2);
		}
		combiningModel.updateModelParameters(momentum, beta1, beta2, alpha, OneMinusBeta1, OneMinusBeta2);

	}

	@Override
	public void resetState() {
		for(Model subModel: subModels) {
			subModel.resetState();
		}
		combiningModel.resetState();
		
	}

	@Override
	public String provideDescription() {
		StringBuilder stringBuilder = new StringBuilder();
		provideDescription(stringBuilder);
		return stringBuilder.toString();
	}

	@Override
	public void provideDescription(StringBuilder stringBuilder) {
		stringBuilder.append("Stacking Combining Model \n\r");
		combiningModel.provideDescription(stringBuilder);
		stringBuilder.append("\n\r");
		for (Model model : subModels) {
			model.provideDescription(stringBuilder);
			stringBuilder.append("\n\r");
		}
	}
}
