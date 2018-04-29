package Models;

import DataStructures.DataStep;
import GeneralUtilities.CustomRandom;
import Matrices.Vector;
import NonlinearityFunctions.NonLinearity;
import NonlinearityFunctions.RoughTanhUnit;

import java.util.ArrayList;

public class NeuralNetworkModel implements Model {

	private ArrayList<Layer>  layers = new ArrayList<>();//The layers of th neural network
	private ArrayList<Vector> layerOutputs = new ArrayList<>();//Created here to avoid void creating objects in a loop

	public NeuralNetworkModel(int numOfLayers, int inputDimension, int hiddenDimension, int outputDimension, CustomRandom util) {
		RoughTanhUnit roughTanhUnit = new RoughTanhUnit();//Goes between -1 and 1
		layers.add( new FeedForwardLayer(inputDimension, hiddenDimension, roughTanhUnit, util) );//Defaults to feed-forward layer
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
		if(option == 0) {//Should be feed-forward layer
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
		layers.get(i).run(input, layerOutputs.get(i));//Runs the first layer
		for(; i<layerOutputs.size()-1;i++) {
			layers.get(i + 1).run(layerOutputs.get(i), layerOutputs.get(i + 1));//Runs all layers except the first and last layer
		}
		layers.get(i + 1).run(layerOutputs.get(i), output);//Runs the last layer
	}

	@Override
	public void runAndDecideImprovements(DataStep input, Vector output, Vector targetOutput) {
		int i = 0;
		layers.get(i).runWithBackPropagation(input.getInputVector(), layerOutputs.get(i));//Runs the first layer and decides how to update the model parameters
		for(; i<layerOutputs.size()-1;i++) {
			layers.get(i + 1).runWithBackPropagation(layerOutputs.get(i), layerOutputs.get(i + 1));//Runs all layers except the first and last and decides how to update the model parameters
		}
		layers.get(i + 1).runWithBackPropagation(layerOutputs.get(i), output, targetOutput);//Runs the last layer and decides how to update the model parameters

		for(;i>0;i--) {
			layers.get(i).backPropagate( layerOutputs.get(i-1) , layerOutputs.get(i), layers.get(i+1).getDerivativeWithRespectToInput());//Uses the next layers to calculate thow the model parameters should be updated
		}
		layers.get(0).backPropagate(input.getInputVector(), layerOutputs.get(0), layers.get(1).getDerivativeWithRespectToInput());//Uses the next layers to calculate thow the model parameters should be updated


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
