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
	
	private DataSequence trainingDataSequence = new DataSequence();
	private DataSequence testingDataSequence = new DataSequence();
    private final static String borderForReport = "==============================================================================================";
    private final double reciprocalOfTrainingSize;
	private CustomRandom random;
    private DataProcessing dataProcessing;
    private Vector modelOutput = new Vector(DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR);
    private Vector modelInput = new Vector(DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR);
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

		ArrayList<String> linesFromTextFile = DataImport.getLinesFromTextFile(dataFilePath);
        dataProcessing = new DataProcessing(linesFromTextFile);
		stringDistanceLoss = new LossStringDistance(dataProcessing);

		for(String line: linesFromTextFile) {

			double[] flashcardDoubleArray = dataProcessing.stringToDoubleArray(Flashcard.getFlashcard(line));
			double[] sentenceDoubleArray = dataProcessing.stringToDoubleArray(Flashcard.getSentence(line));
			if(this.random.randomBoolean() || this.random.randomBoolean()) {
				trainingDataSequence.addDataStepsWithCapitalisationVariation(flashcardDoubleArray, sentenceDoubleArray,Flashcard.getFlashcard(line),Flashcard.getSentence(line), dataProcessing);
			}else {
				testingDataSequence.addDataStep(flashcardDoubleArray, sentenceDoubleArray,Flashcard.getFlashcard(line),Flashcard.getSentence(line));
			}
		}
		System.out.println("Total phrases = " + dataProcessing.getNumOfPhrases());
		System.out.println(trainingDataSequence.getSize() + " steps in trainingDataSequence set");
		System.out.println(testingDataSequence.getSize() + " steps in testingDataSequence set");
		reciprocalOfTrainingSize = 1.0/ trainingDataSequence.getSize();

    }

    public DataProcessing getDataProcessing() {
        return dataProcessing;
    }
	
	@Override
	public void displayReport(Model model) {
		DataStep exampleTestingDataStep =  testingDataSequence.getRandom(random) ;

		StringBuilder reportBuilder = new StringBuilder(300);

		reportBuilder.append(borderForReport).append("\nReport:\n");

		reportBuilder.append("Input: ").append(exampleTestingDataStep.getInputText()).append("\n");

        reportBuilder.append(Utilities.arrayToString(exampleTestingDataStep.getInput())).append("\n");
		
		reportBuilder.append("Expected Output: \t").append( exampleTestingDataStep.getTargetOutputText()).append("\n");

        reportBuilder.append(Utilities.arrayToString(exampleTestingDataStep.getTargetOutput())).append("\n");
		
		modelInput.setData(exampleTestingDataStep.getInput());

        model.run(new DataStep(modelInput), modelOutput);
		
		String modelOutputString = dataProcessing.doubleArrayToString(modelOutput.getData());

		reportBuilder.append("Actual Output: \t").append(modelOutputString ).append("\n");

        reportBuilder.append(Utilities.arrayToString(modelOutput.getData())).append("\n");

		reportBuilder.append("String Distance: ").append(stringDistanceLoss.measure(modelOutputString, exampleTestingDataStep.getTargetOutputText()));
		
		reportBuilder.append("\n").append(borderForReport);

		System.out.println(reportBuilder.toString());
		
	}


}
