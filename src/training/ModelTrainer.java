package training;

import dataStructures.DataSet;
import dataStructures.DataStep;
import fileManipulation.DataExport;
import fileManipulation.DataImport;
import generalUtilities.CustomRandom;
import matrices.Vector;
import models.Model;

public class ModelTrainer {

	private double numeratorLoss;
	private Vector output = new Vector(DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR);
	private static final String minLossPath = "Models/Minimum Loss.txt";
	
	final private double alpha,beta1, beta2, oneMinusBeta1, oneMinusBeta2,momentum;

	private double beta1ForEpoch = 1.0;
	private double beta2ForEpoch = 1.0;
	
	
	
	public ModelTrainer() {
		alpha = 0.001;
		beta1 = 0.9;
		beta2 = 0.999;
		
		oneMinusBeta1 = 1-beta1;
		oneMinusBeta2 = 1-beta2;
		
		momentum = 0.0001;
	}

	ModelTrainer(double alpha, double beta1, double beta2, double momentum) {
		this.alpha = alpha;
		this.beta1 = beta1;
		this.beta2 = beta2;
		
		oneMinusBeta1 = 1-beta1;
		oneMinusBeta2 = 1-beta2;
		
		this.momentum = momentum;
		
	}
	
	
	
	public double train(int maxTrainingEpochs, Model model, DataSet data, int displayReportPeriod, int showEpochPeriod,int checkMinimumPeriod, String savePath, CustomRandom random) {
		int increasingStreak = 0;
		StringBuilder epochStringBuilder = new StringBuilder(110);
		long previousTime,duration;
		double trainingLoss;
		double testingLoss;
		final String epochSuffix = "/"+maxTrainingEpochs+"]";
		double localMinimum = Double.MAX_VALUE;
		double globalMinimum = DataImport.getDoubleFromFile(minLossPath);
		
		
		System.gc();
		for(int epoch = 1; epoch<=maxTrainingEpochs; epoch++) {
			
			previousTime = System.currentTimeMillis();
			trainingLoss = trainingPass(model,data);
			if(trainingLoss < localMinimum) {
				localMinimum = trainingLoss;
				increasingStreak = 0;
				if(trainingLoss < globalMinimum) {
					epochStringBuilder.setLength(0);
					model.provideDescription(epochStringBuilder);
					DataExport.overwriteTextFile(epochStringBuilder, savePath);
					globalMinimum = trainingLoss;
					DataExport.overwriteTextFile(globalMinimum, minLossPath);
				}
			}else {
				increasingStreak++;
				if(increasingStreak==checkMinimumPeriod) {
					break;
				}
			}
				
			
			
			
			if(epoch % showEpochPeriod == 0) {
					
				testingLoss = testingPass(model, data,1500,random);
				epochStringBuilder.setLength(0);
				epochStringBuilder.append("epoch[").append(epoch).append(epochSuffix);
				duration = System.currentTimeMillis() - previousTime;
				epochStringBuilder.append("\ttime = ").append(duration);
				epochStringBuilder.append("\t training loss = ").append(trainingLoss);
				epochStringBuilder.append("\t testing loss = ").append(testingLoss);
				System.out.println(epochStringBuilder.toString());
				if(epoch % displayReportPeriod == 0) {
					data.displayReport(model);
				}
					
			}
			
			
			
		}
		
		return localMinimum;
		
		
	}
	
	private  double testingPass(Model model, DataSet data, int numTested, CustomRandom rand) {
		numeratorLoss = 0;
		DataStep step;

		int[] toTest = rand.randomIntArray(numTested, data.getTestingDataStepsSize());
		for(int i=toTest.length-1;i>=0;i--) {
			step = data.getTestingDataSteps().get(toTest[i]);
			model.run(step, output);
			numeratorLoss += DataSet.trainingLoss.measureLoss(output, step.getTargetOutputVector());

		}

		return numeratorLoss/toTest.length;
	}

	private double trainingPass(Model model, DataSet data) {
		numeratorLoss = 0;
		DataStep step;
		for(int i = data.getTrainingDataStepsSize()-1; i>=0; i--) {
			step = data.getTrainingDataSteps().get(i);
			//System.out.println(i);
			model.runAndDecideImprovements(step, output, step.getTargetOutputVector());
			numeratorLoss += DataSet.trainingLoss.measureLoss(output, step.getTargetOutputVector());
		}
		
		beta1ForEpoch *= beta1;
		beta2ForEpoch *= beta2;

		double alphaForEpoch = alpha * Math.sqrt(1 - beta2ForEpoch) / (1 - beta1ForEpoch);

		model.updateModelParameters(momentum, beta1, beta2, alphaForEpoch, oneMinusBeta1, oneMinusBeta2);
		
		return numeratorLoss*data.getReciprocalOfTrainingSize();
	}

	
}
