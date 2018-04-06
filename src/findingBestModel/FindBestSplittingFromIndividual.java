package findingBestModel;

import java.util.ArrayList;

import dataSplitting.DataSplitOp;
import dataSplitting.Splitter;
import dataStructures.DataStep;
import dataStructures.FlashcardDataSet;
import fileManipulation.DataExport;
import fileManipulation.DataImport;
import models.*;
import nonlinearityFunctions.RoughTanhUnit;
import training.DataPreparation;
import training.Trainer;
import generalUtilities.CustomRandom;
import generalUtilities.Util;



public class FindBestSplittingFromIndividual {

	public static void main(String[] args) {
		CustomRandom util = new CustomRandom();

		FlashcardDataSet data = new FlashcardDataSet("DataSets/TranslatedFlashcards.txt", util);
		
		int numOfTrainingEpochs = 10000000;
		int displayReportPeriod = 100000;
		int showEpochPeriod = 10000;
		int checkMinimumPeriod = 50;
		String savePath = "Models/CardToSentence.txt";
			
		ArrayList<Model> modelList = new ArrayList<>();
		
		int attempts = 3;
		
		Model bestTempModel = new BasicCopying();
		double minLoss;
		
		ArrayList<String> lineFromTextFile = DataImport.getLines("bestModel/BestLinearLayerWeights.txt");
		double[] weights = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(0) );
		bestTempModel = new LinearLayer(weights, DataPreparation.FIXED_VECTOR_SIZE);
		minLoss = (new Trainer()).train(numOfTrainingEpochs, bestTempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		
		
		lineFromTextFile = DataImport.getLines("bestModel/BestFeedForwardLayerParams.txt");
		double[] biases = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(0) );
		weights = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(1) );
		bestTempModel = new FeedForwardLayer(weights, biases, new RoughTanhUnit());		
		minLoss = (new Trainer()).train(numOfTrainingEpochs, bestTempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		
		bestTempModel = new CategoricPortionProbabilityModelForCharacter(data.getTrainingDataSteps(), 9, data.getDataPrep(), util);
		minLoss = (new Trainer()).train(numOfTrainingEpochs, bestTempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		
		bestTempModel = new CategoricPortionProbabilityModelForCharacter(data.getTrainingDataSteps(), 4, data.getDataPrep(), util);
		minLoss = (new Trainer()).train(numOfTrainingEpochs, bestTempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		
		
		
		
		lineFromTextFile = DataImport.getLines("bestModel/Best2LayerNeuralNetwork.txt");
		ArrayList<Layer> layers = new ArrayList<>();
		double[] biases1 = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(1) );
		double[] weights1 = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(2) );
		layers.add(new FeedForwardLayer(weights1, biases1, new RoughTanhUnit()));
		double[] biases2 = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(4) );
		double[] weights2 = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(5) );
		layers.add(new FeedForwardLayer(weights2, biases2, new RoughTanhUnit()));
		bestTempModel = new NeuralNetwork(layers);		
		minLoss = (new Trainer()).train(numOfTrainingEpochs, bestTempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		
		lineFromTextFile = DataImport.getLines("bestModel/Best3LayerNeuralNetwork.txt");
		ArrayList<Layer> layersFor3NN = new ArrayList<>();
		double[] biases1For3NN = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(1) );
		double[] weights1For3NN = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(2) );
		layersFor3NN.add(new FeedForwardLayer(weights1For3NN, biases1For3NN, new RoughTanhUnit()));
		double[] biases2For3NN = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(4) );
		double[] weights2For3NN = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(5) );
		layersFor3NN.add(new FeedForwardLayer(weights2For3NN, biases2For3NN, new RoughTanhUnit()));
		double[] biases3For3NN = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(7) );
		double[] weights3For3NN = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(8) );
		layersFor3NN.add(new FeedForwardLayer(weights3For3NN, biases3For3NN, new RoughTanhUnit()));		
		bestTempModel = new NeuralNetwork(layersFor3NN);		
		minLoss = (new Trainer()).train(numOfTrainingEpochs, bestTempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
			
		bestTempModel = new AverageModel(data.getTrainingDataSteps());
		minLoss = (new Trainer()).train(numOfTrainingEpochs, bestTempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		
		bestTempModel = new CharacterManipulationFromStringDistance(data.getTrainingDataSteps(), data.getDataPrep(), util);
		minLoss = (new Trainer()).train(numOfTrainingEpochs, bestTempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);

		
		Model[] models = modelList.toArray(new Model[0]);
		
		System.gc();
		
		double total, average;
		
		for(Model originalModel: models) {
			DataSplitOp splitOp = Splitter.getBestDataSplit(data.getTrainingDataSteps(), originalModel, data.getDataPrep());
			ArrayList<DataStep> badValues = Splitter.getStepsNotInSplit(data.getTrainingDataSteps(), splitOp);
			
			for(int i=0;i<=9;i++) {
				total = 0.0;
				Model otherModel = null;
				for(int j=0;j<attempts;j++) {
					otherModel = getModelFromIndex(i,badValues,data.getDataPrep(),util);				
				//(new Trainer()).train(numOfTrainingEpochs, otherModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
					Model model = new SplittingEnsembleModel(originalModel, otherModel, splitOp);
					total += (new Trainer()).train(numOfTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
				}
				average = total / (double) attempts;

				StringBuilder tempBuilder = new StringBuilder();
				splitOp.toString(tempBuilder);
				String tempString = "Splitting Model with split "+tempBuilder.toString()+" with good model "+originalModel.toString()+" and bad model "+otherModel.toString()+": "+average; 
				tempBuilder.append(tempString);
				System.out.println(tempBuilder.toString());
				DataExport.appendToTextFile(tempBuilder.toString(), "Models/ParameterTuning.txt");

			}
		
		
			
			
			
		}
		
		
	}

	public static Model getModelFromIndex(int index, ArrayList<DataStep> trainingData, DataPreparation dataPrep, CustomRandom rand) {
		switch(index) {
		case 0:
			return new AdvancedCopying(1, DataPreparation.FIXED_VECTOR_SIZE);
		case 1:
			return new AverageModel(trainingData);
		case 2:
			return new BasicCopying();
		case 3:
			return new CategoricPortionProbabilityModelForCharacter(trainingData, 4, dataPrep, rand);
		case 4:
			return new CharacterManipulationFromStringDistance(trainingData, dataPrep, rand);
		case 5:
			return new FeedForwardLayer(DataPreparation.FIXED_VECTOR_SIZE, DataPreparation.FIXED_VECTOR_SIZE, new RoughTanhUnit(), rand);
		case 6:
			return new LinearLayer(DataPreparation.FIXED_VECTOR_SIZE, DataPreparation.FIXED_VECTOR_SIZE, rand);
		case 7:
			return new NeuralNetwork(new int[] {1,0}, DataPreparation.FIXED_VECTOR_SIZE, new int[] {20}, DataPreparation.FIXED_VECTOR_SIZE, rand);
		case 8:
			return new NeuralNetwork(new int[] {0,0}, DataPreparation.FIXED_VECTOR_SIZE, new int[] {2}, DataPreparation.FIXED_VECTOR_SIZE, rand);
		case 9:
			return new NeuralNetwork(new int[] {0,0,0}, DataPreparation.FIXED_VECTOR_SIZE, new int[] {3,2}, DataPreparation.FIXED_VECTOR_SIZE, rand);

		default:
			return new BasicCopying();
		}
	}
	
	
}

/*
public class FindBestSplittingFromIndividual {

	public static void main(String[] args) {
		CustomRandom util = new CustomRandom();

		FlashcardDataSet data = new FlashcardDataSet("DataSets/TranslatedFlashcards.txt", util);
		
		int numOfTrainingEpochs = 100000;
		int displayReportPeriod = 10000;
		int showEpochPeriod = 1000;
		int checkMinimumPeriod = 100;
		String savePath = "Models/CardToSentence.txt";
		
		Model tempModel;
		double loss;
		
		ArrayList<Model> modelList = new ArrayList<>();
		tempModel = new AverageModel(data.getTrainingDataSteps());
		loss = (new Trainer()).train(numOfTrainingEpochs, tempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(tempModel.toString()+" : "+loss);
		modelList.add(tempModel);

		
		Model[] models = modelList.toArray(new Model[0]);
		
		
		for(Model originalModel: models) {
			DataSplitOp splitOp = Splitter.getBestDataSplit(data.getTrainingDataSteps(), originalModel, data.getDataPrep());
			ArrayList<DataStep> badValues = Splitter.getStepsNotInSplit(data.getTrainingDataSteps(), splitOp);
			
			for(int i=0;i<=7;i++) {
				Model otherModel = getModelFromIndex(i,badValues,data.getDataPrep(),util);				
				(new Trainer()).train(numOfTrainingEpochs, otherModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
				Model model = new SplittingEnsembleModel(originalModel, otherModel, splitOp);
				double tempLoss = (new Trainer()).train(numOfTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
				StringBuilder tempBuilder = new StringBuilder();
				splitOp.toString(tempBuilder);
				String tempString = "Splitting Model with split "+tempBuilder.toString()+" with good model "+originalModel.toString()+" and bad model "+otherModel.toString()+": "+tempLoss; 
				System.out.println(tempString);
				DataExport.appendToTextFile(tempString, "Models/ParameterTuning.txt");

			}
		
		
			
			
			
		}
		
		
	}

	public static Model getModelFromIndex(int index, ArrayList<DataStep> trainingData, DataPreparation dataPrep, CustomRandom rand) {
		switch(index) {
		case 0:
			return new AdvancedCopying(2, DataPreparation.FIXED_VECTOR_SIZE);
		case 1:
			return new AverageModel(trainingData);
		case 2:
			return new BasicCopying();
		case 3:
			return new CategoricPortionProbabilityModelForCharacter(trainingData, 3, dataPrep, rand);
		case 4:
			return new CharacterManipulationFromStringDistance(trainingData, dataPrep, rand);
		case 5:
			return new FeedForwardLayer(DataPreparation.FIXED_VECTOR_SIZE, DataPreparation.FIXED_VECTOR_SIZE, new RoughTanhUnit(), rand);
		case 6:
			return new LinearLayer(DataPreparation.FIXED_VECTOR_SIZE, DataPreparation.FIXED_VECTOR_SIZE, rand);
		case 7:
			return new NeuralNetwork(2, DataPreparation.FIXED_VECTOR_SIZE, 10, DataPreparation.FIXED_VECTOR_SIZE, rand);	
		default:
			return new BasicCopying();
		}
	}
	
	
}*/
