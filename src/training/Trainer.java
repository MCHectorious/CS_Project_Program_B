package training;

import dataStructures.DataSet;
import dataStructures.DataStep;
import fileManipulation.DataExport;
import fileManipulation.DataImport;
import generalUtilities.CustomRandom;
import matrices.Vector;
import models.Model;

public class Trainer {
	
	
	
	 
	private double numeratorLoss;
	private Vector output = new Vector(DataProcessing.FIXED_VECTOR_SIZE);
	private static final String minLossPath = "Models/Minimum Loss.txt";
	
	final private double alpha,beta1, beta2, oneMinusBeta1, oneMinusBeta2,momentum;

	private double beta1ForEpoch = 1.0;
	private double beta2ForEpoch = 1.0;
	
	
	
	public Trainer() {
		alpha = 0.001;
		beta1 = 0.9;
		beta2 = 0.999;
		
		oneMinusBeta1 = 1-beta1;
		oneMinusBeta2 = 1-beta2;
		
		momentum = 0.0001;
	}

	Trainer(double alpha, double beta1, double beta2, double momentum) {
		this.alpha = alpha;
		this.beta1 = beta1;
		this.beta2 = beta2;
		
		oneMinusBeta1 = 1-beta1;
		oneMinusBeta2 = 1-beta2;
		
		this.momentum = momentum;
		
	}
	
	
	
	public double train(int numOfTrainingEpochs, Model model, DataSet data, int displayReportPeriod, int showEpochPeriod,int checkMinimumPeriod, String savePath, CustomRandom util) {
		int increasingStreak = 0;
		StringBuilder builder = new StringBuilder(110);
		long previousTime,duration;
		double trainingLoss;
		double testingLoss;
		final String epochSuffix = "/"+numOfTrainingEpochs+"]";
		double localMinimum = Double.MAX_VALUE;
		double globalMinimum = DataImport.getDoubleFromFile(minLossPath);
		
		
		System.gc();
		for(int epoch = 1; epoch<=numOfTrainingEpochs; epoch++) {
			
			previousTime = System.currentTimeMillis();
			trainingLoss = trainingPass(model,data);
			if(trainingLoss < localMinimum) {
				localMinimum = trainingLoss;
				increasingStreak = 0;
				if(trainingLoss < globalMinimum) {
					builder.setLength(0);
					model.description(builder);
					DataExport.overwriteToTextFile(builder, savePath);
					globalMinimum = trainingLoss;
					DataExport.overwriteToTextFile(globalMinimum, minLossPath);
				}
			}else {
				increasingStreak++;
				if(increasingStreak==checkMinimumPeriod) {
					break;
				}
			}
				
			
			
			
			if(epoch % showEpochPeriod == 0) {
					
				testingLoss = testingPass(model, data,1500,util);
				//DataExport.appendToTextFile(trainingLoss+"\t"+testingLoss, "Models/Losses.txt");
				builder.setLength(0);
				//model.getParams(builder);
				builder.append("epoch[").append(epoch).append(epochSuffix);
				duration = System.currentTimeMillis() - previousTime;
				builder.append("\ttime = ").append(duration);
				builder.append("\t training loss = ").append(trainingLoss);
				builder.append("\t testing loss = ").append(testingLoss);
				System.out.println(builder.toString());
				if(epoch % displayReportPeriod == 0) {
					data.DisplayReport(model);
				}
					
			}
			
			
			
		}
		
		return localMinimum;
		
		
	}
	
	private  double testingPass(Model model, DataSet data, int numTested, CustomRandom rand) {
		numeratorLoss = 0;
		DataStep step;
		
		//for(DataStep step:  data.getTestingDataSteps()) {
		//model.run(step.input, output);
			//numeratorLoss += DataSet.lossTraining.measure(output, step.targetOutput);
		//}
		int[] toTest = rand.randomIntArray(numTested, data.getTestingSize());
		for(int i=toTest.length-1;i>=0;i--) {
			step = data.getTestingDataSteps().get(toTest[i]);
			model.run(step, output);
			numeratorLoss += DataSet.lossTraining.measure(output, step.getTargetOutputVector());

		}

		return numeratorLoss/toTest.length;
	}

	private double trainingPass(Model model, DataSet data) {
		numeratorLoss = 0;
		DataStep step;
		for(int i=data.getTrainingSize()-1;i>=0;i--) {
			step = data.getTrainingDataSteps().get(i);
			//System.out.println(i);
			model.runAndDecideImprovements(step, output, step.getTargetOutputVector());
			numeratorLoss += DataSet.lossTraining.measure(output, step.getTargetOutputVector());
		}
		
		beta1ForEpoch *= beta1;
		beta2ForEpoch *= beta2;

		double alphaForEpoch = alpha * Math.sqrt(1 - beta2ForEpoch) / (1 - beta1ForEpoch);

		model.updateModelParameters(momentum, beta1, beta2, alphaForEpoch, oneMinusBeta1, oneMinusBeta2);
		
		return numeratorLoss*data.getReciprocalOfTrainingSize();
	}

	
}
