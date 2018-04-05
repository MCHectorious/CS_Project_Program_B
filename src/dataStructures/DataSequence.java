package dataStructures;

import java.util.ArrayList;
import java.util.List;

import manualTranslation.AutoDataIncrease;
import training.DataPreparation;
import util.CustomRandom;

public class DataSequence {
	private List<DataStep> dataSteps = new ArrayList<>();
	public DataSequence(List<DataStep> steps) {
		this.dataSteps = steps;
	}
	public List<DataStep> getDataSteps(){
		return dataSteps;
	}
	public DataSequence() {}
	public int getSize() {
		return dataSteps.size();
	}
	public DataStep getRandom(CustomRandom util) {
		return dataSteps.get(util.randomInt(dataSteps.size()-1));
	}
	public void addDataStep(double[] input, double[] targetOutput) {
		dataSteps.add(new DataStep(input, targetOutput));
	}
	public void addDataStep(double[] input, double[] targetOutput, String inputText, String outputText) {
		dataSteps.add(new DataStep(input, targetOutput,inputText, outputText));
	}
	
	public void addDataStepsWithCapitilisationVariation(double[] input, double[] output,  String inputText, String outputText, DataPreparation dataPrep) {
		dataSteps.add(new DataStep(input, output,inputText, outputText));

		Flashcard card = new Flashcard(inputText);
		
		ArrayList<DataStep> steps = AutoDataIncrease.addCapitalisationVariation(card.getFront(), card.getBack(), dataPrep, output, outputText);
		//System.out.print("~"+steps.size()+"~");
		for(DataStep step: steps) {
			//System.out.println("Input: "+step.getInputText()+" Output:"+step.getOutputText());
			dataSteps.add(step);
		}
		
	}
}
