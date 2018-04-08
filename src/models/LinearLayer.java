package models;

import dataStructures.DataStep;
import generalUtilities.CustomRandom;
import matrices.Matrix;
import matrices.Vector;

public class LinearLayer implements Layer, Model{


	private Matrix weights;
	private double[] derivativeOfCostWithRespectToWeight, derivativeOfCostWithRespectToOutput;
	private double[] derivativeOfCostWithRespectToTotal, derivativeOfCostWithRespectToInput;
	private double[] meanForWeights;
	private double[] varianceForWeights;
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
            total = 0;
			int startingPos = i * inputSize;
            for( int j = inputSize-1; j >=0; j-- ) {
				total += weights.get(startingPos + j) * input.get(j);
			}

			output.set(i, total);
		}
		for (int i = 0; i < outputSize; i++) {
			int startingPos = i * inputSize;
			for (int j = 0; j < inputSize; j++) {
				derivativeOfCostWithRespectToInput[j] += weights.get(startingPos + j);
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
				total += weights.get(index--) * input.get(j);
			}
			derivativeOfCostWithRespectToOutput[i] = total - targetOutput.get(i);
			derivativeOfCostWithRespectToTotal[i] = derivativeOfCostWithRespectToOutput[i];
			Output.set(i, total);
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
		//System.out.print("Doing run pass");
		int index = weights.getSize() - 1;
		double total;
		for (int i = outputSize - 1; i >= 0; i--) {
			total = 0;
			for (int j = inputSize - 1; j >= 0; j--) {
				total += weights.get(index--) * input.get(j);
			}
			Output.set(i, total);
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
				total+= weights.get(startingPosition+j)*derivativeOfCostWithRespectToInputFromNextLayer[i];
			}
			derivativeOfCostWithRespectToOutput[i] = total;
			derivativeOfCostWithRespectToTotal[i] = total;			
		}
		
		for(int i=0;i<outputSize;i++) {
			int startingPos = i*inputSize;
			for(int j=0;j<inputSize;j++) {
				derivativeOfCostWithRespectToWeight[startingPos+j] += input.get(j)*derivativeOfCostWithRespectToTotal[i];
        		derivativeOfCostWithRespectToInput[j] += weights.get(startingPos+j)*derivativeOfCostWithRespectToTotal[i];

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
