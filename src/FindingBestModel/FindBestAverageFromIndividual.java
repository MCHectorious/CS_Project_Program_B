package FindingBestModel;

import DataStructures.FlashcardDataSet;
import FileManipulation.DataExport;
import FileManipulation.DataImport;
import GeneralUtilities.CustomRandom;
import GeneralUtilities.Subsets;
import GeneralUtilities.Utilities;
import Models.*;
import NonlinearityFunctions.RoughTanhUnit;
import Training.DataProcessing;
import Training.ModelTrainer;

import java.util.ArrayList;
import java.util.Arrays;



public class FindBestAverageFromIndividual {

	public static void main(String[] args) {
		CustomRandom random = new CustomRandom();

		FlashcardDataSet data = new FlashcardDataSet("DataSets/TranslatedFlashcards.txt", random);
		
		int maximumTrainingEpochs = 10000000;
		int displayReportPeriod = 100000;
		int showEpochPeriod = 10000;
		int checkMinimumPeriod = 50;
		String savePath = "Models/CardToSentence.txt";//Where the best model is to be stored
		
		ArrayList<Model> modelList = new ArrayList<>();//List of Models of which the programs is trying to find the best average

        Model bestTemporaryModel;
		double minimumLoss;
		
		ArrayList<String> linesFromTextFile = DataImport.getLinesFromTextFile("bestModel/BestLinearLayerWeights.txt");
		double[] weights = DataImport.getDoubleArrayFromLine( linesFromTextFile.get(0) );
        bestTemporaryModel = new LinearLayer(weights, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR);
        //Imports the linear layer from the text file
		minimumLoss = (new ModelTrainer()).train(maximumTrainingEpochs, bestTemporaryModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, random);
		//Re-trains because the data steps will be slightly different this time
		System.out.println(bestTemporaryModel.toString()+" : "+minimumLoss);
		modelList.add(bestTemporaryModel);
		
		linesFromTextFile = DataImport.getLinesFromTextFile("bestModel/BestFeedForwardLayerParams.txt");
		double[] biases = DataImport.getDoubleArrayFromLine( linesFromTextFile.get(0) );
		weights = DataImport.getDoubleArrayFromLine( linesFromTextFile.get(1) );
		bestTemporaryModel = new FeedForwardLayer(weights, biases, new RoughTanhUnit());
        //Imports the feed-forward layer from the text file
        minimumLoss = (new ModelTrainer()).train(maximumTrainingEpochs, bestTemporaryModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, random);
        //Re-trains because the data steps will be slightly different this time
        System.out.println(bestTemporaryModel.toString()+" : "+minimumLoss);
		modelList.add(bestTemporaryModel);

        bestTemporaryModel = new ProportionalProbabilityForCharacterModel(data.getTrainingDataSteps(), 9, data.getDataProcessing(), random);
		minimumLoss = (new ModelTrainer()).train(maximumTrainingEpochs, bestTemporaryModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, random);
		System.out.println(bestTemporaryModel.toString()+" : "+minimumLoss);
		modelList.add(bestTemporaryModel);

        bestTemporaryModel = new ProportionalProbabilityForCharacterModel(data.getTrainingDataSteps(), 4, data.getDataProcessing(), random);
		minimumLoss = (new ModelTrainer()).train(maximumTrainingEpochs, bestTemporaryModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, random);
		System.out.println(bestTemporaryModel.toString()+" : "+minimumLoss);
		modelList.add(bestTemporaryModel);

		linesFromTextFile = DataImport.getLinesFromTextFile("bestModel/Best2LayerNeuralNetwork.txt");
		ArrayList<Layer> layers = new ArrayList<>();
		double[] biasesForFirstLayerOf2LayerNeuralNetwork = DataImport.getDoubleArrayFromLine( linesFromTextFile.get(1) );
		double[] weightsForFirstLayerOf2LayerNeuralNetwork = DataImport.getDoubleArrayFromLine( linesFromTextFile.get(2) );
		layers.add(new FeedForwardLayer(weightsForFirstLayerOf2LayerNeuralNetwork, biasesForFirstLayerOf2LayerNeuralNetwork, new RoughTanhUnit()));
		double[] biasesForSecondLayerOf2LayerNeuralNetwork = DataImport.getDoubleArrayFromLine( linesFromTextFile.get(4) );
		double[] weightsForSecondLayerOf2LayerNeuralNetwork = DataImport.getDoubleArrayFromLine( linesFromTextFile.get(5) );
		layers.add(new FeedForwardLayer(weightsForSecondLayerOf2LayerNeuralNetwork, biasesForSecondLayerOf2LayerNeuralNetwork, new RoughTanhUnit()));
        bestTemporaryModel = new NeuralNetworkModel(layers);
        //Imports the neural network from the text file
        minimumLoss = (new ModelTrainer()).train(maximumTrainingEpochs, bestTemporaryModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, random);
		System.out.println(bestTemporaryModel.toString()+" : "+minimumLoss);
		modelList.add(bestTemporaryModel);
		
		linesFromTextFile = DataImport.getLinesFromTextFile("bestModel/Best3LayerNeuralNetwork.txt");
		ArrayList<Layer> layersFor3NN = new ArrayList<>();
		double[] biasesForFirstLayerOf3LayerNeuralNetwork = DataImport.getDoubleArrayFromLine( linesFromTextFile.get(1) );
		double[] weightsForFirstLayerOf3LayerNeuralNetwork = DataImport.getDoubleArrayFromLine( linesFromTextFile.get(2) );
		layersFor3NN.add(new FeedForwardLayer(weightsForFirstLayerOf3LayerNeuralNetwork, biasesForFirstLayerOf3LayerNeuralNetwork, new RoughTanhUnit()));
		double[] biasesForSecondLayerOf3LayerNeuralNetwork = DataImport.getDoubleArrayFromLine( linesFromTextFile.get(4) );
		double[] weightsForSecondLayerOf3LayerNeuralNetwork = DataImport.getDoubleArrayFromLine( linesFromTextFile.get(5) );
		layersFor3NN.add(new FeedForwardLayer(weightsForSecondLayerOf3LayerNeuralNetwork, biasesForSecondLayerOf3LayerNeuralNetwork, new RoughTanhUnit()));
		double[] biasesForThirdLayerOf3LayerNeuralNetwork = DataImport.getDoubleArrayFromLine( linesFromTextFile.get(7) );
		double[] weightsForThridLayerOf3LayerNeuralNetwork = DataImport.getDoubleArrayFromLine( linesFromTextFile.get(8) );
        layersFor3NN.add(new FeedForwardLayer(weightsForThridLayerOf3LayerNeuralNetwork, biasesForThirdLayerOf3LayerNeuralNetwork, new RoughTanhUnit()));
        bestTemporaryModel = new NeuralNetworkModel(layersFor3NN);
		minimumLoss = (new ModelTrainer()).train(maximumTrainingEpochs, bestTemporaryModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, random);
		System.out.println(bestTemporaryModel.toString()+" : "+minimumLoss);
		modelList.add(bestTemporaryModel);
			
		bestTemporaryModel = new AverageModel(data.getTrainingDataSteps());
		minimumLoss = (new ModelTrainer()).train(maximumTrainingEpochs, bestTemporaryModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, random);
		System.out.println(bestTemporaryModel.toString()+" : "+minimumLoss);
		modelList.add(bestTemporaryModel);

        bestTemporaryModel = new CharacterManipulationFromStringDistanceModel(data.getTrainingDataSteps(), data.getDataProcessing(), random);
		minimumLoss = (new ModelTrainer()).train(maximumTrainingEpochs, bestTemporaryModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, random);
		System.out.println(bestTemporaryModel.toString()+" : "+minimumLoss);
		modelList.add(bestTemporaryModel);

		System.gc();

		ArrayList<Model[]> subsets = Subsets.getSubsetsGreaterThanSize1(modelList);

        for(Model[] modelSubset: subsets) {
            Model model = new AveragingEnsembleModel(new ArrayList<>(Arrays.asList(modelSubset)), DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR);
            double loss = (new ModelTrainer()).train(maximumTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, random);
            String tempString = "Averaging Model with Models " + Utilities.arrayToString(modelSubset) + ":\t" + loss;
            System.out.println(tempString);
            DataExport.appendToTextFile(tempString, "Models/ParameterTuning.txt");

            System.gc();
        }

	}

}