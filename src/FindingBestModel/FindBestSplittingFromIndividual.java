package FindingBestModel;

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


public class FindBestSplittingFromIndividual {

	public static void main(String[] args) {
		CustomRandom random = new CustomRandom();
		FlashcardDataSet data = new FlashcardDataSet("DataSets/TranslatedFlashcards.txt", random);
		
		int maximumTrainingEpochs = 10000000;
		int displayReportPeriod = 100000;
		int showEpochPeriod = 10000;
		int checkMinimumPeriod = 50;
		String savePath = "Models/CardToSentence.txt";
			
		ArrayList<Model> modelList = new ArrayList<>();
		
		int attempts = 3;

        Model bestTemporaryModel;
		double minimumLoss;
		
		ArrayList<String> lineFromTextFile = DataImport.getLinesFromTextFile("bestModel/BestLinearLayerWeights.txt");
		double[] weights = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(0) );
        bestTemporaryModel = new LinearLayer(weights, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR);
		minimumLoss = (new ModelTrainer()).train(maximumTrainingEpochs, bestTemporaryModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, random);
		System.out.println(bestTemporaryModel.toString()+" : "+minimumLoss);
		modelList.add(bestTemporaryModel);

		lineFromTextFile = DataImport.getLinesFromTextFile("bestModel/BestFeedForwardLayerParams.txt");
		double[] biases = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(0) );
		weights = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(1) );
		bestTemporaryModel = new FeedForwardLayer(weights, biases, new RoughTanhUnit());
		minimumLoss = (new ModelTrainer()).train(maximumTrainingEpochs, bestTemporaryModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, random);
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

		lineFromTextFile = DataImport.getLinesFromTextFile("bestModel/Best2LayerNeuralNetwork.txt");
		ArrayList<Layer> layers = new ArrayList<>();
		double[] biasesForFirstLayerOf2LayerNeuralNetwork = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(1) );
		double[] weightsForFirstLayerOf2LayerNeuralNetwork = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(2) );
		layers.add(new FeedForwardLayer(weightsForFirstLayerOf2LayerNeuralNetwork, biasesForFirstLayerOf2LayerNeuralNetwork, new RoughTanhUnit()));
		double[] biasesForSecondLayerOf2LayerNeuralNetwork = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(4) );
		double[] weightsForSecondLayerOf2LayerNeuralNetwork = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(5) );
		layers.add(new FeedForwardLayer(weightsForSecondLayerOf2LayerNeuralNetwork, biasesForSecondLayerOf2LayerNeuralNetwork, new RoughTanhUnit()));
        bestTemporaryModel = new NeuralNetworkModel(layers);
		minimumLoss = (new ModelTrainer()).train(maximumTrainingEpochs, bestTemporaryModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, random);
		System.out.println(bestTemporaryModel.toString()+" : "+minimumLoss);
		modelList.add(bestTemporaryModel);
		
		lineFromTextFile = DataImport.getLinesFromTextFile("bestModel/Best3LayerNeuralNetwork.txt");
		ArrayList<Layer> layersFor3LayerNeuralNetwork = new ArrayList<>();
		double[] biasesForFirstLayerOf3LayerNeuralNetwork = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(1) );
		double[] weightsForFirstLayerOf3LayerNeuralNetwork = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(2) );
		layersFor3LayerNeuralNetwork.add(new FeedForwardLayer(weightsForFirstLayerOf3LayerNeuralNetwork, biasesForFirstLayerOf3LayerNeuralNetwork, new RoughTanhUnit()));
		double[] biasesForSecondLayerOf3LayerNeuralNetwork = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(4) );
		double[] weightsForSecondLayerOf3LayerNeuralNetwork = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(5) );
		layersFor3LayerNeuralNetwork.add(new FeedForwardLayer(weightsForSecondLayerOf3LayerNeuralNetwork, biasesForSecondLayerOf3LayerNeuralNetwork, new RoughTanhUnit()));
		double[] biasesForThirdLayerOf3LayerNeuralNetwork = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(7) );
		double[] weightsForThridLayerOf3LayerNeuralNetwork = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(8) );
        layersFor3LayerNeuralNetwork.add(new FeedForwardLayer(weightsForThridLayerOf3LayerNeuralNetwork, biasesForThirdLayerOf3LayerNeuralNetwork, new RoughTanhUnit()));
        bestTemporaryModel = new NeuralNetworkModel(layersFor3LayerNeuralNetwork);
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

		Model[] models = modelList.toArray(new Model[0]);
		
		System.gc();
		
		double total, average;
		
		for(Model originalModel: models) {
            DataSplitOperation splitOp = DataSplitter.getBestDataSplit(data.getTrainingDataSteps(), originalModel, data.getDataProcessing());
			ArrayList<DataStep> badValues = DataSplitter.getStepsNotInSplit(data.getTrainingDataSteps(), splitOp);
			
			for(int i=0;i<=9;i++) {
				total = 0.0;
				Model otherModel = null;
				for(int j=0;j<attempts;j++) {
					otherModel = getModelFromIndex(i,badValues,data.getDataProcessing(),random);
					Model model = new SplittingEnsembleModel(originalModel, otherModel, splitOp);
					total += (new ModelTrainer()).train(maximumTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, random);
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
			return new AverageModel(trainingData);
		case 2:
            return new BasicCopyingModel();
		case 3:
            return new ProportionalProbabilityForCharacterModel(trainingData, 4, dataPrep, rand);
		case 4:
            return new CharacterManipulationFromStringDistanceModel(trainingData, dataPrep, rand);
		case 5:
            return new FeedForwardLayer(DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, new RoughTanhUnit(), rand);
		case 6:
            return new LinearLayer(DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, rand);
		case 7:
            return new NeuralNetworkModel(new int[]{1, 0}, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, new int[]{20}, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, rand);
		case 8:
            return new NeuralNetworkModel(new int[]{0, 0}, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, new int[]{2}, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, rand);
		case 9:
            return new NeuralNetworkModel(new int[]{0, 0, 0}, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, new int[]{3, 2}, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, rand);

		default:
            return new BasicCopyingModel();
		}
	}

}