package findingBestModel;
/*
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dataStructures.FlashcardDataSet;
import fileIO.DataExport;
import model.*;
import nonlinearity.RoughTanhUnit;
import training.DataPreparation;
import training.Trainer;
import util.CustomRandom;
import util.Util;

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
		
		Model bestTempModel = new BasicCopying();
		double minLoss;
		
		minLoss = Double.MAX_VALUE;
		for(int i=0;i<attempts;i++) {
			Model tempModel = new LinearLayer(DataPreparation.FIXED_VECTOR_SIZE, DataPreparation.FIXED_VECTOR_SIZE, util);
			double loss = (new Trainer()).train(numOfTrainingEpochs, tempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
			if(loss<minLoss) {
				minLoss = loss;
				bestTempModel = tempModel;
			}
		}
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);

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

		bestTempModel = new CategoricPortionProbabilityModelForCharacter(data.getTrainingDataSteps(), 9, data.getDataPrep(), util);
		minLoss = (new Trainer()).train(numOfTrainingEpochs, bestTempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		
		bestTempModel = new CategoricPortionProbabilityModelForCharacter(data.getTrainingDataSteps(), 4, data.getDataPrep(), util);
		minLoss = (new Trainer()).train(numOfTrainingEpochs, bestTempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		
		
		minLoss = Double.MAX_VALUE;
		for(int i=0;i<attempts;i++) {
			Model tempModel = new NeuralNetwork(new int[] {0,0}, DataPreparation.FIXED_VECTOR_SIZE, new int[] {2}, DataPreparation.FIXED_VECTOR_SIZE, util);
			double loss = (new Trainer()).train(numOfTrainingEpochs, tempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
			if(loss<minLoss) {
				minLoss = loss;
				bestTempModel = tempModel;
			}
		}
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		
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
		
		bestTempModel = new AverageModel(data.getTrainingDataSteps());
		minLoss = (new Trainer()).train(numOfTrainingEpochs, bestTempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		
		bestTempModel = new CharacterManipulationFromStringDistance(data.getTrainingDataSteps(), data.getDataPrep(), util);
		minLoss = (new Trainer()).train(numOfTrainingEpochs, bestTempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
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
		    			combiningModel = new FeedForwardLayer(modelSubset.length*DataPreparation.FIXED_VECTOR_SIZE, DataPreparation.FIXED_VECTOR_SIZE, new RoughTanhUnit(), util);
						(new Trainer()).train(numOfTrainingEpochs, combiningModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
						total = 0.0;
			    		for(int q=0;q<attempts;q++) {
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
		    			combiningModel = new LinearLayer(modelSubset.length*DataPreparation.FIXED_VECTOR_SIZE, DataPreparation.FIXED_VECTOR_SIZE,  util);
						(new Trainer()).train(numOfTrainingEpochs, combiningModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
						total = 0.0;
			    		for(int q=0;q<attempts;q++) {
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
		    						combiningModel = new NeuralNetwork(layerTypes[a],DataPreparation.FIXED_VECTOR_SIZE,hiddenDims[b],DataPreparation.FIXED_VECTOR_SIZE,util);
		    						(new Trainer()).train(numOfTrainingEpochs, combiningModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		    						total = 0.0;
		    			    		for(int q=0;q<attempts;q++) {
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dataStructures.FlashcardDataSet;
import fileIO.DataExport;
import fileIO.DataImport;
import model.*;
import nonlinearity.RoughTanhUnit;
import training.DataPreparation;
import training.Trainer;
import util.CustomRandom;
import util.Util;

public class FindBestStackingFromIndividual {

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
		
		Model bestTempModel = new BasicCopying();
		double minLoss;
			
		ArrayList<String> lineFromTextFile = DataImport.getLines("bestModel/BestLinearLayerWeights.txt");
		double[] weights = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(0) );
        int indexA = weights.length-1;

        StringBuilder builder = new StringBuilder();
		/*for( int i = 100-1; i >=0; i-- ) {
            //total = 0;
           builder.append("public static double getOutputAtPosition"+i+"(double[] input){");
           builder.append("\n"); 
           builder.append("\treturn ");
            
            for( int j = 100-1; j >=0; j-- ) {
                //total += Weights[indexA--] * input[j];
            	builder.append(weights[indexA--]+"*input["+j+"]+");
            }
            builder.append(";");
            builder.append("\n");
            builder.append("}");
            builder.append("\n");
            //output[i] =  total ;
        }*/
        for( int i = 100-1; i >=0; i-- ) {
        	builder.append("output["+i+"] = getOutputAtPosition"+i+"(input);\n");
        }
		
		DataExport.overwriteToTextFile(builder, "Models/Temp2.txt");
		
		//bestTempModel = new LinearLayer(weights, DataPreparation.FIXED_VECTOR_SIZE);
		/*minLoss = (new Trainer()).train(numOfTrainingEpochs, bestTempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		
		
		lineFromTextFile = DataImport.getLines("bestModel/BestFeedForwardLayerParams.txt");
		double[] biases = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(0) );
		weights = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(1) );
		bestTempModel = new FeedForwardLayer(weights, biases, new RoughTanhUnit());		
		minLoss = (new Trainer()).train(numOfTrainingEpochs, bestTempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		
		bestTempModel = new CategoricPortionProbabilityModelForCharacter(data.getTrainingDataSteps(), 9, data.getDataPrep(), util);
		minLoss = (new Trainer()).train(numOfTrainingEpochs, bestTempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		
		bestTempModel = new CategoricPortionProbabilityModelForCharacter(data.getTrainingDataSteps(), 4, data.getDataPrep(), util);
		minLoss = (new Trainer()).train(numOfTrainingEpochs, bestTempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		
		
		
		
		lineFromTextFile = DataImport.getLines("bestModel/Best2LayerNeuralNetwork.txt");
		ArrayList<Layer> layers = new ArrayList<>();
		double[] biases1 = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(1) );
		double[] weights1 = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(2) );
		layers.add(new FeedForwardLayer(weights1, biases1, new RoughTanhUnit()));
		double[] biases2 = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(4) );
		double[] weights2 = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(5) );
		layers.add(new FeedForwardLayer(weights2, biases2, new RoughTanhUnit()));
		bestTempModel = new NeuralNetwork(layers);		
		minLoss = (new Trainer()).train(numOfTrainingEpochs, bestTempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		
		lineFromTextFile = DataImport.getLines("bestModel/Best3LayerNeuralNetwork.txt");
		ArrayList<Layer> layersFor3NN = new ArrayList<>();
		double[] biases1For3NN = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(1) );
		double[] weights1For3NN = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(2) );
		layersFor3NN.add(new FeedForwardLayer(weights1For3NN, biases1For3NN, new RoughTanhUnit()));
		double[] biases2For3NN = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(4) );
		double[] weights2For3NN = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(5) );
		layersFor3NN.add(new FeedForwardLayer(weights2For3NN, biases2For3NN, new RoughTanhUnit()));
		double[] biases3For3NN = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(7) );
		double[] weights3For3NN = DataImport.getDoubleArrayFromLine( lineFromTextFile.get(8) );
		layersFor3NN.add(new FeedForwardLayer(weights3For3NN, biases3For3NN, new RoughTanhUnit()));		
		bestTempModel = new NeuralNetwork(layersFor3NN);		
		minLoss = (new Trainer()).train(numOfTrainingEpochs, bestTempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
			
		bestTempModel = new AverageModel(data.getTrainingDataSteps());
		minLoss = (new Trainer()).train(numOfTrainingEpochs, bestTempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		
		bestTempModel = new CharacterManipulationFromStringDistance(data.getTrainingDataSteps(), data.getDataPrep(), util);
		minLoss = (new Trainer()).train(numOfTrainingEpochs, bestTempModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		System.out.println(bestTempModel.toString()+" : "+minLoss);
		modelList.add(bestTempModel);
		
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
		    				//(new Trainer()).train(numOfTrainingEpochs, combiningModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		    				
			    		
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
		    				//(new Trainer()).train(numOfTrainingEpochs, combiningModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		    				
			    		
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
		    					for(int b=0;b<hiddenDims.length;b++) {
		    						total = 0.0;
		    						for(int q=0;q<attempts;q++) {
		    							combiningModel = new NeuralNetwork(layerTypes[a],modelSubset.length*DataPreparation.FIXED_VECTOR_SIZE,hiddenDims[b],DataPreparation.FIXED_VECTOR_SIZE,util);
		    							//(new Trainer()).train(numOfTrainingEpochs, combiningModel, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, util);
		    							
		    			    		
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
		*/
		
	}

	static Model[] getSubset(Model[] input, int[] subset) {
	    Model[] result = new Model[subset.length]; 
	    for (int i = 0; i < subset.length; i++) 
	        result[i] = input[subset[i]];
	    return result;
	}
	
	

}

