package findingBestModel;

import java.util.ArrayList;

import dataStructures.DataStep;
import dataStructures.FlashcardDataSet;
import fileIO.DataExport;
import matrix.Vector;
import model.*;
import nonlinearity.RoughTanhUnit;
import training.DataPreparation;
import training.Trainer;
import util.CustomRandom;
import util.Util;

public class FindBestIndividualModel {

	public static void main(String[] args) {
		//Model bestModel;
		//double bestLoss = Double.MAX_VALUE;
		int attempts = 10;
		CustomRandom util = new CustomRandom();

		FlashcardDataSet data = new FlashcardDataSet("DataSets/TranslatedFlashcards.txt", util);

		String savePath = "Models/CardToSentence.txt";

		double total,average;
		
		int numOfTrainingEpochs = 10000000;
		int displayReportPeriod = 1;
		int showEpochPeriod = 20000;
		int checkMinimumPeriod = 40;
		
		/*
		for(int size=0;size<5;size++) {
			total = 0.0;
			for(int i=0;i<attempts;i++) {
				Model model = new AdvancedCopying(size, DataPreparation.FIXED_VECTOR_SIZE);
				total += (new Trainer()).train(numOfTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
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
			total += (new Trainer()).train(numOfTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		}
		average = total/(double) attempts;
		String tempString ="Average Model:\t"+average;
		System.out.println(tempString);
		DataExport.appendToTextFile(tempString, "Models/ParameterTuning.txt");
		System.gc();
		/*
		
		total = 0.0;
		for(int i=0;i<attempts;i++) {
			Model model = new BasicCopying();
			total += (new Trainer()).train(numOfTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		}
		average = total/(double) attempts;
		tempString ="Basic Copying Model:\t"+average;
		System.out.println(tempString);
		DataExport.appendToTextFile(tempString, "Models/ParameterTuning.txt");
		System.gc();


		for(int size=0;size<10;size++) {
			total = 0.0;
			for(int i=0;i<attempts;i++) {
				Model model = new CategoricPortionProbabilityModelForCharacter(data.getTrainingDataSteps(), size, data.getDataPrep(), util);
				total += (new Trainer()).train(numOfTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
			}
			average = total/(double) attempts;
			tempString ="Categoric Prob for Character with size of "+size+":\t"+average; 
			System.out.println(tempString);
			DataExport.appendToTextFile(tempString, "Models/ParameterTuning.txt");
			System.gc();
		}
		
		total = 0.0;
		for(int i=0;i<attempts;i++) {
			Model model = new CharacterManipulationFromStringDistance(data.getTrainingDataSteps(), data.getDataPrep(), util);
			total += (new Trainer()).train(numOfTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		}
		average = total/(double) attempts;
		tempString = "Character Manipulation From String Distance Model:\t"+average; 
		System.out.println(tempString);
		DataExport.appendToTextFile(tempString, "Models/ParameterTuning.txt");

		System.gc();
		
		
		total = 0.0;
		for(int i=0;i<attempts;i++) {
			Model model = new FeedForwardLayer(DataPreparation.FIXED_VECTOR_SIZE, DataPreparation.FIXED_VECTOR_SIZE, new RoughTanhUnit(), util);
			total += (new Trainer()).train(numOfTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		}
		average = total/(double) attempts;
		tempString = "FeedForward Layer Model:\t"+average; 
		System.out.println(tempString);
		DataExport.appendToTextFile(tempString, "Models/ParameterTuning.txt");

		System.gc();

		
		total = 0.0;
		for(int i=0;i<attempts;i++) {
			Model model = new LinearLayer(DataPreparation.FIXED_VECTOR_SIZE, DataPreparation.FIXED_VECTOR_SIZE, util);
			total += (new Trainer()).train(numOfTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
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
					
					//.out.println(Util.arrayToString(hiddenDims[b]));
					for(int i=0;i<attempts;i++) {
						Model model = new NeuralNetwork(layerTypes[a],DataPreparation.FIXED_VECTOR_SIZE,hiddenDims[b],DataPreparation.FIXED_VECTOR_SIZE,util);
						total += (new Trainer()).train(numOfTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
					}
					average = total/(double) attempts;
					tempString = "Neural Network with layers "+Util.arrayToString(layerTypes[a])+"and hidden dimensions "+Util.arrayToString(hiddenDims[b])+"  Model:\t"+average; 
					System.out.println(tempString);
					DataExport.appendToTextFile(tempString, "Models/ParameterTuning.txt");
					System.gc();
				}
			}			
		}
		
		*/
		
	}


	
}
