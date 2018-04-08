package models;

import dataStructures.DataStep;
import generalUtilities.CustomRandom;
import matrices.Vector;
import training.DataProcessing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProportionProbabilityForCharacterModel implements Model {


	private DataProcessing dataProcessing;
	private Map<String, ProportionProbability> getProbabilityDecider = new HashMap<>();
	private int windowSideSize;
	private CustomRandom random;
	private StringBuilder stringBuilder = new StringBuilder();

	public ProportionProbabilityForCharacterModel(List<DataStep> trainingData, int size, DataProcessing dataProcessing, CustomRandom random) {
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
				int lowerBound = (i-windowSideSize<0)? 0:i-windowSideSize;
				int upperBound = (i+windowSideSize>inputText.length())? inputText.length():i+windowSideSize;
				String substring = inputText.substring(lowerBound,upperBound);
				if(i>outputText.length()-1) {
					break;
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
					counts.set(index, prevValue+1);
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
		}

		for (String uniqueString : uniqueStrings) {
			ArrayList<Character> charactersForString = new ArrayList<>();
			ArrayList<Integer> countForCharacter = new ArrayList<>();
			int totalCountsForString = 0;
			for (int i = 0; i < strings.size(); i++) {
				if (uniqueString.equals(strings.get(i))) {
					charactersForString.add(characters.get(i));
					countForCharacter.add(counts.get(i));
					totalCountsForString += counts.get(i);
				}

			}
			char[] characterArray = new char[charactersForString.size()];
			double[] probabilities = new double[countForCharacter.size()];
			for (int i = 0; i < charactersForString.size(); i++) {
				characterArray[i] = charactersForString.get(i);
				probabilities[i] = (double) countForCharacter.get(i) / (double) totalCountsForString;
			}
			getProbabilityDecider.put(uniqueString, new ProportionProbability(probabilities, characterArray));
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
		stringBuilder.setLength(0);
		for (int i = 0; i < input.getInputText().length(); i++) {
			int lowerBound = (i-windowSideSize<0)? 0:i-windowSideSize;
			int upperBound = (i + windowSideSize > input.getInputText().length()) ? input.getInputText().length() : i + windowSideSize;
			String substring = input.getInputText().substring(lowerBound, upperBound);
			if(getProbabilityDecider.containsKey(substring)) {
				stringBuilder.append(getProbabilityDecider.get(substring).execute(random));
			}else {
				stringBuilder.append(input.getInputText().charAt(i));
			}

		}
		output.setData(dataProcessing.stringToDoubleArray(stringBuilder.toString()));
	}

	@Override
	public void runAndDecideImprovements(DataStep input, Vector output, Vector targetOutput) {
		run(input, output);

	}

	@Override
	public void updateModelParameters(double momentum, double beta1, double beta2, double alpha, double OneMinusBeta1,
									  double OneMinusBeta2) {

	}

	private class ProportionProbability {

		boolean onlyOneOption;
		char onlyOption;
		private double[] valuesForProbabilities;
		private char[] outputs;

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
			if (onlyOneOption) {
				return onlyOption;
			} else {
				double value = random.randomDouble();
				for (int i = 0; i < valuesForProbabilities.length; i++) {
					if (value < valuesForProbabilities[i]) {
						return outputs[i];
					}
				}
				return ' ';
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

