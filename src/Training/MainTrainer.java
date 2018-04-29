package Training;

import DataStructures.FlashcardDataSet;
import GeneralUtilities.CustomRandom;
import Models.*;

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
		
		int[] layerTypes = {1,1,1,0};
		int[] hiddenDimensions = {50,10,100};
		NeuralNetworkModel model = new NeuralNetworkModel(layerTypes, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, hiddenDimensions, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, util);
		int trainingEpochs = 100000000;
		int displayReportPeriod = 10;
		int showEpochPeriod = 1;
		int checkMinimumPeriod = 30;
		
		(new ModelTrainer()).train(trainingEpochs,  model, data, displayReportPeriod, showEpochPeriod,checkMinimumPeriod, savePath, util);
		 
	}
	
	private static void TrainEnsembleModel() {
	CustomRandom util = new CustomRandom();
		
		FlashcardDataSet data = new FlashcardDataSet("DataSets/TranslatedFlashcards.txt", util);
		System.out.println("Got this far ");
		String savePath = "Models/CardToSentence.txt";
	
		int numOfLayers = 3;
		int hiddenDimension = 10;
	
		ArrayList<Model> models = new ArrayList<>();

		//Models.add(new NeuralNetworkModel(6, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, 3, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR,util));

		//Models.add(new NeuralNetworkModel(3, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, 150, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, util));
		
		//for(int i=0;i<3;i++) {
		//	Models.add(new NeuralNetworkModel(numOfLayers, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, hiddenDimension,DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, util));
		//}
		models.add(new BasicCopyingModel());
		//System.out.println("Got this far 3");
		models.add(new AdvancedCopyingModel(2, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR));
		//System.out.println("Got this far 2");
		models.add(new ProportionalProbabilityForCharacterModel(data.getTrainingDataSteps(), 1, data.getDataProcessing(), util));
		
		//System.out.println("Got this far 1");
		AveragingEnsembleModel model = new AveragingEnsembleModel(models, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR);

		
		//Copy model = new Copy();

		
		
		
		int trainingEpochs = 100000000;
		int displayReportPeriod = 10;
		int showEpochPeriod = 1;
		int checkMinimumPeriod = 30;
		System.out.println("Got this far");
		(new ModelTrainer()).train(trainingEpochs,  model, data, displayReportPeriod, showEpochPeriod,checkMinimumPeriod, savePath, util);

	}
	

}
