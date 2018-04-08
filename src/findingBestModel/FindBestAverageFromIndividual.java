package findingBestModel;

import dataStructures.FlashcardDataSet;
import fileManipulation.DataExport;
import fileManipulation.DataImport;
import generalUtilities.CustomRandom;
import generalUtilities.Subsets;
import generalUtilities.Utilities;
import models.*;
import nonlinearityFunctions.RoughTanhUnit;
import training.DataProcessing;
import training.ModelTrainer;

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
		String savePath = "Models/CardToSentence.txt";
		
		ArrayList<Model> modelList = new ArrayList<>();

        Model bestTemporaryModel;
		double minimumLoss;
		
		ArrayList<String> linesFromTextFile = DataImport.getLinesFromTextFile("bestModel/BestLinearLayerWeights.txt");
		double[] weights = DataImport.getDoubleArrayFromLine( linesFromTextFile.get(0) );
        bestTemporaryModel = new LinearLayer(weights, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR);
		minimumLoss = (new ModelTrainer()).train(maximumTrainingEpochs, bestTemporaryModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, random);
		System.out.println(bestTemporaryModel.toString()+" : "+minimumLoss);
		modelList.add(bestTemporaryModel);
		
		
		linesFromTextFile = DataImport.getLinesFromTextFile("bestModel/BestFeedForwardLayerParams.txt");
		double[] biases = DataImport.getDoubleArrayFromLine( linesFromTextFile.get(0) );
		weights = DataImport.getDoubleArrayFromLine( linesFromTextFile.get(1) );
		bestTemporaryModel = new FeedForwardLayer(weights, biases, new RoughTanhUnit());
		minimumLoss = (new ModelTrainer()).train(maximumTrainingEpochs, bestTemporaryModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, random);
		System.out.println(bestTemporaryModel.toString()+" : "+minimumLoss);
		modelList.add(bestTemporaryModel);

        bestTemporaryModel = new ProportionProbabilityForCharacterModel(data.getTrainingDataSteps(), 9, data.getDataProcessing(), random);
		minimumLoss = (new ModelTrainer()).train(maximumTrainingEpochs, bestTemporaryModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, random);
		System.out.println(bestTemporaryModel.toString()+" : "+minimumLoss);
		modelList.add(bestTemporaryModel);

        bestTemporaryModel = new ProportionProbabilityForCharacterModel(data.getTrainingDataSteps(), 4, data.getDataProcessing(), random);
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
            String tempString = "Averaging Model with models " + Utilities.arrayToString(modelSubset) + ":\t" + loss;
            System.out.println(tempString);
            DataExport.appendToTextFile(tempString, "Models/ParameterTuning.txt");

            System.gc();
        }

		
		
		
	}

	
}


/*
public class FindBestAverageFromIndividual {

	public static void main(String[] args) {
		CustomRandom util = new CustomRandom();

		FlashcardDataSet data = new FlashcardDataSet("DataSets/TranslatedFlashcards.txt", util);
		
		int numOfTrainingEpochs = 10000000;
		int displayReportPeriod = 100000;
		int showEpochPeriod = 10000;
		int checkMinimumPeriod = 50;
		int attempts = 5;
		String savePath = "Models/CardToSentence.txt";
		
		ArrayList<Model> modelList = new ArrayList<>();
		
		Model bestTempModel = new BasicCopyingModel();
		double minLoss;
		
		minLoss = Double.MAX_VALUE;
		for(int i=0;i<attempts;i++) {
			Model tempModel = new LinearLayer(DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, util);
			double loss = (new ModelTrainer()).train(numOfTrainingEpochs, tempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
			if(loss<minLoss) {
				minLoss = loss;
				bestTempModel = tempModel;
			}
		}
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);

		minLoss = Double.MAX_VALUE;
		for(int i=0;i<attempts;i++) {
			Model tempModel = new FeedForwardLayer(DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR,new RoughTanhUnit(), util);
			double loss = (new ModelTrainer()).train(numOfTrainingEpochs, tempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
			if(loss<minLoss) {
				minLoss = loss;
				bestTempModel = tempModel;
			}
		}
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		
		minLoss = Double.MAX_VALUE;
		for(int i=0;i<attempts;i++) {
			Model tempModel = new NeuralNetworkModel(new int[] {1,0}, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, new int[] {30}, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, util);
			double loss = (new ModelTrainer()).train(numOfTrainingEpochs, tempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
			if(loss<minLoss) {
				minLoss = loss;
				bestTempModel = tempModel;
			}
		}
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);

		bestTempModel = new ProportionProbabilityForCharacterModel(data.getTrainingDataSteps(), 9, data.getDataProcessing(), util);
		minLoss = (new ModelTrainer()).train(numOfTrainingEpochs, bestTempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		
		bestTempModel = new ProportionProbabilityForCharacterModel(data.getTrainingDataSteps(), 4, data.getDataProcessing(), util);
		minLoss = (new ModelTrainer()).train(numOfTrainingEpochs, bestTempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		
		
		minLoss = Double.MAX_VALUE;
		for(int i=0;i<attempts;i++) {
			Model tempModel = new NeuralNetworkModel(new int[] {0,0}, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, new int[] {2}, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, util);
			double loss = (new ModelTrainer()).train(numOfTrainingEpochs, tempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
			if(loss<minLoss) {
				minLoss = loss;
				bestTempModel = tempModel;
			}
		}
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		
		minLoss = Double.MAX_VALUE;
		for(int i=0;i<attempts;i++) {
			Model tempModel = new NeuralNetworkModel(new int[] {0,0,0}, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, new int[] {2,1}, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, util);
			double loss = (new ModelTrainer()).train(numOfTrainingEpochs, tempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
			if(loss<minLoss) {
				minLoss = loss;
				bestTempModel = tempModel;
			}
		}
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		
		bestTempModel = new AverageModel(data.getTrainingDataSteps());
		minLoss = (new ModelTrainer()).train(numOfTrainingEpochs, bestTempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		
		bestTempModel = new CharacterManipulationFromStringDistanceModel(data.getTrainingDataSteps(), data.getDataProcessing(), util);
		minLoss = (new ModelTrainer()).train(numOfTrainingEpochs, bestTempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		
		Model[] models = modelList.toArray(new Model[0]);
		
		

		for (int k=2;k<=models.length;k++) {
		    List<Model[]> subsets = new ArrayList<>();
		    int[] s = new int[k];  
		    for (int i = 0; (s[i] = i) < k - 1; i++);  
		    subsets.add(getSubset(models, s));
		    for(;;) {
		        int i;
		        for (i = k - 1; i >= 0 && s[i] == models.length - k + i; i--); 
		        if (i < 0) {
		            break;
		        }
		        s[i]++;                    
		        for (++i; i < k; i++) {   
		            s[i] = s[i - 1] + 1; 
		        }
		        subsets.add(getSubset(models, s));
		    }		    
		    for(Model[] modelSubset: subsets) {
		    	Model model = new AveragingEnsembleModel(new ArrayList<>(Arrays.asList(modelSubset)), DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR);
		    	double loss = (new ModelTrainer()).train(numOfTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
				String tempString = "Averaging Model with models "+Utilities.arrayToString(modelSubset)+":\t"+loss;
				System.out.println(tempString);
				DataExport.appendToTextFile(tempString, "Models/ParameterTuning.txt");

				System.gc();
		    }
		}
		
		
		
	}
	
	static Model[] getSubset(Model[] input, int[] subset) {
	    Model[] result = new Model[subset.length]; 
	    for (int i = 0; i < subset.length; i++) 
	        result[i] = input[subset[i]];
	    return result;
	}
	
}
*/