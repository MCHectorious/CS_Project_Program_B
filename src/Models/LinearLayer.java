package Models;

import DataStructures.DataStep;
import GeneralUtilities.CustomRandom;
import Matrices.Matrix;
import Matrices.Vector;

public class LinearLayer implements Layer, Model{


	private Matrix weights;//For the matrix multiplication with the input
	private double[] derivativeOfCostWithRespectToWeight, derivativeOfCostWithRespectToOutput;//For calculating how to improve the parameters
	private double[] derivativeOfCostWithRespectToTotal, derivativeOfCostWithRespectToInput;//For calculating how to improve the parameters
	private double[] meanForWeights, varianceForWeights;//Used to determine how to improve the values of weights
	private int inputSize;
	private int outputSize;
	
	public LinearLayer(double[] weights, int outputDimension) {
		this.weights = new Matrix(weights, outputDimension);
		meanForWeights = new double[weights.length];
		varianceForWeights = new double[weights.length];
		inputSize = weights.length/outputDimension;
		outputSize = outputDimension;
		derivativeOfCostWithRespectToOutput = new double[outputSize];
		derivativeOfCostWithRespectToWeight = new double[weights.length];
		derivativeOfCostWithRespectToTotal= new double[outputSize];
		derivativeOfCostWithRespectToInput= new double[inputSize];

	}
	
	public LinearLayer(int inputDimension, int outputDimension, CustomRandom util) {
		weights = Matrix.random(inputDimension, outputDimension, util);

		derivativeOfCostWithRespectToWeight = new double[inputDimension*outputDimension];
		meanForWeights = new double[inputDimension*outputDimension];
		varianceForWeights = new double[inputDimension*outputDimension];
		inputSize = inputDimension;
		outputSize = outputDimension;
		
		derivativeOfCostWithRespectToOutput = new double[outputSize];
		derivativeOfCostWithRespectToWeight = new double[inputSize*outputSize];
		derivativeOfCostWithRespectToTotal= new double[outputSize];
		derivativeOfCostWithRespectToInput= new double[inputSize];

	}

	
	@Override
	public void run(DataStep input, Vector Output) {
		run(input.getInputVector(), Output);//Uses the overloaded function with vectors for faster computation
	}

	@Override
	public void runAndDecideImprovements(DataStep input, Vector Output, Vector targetOutput) {
		runWithBackPropagation(input.getInputVector(), Output, targetOutput);//Uses the overloaded function with vectors for faster computation
	}

	@Override
	public void runWithBackPropagation(Vector input, Vector output) {
        double total;
        for( int i = outputSize-1; i >=0; i-- ) {
            total = 0;
			int startingPos = i * inputSize;
            for( int j = inputSize-1; j >=0; j-- ) {
				total += weights.get(startingPos + j) * input.get(j);//Matrix multiplication of weights and input
			}

			output.set(i, total);
		}
		for (int i = 0; i < outputSize; i++) {
			int startingPos = i * inputSize;
			for (int j = 0; j < inputSize; j++) {
				derivativeOfCostWithRespectToInput[j] += weights.get(startingPos + j);//In order to decide how to update the value
			}
		}
	}

	@Override
	public void runWithBackPropagation(Vector input, Vector Output, Vector targetOutput) {
		int index = weights.getSize()-1;
		double total;
		for (int i = outputSize - 1; i >= 0; i--) {
			total = 0;
			for (int j = inputSize - 1; j >= 0; j--) {
				total += weights.get(index--) * input.get(j);//matrix multiplication
			}
			derivativeOfCostWithRespectToOutput[i] = total - targetOutput.get(i);//Derivative of cost (sum of squares)
			derivativeOfCostWithRespectToTotal[i] = derivativeOfCostWithRespectToOutput[i];//In order to decide how to update the value
			Output.set(i, total);
		}

		for (int i = 0; i < outputSize; i++) {
			int startingPos = i * inputSize;
			for (int j = 0; j < inputSize; j++) {
				derivativeOfCostWithRespectToWeight[startingPos + j] += input.get(j) * derivativeOfCostWithRespectToTotal[i];//In order to decide how to update the value
				derivativeOfCostWithRespectToInput[j] += weights.get(startingPos + j) * derivativeOfCostWithRespectToTotal[i];//In order to decide how to update the value

			}
		}

	}

	@Override
	public void run(Vector input, Vector Output) {
		int index = weights.getSize() - 1;
		double total;
		for (int i = outputSize - 1; i >= 0; i--) {
			total = 0;
			for (int j = inputSize - 1; j >= 0; j--) {
				total += weights.get(index--) * input.get(j);//Matrix multiplication
			}
			Output.set(i, total);
		}
	}

	@Override
	public void updateModelParameters(double momentum, double beta1, double beta2, double alpha, double OneMinusBeta1, double OneMinusBeta2) {
		
		double gradient,value;
		for(int i=0;i<derivativeOfCostWithRespectToWeight.length;i++) {//Uses ADAM optimiser
			gradient = derivativeOfCostWithRespectToWeight[i];
			meanForWeights[i] =  beta1*meanForWeights[i]+OneMinusBeta1*gradient;
			varianceForWeights[i] =  beta2*varianceForWeights[i]+OneMinusBeta2*gradient*gradient ;
			
			value = alpha*meanForWeights[i]/(Math.sqrt(varianceForWeights[i])+0.00000001);//Adds a small value (called epsilon) to avoid dividing by 0
			
			weights.addToData(i, -value);
			derivativeOfCostWithRespectToWeight[i] = momentum*value;//Decides how much the current derivative depends the previous derivative
			
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
		
	}
	
	@Override
	public void backPropagate(Vector input, Vector output, double[] derivativeOfCostWithRespectToInputFromNextLayer) {
		
		for(int i=0;i<outputSize;i++) {
			double total =0;
        	int startingPosition = i*inputSize;

			for(int j=0;j<inputSize;j++) {
				total+= weights.get(startingPosition+j)*derivativeOfCostWithRespectToInputFromNextLayer[i];//Matrix multiplication
			}
			derivativeOfCostWithRespectToOutput[i] = total;
			derivativeOfCostWithRespectToTotal[i] = total;			
		}
		
		for(int i=0;i<outputSize;i++) {
			int startingPos = i*inputSize;
			for(int j=0;j<inputSize;j++) {
				derivativeOfCostWithRespectToWeight[startingPos+j] += input.get(j)*derivativeOfCostWithRespectToTotal[i];
        		derivativeOfCostWithRespectToInput[j] += weights.get(startingPos+j)*derivativeOfCostWithRespectToTotal[i];//For previous layers

			}
		}
		
	}


	@Override
	public double[] getDerivativeWithRespectToInput() {
		return derivativeOfCostWithRespectToInput;
	}


	@Override
	public String provideDescription() {
		return "weights: " + weights.provideDescription();
	}

	@Override
	public void provideDescription(StringBuilder stringBuilder) {
		stringBuilder.append("weights: ");
		weights.provideDescription(stringBuilder);
	}
}
