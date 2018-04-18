package dataStructures;

import fileManipulation.DataImport;
import generalUtilities.CustomRandom;
import generalUtilities.Utilities;
import lossFunctions.LossStringDistance;
import matrices.Vector;
import models.Model;
import training.DataProcessing;

import java.util.ArrayList;
import java.util.List;

public class FlashcardDataSet implements DataSet{
	
	private DataSequence trainingDataSequence = new DataSequence();//The list of data steps the model will be trained on
	private DataSequence testingDataSequence = new DataSequence();//The list of data steps the model is not trained on in order to see how well themodel performs on data it has not seen before
    private final static String BORDER_FOR_REPORT = "==============================================================================================";//Used to separate the report from any other output in the console
    private final double reciprocalOfTrainingSize;//Calculated once to avoid doing division many times which can be time-consuming
	private CustomRandom random;
    private DataProcessing dataProcessing;//Used to convert data from the numerical form to a textual form and vice versa
    private Vector modelOutput = new Vector(DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR);//Created once to avoid creating objects in a loop which can be time consuming
	private LossStringDistance stringDistanceLoss;
	
	public int getTrainingDataStepsSize() {
		return trainingDataSequence.getSize();
	}
	
	public double getReciprocalOfTrainingSize() {
		return reciprocalOfTrainingSize;
	}

	public List<DataStep> getTrainingDataSteps() {
		return trainingDataSequence.getDataSteps();
	}
	
	public List<DataStep> getTestingDataSteps() {
		return testingDataSequence.getDataSteps();
	}
	
	public int getTestingDataStepsSize() {
		return testingDataSequence.getSize();
	}
	
	public FlashcardDataSet(String dataFilePath, CustomRandom random) {
		this.random = random;
		ArrayList<String> linesFromTextFile = DataImport.getLinesFromTextFile(dataFilePath);//Gets the lines representing the data steps from a text file
        dataProcessing = new DataProcessing(linesFromTextFile);//Initiated so that the program can convert numerical data to textual data and vice versa
		stringDistanceLoss = new LossStringDistance(dataProcessing);//Used to measure the real-world loss of  the model

		for(String line: linesFromTextFile) {

			double[] flashcardDoubleArray = dataProcessing.stringToDoubleArray(Flashcard.getFlashcard(line));
			double[] sentenceDoubleArray = dataProcessing.stringToDoubleArray(Flashcard.getSentence(line));
			if(this.random.randomBoolean() || this.random.randomBoolean()) {//Should be true approximately 3/4o of the time
				trainingDataSequence.addDataStepsWithCapitalisationVariation(flashcardDoubleArray, sentenceDoubleArray,Flashcard.getFlashcard(line),Flashcard.getSentence(line), dataProcessing);//Also includes capitalisation variation to give the model more data to learn from
			}else {
				testingDataSequence.addDataStep(flashcardDoubleArray, sentenceDoubleArray,Flashcard.getFlashcard(line),Flashcard.getSentence(line));
			}

		}
		System.out.println("Total phrases = " + dataProcessing.getNumberOfPhrases());
		System.out.println(trainingDataSequence.getSize() + " steps in trainingDataSequence set");
		System.out.println(testingDataSequence.getSize() + " steps in testingDataSequence set");
		reciprocalOfTrainingSize = 1.0/ trainingDataSequence.getSize();//Calculated just once

    }

    public DataProcessing getDataProcessing() {
        return dataProcessing;
    }
	
	@Override
	public void displayReport(Model model) {
		DataStep exampleTestingDataStep =  testingDataSequence.getRandomDataStep(random) ;// gets a random testing data step

		StringBuilder reportBuilder = new StringBuilder(300);//Initiated to 300 because that it approximately the expected size of the output

		reportBuilder.append(BORDER_FOR_REPORT).append("\nReport:\n");//Provides a title for the report

		reportBuilder.append("Input: ").append(exampleTestingDataStep.getInputText()).append("\n");//Shows the input textually

        reportBuilder.append(Utilities.arrayToString(exampleTestingDataStep.getInput())).append("\n");//Shows the input numerically
		
		reportBuilder.append("Expected Output: \t").append( exampleTestingDataStep.getTargetOutputText()).append("\n");//Shows the target output textually

        reportBuilder.append(Utilities.arrayToString(exampleTestingDataStep.getTargetOutput())).append("\n");// Shows the expected output numerically

        model.run(exampleTestingDataStep, modelOutput);//Runs the model
		
		String modelOutputString = dataProcessing.doubleArrayToString(modelOutput.getData());//Gets the output as a string

		reportBuilder.append("Actual Output: \t").append(modelOutputString ).append("\n");//Shows the output textually

        reportBuilder.append(Utilities.arrayToString(modelOutput.getData())).append("\n");//Shows the output numerically

		reportBuilder.append("String Distance: ").append(stringDistanceLoss.measure(modelOutputString, exampleTestingDataStep.getTargetOutputText()));//Shows the string distance between the output from the model and the target output
		
		reportBuilder.append("\n").append(BORDER_FOR_REPORT);//Shows the end of the report

		System.out.println(reportBuilder.toString());//Outputs this information
		
	}


}
