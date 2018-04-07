package training;

import dataStructures.FlashcardDataSet;
import generalUtilities.CustomRandom;
import models.*;

import java.util.ArrayList;

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
	
		NeuralNetworkModel model;
		if(initFromSaved) {
			model = NeuralNetworkModel.importFromSaved(savePath);
		}else {
			model = new NeuralNetworkModel(numOfLayers, DataProcessing.FIXED_VECTOR_SIZE, hiddenDimension,DataProcessing.FIXED_VECTOR_SIZE, util);
		}
		
		
		//Copy model = new Copy();
		AdvancedCopyingModel model = new AdvancedCopyingModel(3, DataProcessing.FIXED_VECTOR_SIZE);
		
		*/
		
		//CategoricPortionProbabilityModelForString model = new CategoricPortionProbabilityModelForString(data.getTrainingDataSteps(), 1,2, data.getDataPrep(), util);
		
		
		int[] layerTypes = {1,1,1,0};
		int[] hiddenDimensions = {50,10,100};
		NeuralNetworkModel model = new NeuralNetworkModel(layerTypes, DataProcessing.FIXED_VECTOR_SIZE, hiddenDimensions, DataProcessing.FIXED_VECTOR_SIZE, util);
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

		//models.add(new NeuralNetworkModel(6, DataProcessing.FIXED_VECTOR_SIZE, 3, DataProcessing.FIXED_VECTOR_SIZE,util));

		//models.add(new NeuralNetworkModel(3, DataProcessing.FIXED_VECTOR_SIZE, 150, DataProcessing.FIXED_VECTOR_SIZE, util));
		
		//for(int i=0;i<3;i++) {
		//	models.add(new NeuralNetworkModel(numOfLayers, DataProcessing.FIXED_VECTOR_SIZE, hiddenDimension,DataProcessing.FIXED_VECTOR_SIZE, util));
		//}
		models.add(new BasicCopyingModel());
		//System.out.println("Got this far 3");
		models.add(new AdvancedCopyingModel(2, DataProcessing.FIXED_VECTOR_SIZE));
		//System.out.println("Got this far 2");
		models.add(new ProportionProbabilityForCharacterModel(data.getTrainingDataSteps(), 1, data.getDataPrep(), util));
		
		//System.out.println("Got this far 1");
		AveragingEnsembleModel model = new AveragingEnsembleModel(models, DataProcessing.FIXED_VECTOR_SIZE);

		
		//Copy model = new Copy();

		
		
		
		int trainingEpochs = 100000000;
		int displayReportPeriod = 10;
		int showEpochPeriod = 1;
		int checkMinimumPeriod = 30;
		System.out.println("Got this far");
		(new Trainer()).train(trainingEpochs,  model, data, displayReportPeriod, showEpochPeriod,checkMinimumPeriod, savePath, util);

	}
	

}
