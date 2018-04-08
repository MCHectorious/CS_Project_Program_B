package dataStructures;

import fileManipulation.DataImport;
import generalUtilities.CustomRandom;
import lossFunctions.LossStringDistance;
import matrices.Vector;
import models.Model;
import training.DataProcessing;

import java.util.ArrayList;
import java.util.List;

public class TestingDataSet implements DataSet {

	private static final String reportBorder = "--------------------------------------------------------------";
	private DataSequence trainingDataSequence = new DataSequence();
	private DataSequence testingDataSequence = new DataSequence();
    private DataProcessing dataProcessing;
	private double reciprocalOfTrainingSize;
	private CustomRandom random;
	private Vector modelOutput = new Vector(3);
	private Vector modelInput = new Vector(3);
	private LossStringDistance stringDistanceLoss;
	
	public TestingDataSet(CustomRandom rand) {
		random = rand;

		ArrayList<String> linesFromTextFile = DataImport.getLinesFromTextFile("DataSets/TranslatedFlashcards.txt");
        dataProcessing = new DataProcessing(linesFromTextFile);
		stringDistanceLoss = new LossStringDistance(dataProcessing);
		
		trainingDataSequence.addDataStep( new double[]{0.25,-0.75,-0.25} , new double[] {-0.9493670886075949, -0.9646017699115044, -0.9767441860465116}, "man", "anm");
		trainingDataSequence.addDataStep( new double[]{-0.25,-0.75,-0.25} , new double[]{-0.9646017699115044, -0.9760479041916168, -0.9836065573770492} , "saw", "aws");
		trainingDataSequence.addDataStep( new double[]{-0.15,-0.65,-0.95} , new double[]{-0.9748427672955975, -0.9835119538334708, -0.988399071925754} , "que", "ueq");
		
		testingDataSequence.addDataStep( new double[]{-0.85,-0.35,-0.995} , new double[]{-0.9782797567332754, -0.9857893988915731, -0.9899152884227511} , "nod", "odn");
		testingDataSequence.addDataStep( new double[]{-0.65,-0.765,-0.765} , new double[]{-0.9785913080710769, -0.9860393689794779, -0.9900833002776676} , "uno", "nou");
		testingDataSequence.addDataStep( new double[]{-0.125,-0.715,-0.85} , new double[]{-0.9740630268447672, -0.9829685770246104, -0.9880411384836164} , "two", "wot");

		System.out.println("Total phrases = " + dataProcessing.getNumOfPhrases());
		System.out.println(trainingDataSequence.getSize() + " steps in trainingDataSequence set");
		System.out.println(testingDataSequence.getSize() + " steps in testingDataSequence set");
		reciprocalOfTrainingSize = 1.0/ trainingDataSequence.getSize();

	}
	
	@Override
	public void displayReport(Model model) {
		DataStep exampleTestingDataStep =  testingDataSequence.getRandom(random) ;
		StringBuilder reportBuilder = new StringBuilder(300);
		reportBuilder.append(reportBorder +"\nReport:\n");
		reportBuilder.append("Input: ").append(exampleTestingDataStep.getInputText()).append("\n");
		reportBuilder.append("Expected Output: \t").append( exampleTestingDataStep.getTargetOutputText()).append("\n");
		modelInput.setData(exampleTestingDataStep.getInput());
        model.run(new DataStep(modelInput), modelOutput);
		String modelOutputString = dataProcessing.doubleArrayToString(modelOutput.getData());
		reportBuilder.append("Actual Output: \t\t").append(modelOutputString ).append("\n");
		reportBuilder.append("String Distance: ").append(stringDistanceLoss.measure(modelOutputString, exampleTestingDataStep.getTargetOutputText()));
		reportBuilder.append("\n").append(reportBorder);
		System.out.println(reportBuilder.toString());
	}

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
	

}
