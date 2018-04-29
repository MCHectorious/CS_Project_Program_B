package FindingBestModel.TemporarySearches;

import DataStructures.FlashcardDataSet;
import FileManipulation.DataExport;
import FileManipulation.DataImport;
import GeneralUtilities.CustomRandom;
import GeneralUtilities.Utilities;
import Models.*;
import NonlinearityFunctions.RoughTanhUnit;
import Training.DataProcessing;
import Training.ModelTrainer;

import java.util.ArrayList;


public class TempStacking1 {

	public static void main(String[] args) {
		CustomRandom random = new CustomRandom();

		FlashcardDataSet data = new FlashcardDataSet("DataSets/TranslatedFlashcards.txt", random);
		
		int maximumTrainingEpochs = 10000000;
		int displayReportPeriod = 100000;
		int showEpochPeriod = 50;
		int checkMinimumPeriod = 50;
		String savePath = "Models/TempStackModel1.txt";
			
		ArrayList<Model> modelList = new ArrayList<>();

        Model bestTemporaryModel;


		ArrayList<String> lineFromTextFile = DataImport.getLinesFromTextFile("Models/TempModel8.txt");
		ArrayList<Layer> layers = new ArrayList<>();
		double[] biases = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(1).substring(9) );
		double[] weights = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(2).substring(8) );
		layers.add( new FeedForwardLayer(weights, biases, new RoughTanhUnit()));
		biases = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(4).substring(9) );
		weights = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(5).substring(8) );
		layers.add( new FeedForwardLayer(weights, biases, new RoughTanhUnit()));
		biases = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(7).substring(9) );
		weights = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(8).substring(8) );
		layers.add( new FeedForwardLayer(weights, biases, new RoughTanhUnit()));
		bestTemporaryModel = new NeuralNetworkModel(layers);
		modelList.add(bestTemporaryModel);

		lineFromTextFile = DataImport.getLinesFromTextFile("Models/TempModel7.txt");
		ArrayList<Layer> layers2 = new ArrayList<>();
		biases = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(1).substring(9) );
		weights = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(2).substring(8) );
		layers2.add( new FeedForwardLayer(weights, biases, new RoughTanhUnit()));
		biases = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(4).substring(9) );
		weights = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(5).substring(8) );
		layers2.add( new FeedForwardLayer(weights, biases, new RoughTanhUnit()));
		bestTemporaryModel = new NeuralNetworkModel(layers2);
		modelList.add(bestTemporaryModel);

		bestTemporaryModel = new ProportionalProbabilityForCharacterModel(data.getTrainingDataSteps(), 2, data.getDataProcessing(), random);
		modelList.add(bestTemporaryModel);

		int attempts = 2;
		double total;

		total = 0;
		for(int i=0;i<attempts;i++){
			Model combiningModel = new FeedForwardLayer(3*DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, new RoughTanhUnit(), random);
			Model model = new StackingEnsembleModel(combiningModel,modelList,DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR,DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR,random);
			total += (new ModelTrainer("Models/TempStackLoss1.txt")).train(maximumTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, random);

		}
		double average = total/(double) attempts;
		String tempString = "Stacking Model with model "+"Feed forward layer"+" with Models "+Utilities.arrayToString(modelList.toArray(new Model[0]))+":\t"+average;
		System.out.println(tempString);
		DataExport.appendToTextFile(tempString, "Models/ParameterTuning.txt");
		System.gc();



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
		loss = (new ModelTrainer()).train(numOfTrainingEpochs, tempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(tempModel.toString()+" : "+loss);
		modelList.add(tempModel);

		
		Model[] Models = modelList.toArray(new Model[0]);
		
		
		for(Model originalModel: Models) {
			DataSplitOperation splitOp = DataSplitter.getBestDataSplit(data.getTrainingDataSteps(), originalModel, data.getDataProcessing());
			ArrayList<DataStep> badValues = DataSplitter.getStepsNotInSplit(data.getTrainingDataSteps(), splitOp);
			
			for(int i=0;i<=7;i++) {
				Model otherModel = getModelFromIndex(i,badValues,data.getDataProcessing(),util);
				(new ModelTrainer()).train(numOfTrainingEpochs, otherModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
				Model model = new SplittingEnsembleModel(originalModel, otherModel, splitOp);
				double tempLoss = (new ModelTrainer()).train(numOfTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
				StringBuilder tempBuilder = new StringBuilder();
				splitOp.toString(tempBuilder);
				String tempString = "Splitting Model with split "+tempBuilder.toString()+" with good model "+originalModel.toString()+" and bad model "+otherModel.toString()+": "+tempLoss; 
				System.out.println(tempString);
				DataExport.appendToTextFile(tempString, "Models/ParameterTuning.txt");

			}
		
		
			
			
			
		}
		
		
	}

	public static Model getModelFromIndex(int index, ArrayList<DataStep> trainingData, DataProcessing dataPrep, CustomRandom random) {
		switch(index) {
		case 0:
			return new AdvancedCopyingModel(2, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR);
		case 1:
			return new AverageModel(trainingData);
		case 2:
			return new BasicCopyingModel();
		case 3:
			return new ProportionalProbabilityForCharacterModel(trainingData, 3, dataPrep, random);
		case 4:
			return new CharacterManipulationFromStringDistanceModel(trainingData, dataPrep, random);
		case 5:
			return new FeedForwardLayer(DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, new RoughTanhUnit(), random);
		case 6:
			return new LinearLayer(DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, random);
		case 7:
			return new NeuralNetworkModel(2, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, 10, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, random);
		default:
			return new BasicCopyingModel();
		}
	}
	
	
}*/
