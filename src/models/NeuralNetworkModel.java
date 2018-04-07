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
		RoughTanhUnit sig = new RoughTanhUnit();
		layers.add( new FeedForwardLayer(inputDimension, hiddenDimension, sig, util) );
		layerOutputs.add(new Vector(hiddenDimension));
		for(int i = 0; i<numOfLayers-2;i++) {
			layers.add(new FeedForwardLayer(hiddenDimension,hiddenDimension,sig,util));
			layerOutputs.add(new Vector(hiddenDimension));
		}
		layers.add(new FeedForwardLayer(hiddenDimension, outputDimension, sig,util));
				
	}
	
	/*public NeuralNetworkModel(int numOfLayers, int inputDimension, int hiddenDimension, int outputDimension,CustomRandom util ) {
		//RoughTanhUnit sig = new RoughTanhUnit();
		layers.add( new LinearLayer(inputDimension, hiddenDimension, util) );
		layerOutputs.add(new Vector(hiddenDimension));
		for(int i = 0; i<numOfLayers-2;i++) {
			layers.add(new LinearLayer(hiddenDimension,hiddenDimension, util));
			layerOutputs.add(new Vector(hiddenDimension));
		}
		layers.add(new LinearLayer(hiddenDimension, outputDimension, util));
				
	}*/

	public NeuralNetworkModel(int[] layerTypes, int inputDimension, int[] hiddenDimensions, int outputDimension, CustomRandom random) {
		RoughTanhUnit tanh = new RoughTanhUnit();
		chooseLayer(layerTypes[0],inputDimension,hiddenDimensions[0],tanh,random);
		layerOutputs.add(new Vector(hiddenDimensions[0]));
		int i =1;
		for(;i<hiddenDimensions.length;i++) {
			chooseLayer(layerTypes[i],hiddenDimensions[i-1],hiddenDimensions[i],tanh,random);
			layerOutputs.add(new Vector(hiddenDimensions[i]));
		}
		//System.out.print(i);
		chooseLayer(layerTypes[i],hiddenDimensions[i-1],outputDimension,tanh,random);
	}
	
	private void chooseLayer(int option, int in, int out, NonLinearity sigmoid, CustomRandom util) {
		if(option == 0) {
			layers.add( new FeedForwardLayer(in, out, sigmoid, util) );
		}else {
			layers.add( new LinearLayer(in, out, util) );

		}
	}
	
	
	/*public NeuralNetworkModel(boolean[] linearLayers, int inputDimension, int hiddenDimension, int outputDimension, CustomRandom rand) {
		RoughTanhUnit sigmoid =  new RoughTanhUnit();
		
		layers.add(e);
	}*/


	public NeuralNetworkModel(ArrayList<Layer> layersList) {
		layers = layersList;
		for (int i = 0; i < layersList.size() - 1; i++) {
			layerOutputs.add(new Vector(layersList.get(i).getWeightCols()));
		}
	}

	@Override
	public void run(DataStep input, Vector output) {
		int i = 0;
		//System.out.println(i+": "+input.getSize()+" , "+layers.get(i).getWeightRows()+" , "+layerOutputs.get(0).getSize()+", "+layers.get(i).getWeightCols());
		layers.get(i).run(input, layerOutputs.get(i));
		for(; i<layerOutputs.size()-1;i++) {
			//System.out.println(i+": "+layerOutputs.get(i).getSize()+" , "+layers.get(i+1).getWeightRows()+" , "+layerOutputs.get(i+1).getSize()+", "+layers.get(i+1).getWeightCols());
			layers.get(i + 1).run(layerOutputs.get(i), layerOutputs.get(i + 1));
		}
		//i++;
		//System.out.println(i+":- "+layerOutputs.get(i).getSize()+" , "+layers.get(i+1).getWeightRows()+" , "+output.getSize()+", "+layers.get(i+1).getWeightCols());

		layers.get(i + 1).run(layerOutputs.get(i), output);
	}

	@Override
	public void runAndDecideImprovements(DataStep input, Vector output, Vector targetOutput) {
		int i = 0;
		layers.get(i).runWithBackProp(input.getInputVector(), layerOutputs.get(i));
		for(; i<layerOutputs.size()-1;i++) {

			layers.get(i + 1).runWithBackProp(layerOutputs.get(i), layerOutputs.get(i + 1));
			//System.out.println(i);
		}
		//System.out.println(i);
		layers.get(i + 1).runWithBackProp(layerOutputs.get(i), output, targetOutput);



		for(;i>0;i--) {
			layers.get(i).backProp( layerOutputs.get(i-1) , layerOutputs.get(i), layers.get(i+1).getDerivativeWithRespectToInput());
		}
		layers.get(0).backProp(input.getInputVector(), layerOutputs.get(0), layers.get(1).getDerivativeWithRespectToInput());


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
	public String description() {
		StringBuilder stringBuilder = new StringBuilder();
		description(stringBuilder);
		return stringBuilder.toString();
	}

	@Override
	public void description(StringBuilder stringBuilder) {
		for(int i=0;i<layers.size();i++) {
			stringBuilder.append("Layer ").append(i).append(": \n");
			layers.get(i).description(stringBuilder);
		}
	}
}
