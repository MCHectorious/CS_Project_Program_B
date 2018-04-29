package Models;

import DataStructures.DataStep;
import GeneralUtilities.CustomRandom;
import Matrices.Vector;
import Training.DataProcessing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProportionalProbabilityForCharacterModel implements Model {


	private DataProcessing dataProcessing;//To convert the numerical data to text and vice versa
	private Map<String, ProportionProbability> getProbabilityDecider = new HashMap<>();//Gets what should occur given an input string
	private int windowSideSize;//The size of the strings to chheck
	private CustomRandom random;
	private StringBuilder stringBuilder = new StringBuilder();//Created here to avoid creating objects in a loop

	public ProportionalProbabilityForCharacterModel(List<DataStep> trainingData, int size, DataProcessing dataProcessing, CustomRandom random) {
		this.dataProcessing = dataProcessing;
		this.random = random;
		windowSideSize = size;

		ArrayList<String> strings = new ArrayList<>();
		ArrayList<Character> characters = new ArrayList<>();
		ArrayList<Integer> counts = new ArrayList<>();

		for(DataStep step: trainingData) {

			String inputText = step.getInputText();
			String outputText = step.getTargetOutputText();

			for(int i=0;i<inputText.length();i++) {
				int lowerBound = (i-windowSideSize<0)? 0:i-windowSideSize;//So that early sub-strings can be used as samples
				int upperBound = (i+windowSideSize>inputText.length())? inputText.length():i+windowSideSize;//So that late sub-strings can be used as samples
				String substring = inputText.substring(lowerBound,upperBound);
				if(i>outputText.length()-1) {
					break;//Stop when the end of the text has been reached
				}
				Character character = outputText.charAt(i);

				boolean tupleExists = false;
				for(int j=0;j<strings.size();j++) {
					if(substring.equals(strings.get(j))) {
						if(characters.get(j).charValue() == character.charValue()) {
							tupleExists = true;
							break;
						}
					}

				}

				if(tupleExists) {
					int index = strings.indexOf(substring);
					int prevValue = counts.get(index);
					counts.set(index, prevValue+1);//increments the count of the input-output pair//Adds the input output pair
				}else {
					strings.add(substring);
					characters.add(character);
					counts.add(1);
				}


			}

		}

		ArrayList<String> uniqueStrings = new ArrayList<>();

		for (String string : strings) {
			if (!uniqueStrings.contains(string)) {
				uniqueStrings.add(string);
			}
		}//Gets the unique strings

		for (String uniqueString : uniqueStrings) {
			ArrayList<Character> charactersForString = new ArrayList<>();
			ArrayList<Integer> countForCharacter = new ArrayList<>();
			int totalCountsForString = 0;
			for (int i = 0; i < strings.size(); i++) {
				if (uniqueString.equals(strings.get(i))) {
					charactersForString.add(characters.get(i));
					countForCharacter.add(counts.get(i));
					totalCountsForString += counts.get(i);//Gets the total
				}

			}
			char[] characterArray = new char[charactersForString.size()];
			double[] probabilities = new double[countForCharacter.size()];
			for (int i = 0; i < charactersForString.size(); i++) {
				characterArray[i] = charactersForString.get(i);
				probabilities[i] = (double) countForCharacter.get(i) / (double) totalCountsForString;//Gets the empirical probability
			}
			getProbabilityDecider.put(uniqueString, new ProportionProbability(probabilities, characterArray));//Adds the probability decider
		}


	}

	@Override
	public String provideDescription() {
		StringBuilder stringBuilder = new StringBuilder();
		for (String string : getProbabilityDecider.keySet()) {
			stringBuilder.append(string).append("\t");
			getProbabilityDecider.get(string).toString(stringBuilder);
			stringBuilder.append("\n");
		}
		return stringBuilder.toString();
	}

	@Override
	public void provideDescription(StringBuilder stringBuilder) {
		for (String string : getProbabilityDecider.keySet()) {
			stringBuilder.append(string).append("\t");
			getProbabilityDecider.get(string).toString(stringBuilder);
			stringBuilder.append("\n");
		}

	}

	@Override
	public void run(DataStep input, Vector output) {
		stringBuilder.setLength(0);//resets the string builder
		for (int i = 0; i < input.getInputText().length(); i++) {
			int lowerBound = (i-windowSideSize<0)? 0:i-windowSideSize;//so that early strings can  be used
			int upperBound = (i + windowSideSize > input.getInputText().length()) ? input.getInputText().length() : i + windowSideSize;//so that late strings can  be used
			String substring = input.getInputText().substring(lowerBound, upperBound);
			if(getProbabilityDecider.containsKey(substring)) {
				stringBuilder.append(getProbabilityDecider.get(substring).execute(random));
			}else {
				stringBuilder.append(input.getInputText().charAt(i));//Otherwise it just copies the value
			}

		}
		output.setData(dataProcessing.stringToDoubleArray(stringBuilder.toString()));//Sets the output
	}

	@Override
	public void runAndDecideImprovements(DataStep input, Vector output, Vector targetOutput) {
		run(input, output);//Will not decide improvements

	}

	@Override
	public void updateModelParameters(double momentum, double beta1, double beta2, double alpha, double OneMinusBeta1,
									  double OneMinusBeta2) {
		//Will not update the model parameters
	}

	private class ProportionProbability {

		private boolean onlyOneOption;//states whether there is only one character output
		private char onlyOption;//the only output, if there is only one output
		private double[] valuesForProbabilities;//the empirical probabilities of the outputs
		private char[] outputs;//the possible characters

		ProportionProbability(double[] probabilities, char[] outputs) {
			if (outputs.length == 1) {
				onlyOneOption = true;
				onlyOption = outputs[0];
			} else {
				valuesForProbabilities = new double[probabilities.length];
				double runningTotal = 0;
				for (int i = 0; i < probabilities.length; i++) {
					runningTotal += probabilities[i];
					valuesForProbabilities[i] = runningTotal;
				}
				this.outputs = outputs;
			}

		}

		char execute(CustomRandom random) {
			if (onlyOneOption) {//to save on computation time
				return onlyOption;
			} else {
				double value = random.randomDouble();
				for (int i = 0; i < valuesForProbabilities.length; i++) {
					if (value < valuesForProbabilities[i]) {
						return outputs[i];
					}
				}
				return ' ';//defaults to a space
			}

		}

		public void toString(StringBuilder b) {
			if (onlyOneOption) {
				b.append("[").append(onlyOption).append("]");
			} else {
				b.append("[");
				for (int i = 0; i < outputs.length; i++) {
					b.append(valuesForProbabilities[i]).append(" - ").append(outputs[i]).append("\t");
				}
				b.append("]");
			}

		}
	}


	@Override
	public void resetState() {
		
	}

	
	
}

