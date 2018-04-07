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


	private DataProcessing dataPrep;
	private Map<String, ProportionProbability> getProbDecider = new HashMap<>();
	private int windowSideSize;
	private CustomRandom random;
	private StringBuilder builder = new StringBuilder();

	public ProportionProbabilityForCharacterModel(List<DataStep> trainingData, int size, DataProcessing dataPrep, CustomRandom random) {
		this.dataPrep = dataPrep;
		this.random = random;
		windowSideSize = size;
		//Map<StringCharTuple, Integer> count = new HashMap<>();
		//System.out.println("Starting Categoric Proportion Probability Model Initialisation");

		ArrayList<String> strings = new ArrayList<>();
		ArrayList<Character> chars = new ArrayList<>();
		ArrayList<Integer> count = new ArrayList<>();

		//int tempCounter = 0;
		for(DataStep step: trainingData) {
			//if(tempCounter % 100 == 0) {
				//System.out.println(tempCounter);
			//}
			//tempCounter++;

			String input = step.getInputText();
			String outputText = step.getOutputText();

			for(int i=0;i<input.length();i++) {
				int lowerBound = (i-windowSideSize<0)? 0:i-windowSideSize;
				int upperBound = (i+windowSideSize>input.length())? input.length():i+windowSideSize;
				String substring = input.substring(lowerBound,upperBound);
				if(i>outputText.length()-1) {
					break;
				}
				Character c = outputText.charAt(i);

				boolean tupleExists = false;
				for(int j=0;j<strings.size();j++) {
					//System.out.println(string+" "+substring+" "+substring.equals(string));

					if(substring.equals(strings.get(j))) {
						//System.out.println("// "+chars.get(strings.indexOf(string))+" "+c+" "+chars.get(strings.indexOf(string)).equals(c));
						//System.out.println(chars.get(j) + " " + c + " " + (chars.get(j) == c) );
						if(chars.get(j).charValue() == c.charValue()) {
							tupleExists = true;
							break;
						}
					}

				}

				if(tupleExists) {
					int index = strings.indexOf(substring);
					int prevValue = count.get(index);
					count.set(index, prevValue+1);
				}else {
					strings.add(substring);
					chars.add(c);
					count.add(1);
				}


			}

		}

		ArrayList<String> uniqueStrings = new ArrayList<>();

		for (String string1 : strings) {
			//DataExport.appendToTextFile(strings.get(i)+" --> "+chars.get(i)+" = "+count.get(i), "Models/Temp.txt");
			//System.out.println(strings.get(i)+" --> "+chars.get(i)+" = "+count.get(i));
			if (!uniqueStrings.contains(string1)) {
				uniqueStrings.add(string1);
				//System.out.println(strings.get(i));
			}
		}

		//System.out.println("Unique String Phrases: "+uniqueStrings.size() );


		for (String uniqueString : uniqueStrings) {
			ArrayList<Character> charsForString = new ArrayList<>();
			ArrayList<Integer> countForChar = new ArrayList<>();
			int totalCountsForString = 0;
			for (int i = 0; i < strings.size(); i++) {
				if (uniqueString.equals(strings.get(i))) {
					charsForString.add(chars.get(i));
					countForChar.add(count.get(i));
					totalCountsForString += count.get(i);
				}

			}
			char[] charsArray = new char[charsForString.size()];
			double[] probs = new double[countForChar.size()];
			//System.out.print(string+"\t");
			for (int i = 0; i < charsForString.size(); i++) {
				charsArray[i] = charsForString.get(i);
				probs[i] = (double) countForChar.get(i) / (double) totalCountsForString;
				//System.out.print(probs[i]+" - "+charsArray[i]+" ");
			}
			getProbDecider.put(uniqueString, new ProportionProbability(probs, charsArray));
			//System.out.println();
		}

		//StringBuilder tempBuilder = new StringBuilder();

		//this.getParams(tempBuilder);
		//System.out.println(tempBuilder.toString());


		//for(StringCharTuple tuple: count.keySet()) {
		//	System.out.println(tuple.string+" --> "+tuple.character+" = "+count.get(tuple));
		//}

		/*System.out.println("Finished Counting");

		ArrayList<String> strings = new ArrayList<>();
		for(StringCharTuple stringAndChar: count.keySet()) {
			if(!strings.contains(stringAndChar.getString())) {
				strings.add(stringAndChar.getString());
			}
		}

		System.out.println("Organised Phrases");

		System.out.println(strings.size());
		for(int a=strings.size()-1;a>=0;a--) {
			if(a % 1000 == 0) {
				System.out.print(a+" ");
			}

			int previous =0;
			String string = strings.get(a);

			ArrayList<Integer> values = new ArrayList<>();
			ArrayList<Character> characters = new ArrayList<>();

			//Map<Integer, Character> map = new HashMap<>();
			for(StringCharTuple stringAndChar: count.keySet()) {
				if(string.equals(stringAndChar.getString())) {

					previous += count.get(stringAndChar);
					//map.put(previous, stringAndChar.character);
					values.add(previous);
					characters.add(stringAndChar.character);

				}
			}
			//Integer[] values =  map.keySet().toArray(new Integer[0]);
			//for(Integer i: map.keySet()) {
				//values.add(i);
			//}
			//Collections.sort(values);
			double[] probs = new double[values.size()];
			char[] chars = new char[values.size()];

			//int value;
			double reciprocalOfSize = 1.0/previous;
			for(int index=0;index<values.size();index++) {

				probs[index] = reciprocalOfSize*values.get(index);
				chars[index] = characters.get(index);
			}
			getProbDecider.put(string, new ProportionProbability(probs,chars));
		}
		*/
		//System.out.println("\n Finished Intialisation");

	}

	@Override
	public String description() {
		StringBuilder stringBuilder = new StringBuilder();
		for (String string : getProbDecider.keySet()) {
			stringBuilder.append(string).append("\t");
			getProbDecider.get(string).toString(stringBuilder);
			stringBuilder.append("\n");
		}
		return stringBuilder.toString();
	}

	@Override
	public void description(StringBuilder stringBuilder) {
		for (String string : getProbDecider.keySet()) {
			stringBuilder.append(string).append("\t");
			getProbDecider.get(string).toString(stringBuilder);
			stringBuilder.append("\n");
		}

	}

	@Override
	public void run(DataStep input, Vector output) {
		builder.setLength(0);
		for (int i = 0; i < input.getInputText().length(); i++) {
			int lowerBound = (i-windowSideSize<0)? 0:i-windowSideSize;
			int upperBound = (i + windowSideSize > input.getInputText().length()) ? input.getInputText().length() : i + windowSideSize;
			String substring = input.getInputText().substring(lowerBound, upperBound);
			if(getProbDecider.containsKey(substring)) {
				builder.append(getProbDecider.get(substring).execute(random));
			}else {
				builder.append(input.getInputText().charAt(i));
			}

		}
		output.setData(dataPrep.stringToDoubleArray(builder.toString()));
	}

	@Override
	public void runAndDecideImprovements(DataStep input, Vector output, Vector targetOutput) {
		run(input, output);

	}

	@Override
	public void updateModelParameters(double momentum, double beta1, double beta2, double alpha, double OneMinusBeta1,
									  double OneMinusBeta2) {

	}

	private class StringCharTuple {
		private String string;
		private char character;


		public StringCharTuple(String s, char c) {
			string = s;
			character = c;
		}

		public String getString() {
			return string;
		}


	}

	private class ProportionProbability {

		boolean onlyOneOption;
		char onlyOption;
		private double[] valuesForProbs;
		private char[] outputs;

		ProportionProbability(double[] probs, char[] out) {
			if (out.length == 1) {
				onlyOneOption = true;
				onlyOption = out[0];
			} else {
				valuesForProbs = new double[probs.length];
				double runningTotal = 0;
				for (int i = 0; i < probs.length; i++) {
					runningTotal += probs[i];
					valuesForProbs[i] = runningTotal;
				}
				outputs = out;
			}


			//for(int i=0;i<probs.length;i++) {
			//System.out.print(out[i]+"~"+probs[i]+" ");
			//}
			//System.out.println();
		}

		char execute(CustomRandom random) {
			if (onlyOneOption) {
				return onlyOption;
			} else {
				double value = random.randomDouble();
				for (int i = 0; i < valuesForProbs.length; i++) {
					if (value < valuesForProbs[i]) {
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
					b.append(valuesForProbs[i]).append(" - ").append(outputs[i]).append("\t");
				}
				b.append("]");
			}

		}
	}


	@Override
	public void resetState() {
		
	}

	
	
}

