package findingBestModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dataStructures.DataStep;
import dataStructures.FlashcardDataSet;
import fileManipulation.DataExport;
import fileManipulation.DataImport;
import models.*;
import nonlinearityFunctions.RoughTanhUnit;
import training.DataPreparation;
import training.Trainer;
import generalUtilities.CustomRandom;
import generalUtilities.Util;
/*package findBestModel;

import java.util.ArrayList;

import dataStructures.FlashcardDataSet;
import model.BasicCopying;
import model.FeedForwardLayer;
import model.LinearLayer;
import model.Model;
import model.NeuralNetwork;
import nonlinearity.RoughTanhUnit;
import training.DataPreparation;
import training.Trainer;
import util.CustomRandom;

public class TempTestingMain {

	public static void main(String[] args) {

		CustomRandom util = new CustomRandom();

		FlashcardDataSet data = new FlashcardDataSet("DataSets/TranslatedFlashcards.txt", util);
		
		int numOfTrainingEpochs = 10000000;
		int displayReportPeriod = 100000;
		int showEpochPeriod = 10000;
		int checkMinimumPeriod = 50;
		String savePath = "Models/CardToSentence.txt";
		
		int attempts = 50;
		
		ArrayList<Model> modelList = new ArrayList<>();
		
		Model bestTempModel = new BasicCopying();
		double minLoss;
		
		minLoss = Double.MAX_VALUE;
		for(int i=0;i<attempts;i++) {
			Model tempModel = new NeuralNetwork(new int[] {1,0}, DataPreparation.FIXED_VECTOR_SIZE, new int[] {30}, DataPreparation.FIXED_VECTOR_SIZE, util);
			double loss = (new Trainer()).train(numOfTrainingEpochs, tempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
			if(loss<minLoss) {
				minLoss = loss;
				bestTempModel = tempModel;
			}
		}
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		
	}

}
*/



public class TempTestingMain {

