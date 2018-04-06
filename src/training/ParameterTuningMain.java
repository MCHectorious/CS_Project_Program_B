package training;

import generalUtilities.CustomRandom;
import generalUtilities.Util;
import dataStructures.FlashcardDataSet;
import fileManipulation.DataExport;
import models.Model;
import models.NeuralNetwork;

public class ParameterTuningMain {

	public static void main(String[] args) {
		//new AdaptiveParameterTuning().run();
		systematicTuning();

		
		
	}

	
	public static void systematicTuning() {
		CustomRandom util = new CustomRandom();
		FlashcardDataSet data = new FlashcardDataSet("DataSets/TranslatedFlashcards.txt", util);
		String savePath = "Models/CardToSentence.txt";
		
		String parameterSetSavePath = "Models/ParameterTuning.txt";
		
		int trainingEpochs = 10000;
		int displayReportPeriod = 5000;
		int showEpochPeriod = 1000;
		int checkMinimumPeriod = 30;
		double parameterSetLoss;
		
		int numOfIterations = 3;
		
		double[] alphaSet = {0.01,0.00001};
		double[] beta1Set = {0.9,0.9999999999999};
		double[] beta2Set = {0.9,0.9999999999999};
		double[] momentumSet = {0,0.9,1};
		
		int[][] layerTypesSets = {{0,0},{0,1},{1,0},{1,1}};
		int[][] hiddenDimensionSet = {{5},{10},{15}};
		
		
		DataExport.appendToTextFile("Alpha"+"\t"+"Beta1"+"\t"+"Beta2"+"\t"+"Momentum"+"\t"+"Number Of Layers"+"\t"+"Hidden Dimension"+"\t"+"Accuracy", parameterSetSavePath);

		
		for(int alphaPos=0;alphaPos<alphaSet.length;alphaPos++) {
			for(int beta1Pos=0;beta1Pos<beta1Set.length;beta1Pos++) {
				for(int beta2Pos=0;beta2Pos<beta2Set.length;beta2Pos++) {
					for(int momentumPos=0;momentumPos<momentumSet.length;momentumPos++) {
						for(int layersTypesPos=0;layersTypesPos<layerTypesSets.length;layersTypesPos++) {
							for(int hiddenPos=0;hiddenPos<hiddenDimensionSet.length;hiddenPos++) {
								double alpha = alphaSet[alphaPos];
								double beta1 = beta1Set[beta1Pos];
								double beta2 = beta2Set[beta2Pos];
								double momentum = momentumSet[momentumPos];
								int[] layerTypes = layerTypesSets[layersTypesPos];
								int[] hiddenDimensions = hiddenDimensionSet[hiddenPos];
								
								parameterSetLoss = 0.0;
								for(int iteration = 0;iteration<numOfIterations;iteration++) {
									Trainer trainer = new Trainer(alpha, beta1,  beta2,momentum);
									//Model model = new NeuralNetwork(numOfLayers, DataPreparation.FIXED_VECTOR_SIZE, hiddenDimension, DataPreparation.FIXED_VECTOR_SIZE, util );
									
									Model model = new NeuralNetwork(layerTypes, DataPreparation.FIXED_VECTOR_SIZE, hiddenDimensions, DataPreparation.FIXED_VECTOR_SIZE, util);
									
									parameterSetLoss += trainer.train(trainingEpochs, model, data, displayReportPeriod, showEpochPeriod,checkMinimumPeriod, savePath, util);
								}
								System.out.println("Set Of Parameters Completed:\t"+alpha+"\t"+beta1+"\t"+beta2+"\t"+momentum+"\t"+Util.arrayToString(layerTypes)+"\t"+Util.arrayToString(hiddenDimensions)+"\t"+parameterSetLoss/numOfIterations);
								DataExport.appendToTextFile(alpha+"\t"+beta1+"\t"+beta2+"\t"+momentum+"\t"+Util.arrayToString(layerTypes)+"\t"+Util.arrayToString(hiddenDimensions)+"\t"+parameterSetLoss/numOfIterations, parameterSetSavePath);
								
							}	
						}	
					}	
				}	
			}
		}
	}
	
}
