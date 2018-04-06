package dataStructures;

import java.util.ArrayList;
import java.util.List;

import fileManipulation.DataImport;
import generalUtilities.CustomRandom;
import lossFunctions.LossStringDistance;
import matrices.Vector;

import models.Model;
import nonlinearityFunctions.NonLinearity;
import nonlinearityFunctions.RoughTanhUnit;
import training.DataPreparation;

public class TestingDataSet implements DataSet {

	private static final String border = "--------------------------------------------------------------";
	private DataSequence training = new DataSequence();
	private DataSequence testing = new DataSequence();
	private DataPreparation dataPrep;
	private double ReciprocalOfTrainingSize;
	private double ReciprocalOfTestingSize;
	private CustomRandom random;
	private Vector modelOutput = new Vector(3);
	private Vector modelInput = new Vector(3);
	private LossStringDistance stringLoss;
	private RoughTanhUnit roughTanhUnit = new RoughTanhUnit();

	
	
	public TestingDataSet(CustomRandom rand) {
		random = rand;
		

		
		ArrayList<String> lines = DataImport.getLines("DataSets/TranslatedFlashcards.txt");
		dataPrep = new DataPreparation(lines);
		stringLoss = new LossStringDistance(dataPrep);
		
		training.addDataStep( new double[]{0.25,-0.75,-0.25} , new double[] {-0.9493670886075949, -0.9646017699115044, -0.9767441860465116}, "man", "anm");
		training.addDataStep( new double[]{-0.25,-0.75,-0.25} , new double[]{-0.9646017699115044, -0.9760479041916168, -0.9836065573770492} , "saw", "aws");
		training.addDataStep( new double[]{-0.15,-0.65,-0.95} , new double[]{-0.9748427672955975, -0.9835119538334708, -0.988399071925754} , "que", "ueq");
		
		testing.addDataStep( new double[]{-0.85,-0.35,-0.995} , new double[]{-0.9782797567332754, -0.9857893988915731, -0.9899152884227511} , "nod", "odn");
		testing.addDataStep( new double[]{-0.65,-0.765,-0.765} , new double[]{-0.9785913080710769, -0.9860393689794779, -0.9900833002776676} , "uno", "nou");
		testing.addDataStep( new double[]{-0.125,-0.715,-0.85} , new double[]{-0.9740630268447672, -0.9829685770246104, -0.9880411384836164} , "two", "wot");

		System.out.println("Total phrases = " + dataPrep.getNumOfPhrases());
		System.out.println(training.getSize() + " steps in training set");
		System.out.println(testing.getSize() + " steps in testing set");
		ReciprocalOfTrainingSize = 1.0/training.getSize();
		ReciprocalOfTestingSize = 1.0/testing.getSize();
		
		lines = null;
		
	}
	
	@Override
	public void DisplayReport(Model model) {
		DataStep example =  testing.getRandom(random) ;
		StringBuilder builder = new StringBuilder(300);
		builder.append(border+"\nReport:\n");
		builder.append("Input: ").append(example.getInputText()).append("\n");
		builder.append("Expected Output: \t").append( example.getOutputText()).append("\n");
		modelInput.setData(example.getInput());
		//modelInput.toString(builder);
		//builder.append("\n");
		model.forward( modelInput, modelOutput);
		//modelOutput.toString(builder);
		//builder.append("\n");
		String modelOutputString = dataPrep.doubleArrayToString(modelOutput.getData());
		builder.append("Actual Output: \t\t").append(modelOutputString ).append("\n");
		builder.append("String Distance: ").append(stringLoss.measure(modelOutputString, example.getOutputText()));
		builder.append("\n").append(border);
		System.out.println(builder.toString());
	}

	@Override
	public NonLinearity getDataSetNonLinearity() {
		
		return roughTanhUnit;
	}

	public int getTrainingSize() {
		return training.getSize();
	}
	
	public double getReciprocalOfTrainingSize() {
		return ReciprocalOfTrainingSize;
	}

	public double getReciprocalOfTestingSize() {
		return ReciprocalOfTestingSize;
	}

	public List<DataStep> getTrainingDataSteps() {
		return training.getDataSteps();
	}
	
	public List<DataStep> getTestingDataSteps() {
		return testing.getDataSteps();
	}
	
	public int getTestingSize() {
		return testing.getSize();
	}
	
	public DataPreparation getDataPrep() {
		return dataPrep;
	}

}
