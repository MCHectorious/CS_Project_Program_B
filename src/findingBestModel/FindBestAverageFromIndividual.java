package findingBestModel;

import dataStructures.FlashcardDataSet;
import fileManipulation.DataExport;
import fileManipulation.DataImport;
import generalUtilities.CustomRandom;
import generalUtilities.Utilities;
import models.*;
import nonlinearityFunctions.RoughTanhUnit;
import training.DataProcessing;
import training.Trainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class FindBestAverageFromIndividual {

	public static void main(String[] args) {
		CustomRandom util = new CustomRandom();

		FlashcardDataSet data = new FlashcardDataSet("DataSets/TranslatedFlashcards.txt", util);
		
		int numOfTrainingEpochs = 10000000;
		int displayReportPeriod = 100000;
		int showEpochPeriod = 10000;
		int checkMinimumPeriod = 50;
		String savePath = "Models/CardToSentence.txt";
		
		ArrayList<Model> modelList = new ArrayList<>();

        Model bestTempModel;
		double minLoss;
		
		ArrayList<String> lineFromTextFile = DataImport.getLines("bestModel/BestLinearLayerWeights.txt");
		double[] weights = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(0) );
        bestTempModel = new LinearLayer(weights, DataProcessing.FIXED_VECTOR_SIZE);
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

        bestTempModel = new ProportionProbabilityForCharacterModel(data.getTrainingDataSteps(), 9, data.getDataPrep(), util);
		minLoss = (new Trainer()).train(numOfTrainingEpochs, bestTempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);

        bestTempModel = new ProportionProbabilityForCharacterModel(data.getTrainingDataSteps(), 4, data.getDataPrep(), util);
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
        bestTempModel = new NeuralNetworkModel(layers);
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
        bestTempModel = new NeuralNetworkModel(layersFor3NN);
		minLoss = (new Trainer()).train(numOfTrainingEpochs, bestTempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
			
		bestTempModel = new AverageModel(data.getTrainingDataSteps());
		minLoss = (new Trainer()).train(numOfTrainingEpochs, bestTempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);

        bestTempModel = new CharacterManipulationFromStringDistanceModel(data.getTrainingDataSteps(), data.getDataPrep(), util);
		minLoss = (new Trainer()).train(numOfTrainingEpochs, bestTempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		
		Model[] models = modelList.toArray(new Model[0]);
		
		System.gc();

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
                Model model = new AveragingEnsembleModel(new ArrayList<>(Arrays.asList(modelSubset)), DataProcessing.FIXED_VECTOR_SIZE);
		    	double loss = (new Trainer()).train(numOfTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
                String tempString = "Averaging Model with models " + Utilities.arrayToString(modelSubset) + ":\t" + loss;
				System.out.println(tempString);
				DataExport.appendToTextFile(tempString, "Models/ParameterTuning.txt");

				System.gc();
		    }
		}
		
		
		
	}

    private static Model[] getSubset(Model[] input, int[] subset) {
	    Model[] result = new Model[subset.length]; 
	    for (int i = 0; i < subset.length; i++) 
	        result[i] = input[subset[i]];
	    return result;
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
			Model tempModel = new LinearLayer(DataProcessing.FIXED_VECTOR_SIZE, DataProcessing.FIXED_VECTOR_SIZE, util);
			double loss = (new Trainer()).train(numOfTrainingEpochs, tempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
			if(loss<minLoss) {
				minLoss = loss;
				bestTempModel = tempModel;
			}
		}
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);

		minLoss = Double.MAX_VALUE;
		for(int i=0;i<attempts;i++) {
			Model tempModel = new FeedForwardLayer(DataProcessing.FIXED_VECTOR_SIZE, DataProcessing.FIXED_VECTOR_SIZE,new RoughTanhUnit(), util);
			double loss = (new Trainer()).train(numOfTrainingEpochs, tempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
			if(loss<minLoss) {
				minLoss = loss;
				bestTempModel = tempModel;
			}
		}
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		
		minLoss = Double.MAX_VALUE;
		for(int i=0;i<attempts;i++) {
			Model tempModel = new NeuralNetworkModel(new int[] {1,0}, DataProcessing.FIXED_VECTOR_SIZE, new int[] {30}, DataProcessing.FIXED_VECTOR_SIZE, util);
			double loss = (new Trainer()).train(numOfTrainingEpochs, tempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
			if(loss<minLoss) {
				minLoss = loss;
				bestTempModel = tempModel;
			}
		}
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);

		bestTempModel = new ProportionProbabilityForCharacterModel(data.getTrainingDataSteps(), 9, data.getDataPrep(), util);
		minLoss = (new Trainer()).train(numOfTrainingEpochs, bestTempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		
		bestTempModel = new ProportionProbabilityForCharacterModel(data.getTrainingDataSteps(), 4, data.getDataPrep(), util);
		minLoss = (new Trainer()).train(numOfTrainingEpochs, bestTempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		
		
		minLoss = Double.MAX_VALUE;
		for(int i=0;i<attempts;i++) {
			Model tempModel = new NeuralNetworkModel(new int[] {0,0}, DataProcessing.FIXED_VECTOR_SIZE, new int[] {2}, DataProcessing.FIXED_VECTOR_SIZE, util);
			double loss = (new Trainer()).train(numOfTrainingEpochs, tempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
			if(loss<minLoss) {
				minLoss = loss;
				bestTempModel = tempModel;
			}
		}
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		
		minLoss = Double.MAX_VALUE;
		for(int i=0;i<attempts;i++) {
			Model tempModel = new NeuralNetworkModel(new int[] {0,0,0}, DataProcessing.FIXED_VECTOR_SIZE, new int[] {2,1}, DataProcessing.FIXED_VECTOR_SIZE, util);
			double loss = (new Trainer()).train(numOfTrainingEpochs, tempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
			if(loss<minLoss) {
				minLoss = loss;
				bestTempModel = tempModel;
			}
		}
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		
		bestTempModel = new AverageModel(data.getTrainingDataSteps());
		minLoss = (new Trainer()).train(numOfTrainingEpochs, bestTempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		
		bestTempModel = new CharacterManipulationFromStringDistanceModel(data.getTrainingDataSteps(), data.getDataPrep(), util);
		minLoss = (new Trainer()).train(numOfTrainingEpochs, bestTempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
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
		    	Model model = new AveragingEnsembleModel(new ArrayList<>(Arrays.asList(modelSubset)), DataProcessing.FIXED_VECTOR_SIZE);
		    	double loss = (new Trainer()).train(numOfTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
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