	public static void main(String[] args) {
		CustomRandom util = new CustomRandom();

		FlashcardDataSet data = new FlashcardDataSet("DataSets/TranslatedFlashcards.txt", util);
		
		int numOfTrainingEpochs = 10000000;
		int displayReportPeriod = 100000;
		int showEpochPeriod = 10000;
		int checkMinimumPeriod = 50;
		String savePath = "Models/CardToSentence.txt";
		
		int attempts = 2;
		
		ArrayList<Model> modelList = new ArrayList<>();
			
		modelList.add(new CategoricPortionProbabilityModelForCharacter( data.getTrainingDataSteps(), 1, data.getDataPrep(), util));
		modelList.add(new CategoricPortionProbabilityModelForCharacter( data.getTrainingDataSteps(), 2, data.getDataPrep(), util));
		modelList.add(new CategoricPortionProbabilityModelForCharacter( data.getTrainingDataSteps(), 3, data.getDataPrep(), util));
		modelList.add(new CategoricPortionProbabilityModelForCharacter( data.getTrainingDataSteps(), 4, data.getDataPrep(), util));
		modelList.add(new CategoricPortionProbabilityModelForCharacter( data.getTrainingDataSteps(), 5, data.getDataPrep(), util));        
		
		Model[] models = modelList.toArray(new Model[0]);
		
		System.gc();
		
		double total, average;

		for (int k=models.length-1;k>2;k--) {
		    List<Model[]> subsets = new ArrayList<>();
		    int[] s = new int[k];  
		    for (int i = 0; (s[i] = i) < k - 1; i++);  
		    subsets.add(getSubset(models, s));
		    for(;;) {
		        int i;
		        for (i = k - 1; i >= 0 && s[i] == models.length - k + i; i--); 
		        if (i < 0) {
		            break;
		        }
		        s[i]++;                    
		        for (++i; i < k; i++) {   
		            s[i] = s[i - 1] + 1; 
		        }
		        subsets.add(getSubset(models, s));
		    }		    
		    String tempString;
		    for(Model[] modelSubset: subsets) {
		    	Model combiningModel = new BasicCopying();
		    	for(int modelIndex=0;modelIndex<=2;modelIndex++) {
		    		switch(modelIndex) {
		    		case 0:
		    			total = 0.0;
		    			for(int q=0;q<attempts;q++) {
		    				combiningModel = new FeedForwardLayer(modelSubset.length*DataPreparation.FIXED_VECTOR_SIZE, DataPreparation.FIXED_VECTOR_SIZE, new RoughTanhUnit(), util);	    				
			    		
			    			Model model = new StackingEnsembleModel(combiningModel,new ArrayList<>(Arrays.asList(modelSubset)),DataPreparation.FIXED_VECTOR_SIZE,DataPreparation.FIXED_VECTOR_SIZE,util);
			    			total += (new Trainer()).train(numOfTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
			    		}
			    		average = total/(double) attempts;
						tempString = "Stacking Model with model "+combiningModel.toString()+" with models "+Util.arrayToString(modelSubset)+":\t"+average; 
						System.out.println(tempString);
						DataExport.appendToTextFile(tempString, "Models/ParameterTuning.txt");
			    		System.gc();
						break;
		    		case 1:
		    			total = 0.0;
		    			for(int q=0;q<attempts;q++) {
		    				combiningModel = new LinearLayer(modelSubset.length*DataPreparation.FIXED_VECTOR_SIZE, DataPreparation.FIXED_VECTOR_SIZE,  util);
		    				
			    		
			    			Model model = new StackingEnsembleModel(combiningModel,new ArrayList<>(Arrays.asList(modelSubset)),DataPreparation.FIXED_VECTOR_SIZE,DataPreparation.FIXED_VECTOR_SIZE,util);
			    			total += (new Trainer()).train(numOfTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
			    		}
			    		average = total/(double) attempts;
			    		System.out.println("Stacking Model with model "+combiningModel.toString()+" with models "+Util.arrayToString(modelSubset)+":\t"+average);
						tempString = "Stacking Model with model "+combiningModel.toString()+" with models "+Util.arrayToString(modelSubset)+":\t"+average; 
						System.out.println(tempString);
						DataExport.appendToTextFile(tempString, "Models/ParameterTuning.txt");
			    		System.gc();
		    			break;
		    		case 2:	
		    			
		    			int [] layerTypeSet = {0,1};
		    			int [] hiddenDimensionSet = {1,2,5,10};
		    			int maxArraySize = 3;
		    			
		    			for(int j=2;j<=maxArraySize;j++) {
		    				int[][] layerTypes = new int[(int) Math.pow(layerTypeSet.length, j)][j];
		    				for(int i=0;i<=Math.pow(layerTypeSet.length, j)-1;i++) {
		    					for(int z=0;z<j;z++) {
		    						layerTypes[i][z] =  layerTypeSet[ (int) Math.floor((i)/(int) Math.floor(Math.pow(layerTypeSet.length, z)) ) % layerTypeSet.length ]; 
		    					}
		    				}			
		    				int[][] hiddenDims = new int[(int) Math.pow(hiddenDimensionSet.length, j-1)][j-1];
		    				for(int i=0;i<=Math.pow(hiddenDimensionSet.length, j-1)-1;i++) {
		    					for(int z=0;z<j-1;z++) {
		    						hiddenDims[i][z] =  hiddenDimensionSet[ (int) Math.floor((i)/(int) Math.floor(Math.pow(hiddenDimensionSet.length, z)) ) % hiddenDimensionSet.length ];
		    					}
		    				}
		    				for(int a=0;a<layerTypes.length;a++) {
		    					if(layerTypes[a][layerTypes[a].length-1]==1) {
		    						continue;
		    					}
		    					
		    					for(int b=0;b<hiddenDims.length;b++) {
		    						total = 0.0;
		    						for(int q=0;q<attempts;q++) {
		    							combiningModel = new NeuralNetwork(layerTypes[a],modelSubset.length*DataPreparation.FIXED_VECTOR_SIZE,hiddenDims[b],DataPreparation.FIXED_VECTOR_SIZE,util);
		    							
		    			    		
		    			    			Model model = new StackingEnsembleModel(combiningModel,new ArrayList<>(Arrays.asList(modelSubset)),DataPreparation.FIXED_VECTOR_SIZE,DataPreparation.FIXED_VECTOR_SIZE,util);
		    			    			total += (new Trainer()).train(numOfTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		    			    		}
		    			    		average = total/(double) attempts;
		    						tempString = "Stacking Model with model "+combiningModel.toString()+" with models "+Util.arrayToString(modelSubset)+":\t"+average; 
		    						System.out.println(tempString);
		    						DataExport.appendToTextFile(tempString, "Models/ParameterTuning.txt");
		    			    		System.gc();
		    					}
		    				}			
		    			}
		    			break;
		    		}
		    		
		    		
		    	}
		    	

		    }
		}
		
		
	}

	static Model[] getSubset(Model[] input, int[] subset) {
	    Model[] result = new Model[subset.length]; 
	    for (int i = 0; i < subset.length; i++) 
	        result[i] = input[subset[i]];
	    return result;
	}
	
	

}

/*
public class TempTestingMain {

	public static void main(String[] args) {
		CustomRandom util = new CustomRandom();

		FlashcardDataSet data = new FlashcardDataSet("DataSets/TranslatedFlashcards.txt", util);
		
		int numOfTrainingEpochs = 10000000;
		int displayReportPeriod = 100000;
		int showEpochPeriod = 10000;
		int checkMinimumPeriod = 50;
		String savePath = "Models/CardToSentence.txt";
		
		int attempts = 2;
		
		ArrayList<Model> modelList = new ArrayList<>();
			
		modelList.add(new CategoricPortionProbabilityModelForCharacter( data.getTrainingDataSteps(), 1, data.getDataPrep(), util));
		modelList.add(new CategoricPortionProbabilityModelForCharacter( data.getTrainingDataSteps(), 2, data.getDataPrep(), util));
		modelList.add(new CategoricPortionProbabilityModelForCharacter( data.getTrainingDataSteps(), 3, data.getDataPrep(), util));
		modelList.add(new CategoricPortionProbabilityModelForCharacter( data.getTrainingDataSteps(), 4, data.getDataPrep(), util));
		modelList.add(new CategoricPortionProbabilityModelForCharacter( data.getTrainingDataSteps(), 5, data.getDataPrep(), util));        
		
		Model[] models = modelList.toArray(new Model[0]);
		
		System.gc();
		
		double total, average;

		for (int k=2;k<=models.length;k++) {
		    List<Model[]> subsets = new ArrayList<>();
		    int[] s = new int[k];  
		    for (int i = 0; (s[i] = i) < k - 1; i++);  
		    subsets.add(getSubset(models, s));
		    for(;;) {
		        int i;
		        for (i = k - 1; i >= 0 && s[i] == models.length - k + i; i--); 
		        if (i < 0) {
		            break;
		        }
		        s[i]++;                    
		        for (++i; i < k; i++) {   
		            s[i] = s[i - 1] + 1; 
		        }
		        subsets.add(getSubset(models, s));
		    }		    
		    String tempString;
		    for(Model[] modelSubset: subsets) {
		    	Model combiningModel = new BasicCopying();
		    	for(int modelIndex=0;modelIndex<=2;modelIndex++) {
		    		switch(modelIndex) {
		    		case 0:
		    			total = 0.0;
		    			for(int q=0;q<attempts;q++) {
		    				combiningModel = new FeedForwardLayer(modelSubset.length*DataPreparation.FIXED_VECTOR_SIZE, DataPreparation.FIXED_VECTOR_SIZE, new RoughTanhUnit(), util);	    				
			    		
			    			Model model = new StackingEnsembleModel(combiningModel,new ArrayList<>(Arrays.asList(modelSubset)),DataPreparation.FIXED_VECTOR_SIZE,DataPreparation.FIXED_VECTOR_SIZE,util);
			    			total += (new Trainer()).train(numOfTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
			    		}
			    		average = total/(double) attempts;
						tempString = "Stacking Model with model "+combiningModel.toString()+" with models "+Util.arrayToString(modelSubset)+":\t"+average; 
						System.out.println(tempString);
						DataExport.appendToTextFile(tempString, "Models/ParameterTuning.txt");
			    		System.gc();
						break;
		    		case 1:
		    			total = 0.0;
		    			for(int q=0;q<attempts;q++) {
		    				combiningModel = new LinearLayer(modelSubset.length*DataPreparation.FIXED_VECTOR_SIZE, DataPreparation.FIXED_VECTOR_SIZE,  util);
		    				
			    		
			    			Model model = new StackingEnsembleModel(combiningModel,new ArrayList<>(Arrays.asList(modelSubset)),DataPreparation.FIXED_VECTOR_SIZE,DataPreparation.FIXED_VECTOR_SIZE,util);
			    			total += (new Trainer()).train(numOfTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
			    		}
			    		average = total/(double) attempts;
			    		System.out.println("Stacking Model with model "+combiningModel.toString()+" with models "+Util.arrayToString(modelSubset)+":\t"+average);
						tempString = "Stacking Model with model "+combiningModel.toString()+" with models "+Util.arrayToString(modelSubset)+":\t"+average; 
						System.out.println(tempString);
						DataExport.appendToTextFile(tempString, "Models/ParameterTuning.txt");
			    		System.gc();
		    			break;
		    		case 2:	
		    			
		    			int [] layerTypeSet = {0,1};
		    			int [] hiddenDimensionSet = {1,2,5,10};
		    			int maxArraySize = 3;
		    			
		    			for(int j=2;j<=maxArraySize;j++) {
		    				int[][] layerTypes = new int[(int) Math.pow(layerTypeSet.length, j)][j];
		    				for(int i=0;i<=Math.pow(layerTypeSet.length, j)-1;i++) {
		    					for(int z=0;z<j;z++) {
		    						layerTypes[i][z] =  layerTypeSet[ (int) Math.floor((i)/(int) Math.floor(Math.pow(layerTypeSet.length, z)) ) % layerTypeSet.length ]; 
		    					}
		    				}			
		    				int[][] hiddenDims = new int[(int) Math.pow(hiddenDimensionSet.length, j-1)][j-1];
		    				for(int i=0;i<=Math.pow(hiddenDimensionSet.length, j-1)-1;i++) {
		    					for(int z=0;z<j-1;z++) {
		    						hiddenDims[i][z] =  hiddenDimensionSet[ (int) Math.floor((i)/(int) Math.floor(Math.pow(hiddenDimensionSet.length, z)) ) % hiddenDimensionSet.length ];
		    					}
		    				}
		    				for(int a=0;a<layerTypes.length;a++) {
		    					if(layerTypes[a][layerTypes[a].length-1]==1) {
		    						continue;
		    					}
		    					
		    					for(int b=0;b<hiddenDims.length;b++) {
		    						total = 0.0;
		    						for(int q=0;q<attempts;q++) {
		    							combiningModel = new NeuralNetwork(layerTypes[a],modelSubset.length*DataPreparation.FIXED_VECTOR_SIZE,hiddenDims[b],DataPreparation.FIXED_VECTOR_SIZE,util);
		    							
		    			    		
		    			    			Model model = new StackingEnsembleModel(combiningModel,new ArrayList<>(Arrays.asList(modelSubset)),DataPreparation.FIXED_VECTOR_SIZE,DataPreparation.FIXED_VECTOR_SIZE,util);
		    			    			total += (new Trainer()).train(numOfTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		    			    		}
		    			    		average = total/(double) attempts;
		    						tempString = "Stacking Model with model "+combiningModel.toString()+" with models "+Util.arrayToString(modelSubset)+":\t"+average; 
		    						System.out.println(tempString);
		    						DataExport.appendToTextFile(tempString, "Models/ParameterTuning.txt");
		    			    		System.gc();
		    					}
		    				}			
		    			}
		    			break;
		    		}
		    		
		    		
		    	}
		    	

		    }
		}
		
		
	}

	static Model[] getSubset(Model[] input, int[] subset) {
	    Model[] result = new Model[subset.length]; 
	    for (int i = 0; i < subset.length; i++) 
	        result[i] = input[subset[i]];
	    return result;
	}
	
	

}
*/

/*package findBestModel;

import java.util.ArrayList;

import dataSplitting.DataSplitOp;
import dataSplitting.Splitter;
import dataStructures.DataStep;
import dataStructures.FlashcardDataSet;
import fileIO.DataExport;
import fileIO.DataImport;
import model.AdvancedCopying;
import model.AverageModel;
import model.BasicCopying;
import model.CategoricPortionProbabilityModelForCharacter;
import model.CharacterManipulationFromStringDistance;
import model.FeedForwardLayer;
import model.LinearLayer;
import model.Model;
import model.NeuralNetwork;
import model.SplittingEnsembleModel;
import nonlinearity.RoughTanhUnit;
import training.DataPreparation;
import training.Trainer;
import util.CustomRandom;

public class TempTestingMain {

	public static void main(String[] args) {

		CustomRandom util = new CustomRandom();

		FlashcardDataSet data = new FlashcardDataSet("DataSets/TranslatedFlashcards.txt", util);
		
		int numOfTrainingEpochs = 10000000;
		int displayReportPeriod = 100000;
		int showEpochPeriod = 10000;
		int checkMinimumPeriod = 50;
		String savePath = "Models/CardToSentence.txt";
				
		ArrayList<String> lineFromTextFile = DataImport.getLines("bestModel/BestLinearLayerWeights.txt");
		double[] weights = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(0) );
		LinearLayer LinearModel = new LinearLayer(weights, DataPreparation.FIXED_VECTOR_SIZE);
		double loss = (new Trainer()).train(numOfTrainingEpochs, LinearModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(LinearModel.toString()+" : "+loss);
		DataExport.appendToTextFile(LinearModel.toString()+" : "+loss, "Models/ParameterTuning.txt");

		
		DataSplitOp splitOp = Splitter.getBestDataSplit(data.getTrainingDataSteps(), LinearModel, data.getDataPrep());
		ArrayList<DataStep> badValues = Splitter.getStepsNotInSplit(data.getTrainingDataSteps(), splitOp);

		int attempts = 50;
		
		for(int i=0;i<=1;i++) {
			for(int j=0;j<attempts;j++) {
				Model otherModel = getModelFromIndex(i,badValues,data.getDataPrep(),util);				
				(new Trainer()).train(numOfTrainingEpochs, otherModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
				Model model = new SplittingEnsembleModel(LinearModel, otherModel, splitOp);
				double tempLoss = (new Trainer()).train(numOfTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
				StringBuilder tempBuilder = new StringBuilder();
				splitOp.toString(tempBuilder);
				String tempString = "Splitting Model with split "+tempBuilder.toString()+" with good model "+LinearModel.toString()+" and bad model "+otherModel.toString()+": "+tempLoss; 
				System.out.println(tempString);
				DataExport.appendToTextFile(tempString, "Models/ParameterTuning.txt");
			}


		}
		
		
		/*ArrayList<Model> modelList = new ArrayList<>();
		
		Model bestTempModel = new BasicCopying();
		double minLoss;
		
		minLoss = Double.MAX_VALUE;
		for(int i=0;i<attempts;i++) {
			Model tempModel = new FeedForwardLayer(DataPreparation.FIXED_VECTOR_SIZE, DataPreparation.FIXED_VECTOR_SIZE,new RoughTanhUnit(), util);
			double loss = (new Trainer()).train(numOfTrainingEpochs, tempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
			if(loss<minLoss) {
				minLoss = loss;
				bestTempModel = tempModel;
			}
		}
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		*/
/*
	}
	
	public static Model getModelFromIndex(int index, ArrayList<DataStep> trainingData, DataPreparation dataPrep, CustomRandom rand) {
		switch(index) {
		case 0:
			return new LinearLayer(DataPreparation.FIXED_VECTOR_SIZE, DataPreparation.FIXED_VECTOR_SIZE, rand);
		case 1: 
			return new FeedForwardLayer(DataPreparation.FIXED_VECTOR_SIZE, DataPreparation.FIXED_VECTOR_SIZE, new RoughTanhUnit(), rand);
		default:
			return new BasicCopying();
		}
	}

}
*/
/*
 package findBestModel;

import java.util.ArrayList;

import dataSplitting.DataSplitOp;
import dataSplitting.Splitter;
import dataStructures.DataStep;
import dataStructures.FlashcardDataSet;
import fileIO.DataExport;
import fileIO.DataImport;
import model.AdvancedCopying;
import model.BasicCopying;
import model.CharacterManipulationFromStringDistance;
import model.FeedForwardLayer;
import model.LinearLayer;
import model.*;
import model.NeuralNetwork;
import model.SplittingEnsembleModel;
import nonlinearity.RoughTanhUnit;
import training.DataPreparation;
import training.Trainer;
import util.CustomRandom;

public class TempTestingMain {

	public static void main(String[] args) {

		CustomRandom util = new CustomRandom();

		FlashcardDataSet data = new FlashcardDataSet("DataSets/TranslatedFlashcards.txt", util);
		
		int numOfTrainingEpochs = 10000000;
		int displayReportPeriod = 100000;
		int showEpochPeriod = 10000;
		int checkMinimumPeriod = 50;
		String savePath = "Models/CardToSentence.txt";
		
		//int attempts = 20;
		
		/*ArrayList<Model> modelList = new ArrayList<>();
		
		Model bestTempModel = new BasicCopying();
		double minLoss;
		
		minLoss = Double.MAX_VALUE;
		for(int i=0;i<attempts;i++) {
			Model tempModel = new NeuralNetwork(new int[] {0,0,0}, DataPreparation.FIXED_VECTOR_SIZE, new int[] {2,1}, DataPreparation.FIXED_VECTOR_SIZE, util);
			double loss = (new Trainer()).train(numOfTrainingEpochs, tempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
			if(loss<minLoss) {
				minLoss = loss;
				bestTempModel = tempModel;
			}
		}
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		*/
		/*
		ArrayList<String> lineFromTextFile = DataImport.getLines("bestModel/BestFeedForwardLayerParams.txt");
		double[] biases = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(0) );
		double[] weights = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(1) );
		FeedForwardLayer FeedForwardModel = new FeedForwardLayer(weights, biases, new RoughTanhUnit());		
		double loss = (new Trainer()).train(numOfTrainingEpochs, FeedForwardModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(FeedForwardModel.toString()+" : "+loss);
		DataExport.appendToTextFile(FeedForwardModel.toString()+" : "+loss, "Models/ParameterTuning.txt");

		
		DataSplitOp splitOp = Splitter.getBestDataSplit(data.getTrainingDataSteps(), FeedForwardModel, data.getDataPrep());
		ArrayList<DataStep> badValues = Splitter.getStepsNotInSplit(data.getTrainingDataSteps(), splitOp);

		
		int attempts = 50;
		
		for(int i=0;i<=1;i++) {
			for(int j=0;j<attempts;j++) {
				Model otherModel = getModelFromIndex(i,badValues,data.getDataPrep(),util);				
				//(new Trainer()).train(numOfTrainingEpochs, otherModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
				Model model = new SplittingEnsembleModel(FeedForwardModel, otherModel, splitOp);
				double tempLoss = (new Trainer()).train(numOfTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
				StringBuilder tempBuilder = new StringBuilder();
				splitOp.toString(tempBuilder);
				String tempString = "Splitting Model with split "+tempBuilder.toString()+" with good model "+FeedForwardModel.toString()+" and bad model "+otherModel.toString()+": "+tempLoss; 
				System.out.println(tempString);
				DataExport.appendToTextFile(tempString, "Models/ParameterTuning.txt");
			}


		}
		
	}
	
	
	public static Model getModelFromIndex(int index, ArrayList<DataStep> trainingData, DataPreparation dataPrep, CustomRandom rand) {
		switch(index) {
		case 0:
			return new LinearLayer(DataPreparation.FIXED_VECTOR_SIZE, DataPreparation.FIXED_VECTOR_SIZE, rand);
		case 1: 
			return new FeedForwardLayer(DataPreparation.FIXED_VECTOR_SIZE, DataPreparation.FIXED_VECTOR_SIZE, new RoughTanhUnit(), rand);
		default:
			return new BasicCopying();
		}
	}

}

 */

/*
 package findBestModel;

import java.util.ArrayList;

import dataSplitting.ContainPhrase;
import dataSplitting.DataSplitOp;
import dataSplitting.Splitter;
import dataStructures.DataStep;
import dataStructures.FlashcardDataSet;
import fileIO.DataExport;
import fileIO.DataImport;
import model.AdvancedCopying;
import model.AverageModel;
import model.AveragingEnsembleModel;
import model.BasicCopying;
import model.CategoricPortionProbabilityModelForCharacter;
import model.CharacterManipulationFromStringDistance;
import model.FeedForwardLayer;
import model.LinearLayer;
import model.Model;
import model.NeuralNetwork;
import model.SplittingEnsembleModel;
import nonlinearity.RoughTanhUnit;
import training.DataPreparation;
import training.Trainer;
import util.CustomRandom;

public class TempTestingMain {

	public static void main(String[] args) {

		CustomRandom util = new CustomRandom();

		FlashcardDataSet data = new FlashcardDataSet("DataSets/TranslatedFlashcards.txt", util);
		
		int numOfTrainingEpochs = 10000000;
		int displayReportPeriod = 100000;
		int showEpochPeriod = 10000;
		int checkMinimumPeriod = 50;
		String savePath = "Models/CardToSentence.txt";
				
		ArrayList<Model> modelList = new ArrayList<>();
		
		ArrayList<String> lineFromTextFile = DataImport.getLines("bestModel/LinearLayerGood1.txt");
		double[] weights = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(0) );
		LinearLayer LinearModelGood1 = new LinearLayer(weights, DataPreparation.FIXED_VECTOR_SIZE);


		lineFromTextFile = DataImport.getLines("bestModel/LinearLayerBad1.txt");
		weights = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(0) );
		LinearLayer LinearModelBad1 = new LinearLayer(weights, DataPreparation.FIXED_VECTOR_SIZE);

		
		DataSplitOp splitOp1 = new ContainPhrase("h",data.getDataPrep());
		SplittingEnsembleModel splitting1 = new SplittingEnsembleModel(LinearModelGood1, LinearModelBad1, splitOp1);
		double loss = (new Trainer()).train(numOfTrainingEpochs, splitting1, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		loss = (new Trainer()).train(numOfTrainingEpochs, splitting1, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);

		System.out.println(splitting1.toString()+" : "+loss);	
		modelList.add(splitting1);
		
		
		lineFromTextFile = DataImport.getLines("bestModel/FeedForwardLayerGood2.txt");
		double[] biases = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(0) );
		weights = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(1) );
		FeedForwardLayer FeedForwardGood1 = new FeedForwardLayer(weights, biases, new RoughTanhUnit());			

		lineFromTextFile = DataImport.getLines("bestModel/LinearLayerBad2.txt");
		weights = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(0) );
		LinearLayer LinearModelBad2 = new LinearLayer(weights, DataPreparation.FIXED_VECTOR_SIZE);
			
		DataSplitOp splitOp2 = new ContainPhrase("h",data.getDataPrep());
		SplittingEnsembleModel splitting2 = new SplittingEnsembleModel(FeedForwardGood1, LinearModelBad2, splitOp2);
		loss = (new Trainer()).train(numOfTrainingEpochs, splitting2, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		loss = (new Trainer()).train(numOfTrainingEpochs, splitting2, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);

		System.out.println(splitting2.toString()+" : "+loss);	
		modelList.add(splitting2);
		
		Model categoricChar3 = new CategoricPortionProbabilityModelForCharacter(data.getTrainingDataSteps(), 3, data.getDataPrep(), util);
		loss = (new Trainer()).train(numOfTrainingEpochs, categoricChar3, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(categoricChar3.toString()+" : "+loss);	
		modelList.add(categoricChar3);	
		
		Model categoricChar6 = new CategoricPortionProbabilityModelForCharacter(data.getTrainingDataSteps(), 6, data.getDataPrep(), util);
		loss = (new Trainer()).train(numOfTrainingEpochs, categoricChar6, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(categoricChar6.toString()+" : "+loss);	
		modelList.add(categoricChar6);	
		
		
		Model stringDistanceModel = new CharacterManipulationFromStringDistance(data.getTrainingDataSteps(),data.getDataPrep(),util);
		loss = (new Trainer()).train(numOfTrainingEpochs, stringDistanceModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(stringDistanceModel.toString()+" : "+loss);	
		modelList.add(stringDistanceModel);
		
		Model advancedCopying = new AdvancedCopying(1, DataPreparation.FIXED_VECTOR_SIZE);
		loss = (new Trainer()).train(numOfTrainingEpochs, advancedCopying, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(advancedCopying.toString()+" : "+loss);	
		modelList.add(advancedCopying);		
		
		Model model = new AveragingEnsembleModel(modelList, DataPreparation.FIXED_VECTOR_SIZE);
		loss = (new Trainer()).train(numOfTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		loss = (new Trainer()).train(numOfTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		loss = (new Trainer()).train(numOfTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(model.toString()+" : "+loss);	
		
		/*DataSplitOp splitOp = Splitter.getBestDataSplit(data.getTrainingDataSteps(), LinearModel, data.getDataPrep());
		ArrayList<DataStep> badValues = Splitter.getStepsNotInSplit(data.getTrainingDataSteps(), splitOp);

		int attempts = 50;
		
		for(int i=0;i<=1;i++) {
			for(int j=0;j<attempts;j++) {
				Model otherModel = getModelFromIndex(i,badValues,data.getDataPrep(),util);				
				(new Trainer()).train(numOfTrainingEpochs, otherModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
				Model model = new SplittingEnsembleModel(LinearModel, otherModel, splitOp);
				double tempLoss = (new Trainer()).train(numOfTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
				StringBuilder tempBuilder = new StringBuilder();
				splitOp.toString(tempBuilder);
				String tempString = "Splitting Model with split "+tempBuilder.toString()+" with good model "+LinearModel.toString()+" and bad model "+otherModel.toString()+": "+tempLoss; 
				System.out.println(tempString);
				DataExport.appendToTextFile(tempString, "Models/ParameterTuning.txt");
			}


		}
		*/
		
		/*ArrayList<Model> modelList = new ArrayList<>();
		
		Model bestTempModel = new BasicCopying();
		double minLoss;
		
		minLoss = Double.MAX_VALUE;
		for(int i=0;i<attempts;i++) {
			Model tempModel = new FeedForwardLayer(DataPreparation.FIXED_VECTOR_SIZE, DataPreparation.FIXED_VECTOR_SIZE,new RoughTanhUnit(), util);
			double loss = (new Trainer()).train(numOfTrainingEpochs, tempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
			if(loss<minLoss) {
				minLoss = loss;
				bestTempModel = tempModel;
			}
		}
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		*/
		/*
	}
	
	public static Model getModelFromIndex(int index, ArrayList<DataStep> trainingData, DataPreparation dataPrep, CustomRandom rand) {
		switch(index) {
		case 0:
			return new LinearLayer(DataPreparation.FIXED_VECTOR_SIZE, DataPreparation.FIXED_VECTOR_SIZE, rand);
		case 1: 
			return new FeedForwardLayer(DataPreparation.FIXED_VECTOR_SIZE, DataPreparation.FIXED_VECTOR_SIZE, new RoughTanhUnit(), rand);
		default:
			return new BasicCopying();
		}
	}

}

 */

