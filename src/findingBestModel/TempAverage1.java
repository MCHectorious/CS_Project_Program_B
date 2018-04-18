package findingBestModel;

import dataSplitting.DataSplitOperation;
import dataSplitting.WithinCertainInterval;
import dataStructures.FlashcardDataSet;
import fileManipulation.DataExport;
import fileManipulation.DataImport;
import generalUtilities.CustomRandom;
import generalUtilities.Utilities;
import models.*;
import nonlinearityFunctions.RoughTanhUnit;
import training.DataProcessing;
import training.ModelTrainer;

import java.util.ArrayList;


public class TempAverage1 {

	public static void main(String[] args) {
		CustomRandom random = new CustomRandom();

		FlashcardDataSet data = new FlashcardDataSet("DataSets/TranslatedFlashcards.txt", random);
		
		int maximumTrainingEpochs = 10000000;
		int displayReportPeriod = 100000;
		int showEpochPeriod = 1000;
		int checkMinimumPeriod = 50;
		String savePath = "Models/TempStackModel1.txt";
			
		ArrayList<Model> modelList = new ArrayList<>();

        Model bestTemporaryModel;

		DataSplitOperation splitOp = new WithinCertainInterval(55, 0.5, 0.25);

		ArrayList<String> lineFromTextFile = DataImport.getLinesFromTextFile("Models/TempSplitModel1.txt");
		double[] weights = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(1).substring(8) );
		Model goodModel = new LinearLayer(weights, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR);
		weights = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(2).substring(8) );
		Model badModel = new LinearLayer(weights, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR);

		Model linLin = new SplittingEnsembleModel(goodModel, badModel, splitOp);
		modelList.add(linLin);


		splitOp = new WithinCertainInterval(60,0.5,0.25);

		lineFromTextFile = DataImport.getLinesFromTextFile("Models/TempSplitModel2.txt");
		double[] biases = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(1).substring(9) );
		weights = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(2).substring(8) );
		goodModel = new FeedForwardLayer(weights, biases, new RoughTanhUnit());
		weights = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(3).substring(8) );
		badModel = new LinearLayer(weights, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR);

		Model feedLin = new SplittingEnsembleModel(goodModel, badModel, splitOp);
		modelList.add(feedLin);

		Model model = new AveragingEnsembleModel(modelList, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR);
		double loss = (new ModelTrainer("Models/TempAverageLoss1.txt")).train(maximumTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, random);
		System.out.println("loss"+loss);

		loss = (new ModelTrainer("Models/TempAverageLoss1.txt")).train(maximumTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, random);
		System.out.println("loss"+loss);

		loss = (new ModelTrainer("Models/TempAverageLoss1.txt")).train(maximumTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, random);
		System.out.println("loss"+loss);


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

		
		Model[] models = modelList.toArray(new Model[0]);
		
		
		for(Model originalModel: models) {
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
			return new ProportionProbabilityForCharacterModel(trainingData, 3, dataPrep, random);
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
