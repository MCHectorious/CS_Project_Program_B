package FindingBestModel;

import java.util.ArrayList;
import java.util.Arrays;

import DataStructures.FlashcardDataSet;
import FileManipulation.DataExport;
import GeneralUtilities.Subsets;
import Models.*;
import NonlinearityFunctions.RoughTanhUnit;
import Training.DataProcessing;
import Training.ModelTrainer;
import GeneralUtilities.CustomRandom;
import GeneralUtilities.Utilities;

public class FindBestStackingFromIndividual {

	public static void main(String[] args) {
		CustomRandom util = new CustomRandom();

		FlashcardDataSet data = new FlashcardDataSet("DataSets/TranslatedFlashcards.txt", util);
		
		int numOfTrainingEpochs = 10000000;
		int displayReportPeriod = 100000;
		int showEpochPeriod = 10000;
		int checkMinimumPeriod = 50;
		String savePath = "Models/CardToSentence.txt";
		
		int attempts = 3;//The number of attempts of finding the best model
		
		ArrayList<Model> modelList = new ArrayList<>();
		
		Model bestTempModel = new BasicCopyingModel();
		double minimumLoss = Double.MAX_VALUE;//Initialises to an impossible value
		
		for(int i=0;i<attempts;i++) {//Finds the best linear layer
			Model tempModel = new LinearLayer(DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, util);
			double loss = (new ModelTrainer()).train(numOfTrainingEpochs, tempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
			if(loss<minimumLoss) {
				minimumLoss = loss;
				bestTempModel = tempModel;
			}
		}
		System.out.println(bestTempModel.toString()+" : "+minimumLoss);
		modelList.add(bestTempModel);

		minimumLoss = Double.MAX_VALUE;
		for(int i=0;i<attempts;i++) {//Finds the best feed-forward layer
			Model tempModel = new FeedForwardLayer(DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR,new RoughTanhUnit(), util);
			double loss = (new ModelTrainer()).train(numOfTrainingEpochs, tempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
			if(loss<minimumLoss) {
				minimumLoss = loss;
				bestTempModel = tempModel;
			}
		}
		System.out.println(bestTempModel.toString()+" : "+minimumLoss);
		modelList.add(bestTempModel);
		
		minimumLoss = Double.MAX_VALUE;
		for(int i=0;i<attempts;i++) {//Finds best neural network of this structure
			Model tempModel = new NeuralNetworkModel(new int[] {1,0}, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, new int[] {30}, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, util);
			double loss = (new ModelTrainer()).train(numOfTrainingEpochs, tempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
			if(loss<minimumLoss) {
				minimumLoss = loss;
				bestTempModel = tempModel;
			}
		}
		System.out.println(bestTempModel.toString()+" : "+minimumLoss);
		modelList.add(bestTempModel);


		bestTempModel = new ProportionalProbabilityForCharacterModel(data.getTrainingDataSteps(), 9, data.getDataProcessing(), util);
		minimumLoss = (new ModelTrainer()).train(numOfTrainingEpochs, bestTempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(bestTempModel.toString()+" : "+minimumLoss);
		modelList.add(bestTempModel);
		//Adds a particular proportional probability for character model

		bestTempModel = new ProportionalProbabilityForCharacterModel(data.getTrainingDataSteps(), 4, data.getDataProcessing(), util);
		minimumLoss = (new ModelTrainer()).train(numOfTrainingEpochs, bestTempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(bestTempModel.toString()+" : "+minimumLoss);
		modelList.add(bestTempModel);
		//Adds a particular proportional probability for character model
		
		minimumLoss = Double.MAX_VALUE;
		for(int i=0;i<attempts;i++) {//Finds best neural network of this structure
			Model tempModel = new NeuralNetworkModel(new int[] {0,0}, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, new int[] {2}, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, util);
			double loss = (new ModelTrainer()).train(numOfTrainingEpochs, tempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
			if(loss<minimumLoss) {
				minimumLoss = loss;
				bestTempModel = tempModel;
			}
		}
		System.out.println(bestTempModel.toString()+" : "+minimumLoss);
		modelList.add(bestTempModel);
		
		minimumLoss = Double.MAX_VALUE;
		for(int i=0;i<attempts;i++) {//Finds best neural network of this structure
			Model tempModel = new NeuralNetworkModel(new int[] {0,0,0}, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, new int[] {2,1}, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, util);
			double loss = (new ModelTrainer()).train(numOfTrainingEpochs, tempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
			if(loss<minimumLoss) {
				minimumLoss = loss;
				bestTempModel = tempModel;
			}
		}
		System.out.println(bestTempModel.toString()+" : "+minimumLoss);
		modelList.add(bestTempModel);
		
		bestTempModel = new AverageModel(data.getTrainingDataSteps());
		minimumLoss = (new ModelTrainer()).train(numOfTrainingEpochs, bestTempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(bestTempModel.toString()+" : "+minimumLoss);
		modelList.add(bestTempModel);
		//Adds an average model
		
		bestTempModel = new CharacterManipulationFromStringDistanceModel(data.getTrainingDataSteps(), data.getDataProcessing(), util);
		minimumLoss = (new ModelTrainer()).train(numOfTrainingEpochs, bestTempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(bestTempModel.toString()+" : "+minimumLoss);
		modelList.add(bestTempModel);
		//Adds the character manipulation from string distance model

		ArrayList<Model[]> subsets = Subsets.getSubsetsGreaterThanSize1(modelList);
		//Gets all of the subsets of the models with a size greater than size 1

		double total, average;
		String temporaryString;

		for(Model[] modelSubset: subsets) {
			Model combiningModel;
			for(int modelIndex=0;modelIndex<=2;modelIndex++) {
				switch(modelIndex) {
				case 0://FeedForward Layer Combining Model
					combiningModel = new FeedForwardLayer(modelSubset.length*DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, new RoughTanhUnit(), util);
					(new ModelTrainer()).train(numOfTrainingEpochs, combiningModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
					total = 0.0;
					for(int q=0;q<attempts;q++) {
						Model model = new StackingEnsembleModel(combiningModel,new ArrayList<>(Arrays.asList(modelSubset)),DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR,DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR,util);
						total += (new ModelTrainer()).train(numOfTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
					}
					average = total/(double) attempts;
					temporaryString = "Stacking Model with model "+combiningModel.toString()+" with Models "+Utilities.arrayToString(modelSubset)+":\t"+average;
					System.out.println(temporaryString);
					DataExport.appendToTextFile(temporaryString, "Models/ParameterTuning.txt");
					System.gc();
					break;
				case 1://Linear Layer Combining Model
					combiningModel = new LinearLayer(modelSubset.length*DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR,  util);
					(new ModelTrainer()).train(numOfTrainingEpochs, combiningModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
					total = 0.0;
					for(int q=0;q<attempts;q++) {
						Model model = new StackingEnsembleModel(combiningModel,new ArrayList<>(Arrays.asList(modelSubset)),DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR,DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR,util);
						total += (new ModelTrainer()).train(numOfTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
					}
					average = total/(double) attempts;
					System.out.println("Stacking Model with model "+combiningModel.toString()+" with Models "+Utilities.arrayToString(modelSubset)+":\t"+average);
					temporaryString = "Stacking Model with model "+combiningModel.toString()+" with Models "+Utilities.arrayToString(modelSubset)+":\t"+average;
					System.out.println(temporaryString);
					DataExport.appendToTextFile(temporaryString, "Models/ParameterTuning.txt");
					System.gc();
					break;
				case 2:	//Neural Network Combining Model

					int [] layerTypeSet = {0,1};//Either FeedForward Layer or Linear Layer
					int [] hiddenDimensionSet = {1,2,3,4,5,7,10};//Possible hidden dimensions
					int maxArraySize = 4;//Maximum number of layers

					for(int j=2;j<=maxArraySize;j++) {
						int[][] layerTypes = new int[(int) Math.pow(layerTypeSet.length, j)][j];//Initialises the array so that it can contain all the possible combinations
						for(int i=0;i<=Math.pow(layerTypeSet.length, j)-1;i++) {
							for(int z=0;z<j;z++) {
								layerTypes[i][z] =  layerTypeSet[ (int) Math.floor((i)/(int) Math.floor(Math.pow(layerTypeSet.length, z)) ) % layerTypeSet.length ]; //Gets the ith combination of the layer types
							}
						}
						int[][] hiddenDimensions = new int[(int) Math.pow(hiddenDimensionSet.length, j-1)][j-1];
						for(int i=0;i<=Math.pow(hiddenDimensionSet.length, j-1)-1;i++) {
							for(int z=0;z<j-1;z++) {
								hiddenDimensions[i][z] =  hiddenDimensionSet[ (int) Math.floor((i)/(int) Math.floor(Math.pow(hiddenDimensionSet.length, z)) ) % hiddenDimensionSet.length ];//Gets the ith combination of hidden dimensions
							}
						}
						for (int[] layerType : layerTypes) {
							for (int[] hiddenDimension : hiddenDimensions) {
								combiningModel = new NeuralNetworkModel(layerType, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, hiddenDimension, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, util);
								(new ModelTrainer()).train(numOfTrainingEpochs, combiningModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
								total = 0.0;
								for (int q = 0; q < attempts; q++) {
									Model model = new StackingEnsembleModel(combiningModel, new ArrayList<>(Arrays.asList(modelSubset)), DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, util);
									total += (new ModelTrainer()).train(numOfTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
								}
								average = total / (double) attempts;
								temporaryString = "Stacking Model with model " + combiningModel.toString() + " with Models " + Utilities.arrayToString(modelSubset) + ":\t" + average;
								System.out.println(temporaryString);
								DataExport.appendToTextFile(temporaryString, "Models/ParameterTuning.txt");
								System.gc();//To make more space available
							}
						}
					}
					break;
				}


			}

		}

	}

	
}



