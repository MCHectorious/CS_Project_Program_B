package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dataStructures.DataStep;
import matrices.Vector;
import training.DataPreparation;
import generalUtilities.CustomRandom;

public class CategoricPortionProbabilityModelForString implements Model {

	
	private class StringStringTuple{
		private String input;
		private String output;


		public String getInput() {
			return input;
		}

		public String getOutput() {
			return output;
		}

		public StringStringTuple(String i, String o) {
			input = i;
			output = o;
		}
		
		
	}
	
	private class ProportionProbability {

		private double[] valuesForProbs;
		private String[] outputs;
		
		public ProportionProbability(double[] probs, String[] out) {
			valuesForProbs = probs;
			outputs = out;
		}
		
		public String execute(CustomRandom random) {
			double value = random.randomDouble();
			for(int i=valuesForProbs.length-1;i>=0;i--) {
				if(value<valuesForProbs[i]) {
					return outputs[i];
				}
			}
			return "";
		}
		
		public void toString(StringBuilder b) {
			b.append("[");
			for(int i=0;i<outputs.length;i++) {
				builder.append(valuesForProbs[i]+" - ").append(outputs[i]+"\t");
			}
			b.append("]");
		}
	}

	private Map<String, ProportionProbability> getProbDecider = new HashMap<>();
	
	private int windowSideSize;
	
	private DataPreparation dataPrep;
	private CustomRandom random;
	private StringBuilder builder = new StringBuilder();
	private int outputStringSize;
	
	public CategoricPortionProbabilityModelForString(List<DataStep> trainingData, int paddingSize, int outStringSize, DataPreparation dataPrep, CustomRandom random) {
		this.dataPrep = dataPrep;
		this.random = random;
		this.outputStringSize = outStringSize;
		windowSideSize = paddingSize;
		Map<StringStringTuple, Integer> count = new HashMap<>();
		System.out.println("Starting Categoric Proportion Probability Model Initialisation");
		String input,outputText, inputSubstring, outputSubstring;
		int lowerBound, upperBound;
		StringStringTuple StringAndString;
		for(DataStep step: trainingData) {
			input = step.getInputText();
			outputText = step.getOutputText();
			
			for(int i=0;i<input.length();) {
				lowerBound = (i-windowSideSize<0)? 0:i-windowSideSize;
				upperBound = (i+windowSideSize>input.length()-1)? input.length()-1:i+windowSideSize;
				inputSubstring = input.substring(lowerBound,upperBound);
				//if(i>outputText.length()-1) {
					//break;
				//}
				lowerBound = (i-outputStringSize<0)? 0:i-outputStringSize;
				lowerBound = (lowerBound>outputText.length()-1)? outputText.length()-1:lowerBound; 
				upperBound = (i+outputStringSize>outputText.length()-1)? outputText.length()-1:i+outputStringSize;
				//System.out.println(lowerBound+" "+upperBound);
				outputSubstring = outputText.substring(lowerBound,upperBound);
				i+=outputSubstring.length();
				StringAndString = new StringStringTuple(inputSubstring,outputSubstring);
				if(count.containsKey(StringAndString)) {
					int previousValue = count.get(StringAndString);
					count.remove(StringAndString);
					count.put(StringAndString, previousValue+1);
				}else {
					count.put(StringAndString, 1);
				}
			}
			
		}
		System.out.println("Finished Counting");
		
		ArrayList<String> strings = new ArrayList<>();
		for(StringStringTuple inAndOut: count.keySet()) {
			if(!strings.contains(inAndOut.getInput())) {
				strings.add(inAndOut.getInput());
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
			ArrayList<String> outputs = new ArrayList<>();
			
			//Map<Integer, Character> map = new HashMap<>();
			for(StringStringTuple stringAndString: count.keySet()) {
				if(string.equals(stringAndString.getInput())) {
					
					previous += count.get(stringAndString);
					//map.put(previous, stringAndChar.character);
					values.add(previous);
					outputs.add(stringAndString.getOutput());
					
				}
			}
			//Integer[] values =  map.keySet().toArray(new Integer[0]);
			//for(Integer i: map.keySet()) {
				//values.add(i);
			//}
			//Collections.sort(values);
			double[] probs = new double[values.size()];
			String[] outs = new String[values.size()];
			
			//int value;
			double reciprocalOfSize = 1.0/previous;
			for(int index=0;index<values.size();index++) {
				
				probs[index] = reciprocalOfSize*values.get(index);
				outs[index] = outputs.get(index);
			}
			getProbDecider.put(string, new ProportionProbability(probs,outs));
		}
		
		System.out.println("\n Finished Intialisation");
		
	}
	
	
	@Override
	public void forward(Vector input, Vector output) {
		builder.setLength(0);
		String string = dataPrep.doubleArrayToString(input.getData());
		String outString;
		for(int i=0;i<string.length();) {
			int lowerBound = (i-windowSideSize<0)? 0:i-windowSideSize;
			int upperBound = (i+windowSideSize>string.length()-1)? string.length()-1:i+windowSideSize;
			String substring = string.substring(lowerBound,upperBound);
			if(getProbDecider.containsKey(substring)) {
				outString = getProbDecider.get(substring).execute(random);
				builder.append(outString);
				i+= outString.length();
			}//else {
				//builder.append();
			//}
			
		}
		output.setData(dataPrep.stringToDoubleArray(builder.toString()));
	}

	@Override
	public void forwardWithBackProp(Vector input, Vector output, Vector targetOutput) {
		forward(input,output);

	}

	@Override
	public void getParams(StringBuilder builder) {
		for(String string: getProbDecider.keySet()) {
			builder.append(string+"\t");
			getProbDecider.get(string).toString(builder);
			builder.append("\n");
		}

	}

	@Override
	public void updateModelParams(double momentum, double beta1, double beta2, double alpha, double OneMinusBeta1,
			double OneMinusBeta2) {

	}


	@Override
	public void resetState() {
		
	}

	
	
}

