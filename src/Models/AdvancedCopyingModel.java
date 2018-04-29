package Models;

import DataStructures.DataStep;
import GeneralUtilities.CustomRandom;
import GeneralUtilities.Utilities;
import Matrices.Vector;

public class AdvancedCopyingModel implements Model {

	private int windowSize;//the number of positions it can copy from
	private int inputSize;//The size of the input
	private double[] valuesForWindowPositions;//Represents the probability of copying each position
	private double[] deltaForWindowPositions;//How the probabilities should be changed
	private CustomRandom customRandom = new CustomRandom();//Used to generate the values which will determine which position to keep
	private Vector randomDoubles;//Created here to avoid creating objects in loop as it is very inefficient
	private int[] relativePositionForWindow;//The index of the position relative to the current position
	private double[] windowSizes;//Created here to avoid creating objects in loop as it is very inefficient

	public AdvancedCopyingModel(int size, int inputSize) {
		windowSize=2*size+1;//the current position and the size either side of it
		valuesForWindowPositions = new double[windowSize];
		randomDoubles = new Vector(inputSize);
		this.inputSize = inputSize;
		relativePositionForWindow = new int[windowSize];
		deltaForWindowPositions = new double[windowSize];
		windowSizes = new double[windowSize];
		int index=0;
		for(int i=-size;i<=size;i++) {
			relativePositionForWindow[index++] = i;//How the position compares to the current position
		}
		double equalShare = 1.0/windowSize;
		for(int i=0;i<windowSize;i++) {
			valuesForWindowPositions[i] = i*equalShare;//Starts with the assumption that all positions are equally likely to be copied
		}

	}
	
	@Override
	public void run(DataStep input, Vector output) {
		randomDoubles.setData(customRandom.randomDoublesBetween0and1(inputSize));//gets the random values which will decide which position will be copied
		double randomValue;
		int positionToCopy;
		for(int i=inputSize-1;i>=0;i--) {
			randomValue = randomDoubles.get(i);
			for(int j=windowSize-1;j>=0;j--) {
				
				if(randomValue>=valuesForWindowPositions[j]) {
					positionToCopy = i+relativePositionForWindow[j];
					if(positionToCopy<0 || positionToCopy>=inputSize) {
						output.set(i, input.getInputVector().get(i));//Copies current position because it can't copy the relative position
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
					if(positionToCopy<0 || positionToCopy>=inputSize) {//If position can't be copied
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
				deltaForWindowPositions[j] += (input.getInputVector().get(positionToCopy) > target) ? input.getInputVector().get(positionToCopy) - target : target - input.getInputVector().get(positionToCopy);//Gets how inaccurate th ecoping value is
			}

		}

	}

	@Override
	public void updateModelParameters(double momentum, double beta1, double beta2, double alpha, double OneMinusBeta1,
									  double OneMinusBeta2) {
		double totalDelta = 0, totalReciprocalOfDelta = 0;

		for (double deltaForWindowPosition : deltaForWindowPositions) {
			totalDelta += deltaForWindowPosition;//Gets the total inaccuracy of all of the  positions to copy
		}
		
		for(int i=0;i<deltaForWindowPositions.length;i++) {
			windowSizes[i] = totalDelta/deltaForWindowPositions[i];//Works out the proportion of the inaccuracy is is not responsible for (the higher the number the better)
			totalReciprocalOfDelta += windowSizes[i];
		}
		
		double reciprocalOfTotalOfReciprocals = 1.0/totalReciprocalOfDelta;
		
		double runningTotal = 0;
		for(int i=1;i<deltaForWindowPositions.length;i++) {
			runningTotal += windowSizes[i-1]*reciprocalOfTotalOfReciprocals;
			valuesForWindowPositions[i] = 0.5*(valuesForWindowPositions[i]+runningTotal);//The values will be between 0 and 1
		}

		for(int i=windowSize-1;i>=0;i--) {
			deltaForWindowPositions[i] *= momentum;//decides how much the previous inaccuracy matters to the current inaccuracy
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
