package findingBestModel;
/*
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dataStructures.FlashcardDataSet;
import fileIO.DataExport;
import model.*;
import nonlinearity.RoughTanhUnit;
import training.DataProcessing;
import training.ModelTrainer;
import util.CustomRandom;
import util.Utilities;

public class FindBestStackingFromIndividual {

	public static void main(String[] args) {
		CustomRandom util = new CustomRandom();

		FlashcardDataSet data = new FlashcardDataSet("DataSets/TranslatedFlashcards.txt", util);
		
		int numOfTrainingEpochs = 10000000;
		int displayReportPeriod = 100000;
		int showEpochPeriod = 10000;
		int checkMinimumPeriod = 50;
		String savePath = "Models/CardToSentence.txt";
		
		int attempts = 3;
		
		ArrayList<Model> modelList = new ArrayList<>();
		
		Model bestTempModel = new BasicCopyingModel();
		double minLoss;
		
		minLoss = Double.MAX_VALUE;
		for(int i=0;i<attempts;i++) {
			Model tempModel = new LinearLayer(DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, util);
			double loss = (new ModelTrainer()).train(numOfTrainingEpochs, tempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
			if(loss<minLoss) {
				minLoss = loss;
				bestTempModel = tempModel;
			}
		}
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);

		minLoss = Double.MAX_VALUE;
		for(int i=0;i<attempts;i++) {
			Model tempModel = new FeedForwardLayer(DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR,new RoughTanhUnit(), util);
			double loss = (new ModelTrainer()).train(numOfTrainingEpochs, tempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
			if(loss<minLoss) {
				minLoss = loss;
				bestTempModel = tempModel;
			}
		}
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		
		minLoss = Double.MAX_VALUE;
		for(int i=0;i<attempts;i++) {
			Model tempModel = new NeuralNetworkModel(new int[] {1,0}, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, new int[] {30}, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, util);
			double loss = (new ModelTrainer()).train(numOfTrainingEpochs, tempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
			if(loss<minLoss) {
				minLoss = loss;
				bestTempModel = tempModel;
			}
		}
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);

		bestTempModel = new ProportionProbabilityForCharacterModel(data.getTrainingDataSteps(), 9, data.getDataProcessing(), util);
		minLoss = (new ModelTrainer()).train(numOfTrainingEpochs, bestTempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		
		bestTempModel = new ProportionProbabilityForCharacterModel(data.getTrainingDataSteps(), 4, data.getDataProcessing(), util);
		minLoss = (new ModelTrainer()).train(numOfTrainingEpochs, bestTempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		
		
		minLoss = Double.MAX_VALUE;
		for(int i=0;i<attempts;i++) {
			Model tempModel = new NeuralNetworkModel(new int[] {0,0}, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, new int[] {2}, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, util);
			double loss = (new ModelTrainer()).train(numOfTrainingEpochs, tempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
			if(loss<minLoss) {
				minLoss = loss;
				bestTempModel = tempModel;
			}
		}
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		
		minLoss = Double.MAX_VALUE;
		for(int i=0;i<attempts;i++) {
			Model tempModel = new NeuralNetworkModel(new int[] {0,0,0}, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, new int[] {2,1}, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, util);
			double loss = (new ModelTrainer()).train(numOfTrainingEpochs, tempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
			if(loss<minLoss) {
				minLoss = loss;
				bestTempModel = tempModel;
			}
		}
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		
		bestTempModel = new AverageModel(data.getTrainingDataSteps());
		minLoss = (new ModelTrainer()).train(numOfTrainingEpochs, bestTempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		
		bestTempModel = new CharacterManipulationFromStringDistanceModel(data.getTrainingDataSteps(), data.getDataProcessing(), util);
		minLoss = (new ModelTrainer()).train(numOfTrainingEpochs, bestTempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		
		Model[] models = modelList.toArray(new Model[0]);
		
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
		    	Model combiningModel;
		    	for(int modelIndex=0;modelIndex<=2;modelIndex++) {
		    		switch(modelIndex) {
		    		case 0:
		    			combiningModel = new FeedForwardLayer(modelSubset.length*DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, new RoughTanhUnit(), util);
						(new ModelTrainer()).train(numOfTrainingEpochs, combiningModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
						total = 0.0;
			    		for(int q=0;q<attempts;q++) {
			    			Model model = new StackingEnsembleModel(combiningModel,new ArrayList<>(Arrays.asList(modelSubset)),DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR,DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR,util);
			    			total += (new ModelTrainer()).train(numOfTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
			    		}
			    		average = total/(double) attempts;
						tempString = "Stacking Model with model "+combiningModel.toString()+" with models "+Utilities.arrayToString(modelSubset)+":\t"+average;
						System.out.println(tempString);
						DataExport.appendToTextFile(tempString, "Models/ParameterTuning.txt");
			    		System.gc();
						break;
		    		case 1:
		    			combiningModel = new LinearLayer(modelSubset.length*DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR,  util);
						(new ModelTrainer()).train(numOfTrainingEpochs, combiningModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
						total = 0.0;
			    		for(int q=0;q<attempts;q++) {
			    			Model model = new StackingEnsembleModel(combiningModel,new ArrayList<>(Arrays.asList(modelSubset)),DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR,DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR,util);
			    			total += (new ModelTrainer()).train(numOfTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
			    		}
			    		average = total/(double) attempts;
			    		System.out.println("Stacking Model with model "+combiningModel.toString()+" with models "+Utilities.arrayToString(modelSubset)+":\t"+average);
						tempString = "Stacking Model with model "+combiningModel.toString()+" with models "+Utilities.arrayToString(modelSubset)+":\t"+average;
						System.out.println(tempString);
						DataExport.appendToTextFile(tempString, "Models/ParameterTuning.txt");
			    		System.gc();
		    			break;
		    		case 2:	
		    			
		    			int [] layerTypeSet = {0,1};
		    			int [] hiddenDimensionSet = {1,2,3,4,5,7,10};
		    			int maxArraySize = 4;
		    			
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
		    					for(int b=0;b<hiddenDims.length;b++) {
		    						combiningModel = new NeuralNetworkModel(layerTypes[a],DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR,hiddenDims[b],DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR,util);
		    						(new ModelTrainer()).train(numOfTrainingEpochs, combiningModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		    						total = 0.0;
		    			    		for(int q=0;q<attempts;q++) {
		    			    			Model model = new StackingEnsembleModel(combiningModel,new ArrayList<>(Arrays.asList(modelSubset)),DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR,DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR,util);
		    			    			total += (new ModelTrainer()).train(numOfTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		    			    		}
		    			    		average = total/(double) attempts;
		    						tempString = "Stacking Model with model "+combiningModel.toString()+" with models "+Utilities.arrayToString(modelSubset)+":\t"+average;
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

import dataStructures.FlashcardDataSet;
import fileManipulation.DataExport;
import fileManipulation.DataImport;
import generalUtilities.CustomRandom;
import models.BasicCopyingModel;
import models.Model;

import java.util.ArrayList;

public class FindBestStackingFromIndividual {

	public static void main(String[] args) {
		CustomRandom random = new CustomRandom();

		FlashcardDataSet data = new FlashcardDataSet("DataSets/TranslatedFlashcards.txt", random);
		
		int maximumTrainingEpochs = 10000000;
		int displayReportPeriod = 100000;
		int showEpochPeriod = 10000;
		int checkMinimumPeriod = 50;
		String savePath = "Models/CardToSentence.txt";
		
		int attempts = 2;
		
		ArrayList<Model> modelList = new ArrayList<>();

        Model bestTemporaryModel = new BasicCopyingModel();
		double minimumLoss;
			
		ArrayList<String> linesFromTextFile = DataImport.getLinesFromTextFile("bestModel/BestLinearLayerWeights.txt");
		double[] weights = DataImport.getDoubleArrayFromLine( linesFromTextFile.get(0) );
        int index = weights.length-1;

        StringBuilder stringBuilder = new StringBuilder();
		/*for( int i = 100-1; i >=0; i-- ) {
            //total = 0;
           stringBuilder.append("public static double getOutputAtPosition"+i+"(double[] input){");
           stringBuilder.append("\n");
           stringBuilder.append("\treturn ");
            
            for( int j = 100-1; j >=0; j-- ) {
                //total += Weights[index--] * input[j];
            	stringBuilder.append(weights[index--]+"*input["+j+"]+");
            }
            stringBuilder.append(";");
            stringBuilder.append("\n");
            stringBuilder.append("}");
            stringBuilder.append("\n");
            //output[i] =  total ;
        }*/
        for( int i = 100-1; i >=0; i-- ) {
        	stringBuilder.append("output["+i+"] = getOutputAtPosition"+i+"(input);\n");
        }
		
		DataExport.overwriteTextFile(stringBuilder, "Models/Temp2.txt");

        //bestTemporaryModel = new LinearLayer(weights, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR);
		/*minimumLoss = (new ModelTrainer()).train(maximumTrainingEpochs, bestTemporaryModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, random);
		System.out.println(bestTemporaryModel.toString()+" : "+minimumLoss);
		modelList.add(bestTemporaryModel);
		
		
		linesFromTextFile = DataImport.getLinesFromTextFile("bestModel/BestFeedForwardLayerParams.txt");
		double[] biases = DataImport.getDoubleArrayFromLine( linesFromTextFile.get(0) );
		weights = DataImport.getDoubleArrayFromLine( linesFromTextFile.get(1) );
		bestTemporaryModel = new FeedForwardLayer(weights, biases, new RoughTanhUnit());
		minimumLoss = (new ModelTrainer()).train(maximumTrainingEpochs, bestTemporaryModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, random);
		System.out.println(bestTemporaryModel.toString()+" : "+minimumLoss);
		modelList.add(bestTemporaryModel);
		
		bestTemporaryModel = new ProportionProbabilityForCharacterModel(data.getTrainingDataSteps(), 9, data.getDataProcessing(), random);
		minimumLoss = (new ModelTrainer()).train(maximumTrainingEpochs, bestTemporaryModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, random);
		System.out.println(bestTemporaryModel.toString()+" : "+minimumLoss);
		modelList.add(bestTemporaryModel);
		
		bestTemporaryModel = new ProportionProbabilityForCharacterModel(data.getTrainingDataSteps(), 4, data.getDataProcessing(), random);
		minimumLoss = (new ModelTrainer()).train(maximumTrainingEpochs, bestTemporaryModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, random);
		System.out.println(bestTemporaryModel.toString()+" : "+minimumLoss);
		modelList.add(bestTemporaryModel);
		
		
		
		
		linesFromTextFile = DataImport.getLinesFromTextFile("bestModel/Best2LayerNeuralNetwork.txt");
		ArrayList<Layer> layers = new ArrayList<>();
		double[] biases1 = DataImport.getDoubleArrayFromLine( linesFromTextFile.get(1) );
		double[] weights1 = DataImport.getDoubleArrayFromLine( linesFromTextFile.get(2) );
		layers.add(new FeedForwardLayer(weights1, biases1, new RoughTanhUnit()));
		double[] biases2 = DataImport.getDoubleArrayFromLine( linesFromTextFile.get(4) );
		double[] weights2 = DataImport.getDoubleArrayFromLine( linesFromTextFile.get(5) );
		layers.add(new FeedForwardLayer(weights2, biases2, new RoughTanhUnit()));
		bestTemporaryModel = new NeuralNetworkModel(layers);
		minimumLoss = (new ModelTrainer()).train(maximumTrainingEpochs, bestTemporaryModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, random);
		System.out.println(bestTemporaryModel.toString()+" : "+minimumLoss);
		modelList.add(bestTemporaryModel);
		
		linesFromTextFile = DataImport.getLinesFromTextFile("bestModel/Best3LayerNeuralNetwork.txt");
		ArrayList<Layer> layersFor3NN = new ArrayList<>();
		double[] biases1For3NN = DataImport.getDoubleArrayFromLine( linesFromTextFile.get(1) );
		double[] weights1For3NN = DataImport.getDoubleArrayFromLine( linesFromTextFile.get(2) );
		layersFor3NN.add(new FeedForwardLayer(weights1For3NN, biases1For3NN, new RoughTanhUnit()));
		double[] biases2For3NN = DataImport.getDoubleArrayFromLine( linesFromTextFile.get(4) );
		double[] weights2For3NN = DataImport.getDoubleArrayFromLine( linesFromTextFile.get(5) );
		layersFor3NN.add(new FeedForwardLayer(weights2For3NN, biases2For3NN, new RoughTanhUnit()));
		double[] biases3For3NN = DataImport.getDoubleArrayFromLine( linesFromTextFile.get(7) );
		double[] weights3For3NN = DataImport.getDoubleArrayFromLine( linesFromTextFile.get(8) );
		layersFor3NN.add(new FeedForwardLayer(weights3For3NN, biases3For3NN, new RoughTanhUnit()));		
		bestTemporaryModel = new NeuralNetworkModel(layersFor3NN);
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
		    	Model combiningModel = new BasicCopyingModel();
		    	for(int modelIndex=0;modelIndex<=2;modelIndex++) {
		    		switch(modelIndex) {
		    		case 0:
		    			total = 0.0;
		    			for(int q=0;q<attempts;q++) {
		    				combiningModel = new FeedForwardLayer(modelSubset.length*DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, new RoughTanhUnit(), random);
		    				//(new ModelTrainer()).train(maximumTrainingEpochs, combiningModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, random);
		    				
			    		
			    			Model model = new StackingEnsembleModel(combiningModel,new ArrayList<>(Arrays.asList(modelSubset)),DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR,DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR,random);
			    			total += (new ModelTrainer()).train(maximumTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, random);
			    		}
			    		average = total/(double) attempts;
						tempString = "Stacking Model with model "+combiningModel.toString()+" with models "+Utilities.arrayToString(modelSubset)+":\t"+average;
						System.out.println(tempString);
						DataExport.appendToTextFile(tempString, "Models/ParameterTuning.txt");
			    		System.gc();
						break;
		    		case 1:
		    			total = 0.0;
		    			for(int q=0;q<attempts;q++) {
		    				combiningModel = new LinearLayer(modelSubset.length*DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR,  random);
		    				//(new ModelTrainer()).train(maximumTrainingEpochs, combiningModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, random);
		    				
			    		
			    			Model model = new StackingEnsembleModel(combiningModel,new ArrayList<>(Arrays.asList(modelSubset)),DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR,DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR,random);
			    			total += (new ModelTrainer()).train(maximumTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, random);
			    		}
			    		average = total/(double) attempts;
			    		System.out.println("Stacking Model with model "+combiningModel.toString()+" with models "+Utilities.arrayToString(modelSubset)+":\t"+average);
						tempString = "Stacking Model with model "+combiningModel.toString()+" with models "+Utilities.arrayToString(modelSubset)+":\t"+average;
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
		    					for(int b=0;b<hiddenDims.length;b++) {
		    						total = 0.0;
		    						for(int q=0;q<attempts;q++) {
		    							combiningModel = new NeuralNetworkModel(layerTypes[a],modelSubset.length*DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR,hiddenDims[b],DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR,random);
		    							//(new ModelTrainer()).train(maximumTrainingEpochs, combiningModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, random);
		    							
		    			    		
		    			    			Model model = new StackingEnsembleModel(combiningModel,new ArrayList<>(Arrays.asList(modelSubset)),DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR,DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR,random);
		    			    			total += (new ModelTrainer()).train(maximumTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, random);
		    			    		}
		    			    		average = total/(double) attempts;
		    						tempString = "Stacking Model with model "+combiningModel.toString()+" with models "+Utilities.arrayToString(modelSubset)+":\t"+average;
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
		*/
		
	}


	

}

