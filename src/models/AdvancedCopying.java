package models;

import matrix.Vector;
import util.CustomRandom;
import util.Util;

public class AdvancedCopying implements Model {

	private int windowSize, inputSize;
	private double[] valuesForWindowPositions;
	private double[] deltaForWindowPositions;
	private CustomRandom rand = new CustomRandom();
	private Vector randomDoubles;
	private int[] relativePositionForWindow;
	private double[] windowSizes;
	
	public AdvancedCopying(int size, int inputDimension) {
		windowSize=2*size+1;
		valuesForWindowPositions = new double[windowSize];
		randomDoubles = new Vector(inputDimension);
		inputSize = inputDimension;
		relativePositionForWindow = new int[windowSize];
		deltaForWindowPositions = new double[windowSize];
		windowSizes = new double[windowSize];
		int index=0;
		for(int i=-size;i<=size;i++) {
			relativePositionForWindow[index++] = i;
			//System.out.print(relativePositionForWindow[index-1]+",");
		}
		//System.out.println();
		double equalShare = 1.0/windowSize;
		for(int i=0;i<windowSize;i++) {
			valuesForWindowPositions[i] = i*equalShare;
		}
		
		//for(int i=0;i<windowSize;i++) {
			//System.out.print(valuesForWindowPositions[i]+"\t");
		//}
		//System.out.println();
		
	}
	
	@Override
	public void forward(Vector input, Vector output) {
		randomDoubles.setData(rand.randomDoublesBetween0and1(inputSize));
		//System.out.println(Util.arrayToString(randomDoubles.getData()) );
		double RandomValue;
		int positionToCopy;
		for(int i=inputSize-1;i>=0;i--) {
			RandomValue = randomDoubles.get(i);
			//System.out.print(RandomValue+" ");
			for(int j=windowSize-1;j>=0;j--) {
				
				if(RandomValue>=valuesForWindowPositions[j]) {
					positionToCopy = i+relativePositionForWindow[j];
					if(positionToCopy<0 || positionToCopy>=inputSize) {
						output.set(i, input.get(i));//For empty String
					}else {
						output.set(i, input.get(positionToCopy));						
						//System.out.print(i+", ");
					}
					break;
				}
			}
		}
		//System.out.println();
	}

	@Override
	public void forwardWithBackProp(Vector input, Vector output, Vector targetOutput) {
		randomDoubles.setData(rand.randomDoublesBetween0and1(inputSize));
		double RandomValue,value,target;
		int positionToCopy;
		for(int i=inputSize-1;i>=0;i--) {
			RandomValue = randomDoubles.get(i);
			
			for(int j=windowSize-1;j>=0;j--) {
				if(RandomValue>valuesForWindowPositions[j]) {
					positionToCopy = i+relativePositionForWindow[j];
					if(positionToCopy<0 || positionToCopy>=inputSize) {
						value = input.get(i);
					}else {
						value = input.get(positionToCopy);
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
				deltaForWindowPositions[j] += (input.get(positionToCopy)>target)? input.get(positionToCopy)-target:target-input.get(positionToCopy);
				//System.out.print(deltaForWindowPositions[j]+" ");
			}
			//System.out.println();
			
		}

	}

	@Override
	public void getParams(StringBuilder builder) {
		new Vector(valuesForWindowPositions).toString(builder);

	}

	@Override
	public void updateModelParams(double momentum, double beta1, double beta2, double alpha, double OneMinusBeta1,
			double OneMinusBeta2) {
		double totalDelta = 0, totalReciprocalOfDelta = 0;
		
		for(int i=0;i<deltaForWindowPositions.length;i++) {
			totalDelta += deltaForWindowPositions[i];
			//System.out.println(deltaForWindowPositions[i]);
		}
		
		for(int i=0;i<deltaForWindowPositions.length;i++) {
			windowSizes[i] = totalDelta/deltaForWindowPositions[i];
			
			totalReciprocalOfDelta += windowSizes[i];
			
			//System.out.println(windowSizes[i]);
		}
		
		double reciprocalOfTotalOfReciprocals = 1.0/totalReciprocalOfDelta;
		
		double runningTotal = 0;
		for(int i=1;i<deltaForWindowPositions.length;i++) {
			runningTotal += windowSizes[i-1]*reciprocalOfTotalOfReciprocals;
			//System.out.println(runningTotal+" "+valuesForWindowPositions[i]);
			valuesForWindowPositions[i] = 0.5*(valuesForWindowPositions[i]+runningTotal);
			
		}
		//System.out.println();
		
		
		
		for(int i=windowSize-1;i>=0;i--) {
			deltaForWindowPositions[i] *= momentum;
		}
		
		//for(int i=0;i<windowSize;i++) {
			//System.out.print(valuesForWindowPositions[i]+"\t");
		//}
		//System.out.println();
		
	}

	@Override
	public void resetState() {
		for(int i=windowSize-1;i>=0;i--) {
			deltaForWindowPositions[i] = 0;
		}
		
	}

}
