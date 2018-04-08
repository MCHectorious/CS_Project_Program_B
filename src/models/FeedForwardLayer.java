package models;

import dataStructures.DataStep;
import generalUtilities.CustomRandom;
import generalUtilities.Utilities;
import matrices.Matrix;
import matrices.Vector;
import nonlinearityFunctions.NonLinearity;
import nonlinearityFunctions.RoughTanhUnit;

public class FeedForwardLayer implements Layer, Model{

	private Vector biases;
	private NonLinearity nonLinearity;
	private double[] meanForBiases,varianceForBiases;
	private Matrix weights;
	private double[] derivativeOfCostWithRespectToWeight, derivativeOfCostWithRespectToBias, derivativeOfCostWithRespectToOutput;
	private double[] derivativeOfNonLinWithRespectToTotal, derivativeOfCostWithRespectToTotal, derivativeOfCostWithRespectToInput;
	private double[] meanForWeights;
	private double[] varianceForWeights;
	private int inputSize;
	private int outputSize;

	public FeedForwardLayer(double[] weights, double[] bias, NonLinearity nonLinearity) {
		this.weights = new Matrix(weights, bias.length);
		biases = new Vector(bias);
		this.nonLinearity = nonLinearity;
		meanForWeights = new double[weights.length];
		varianceForWeights = new double[weights.length];
		meanForBiases = new double[bias.length];
		varianceForBiases = new double[bias.length];
		inputSize = weights.length/bias.length;
		outputSize = bias.length;
		derivativeOfCostWithRespectToOutput = new double[outputSize];
		derivativeOfCostWithRespectToWeight = new double[weights.length];
		derivativeOfCostWithRespectToBias = new double[outputSize];
		derivativeOfNonLinWithRespectToTotal = new double[outputSize];
		derivativeOfCostWithRespectToTotal= new double[outputSize];
		derivativeOfCostWithRespectToInput= new double[inputSize];
	}
	
	public FeedForwardLayer(int inputDimension, int outputDimension, NonLinearity nonLinearity, CustomRandom random) {
		weights = Matrix.random(inputDimension, outputDimension, random);
		biases = Vector.randomVector(outputDimension, random);
		this.nonLinearity = nonLinearity;
		derivativeOfCostWithRespectToWeight = new double[inputDimension*outputDimension];
		meanForWeights = new double[inputDimension*outputDimension];
		varianceForWeights = new double[inputDimension*outputDimension];
		meanForBiases = new double[outputDimension];
		varianceForBiases = new double[outputDimension];
		inputSize = inputDimension;
		outputSize = outputDimension;
		derivativeOfCostWithRespectToOutput = new double[outputSize];
		derivativeOfCostWithRespectToWeight = new double[inputSize*outputSize];
		derivativeOfCostWithRespectToBias = new double[outputSize];	
		derivativeOfNonLinWithRespectToTotal = new double[outputSize];
		derivativeOfCostWithRespectToTotal= new double[outputSize];
		derivativeOfCostWithRespectToInput= new double[inputSize];

	}

	public static void main(String[] args) {
		double[] weights = {1, -2, 3, -4, 5, -6, 7, -8, 9};
		System.out.println("weights: " + Utilities.arrayToString(weights));
		double[] biases = {1, -2, 3};
		System.out.println("biases: " + Utilities.arrayToString(biases));
		double[] input = {1, 2, 3};
		System.out.println("Input: " + Utilities.arrayToString(input));
		FeedForwardLayer layer = new FeedForwardLayer(weights, biases, new RoughTanhUnit());
		Vector output = new Vector(3);
		DataStep step = new DataStep(input);
		layer.run(step, output);
		double[] outputArray = output.getData();
		System.out.println("Output: " + Utilities.arrayToString(outputArray));
	}
	
	@Override
	public void run(DataStep input, Vector Output) {
		run(input.getInputVector(), Output);
	}

	@Override
	public void runAndDecideImprovements(DataStep input, Vector Output, Vector targetOutput) {
		runWithBackPropagation(input.getInputVector(), Output, targetOutput);

	}

	@Override
	public void runWithBackPropagation(Vector input, Vector output) {
        double total;
        
        for( int i = outputSize-1; i >=0; i-- ) {
            total = biases.get(i);
			int startingPos = i * inputSize;
            for( int j = inputSize-1; j >=0; j-- ) {
            	total += weights.get(startingPos + j) * input.get(j);
			}
			derivativeOfNonLinWithRespectToTotal[i] = nonLinearity.evaluateDerivative(total);
			output.set(i, nonLinearity.evaluate(total));
		}

		for (int i = 0; i < outputSize; i++) {
			int startingPos = i * inputSize;
			for (int j = 0; j < inputSize; j++) {
				derivativeOfCostWithRespectToInput[j] += weights.get(startingPos + j) * derivativeOfCostWithRespectToTotal[i];
			}
		}

	}

