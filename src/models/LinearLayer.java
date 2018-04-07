package models;

import dataStructures.DataStep;
import generalUtilities.CustomRandom;
import matrices.Matrix;
import matrices.Vector;

public class LinearLayer implements Layer, Model{


	private Matrix Weights;
	private double[] derivativeOfCostWithRespectToWeight, derivativeOfCostWithRespectToOutput;
	private double[] derivativeOfCostWithRespectToTotal, derivativeOfCostWithRespectToInput;
	private double[] meanForWeights;
	private double[] varianceForWeights;
	private int inputSize;
	private int outputSize;
	
	public LinearLayer(double[] weights, int outputDimension) {
		Weights = new Matrix(weights, outputDimension);
		meanForWeights = new double[weights.length];
		varianceForWeights = new double[weights.length];
		//costForBiases = new double[bias.length];
		inputSize = weights.length/outputDimension;
		outputSize = outputDimension;
		derivativeOfCostWithRespectToOutput = new double[outputSize];
		derivativeOfCostWithRespectToWeight = new double[weights.length];
		derivativeOfCostWithRespectToTotal= new double[outputSize];
		derivativeOfCostWithRespectToInput= new double[inputSize];

	}
	
	public LinearLayer(int inputDimension, int outputDimension, CustomRandom util) {
		Weights = Matrix.rand(inputDimension, outputDimension, util);

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
		runWithBackProp(input.getInputVector(), Output, targetOutput);
	}

	@Override
	public void runWithBackProp(Vector input, Vector output) {
        double total;
        for( int i = outputSize-1; i >=0; i-- ) {
            total = 0;
			int startingPos = i * inputSize;
            for( int j = inputSize-1; j >=0; j-- ) {
				total += Weights.get(startingPos + j) * input.get(j);
			}

			output.set(i, total);
		}
		for (int i = 0; i < outputSize; i++) {
			int startingPos = i * inputSize;
			for (int j = 0; j < inputSize; j++) {
				derivativeOfCostWithRespectToInput[j] += Weights.get(startingPos + j);
			}
		}
	}

	@Override
	public void runWithBackProp(Vector input, Vector Output, Vector targetOutput) {
		int indexA = Weights.getSize()-1;
		double total;
		for (int i = outputSize - 1; i >= 0; i--) {
			total = 0;
			for (int j = inputSize - 1; j >= 0; j--) {
				total += Weights.get(indexA--) * input.get(j);
			}
			derivativeOfCostWithRespectToOutput[i] = total - targetOutput.get(i);
			derivativeOfCostWithRespectToTotal[i] = derivativeOfCostWithRespectToOutput[i];
			Output.set(i, total);
		}

		for (int i = 0; i < outputSize; i++) {
			int startingPos = i * inputSize;
			for (int j = 0; j < inputSize; j++) {
				derivativeOfCostWithRespectToWeight[startingPos + j] += input.get(j) * derivativeOfCostWithRespectToTotal[i];
				derivativeOfCostWithRespectToInput[j] += Weights.get(startingPos + j) * derivativeOfCostWithRespectToTotal[i];

			}
		}

	}

	@Override
	public void run(Vector input, Vector Output) {
		//System.out.print("Doing run pass");
		int indexA = Weights.getSize() - 1;
		double total;
		for (int i = outputSize - 1; i >= 0; i--) {
			total = 0;
			for (int j = inputSize - 1; j >= 0; j--) {
				total += Weights.get(indexA--) * input.get(j);
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
			
			Weights.addToData(i, -value);
			derivativeOfCostWithRespectToWeight[i] = momentum*value;
			
		}
		
	}

	@Override
	public int getWeightCols() {
		return outputSize;
	}


	@Override
	public void resetState() {
		for(int i=0;i<derivativeOfCostWithRespectToWeight.length;i++) {
			derivativeOfCostWithRespectToWeight[i] = 0;
		}
		
	}
	
	@Override
	public void backProp(Vector input, Vector output, double[] derivativeOfCostWithRespectToInputFromNextLayer) {
		
		for(int i=0;i<outputSize;i++) {
			double total =0;
        	int startingPos = i*inputSize;

			for(int j=0;j<inputSize;j++) {
				total+= Weights.get(startingPos+j)*derivativeOfCostWithRespectToInputFromNextLayer[i];
			}
			derivativeOfCostWithRespectToOutput[i] = total;
			derivativeOfCostWithRespectToTotal[i] = total;			
		}
		
		for(int i=0;i<outputSize;i++) {
			int startingPos = i*inputSize;
			for(int j=0;j<inputSize;j++) {
				derivativeOfCostWithRespectToWeight[startingPos+j] += input.get(j)*derivativeOfCostWithRespectToTotal[i];
        		derivativeOfCostWithRespectToInput[j] += Weights.get(startingPos+j)*derivativeOfCostWithRespectToTotal[i];

			}
		}
		
	}


	@Override
	public double[] getDerivativeWithRespectToInput() {
		return derivativeOfCostWithRespectToInput;
	}


	@Override
	public String description() {
		return "Weights: " + Weights.description();
	}

	@Override
	public void description(StringBuilder stringBuilder) {
		stringBuilder.append("Weights: ");
		Weights.description(stringBuilder);
	}
}
