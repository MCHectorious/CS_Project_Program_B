package FindingBestModel.TemporarySearches;

import DataSplitting.DataSplitOperation;
import DataSplitting.DataSplitter;
import DataStructures.DataStep;
import DataStructures.FlashcardDataSet;
import FileManipulation.DataExport;
import FileManipulation.DataImport;
import GeneralUtilities.CustomRandom;
import Models.*;
import NonlinearityFunctions.RoughTanhUnit;
import Training.DataProcessing;
import Training.ModelTrainer;

import java.util.ArrayList;


public class TempSplitting2 {

	public static void main(String[] args) {
		CustomRandom random = new CustomRandom();

		FlashcardDataSet data = new FlashcardDataSet("DataSets/TranslatedFlashcards.txt", random);
		
		int maximumTrainingEpochs = 10000000;
		int displayReportPeriod = 100000;
		int showEpochPeriod = 1000;
		int checkMinimumPeriod = 50;
		String savePath = "Models/TempSplitModel2.txt";
			
		ArrayList<Model> modelList = new ArrayList<>();
		
		int attempts = 1;

        Model bestTemporaryModel;

		ArrayList<String> lineFromTextFile = DataImport.getLinesFromTextFile("Models/TempModel5.txt");
		double[] biases = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(0).substring(9) );
		double[] weights = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(1).substring(8) );
		bestTemporaryModel = new FeedForwardLayer(weights, biases, new RoughTanhUnit());
		modelList.add(bestTemporaryModel);

		Model[] models = modelList.toArray(new Model[0]);
		
		System.gc();
		
		double total, average;
		
		for(Model originalModel: models) {
            DataSplitOperation splitOp = DataSplitter.getBestDataSplit(data.getTrainingDataSteps(), originalModel, data.getDataProcessing());
			ArrayList<DataStep> badValues = DataSplitter.getStepsNotInSplit(data.getTrainingDataSteps(), splitOp);
			
			for(int i=0;i<=5;i++) {
				total = 0.0;
				Model otherModel = null;
				for(int j=0;j<attempts;j++) {
					otherModel = getModelFromIndex(i,badValues,data.getDataProcessing(),random);
					Model model = new SplittingEnsembleModel(originalModel, otherModel, splitOp);
					total += (new ModelTrainer("Models/TempSplitLoss2.txt")).train(maximumTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, random);
				}
				average = total / (double) attempts;

				StringBuilder modelEvaluationBuilder = new StringBuilder();
                splitOp.provideDescription(modelEvaluationBuilder);
				String modelEvaluation = "Splitting Model with split "+modelEvaluationBuilder.toString()+" with good model "+originalModel.toString()+" and bad model "+otherModel.toString()+": "+average;
				modelEvaluationBuilder.append(modelEvaluation);
				System.out.println(modelEvaluationBuilder.toString());
				DataExport.appendToTextFile(modelEvaluationBuilder.toString(), "Models/ParameterTuning.txt");

			}

		}

	}

    private static Model getModelFromIndex(int index, ArrayList<DataStep> trainingData, DataProcessing dataPrep, CustomRandom rand) {
		switch(index) {
		case 0:
            return new AdvancedCopyingModel(1, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR);
		case 1:
            return new FeedForwardLayer(DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, new RoughTanhUnit(), rand);
		case 2:
            return new LinearLayer(DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, rand);
		case 3:
            return new NeuralNetworkModel(new int[]{1, 0}, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, new int[]{22}, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, rand);
		case 4:
            return new NeuralNetworkModel(new int[]{0, 0}, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, new int[]{22}, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, rand);
		case 5:
            return new NeuralNetworkModel(new int[]{0, 0, 0}, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, new int[]{5, 2}, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, rand);
		default:
            return new BasicCopyingModel();
		}
	}

}