	@Override
	public void runWithBackPropagation(Vector input, Vector Output, Vector targetOutput) {
		int index = weights.getSize()-1;
		double total;
		double outputValue;
		for (int i = outputSize - 1; i >= 0; i--) {
			total = biases.get(i);
			for (int j = inputSize - 1; j >= 0; j--) {
				total += weights.get(index--) * input.get(j);
			}
			outputValue = nonLinearity.evaluate(total);
			derivativeOfCostWithRespectToOutput[i] = outputValue - targetOutput.get(i);
			derivativeOfNonLinWithRespectToTotal[i] = nonLinearity.evaluateDerivative(total);
			derivativeOfCostWithRespectToTotal[i] = derivativeOfCostWithRespectToOutput[i] * derivativeOfNonLinWithRespectToTotal[i];
			derivativeOfCostWithRespectToBias[i] += derivativeOfCostWithRespectToTotal[i];
			Output.set(i, outputValue);
		}

		for (int i = 0; i < outputSize; i++) {
			int startingPos = i * inputSize;
			for (int j = 0; j < inputSize; j++) {
				derivativeOfCostWithRespectToWeight[startingPos + j] += input.get(j) * derivativeOfCostWithRespectToTotal[i];
				derivativeOfCostWithRespectToInput[j] += weights.get(startingPos + j) * derivativeOfCostWithRespectToTotal[i];
			}
		}

	}

	@Override
	public void run(Vector input, Vector Output) {
		int index = weights.getSize() - 1;
		double total;
		for (int i = outputSize - 1; i >= 0; i--) {
			total = biases.get(i);
			for (int j = inputSize - 1; j >= 0; j--) {
				total += weights.get(index--) * input.get(j);
			}
			Output.set(i, nonLinearity.evaluate(total));
		}
	}
	



	@Override
	public void updateModelParameters(double momentum, double beta1, double beta2, double alpha, double OneMinusBeta1, double OneMinusBeta2) {
		
		double gradient,value;
		for(int i=0;i<derivativeOfCostWithRespectToWeight.length;i++) {
			gradient = derivativeOfCostWithRespectToWeight[i];
			meanForWeights[i] =  beta1*meanForWeights[i]+OneMinusBeta1*gradient;
			varianceForWeights[i] =  beta2*varianceForWeights[i]+OneMinusBeta2*gradient*gradient ;
			value = alpha*meanForWeights[i]/(Math.sqrt(varianceForWeights[i])+0.00000001);
			weights.addToData(i, -value);
			derivativeOfCostWithRespectToWeight[i] = momentum*value;
		}
		for(int i=0;i<derivativeOfCostWithRespectToBias.length;i++) {
			gradient = derivativeOfCostWithRespectToBias[i];
			
			meanForBiases[i] =  beta1*meanForBiases[i]+OneMinusBeta1*gradient;
			
			varianceForBiases[i] =  beta2*varianceForBiases[i]+OneMinusBeta2*gradient*gradient ;
			
			value = alpha*meanForBiases[i]/(Math.sqrt(varianceForBiases[i])+0.00000001);
			
			
			biases.addToData(i, -value);
			derivativeOfCostWithRespectToBias[i] = momentum*value;
		}
	}

	@Override
	public int getWeightColumns() {
		return outputSize;
	}

	@Override
	public void resetState() {
		for(int i=0;i<derivativeOfCostWithRespectToWeight.length;i++) {
			derivativeOfCostWithRespectToWeight[i] = 0;
		}
		for(int i=0;i<derivativeOfCostWithRespectToBias.length;i++) {
			derivativeOfCostWithRespectToBias[i] = 0;
		}
		
	}


	@Override
	public void backPropagate(Vector input, Vector output, double[] derivativeOfCostWithRespectToInputFromNextLayer) {
		for(int i=0;i<outputSize;i++) {
			double total =0;
        	int startingPos = i*inputSize;

			for(int j=0;j<inputSize;j++) {
				total+= weights.get(startingPos+j)*derivativeOfCostWithRespectToInputFromNextLayer[i];
			}
			derivativeOfCostWithRespectToOutput[i] = total;
			
			derivativeOfCostWithRespectToTotal[i] = total*derivativeOfNonLinWithRespectToTotal[i];
			
			
			derivativeOfCostWithRespectToBias[i] += derivativeOfCostWithRespectToTotal[i];
		}
		
		for(int i=0;i<outputSize;i++) {
			int startingPos = i*inputSize;
			for(int j=0;j<inputSize;j++) {
				derivativeOfCostWithRespectToWeight[startingPos+j] += input.get(j)*derivativeOfCostWithRespectToTotal[i];
				
			}
		}

	}

	@Override
	public double[] getDerivativeWithRespectToInput() {
		return derivativeOfCostWithRespectToInput;
	}


	@Override
	public String provideDescription() {
		StringBuilder stringBuilder = new StringBuilder();
		provideDescription(stringBuilder);
		return stringBuilder.toString();
	}

	@Override
	public void provideDescription(StringBuilder stringBuilder) {
		stringBuilder.append("biases: ");
		biases.provideDescription(stringBuilder);
		stringBuilder.append("\n");
		stringBuilder.append("weights: ");
		weights.provideDescription(stringBuilder);
		stringBuilder.append("\n");
	}
}
