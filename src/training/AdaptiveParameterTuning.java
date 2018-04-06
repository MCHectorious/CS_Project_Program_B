package training;

import dataStructures.FlashcardDataSet;
import fileManipulation.DataExport;
import models.Model;
import models.NeuralNetwork;
import generalUtilities.CustomRandom;

public class AdaptiveParameterTuning {
	
	private double alpha = 0.001,beta1 = 0.9,beta2 = 0.999, momentum = 0.5;
	private int numOfLayers = 3;
	private int hiddenDimension = 4;
	
	private static final int numOfIterations = 8;
	private final static CustomRandom util = new CustomRandom();
	private static final int trainingEpochs = 1000000;
	private static FlashcardDataSet data;
	private static final int displayReportPeriod = 20000;
	private static final int showEpochPeriod = 2000;
	private static final int checkMinimumPeriod = 30;
	
	private static final String savePath = "Models/CardToSentence.txt";
	private static final String parameterSetSavePath = "Models/ParameterTuning.txt";
	
	public void run() {
		data = new FlashcardDataSet("DataSets/TranslatedFlashcards.txt", util);
		
		DataExport.appendToTextFile("Alpha"+"\t"+"Beta1"+"\t"+"Beta2"+"\t"+"Momentum"+"\t"+"Number Of Layers"+"\t"+"Hidden Dimension"+"\t"+"Accuracy", parameterSetSavePath);

		

		
		int optimisationIterations = 30;
		
		double loss1,loss2,loss3;
		for(int i=0;i<optimisationIterations;i++) {
			loss1 = getLoss(decrement(alpha,0),beta1,beta2,momentum,numOfLayers,hiddenDimension);
			loss2 = getLoss(alpha,beta1,beta2,momentum,numOfLayers,hiddenDimension);
			loss3 = getLoss(increment(alpha,1),beta1,beta2,momentum,numOfLayers,hiddenDimension);
			alpha = updateValue(alpha,loss1,loss2,loss3,0,1);
		}
		for(int i=0;i<optimisationIterations;i++) {
			loss1 = getLoss(alpha,decrement(beta1,0),beta2,momentum,numOfLayers,hiddenDimension);
			loss2 = getLoss(alpha,beta1,beta2,momentum,numOfLayers,hiddenDimension);
			loss3 = getLoss(alpha,increment(beta1,1),beta2,momentum,numOfLayers,hiddenDimension);
			beta1 = updateValue(beta1,loss1,loss2,loss3,0,1);
		}
		
		
		
		for(int i=0;i<optimisationIterations;i++) {
			loss1 = getLoss(alpha,beta1,decrement(beta2,0),momentum,numOfLayers,hiddenDimension);
			loss2 = getLoss(alpha,beta1,beta2,momentum,numOfLayers,hiddenDimension);
			loss3 = getLoss(alpha,beta1,increment(beta2,1),momentum,numOfLayers,hiddenDimension);
			
			beta2 = updateValue(beta2,loss1,loss2,loss3,0,1);
		}
		for(int i=0;i<optimisationIterations;i++) {
			loss1 = getLoss(alpha,beta1,beta2,decrement(momentum,0),numOfLayers,hiddenDimension);
			loss2 = getLoss(alpha,beta1,beta2,momentum,numOfLayers,hiddenDimension);
			loss3 = getLoss(alpha,beta1,beta2,increment(momentum,1),numOfLayers,hiddenDimension);
			momentum = updateValue(momentum,loss1,loss2,loss3,0,1);
		}
		for(int i=0;i<optimisationIterations;i++) {
			loss1 = getLoss(alpha,beta1,beta2,momentum,decrement(numOfLayers,2),hiddenDimension);
			loss2 = getLoss(alpha,beta1,beta2,momentum,numOfLayers,hiddenDimension);
			loss3 = getLoss(alpha,beta1,beta2,momentum,increment(numOfLayers,6),hiddenDimension);
			numOfLayers = updateValue(numOfLayers,loss1,loss2,loss3,2,6);
		
		}
		for(int i=0;i<3*optimisationIterations;i++) {
			loss1 = getLoss(alpha,beta1,beta2,momentum,numOfLayers,decrement(hiddenDimension,1));
			loss2 = getLoss(alpha,beta1,beta2,momentum,numOfLayers,hiddenDimension);
			loss3 = getLoss(alpha,beta1,beta2,momentum,numOfLayers,increment(hiddenDimension,200));
			
			hiddenDimension = updateValue(hiddenDimension,loss1,loss2,loss3,1,200);
		}
		
		
		
		

		
		
	}
	
	
	
	private static double getLoss(double alpha, double beta1, double beta2, double momentum, int numOfLayers, int hiddenDimension) {
		double parameterSetLoss = 0.0;
		for(int iteration = 0;iteration<numOfIterations;iteration++) {
			Trainer trainer = new Trainer(alpha, beta1,  beta2,momentum);
			Model model = new NeuralNetwork(numOfLayers, DataPreparation.FIXED_VECTOR_SIZE, hiddenDimension, DataPreparation.FIXED_VECTOR_SIZE, util );
			parameterSetLoss += trainer.train(trainingEpochs, model, data, displayReportPeriod, showEpochPeriod,checkMinimumPeriod, savePath, util);
		}
		System.out.println("Set Of Parameters Completed:\t"+alpha+"\t"+beta1+"\t"+beta2+"\t"+momentum+"\t"+numOfLayers+"\t"+hiddenDimension+"\t"+parameterSetLoss/numOfIterations);
		DataExport.appendToTextFile(alpha+"\t"+beta1+"\t"+beta2+"\t"+momentum+"\t"+numOfLayers+"\t"+hiddenDimension+"\t"+parameterSetLoss/numOfIterations, parameterSetSavePath);
		return parameterSetLoss;
	}
	
	private static double updateValue(double currentValue, double loss1,double loss2, double loss3, double lowerBound,double upperBound) {
		if(loss1<loss2 && loss2 < loss3) {
			return decrement(currentValue,lowerBound);
		}
		if(loss3<loss2 && loss2<loss1) {
			return increment(currentValue,upperBound);
		}
		double total = loss1+loss2+loss3;
		return 1.0/(2.0*total)*((total-loss1)*decrement(currentValue,lowerBound)+(total-loss2)*currentValue+(total-loss3)*increment(currentValue,upperBound));
		
	}

	private static int updateValue(int currentValue, double loss1,double loss2, double loss3, int lowerBound,int upperBound) {
		if(loss1<loss2 && loss2 < loss3) {
			return decrement(currentValue,lowerBound);
		}
		if(loss3<loss2 && loss2<loss1) {
			return increment(currentValue,upperBound);
		}
		double total = loss1+loss2+loss3;
		return (int) Math.round( 1.0/(2.0*total)*((total-loss1)*decrement(currentValue,lowerBound)+(total-loss2)*currentValue+(total-loss3)*increment(currentValue,upperBound)) );
		
	}
	
	private static int increment(int x, int upperBound) {
		return (x+1>upperBound)? upperBound:x+1;
	}
	

	
	private static int decrement(int x, int lowerBound) {
		return (x-1<lowerBound)? lowerBound:x-1;
	}
	
	private static double increment(double x, double upperBound) {
		return (x+0.0001>upperBound)? upperBound:x+0.0001;
	}
	
	private static double decrement(double x, double lowerBound) {
		return (x-0.0001<lowerBound)? lowerBound:x-0.0001;
	}
	
	
}
