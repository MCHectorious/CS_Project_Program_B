package Training;

import DataStructures.DataSet;
import DataStructures.DataStep;
import FileManipulation.DataExport;
import FileManipulation.DataImport;
import GeneralUtilities.CustomRandom;
import Matrices.Vector;
import Models.Model;

public class ModelTrainer {

	private double numeratorLoss;//stores the loss of the model
	private Vector output = new Vector(DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR);// created here to avoid creating object in loops
	private String minimumLossPath = "Models/Minimum Loss.txt";//stores the local of the global minimum loss of any model
	
	final private double alpha,beta1, beta2, oneMinusBeta1, oneMinusBeta2,momentum;//Used with the ADAM optimiser except for the momentum which the proportion that the previous staate of the model affects its currnt state

	private double beta1ForEpoch = 1.0;
	private double beta2ForEpoch = 1.0;
	
	public ModelTrainer(String minimumLossPath){
		this.minimumLossPath = minimumLossPath;//Changes where to look for the global minimum loss
		alpha = 0.001;
		beta1 = 0.9;
		beta2 = 0.999;
		//uses the recommended values

		oneMinusBeta1 = 1-beta1;
		oneMinusBeta2 = 1-beta2;

		momentum = 0.0001;
	}
	
	public ModelTrainer() {
		alpha = 0.001;
		beta1 = 0.9;
		beta2 = 0.999;
        //uses the recommended values
		
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
		double globalMinimum = DataImport.getDoubleFromFile(minimumLossPath);
		//Initialise variables to be used later
		
		System.gc();//enables garbage collection so that more space is available

		for(int epoch = 1; epoch<=maxTrainingEpochs; epoch++) {
			
			previousTime = System.currentTimeMillis();// gets the initial time
			trainingLoss = trainingPass(model,data);
			if(trainingLoss < localMinimum) {
				localMinimum = trainingLoss;
				increasingStreak = 0;// resets the the streak as the model is still improving
				if(trainingLoss < globalMinimum) {
					epochStringBuilder.setLength(0);//resets the string builder to avoid creating new objects
					model.provideDescription(epochStringBuilder);
					DataExport.overwriteTextFile(epochStringBuilder, savePath);//Stores the description of the best model
					globalMinimum = trainingLoss;
					DataExport.overwriteTextFile(globalMinimum, minimumLossPath);//stores the best loss
				}
			}else {
				increasingStreak++;
				if(increasingStreak==checkMinimumPeriod) {//if the model has gotten worse for long enough, the training stops
					break;
				}
			}

			if(epoch % showEpochPeriod == 0) {//every time an epoch is shown
				testingLoss = testingPass(model, data,1500,random);// gets the testing loss
				epochStringBuilder.setLength(0);//resets the string builder
				epochStringBuilder.append("epoch[").append(epoch).append(epochSuffix);
				duration = System.currentTimeMillis() - previousTime;
				epochStringBuilder.append("\ttime = ").append(duration);//Shows the time
				epochStringBuilder.append("\t training loss = ").append(trainingLoss);//shows the training loss
				epochStringBuilder.append("\t testing loss = ").append(testingLoss);//shows the testing loss
				System.out.println(epochStringBuilder.toString());
				if(epoch % displayReportPeriod == 0) {
					data.displayReport(model);//Shows a report
				}
					
			}
			
			
			
		}
		
		return localMinimum;
		
		
	}

    private double trainingPass(Model model, DataSet data) {
        numeratorLoss = 0;
        DataStep step;
        for(int i = data.getTrainingDataStepsSize()-1; i>=0; i--) {
            step = data.getTrainingDataSteps().get(i);
            model.runAndDecideImprovements(step, output, step.getTargetOutputVector());
            numeratorLoss += DataSet.trainingLoss.measureLoss(output, step.getTargetOutputVector());
        }

        beta1ForEpoch *= beta1;
        beta2ForEpoch *= beta2;

        double alphaForEpoch = alpha * Math.sqrt(1 - beta2ForEpoch) / (1 - beta1ForEpoch);// so that alpha decreases over time to allow for faster and more accurate convergence

        model.updateModelParameters(momentum, beta1, beta2, alphaForEpoch, oneMinusBeta1, oneMinusBeta2);// updates the model

        return numeratorLoss*data.getReciprocalOfTrainingSize();
    }

    private  double testingPass(Model model, DataSet data, int numTested, CustomRandom rand) {
		numeratorLoss = 0;
		DataStep step;

		int[] toTest = rand.randomDistinctIntArray(numTested, data.getTestingDataStepsSize());//gets the list of indexes of testing data steps to be tested
		for(int i=toTest.length-1;i>=0;i--) {
			step = data.getTestingDataSteps().get(toTest[i]);
			model.run(step, output);// runs the model
			numeratorLoss += DataSet.trainingLoss.measureLoss(output, step.getTargetOutputVector());//gets the loss

		}

		return numeratorLoss/toTest.length;
	}


	
}
