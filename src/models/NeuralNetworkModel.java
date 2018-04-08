package models;

import dataStructures.DataStep;
import generalUtilities.CustomRandom;
import matrices.Vector;
import nonlinearityFunctions.NonLinearity;
import nonlinearityFunctions.RoughTanhUnit;

import java.util.ArrayList;

public class NeuralNetworkModel implements Model {

	private ArrayList<Layer>  layers = new ArrayList<>();
	private ArrayList<Vector> layerOutputs = new ArrayList<>();

	public NeuralNetworkModel(int numOfLayers, int inputDimension, int hiddenDimension, int outputDimension, CustomRandom util) {
		RoughTanhUnit roughTanhUnit = new RoughTanhUnit();
		layers.add( new FeedForwardLayer(inputDimension, hiddenDimension, roughTanhUnit, util) );
		layerOutputs.add(new Vector(hiddenDimension));
		for(int i = 0; i<numOfLayers-2;i++) {
			layers.add(new FeedForwardLayer(hiddenDimension,hiddenDimension,roughTanhUnit,util));
			layerOutputs.add(new Vector(hiddenDimension));
		}
		layers.add(new FeedForwardLayer(hiddenDimension, outputDimension, roughTanhUnit,util));
				
	}

	public NeuralNetworkModel(int[] layerTypes, int inputDimension, int[] hiddenDimensions, int outputDimension, CustomRandom random) {
		RoughTanhUnit roughTanhUnit = new RoughTanhUnit();
		chooseLayer(layerTypes[0],inputDimension,hiddenDimensions[0],roughTanhUnit,random);
		layerOutputs.add(new Vector(hiddenDimensions[0]));
		int i =1;
		for(;i<hiddenDimensions.length;i++) {
			chooseLayer(layerTypes[i],hiddenDimensions[i-1],hiddenDimensions[i],roughTanhUnit,random);
			layerOutputs.add(new Vector(hiddenDimensions[i]));
		}
		chooseLayer(layerTypes[i],hiddenDimensions[i-1],outputDimension,roughTanhUnit,random);
	}
	
	private void chooseLayer(int option, int in, int out, NonLinearity nonLinearity, CustomRandom random) {
		if(option == 0) {
			layers.add( new FeedForwardLayer(in, out, nonLinearity, random) );
		}else {
			layers.add( new LinearLayer(in, out, random) );

		}
	}

	public NeuralNetworkModel(ArrayList<Layer> layers) {
		this.layers = layers;
		for (int i = 0; i < layers.size() - 1; i++) {
			layerOutputs.add(new Vector(layers.get(i).getWeightColumns()));
		}
	}

	@Override
	public void run(DataStep input, Vector output) {
		int i = 0;
		layers.get(i).run(input, layerOutputs.get(i));
		for(; i<layerOutputs.size()-1;i++) {
			layers.get(i + 1).run(layerOutputs.get(i), layerOutputs.get(i + 1));
		}
		layers.get(i + 1).run(layerOutputs.get(i), output);
	}

	@Override
	public void runAndDecideImprovements(DataStep input, Vector output, Vector targetOutput) {
		int i = 0;
		layers.get(i).runWithBackPropagation(input.getInputVector(), layerOutputs.get(i));
		for(; i<layerOutputs.size()-1;i++) {
			layers.get(i + 1).runWithBackPropagation(layerOutputs.get(i), layerOutputs.get(i + 1));
		}
		layers.get(i + 1).runWithBackPropagation(layerOutputs.get(i), output, targetOutput);

		for(;i>0;i--) {
			layers.get(i).backPropagate( layerOutputs.get(i-1) , layerOutputs.get(i), layers.get(i+1).getDerivativeWithRespectToInput());
		}
		layers.get(0).backPropagate(input.getInputVector(), layerOutputs.get(0), layers.get(1).getDerivativeWithRespectToInput());


	}
	
	@Override
	public void updateModelParameters(double momentum, double beta1, double beta2, double alpha, double OneMinusBeta1, double OneMinusBeta2) {
		for(int i=layers.size()-1;i>=0;i--) {
			layers.get(i).updateModelParameters(momentum, beta1, beta2, alpha, OneMinusBeta1, OneMinusBeta2);
		}

	}

	@Override
	public void resetState() {
		for (Layer layer : layers) {
			layer.resetState();
		}
	}


	@Override
	public String provideDescription() {
		StringBuilder stringBuilder = new StringBuilder();
		provideDescription(stringBuilder);
		return stringBuilder.toString();
	}

	@Override
	public void provideDescription(StringBuilder stringBuilder) {
		for(int i=0;i<layers.size();i++) {
			stringBuilder.append("Layer ").append(i).append(": \n");
			layers.get(i).provideDescription(stringBuilder);
		}
	}
}
