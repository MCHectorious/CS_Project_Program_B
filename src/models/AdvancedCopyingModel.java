package models;

import dataStructures.DataStep;
import generalUtilities.CustomRandom;
import generalUtilities.Utilities;
import matrices.Vector;

public class AdvancedCopyingModel implements Model {

	private int windowSize, inputSize;
	private double[] valuesForWindowPositions;
	private double[] deltaForWindowPositions;
	private CustomRandom customRandom = new CustomRandom();
	private Vector randomDoubles;
	private int[] relativePositionForWindow;
	private double[] windowSizes;

	public AdvancedCopyingModel(int size, int inputSize) {
		windowSize=2*size+1;
		valuesForWindowPositions = new double[windowSize];
		randomDoubles = new Vector(inputSize);
		this.inputSize = inputSize;
		relativePositionForWindow = new int[windowSize];
		deltaForWindowPositions = new double[windowSize];
		windowSizes = new double[windowSize];
		int index=0;
		for(int i=-size;i<=size;i++) {
			relativePositionForWindow[index++] = i;
		}
		double equalShare = 1.0/windowSize;
		for(int i=0;i<windowSize;i++) {
			valuesForWindowPositions[i] = i*equalShare;
		}

	}
	
	@Override
	public void run(DataStep input, Vector output) {
		randomDoubles.setData(customRandom.randomDoublesBetween0and1(inputSize));
		double randomValue;
		int positionToCopy;
		for(int i=inputSize-1;i>=0;i--) {
			randomValue = randomDoubles.get(i);
			for(int j=windowSize-1;j>=0;j--) {
				
				if(randomValue>=valuesForWindowPositions[j]) {
					positionToCopy = i+relativePositionForWindow[j];
					if(positionToCopy<0 || positionToCopy>=inputSize) {
						output.set(i, input.getInputVector().get(i));//For empty String
					}else {
						output.set(i, input.getInputVector().get(positionToCopy));
					}
					break;
				}
			}
		}
	}

	@Override
	public void runAndDecideImprovements(DataStep input, Vector output, Vector targetOutput) {
		randomDoubles.setData(customRandom.randomDoublesBetween0and1(inputSize));
		double randomValue,value,target;
		int positionToCopy;
		for(int i=inputSize-1;i>=0;i--) {
			randomValue = randomDoubles.get(i);
			
			for(int j=windowSize-1;j>=0;j--) {
				if(randomValue>valuesForWindowPositions[j]) {
					positionToCopy = i+relativePositionForWindow[j];
					if(positionToCopy<0 || positionToCopy>=inputSize) {
						value = input.getInputVector().get(i);
					}else {
						value = input.getInputVector().get(positionToCopy);
					}
					output.set(i, value);
					
					break;
				}
				
			}
			target = targetOutput.get(i);
			for(int j=0;j<windowSize;j++) {
				positionToCopy = i+relativePositionForWindow[j];
				
				if(positionToCopy<0 || positionToCopy>=inputSize) {
					positionToCopy = i;
				}
				deltaForWindowPositions[j] += (input.getInputVector().get(positionToCopy) > target) ? input.getInputVector().get(positionToCopy) - target : target - input.getInputVector().get(positionToCopy);
			}

		}

	}

	@Override
	public void updateModelParameters(double momentum, double beta1, double beta2, double alpha, double OneMinusBeta1,
									  double OneMinusBeta2) {
		double totalDelta = 0, totalReciprocalOfDelta = 0;

		for (double deltaForWindowPosition : deltaForWindowPositions) {
			totalDelta += deltaForWindowPosition;
		}
		
		for(int i=0;i<deltaForWindowPositions.length;i++) {
			windowSizes[i] = totalDelta/deltaForWindowPositions[i];
			totalReciprocalOfDelta += windowSizes[i];
		}
		
		double reciprocalOfTotalOfReciprocals = 1.0/totalReciprocalOfDelta;
		
		double runningTotal = 0;
		for(int i=1;i<deltaForWindowPositions.length;i++) {
			runningTotal += windowSizes[i-1]*reciprocalOfTotalOfReciprocals;
			valuesForWindowPositions[i] = 0.5*(valuesForWindowPositions[i]+runningTotal);
		}

		for(int i=windowSize-1;i>=0;i--) {
			deltaForWindowPositions[i] *= momentum;
		}

	}

	@Override
	public void resetState() {
		for(int i=windowSize-1;i>=0;i--) {
			deltaForWindowPositions[i] = 0;
		}
		
	}

	@Override
	public String provideDescription() {
		return Utilities.arrayToString(valuesForWindowPositions);
	}

	@Override
	public void provideDescription(StringBuilder stringBuilder) {
		new Vector(valuesForWindowPositions).provideDescription(stringBuilder);
	}
}
