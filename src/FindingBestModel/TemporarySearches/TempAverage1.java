package FindingBestModel.TemporarySearches;

import DataSplitting.DataSplitOperation;
import DataSplitting.WithinCertainInterval;
import DataStructures.FlashcardDataSet;
import FileManipulation.DataImport;
import GeneralUtilities.CustomRandom;
import Models.*;
import NonlinearityFunctions.RoughTanhUnit;
import Training.DataProcessing;
import Training.ModelTrainer;

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
