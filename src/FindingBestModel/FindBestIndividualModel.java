package FindingBestModel;

import DataStructures.FlashcardDataSet;
import FileManipulation.DataExport;
import GeneralUtilities.CustomRandom;
import Models.AverageModel;
import Models.Model;
import Training.ModelTrainer;

public class FindBestIndividualModel {

	public static void main(String[] args) {
		int attempts = 10;
		CustomRandom random = new CustomRandom();
		FlashcardDataSet data = new FlashcardDataSet("DataSets/TranslatedFlashcards.txt", random);
		String savePath = "Models/CardToSentence.txt";

		double total,average;
		
		int maximumTrainingEpochs = 10000000;
		int displayReportPeriod = 1;
		int showEpochPeriod = 20000;
		int checkMinimumPeriod = 40;
		
		/*
		for(int size=0;size<5;size++) {
			total = 0.0;
			for(int i=0;i<attempts;i++) {
				Model model = new AdvancedCopyingModel(size, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR);
				total += (new ModelTrainer()).train(numOfTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
			}
			average = total/(double) attempts;
			String tempString = "Advanced Copying with size of "+size+":\t"+average;
			System.out.println(tempString);
			DataExport.appendToTextFile(tempString, "Models/ParameterTuning.txt");
		}
		System.gc();
		*/
		total = 0.0;
		for(int i=0;i<attempts;i++) {
			Model model = new AverageModel(data.getTrainingDataSteps());
			total += (new ModelTrainer()).train(maximumTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, random);
		}
		average = total/(double) attempts;
		String evaluationOfModel ="Average Model:\t"+average;
		System.out.println(evaluationOfModel);
		DataExport.appendToTextFile(evaluationOfModel, "Models/ParameterTuning.txt");
		System.gc();
		/*
		
		total = 0.0;
		for(int i=0;i<attempts;i++) {
			Model model = new BasicCopyingModel();
			total += (new ModelTrainer()).train(numOfTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		}
		average = total/(double) attempts;
		tempString ="Basic Copying Model:\t"+average;
		System.out.println(tempString);
		DataExport.appendToTextFile(tempString, "Models/ParameterTuning.txt");
		System.gc();


		for(int size=0;size<10;size++) {
			total = 0.0;
			for(int i=0;i<attempts;i++) {
				Model model = new ProportionalProbabilityForCharacterModel(data.getTrainingDataSteps(), size, data.getDataProcessing(), util);
				total += (new ModelTrainer()).train(numOfTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
			}
			average = total/(double) attempts;
			tempString ="Categoric Prob for Character with size of "+size+":\t"+average; 
			System.out.println(tempString);
			DataExport.appendToTextFile(tempString, "Models/ParameterTuning.txt");
			System.gc();
		}
		
		total = 0.0;
		for(int i=0;i<attempts;i++) {
			Model model = new CharacterManipulationFromStringDistanceModel(data.getTrainingDataSteps(), data.getDataProcessing(), util);
			total += (new ModelTrainer()).train(numOfTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		}
		average = total/(double) attempts;
		tempString = "Character Manipulation From String Distance Model:\t"+average; 
		System.out.println(tempString);
		DataExport.appendToTextFile(tempString, "Models/ParameterTuning.txt");

		System.gc();
		
		
		total = 0.0;
		for(int i=0;i<attempts;i++) {
			Model model = new FeedForwardLayer(DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, new RoughTanhUnit(), util);
			total += (new ModelTrainer()).train(numOfTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		}
		average = total/(double) attempts;
		tempString = "FeedForward Layer Model:\t"+average; 
		System.out.println(tempString);
		DataExport.appendToTextFile(tempString, "Models/ParameterTuning.txt");

		System.gc();

		
		total = 0.0;
		for(int i=0;i<attempts;i++) {
			Model model = new LinearLayer(DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, util);
			total += (new ModelTrainer()).train(numOfTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		}
		average = total/(double) attempts;
		tempString = "Linear Layer Model:\t"+average; 
		System.out.println(tempString);
		DataExport.appendToTextFile(tempString, "Models/ParameterTuning.txt");
		System.gc();
		
		
		
		int [] layerTypeSet = {0,1};
		int [] hiddenDimensionSet = {1,2,3,4,5,6,7,8,9,10};
		int maxArraySize = 5;
		
		String tempString;
		
		for(int j=4;j<=maxArraySize;j++) {
			int[][] layerTypes = new int[(int) Math.pow(layerTypeSet.length, j)][j];
			for(int i=0;i<=Math.pow(layerTypeSet.length, j)-1;i++) {
				for(int k=0;k<j;k++) {
					layerTypes[i][k] =  layerTypeSet[ (int) Math.floor((i)/(int) Math.floor(Math.pow(layerTypeSet.length, k)) ) % layerTypeSet.length ]; 
				}
			}			
			int[][] hiddenDims = new int[(int) Math.pow(hiddenDimensionSet.length, j-1)][j-1];
			for(int i=0;i<=Math.pow(hiddenDimensionSet.length, j-1)-1;i++) {
				for(int k=0;k<j-1;k++) {
					hiddenDims[i][k] =  hiddenDimensionSet[ (int) Math.floor((i)/(int) Math.floor(Math.pow(hiddenDimensionSet.length, k)) ) % hiddenDimensionSet.length ];
				}
			}
			for(int a=0;a<layerTypes.length;a++) {
				if(layerTypes[a][layerTypes[a].length-1]==1) {
						continue;
				}
				for(int b=0;b<hiddenDims.length;b++) {
					total = 0.0;
					
					//.out.println(Utilities.arrayToString(hiddenDims[b]));
					for(int i=0;i<attempts;i++) {
						Model model = new NeuralNetworkModel(layerTypes[a],DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR,hiddenDims[b],DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR,util);
						total += (new ModelTrainer()).train(numOfTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
					}
					average = total/(double) attempts;
					tempString = "Neural Network with layers "+Utilities.arrayToString(layerTypes[a])+"and hidden dimensions "+Utilities.arrayToString(hiddenDims[b])+"  Model:\t"+average;
					System.out.println(tempString);
					DataExport.appendToTextFile(tempString, "Models/ParameterTuning.txt");
					System.gc();
				}
			}			
		}
		
		*/
		
	}


	
}
