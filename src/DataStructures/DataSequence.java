package DataStructures;

import GeneralUtilities.CustomRandom;
import ManualTranslation.AutomaticDataPreProcessing;
import Training.DataProcessing;

import java.util.ArrayList;
import java.util.List;

public class DataSequence {

	private List<DataStep> dataSteps = new ArrayList<>();// The data steps in this  sequence

    List<DataStep> getDataSteps() {
		return dataSteps;
	}

	public int getSize() {
		return dataSteps.size();
	}

    DataStep getRandomDataStep(CustomRandom random) {
		return dataSteps.get(random.randomInt(dataSteps.size()-1));
	}

    void addDataStep(double[] input, double[] targetOutput, String inputText, String outputText) {
		dataSteps.add(new DataStep(input, targetOutput,inputText, outputText));
	}

    void addDataStepsWithCapitalisationVariation(double[] input, double[] output, String inputText, String outputText, DataProcessing dataProcessing) {
		dataSteps.add(new DataStep(input, output,inputText, outputText));//Adds the original data step
		Flashcard flashcard = new Flashcard(inputText);
        ArrayList<DataStep> steps = AutomaticDataPreProcessing.addCapitalisationVariation(flashcard.getFlashcardFront(), flashcard.getFlashcardBack(), dataProcessing, output, outputText);//Gets the data step but the capitalisation of the first character is flipped if possible
        dataSteps.addAll(steps);
	}
}
