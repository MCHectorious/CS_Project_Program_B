package dataStructures;

import generalUtilities.CustomRandom;
import manualTranslation.AutomaticDataPreProcessing;
import training.DataProcessing;

import java.util.ArrayList;
import java.util.List;

public class DataSequence {
	private List<DataStep> dataSteps = new ArrayList<>();

    DataSequence() {
    }

    List<DataStep> getDataSteps() {
		return dataSteps;
	}
	public int getSize() {
		return dataSteps.size();
	}

    DataStep getRandom(CustomRandom util) {
		return dataSteps.get(util.randomInt(dataSteps.size()-1));
	}

    void addDataStep(double[] input, double[] targetOutput, String inputText, String outputText) {
		dataSteps.add(new DataStep(input, targetOutput,inputText, outputText));
	}

    void addDataStepsWithCapitilisationVariation(double[] input, double[] output, String inputText, String outputText, DataProcessing dataPrep) {
		dataSteps.add(new DataStep(input, output,inputText, outputText));

		Flashcard card = new Flashcard(inputText);

        ArrayList<DataStep> steps = AutomaticDataPreProcessing.addCapitalisationVariation(card.getFront(), card.getBack(), dataPrep, output, outputText);

        dataSteps.addAll(steps);
		
	}
}
