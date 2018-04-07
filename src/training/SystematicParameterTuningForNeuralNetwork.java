package training;

import dataStructures.FlashcardDataSet;
import fileManipulation.DataExport;
import generalUtilities.CustomRandom;
import generalUtilities.Utilities;
import models.Model;
import models.NeuralNetworkModel;

public class SystematicParameterTuningForNeuralNetwork {

	public static void main(String[] args) {
		//new AdaptiveParameterTuningForNeuralNetwork().run();
		systematicTuning();

		
		
	}


	private static void systematicTuning() {
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


		for (double alpha : alphaSet) {
			for (double beta1 : beta1Set) {
				for (double beta2 : beta2Set) {
					for (double momentum : momentumSet) {
						for (int[] layerTypes : layerTypesSets) {
							for (int[] hiddenDimensions : hiddenDimensionSet) {

								parameterSetLoss = 0.0;
								for (int iteration = 0; iteration < numOfIterations; iteration++) {
									Trainer trainer = new Trainer(alpha, beta1, beta2, momentum);
									//Model model = new NeuralNetworkModel(numOfLayers, DataProcessing.FIXED_VECTOR_SIZE, hiddenDimension, DataProcessing.FIXED_VECTOR_SIZE, util );

									Model model = new NeuralNetworkModel(layerTypes, DataProcessing.FIXED_VECTOR_SIZE, hiddenDimensions, DataProcessing.FIXED_VECTOR_SIZE, util);

									parameterSetLoss += trainer.train(trainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
								}
								System.out.println("Set Of Parameters Completed:\t" + alpha + "\t" + beta1 + "\t" + beta2 + "\t" + momentum + "\t" + Utilities.arrayToString(layerTypes) + "\t" + Utilities.arrayToString(hiddenDimensions) + "\t" + parameterSetLoss / numOfIterations);
								DataExport.appendToTextFile(alpha + "\t" + beta1 + "\t" + beta2 + "\t" + momentum + "\t" + Utilities.arrayToString(layerTypes) + "\t" + Utilities.arrayToString(hiddenDimensions) + "\t" + parameterSetLoss / numOfIterations, parameterSetSavePath);

							}
						}
					}
				}
			}
		}
	}
	
}
