package training;

import java.util.ArrayList;

import dataStructures.FlashcardDataSet;
import models.*;
import generalUtilities.CustomRandom;
import training.Trainer;

public class MainTrainer {
	
	public static void main(String[] args) {
		TrainEnsembleModel();
		//TrainSingleModel();
		
	}
	
	private static void TrainSingleModel() {
		CustomRandom util = new CustomRandom();
		
		FlashcardDataSet data = new FlashcardDataSet("DataSets/TranslatedFlashcards.txt", util);
		String savePath = "Models/CardToSentence.txt";
		
		//DecisionTree tree = new DecisionTree(data.getTrainingDataSteps(),1,data.getDataPrep());
		
		/*boolean initFromSaved = false;
	
		int numOfLayers = 5;
		int hiddenDimension = 20;
	
		NeuralNetwork model;
		if(initFromSaved) {
			model = NeuralNetwork.importFromSaved(savePath);
		}else {
			model = new NeuralNetwork(numOfLayers, DataPreparation.FIXED_VECTOR_SIZE, hiddenDimension,DataPreparation.FIXED_VECTOR_SIZE, util);
		}
		
		
		//Copy model = new Copy();
		AdvancedCopying model = new AdvancedCopying(3, DataPreparation.FIXED_VECTOR_SIZE);
		
		*/
		
		//CategoricPortionProbabilityModelForString model = new CategoricPortionProbabilityModelForString(data.getTrainingDataSteps(), 1,2, data.getDataPrep(), util);
		
		
		int[] layerTypes = {1,1,1,0};
		int[] hiddenDimensions = {50,10,100};
		NeuralNetwork model = new NeuralNetwork(layerTypes, DataPreparation.FIXED_VECTOR_SIZE, hiddenDimensions, DataPreparation.FIXED_VECTOR_SIZE, util);
		int trainingEpochs = 100000000;
		int displayReportPeriod = 10;
		int showEpochPeriod = 1;
		int checkMinimumPeriod = 30;
		
		(new Trainer()).train(trainingEpochs,  model, data, displayReportPeriod, showEpochPeriod,checkMinimumPeriod, savePath, util);
		 
	}
	
	private static void TrainEnsembleModel() {
	CustomRandom util = new CustomRandom();
		
		FlashcardDataSet data = new FlashcardDataSet("DataSets/TranslatedFlashcards.txt", util);
		System.out.println("Got this far ");
		String savePath = "Models/CardToSentence.txt";
	
		int numOfLayers = 3;
		int hiddenDimension = 10;
	
		ArrayList<Model> models = new ArrayList<>();
		
		//models.add(new NeuralNetwork(6, DataPreparation.FIXED_VECTOR_SIZE, 3, DataPreparation.FIXED_VECTOR_SIZE,util));

		//models.add(new NeuralNetwork(3, DataPreparation.FIXED_VECTOR_SIZE, 150, DataPreparation.FIXED_VECTOR_SIZE, util));
		
		//for(int i=0;i<3;i++) {
		//	models.add(new NeuralNetwork(numOfLayers, DataPreparation.FIXED_VECTOR_SIZE, hiddenDimension,DataPreparation.FIXED_VECTOR_SIZE, util));
		//}
		models.add(new BasicCopying());
		//System.out.println("Got this far 3");
		models.add(new AdvancedCopying(2,DataPreparation.FIXED_VECTOR_SIZE));
		//System.out.println("Got this far 2");
		models.add(new CategoricPortionProbabilityModelForCharacter(data.getTrainingDataSteps(), 1, data.getDataPrep(), util));
		
		//System.out.println("Got this far 1");
		AveragingEnsembleModel model = new AveragingEnsembleModel(models, DataPreparation.FIXED_VECTOR_SIZE);

		
		//Copy model = new Copy();

		
		
		
		int trainingEpochs = 100000000;
		int displayReportPeriod = 10;
		int showEpochPeriod = 1;
		int checkMinimumPeriod = 30;
		System.out.println("Got this far");
		(new Trainer()).train(trainingEpochs,  model, data, displayReportPeriod, showEpochPeriod,checkMinimumPeriod, savePath, util);

	}
	

}
