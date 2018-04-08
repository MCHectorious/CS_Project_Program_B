package models;

import characterOperations.*;
import dataStructures.DataStep;
import generalUtilities.CustomRandom;
import lossFunctions.LossStringDistance;
import matrices.Vector;
import training.DataProcessing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CharacterManipulationFromStringDistanceModel implements Model {

	@Override
	public String provideDescription() {
		StringBuilder stringBuilder = new StringBuilder();
		int i=0;
		for(ProbabilitySet set: operations) {
			stringBuilder.append(i++).append(": ");
			for(int j = 0; j<set.getKeys().size(); j++) {
				stringBuilder.append(set.getKeys().get(j)).append(" - ");
				set.getOperations().get(j).provideDescription(stringBuilder);
				stringBuilder.append("\t");
			}
			stringBuilder.append("\n");

		}
		return stringBuilder.toString();
	}

	@Override
	public void provideDescription(StringBuilder stringBuilder) {
		int i=0;
		for(ProbabilitySet set: operations) {
			stringBuilder.append(i++).append(": ");
			for(int j = 0; j<set.getKeys().size(); j++) {
				stringBuilder.append(set.getKeys().get(j)).append(" - ");
				set.getOperations().get(j).provideDescription(stringBuilder);
				stringBuilder.append("\t");
			}
			stringBuilder.append("\n");

		}
	}

	private class ProbabilitySet{

		private ArrayList<Double> keys;
		private ArrayList<CharacterOperation> operations;

		ArrayList<Double> getKeys() {
			return keys;
		}

		ArrayList<CharacterOperation> getOperations() {
			return operations;
		}



		ProbabilitySet(ArrayList<Double> k, ArrayList<CharacterOperation> o) {
			keys = k;
			operations = o;
		}



		CharacterOperation getOperation(double d) {
			for(int i = 0; i< keys.size(); i++) {
				if(d<= keys.get(i)) {
					return operations.get(i);
				}
			}
			return new CopyOperation();
		}

	}


	private ArrayList<ProbabilitySet> operations = new ArrayList<>();

	private DataProcessing dataProcessing;
	private StringBuilder stringBuilder = new StringBuilder();
	private CustomRandom random;

	public CharacterManipulationFromStringDistanceModel(List<DataStep> trainingData, DataProcessing dataProcessing, CustomRandom r) {
		this.dataProcessing = dataProcessing;
		random = r;
		ArrayList<Map<CharacterOperation, Integer>> operationsCombined = new ArrayList<>();

		for(DataStep step: trainingData) {


			ArrayList<CharacterOperation> instructions = LossStringDistance.generateCharacterOperations(step.getInputText(), step.getTargetOutputText());
			for(int i=0;i<instructions.size();i++) {
				if(operationsCombined.size()==i) {
					operationsCombined.add(new HashMap<>());
				}
				boolean found = false;
				CharacterOperation instruction = instructions.get(i);

				if (instruction instanceof CopyOperation){
					for(CharacterOperation characterOperation : operationsCombined.get(i).keySet()) {
						if(characterOperation instanceof CopyOperation) {
							found = true;
							int previousValue = operationsCombined.get(i).get(characterOperation);
							operationsCombined.get(i).remove(characterOperation);
							operationsCombined.get(i).put(characterOperation, previousValue+1);
							break;
						}
					}
					if(!found) {
						operationsCombined.get(i).put(new CopyOperation(), 1);
					}
				}else if (instruction instanceof DeleteOperation){
					for(CharacterOperation characterOperation : operationsCombined.get(i).keySet()) {
						if(characterOperation instanceof DeleteOperation) {
							found = true;
							int prevValue = operationsCombined.get(i).get(characterOperation);
							operationsCombined.get(i).remove(characterOperation);
							operationsCombined.get(i).put(characterOperation, prevValue+1);
							break;
						}
					}
					if(!found) {
						operationsCombined.get(i).put(new DeleteOperation(), 1);
					}
				}else if (instruction instanceof SubstituteOperation){
					char input = instructions.get(i).getRelevantInputs().get(0);
					char output = instructions.get(i).getRelevantOutputs().get(0);
					boolean needsToBeAdded = true;
					for(CharacterOperation op: operationsCombined.get(i).keySet()) {
						if(op instanceof SubstitutionSetOperation) {
							boolean inputExists = false;
							for(int j = 0; j<op.getRelevantInputs().size(); j++) {
								char character = op.getRelevantInputs().get(j);

								//System.out.println(character+" "+input+" "+(character==input));
								if(character==input) {
									inputExists = true;

									if(output== op.getRelevantOutputs().get(j)) {
										int prevValue = operationsCombined.get(i).get(op);
										operationsCombined.get(i).remove(op);
										operationsCombined.get(i).put(op, prevValue+1);
										needsToBeAdded = false;
									}else {
										needsToBeAdded = true;
									}
								}
							}
							if(!inputExists) {
								int prevValue = operationsCombined.get(i).get(op);
								ArrayList<Character> prevInputs = op.getRelevantInputs();
								ArrayList<Character> prevOutputs = op.getRelevantOutputs();
								prevInputs.add(input);
								prevOutputs.add(output);
								operationsCombined.get(i).remove(op);
								operationsCombined.get(i).put(new SubstitutionSetOperation(prevInputs,prevOutputs), prevValue+1);
								needsToBeAdded = false;
								break;
							}else  if (!needsToBeAdded){
								break;
							}

						}
					}
					if(needsToBeAdded) {
						ArrayList<Character> Inputs = new ArrayList<>();
						ArrayList<Character> Outputs = new ArrayList<>();
						Inputs.add(input);
						Outputs.add(output);
						operationsCombined.get(i).put(new SubstitutionSetOperation(Inputs,Outputs), 1);
					}
				}else if (instruction instanceof InsertionOperation){
					boolean hasBeenFound = false;
					for(CharacterOperation op: operationsCombined.get(i).keySet()) {
						if(op instanceof InsertionOperation) {
							if(instructions.get(i).getRelevantOutputs().get(0).charValue()==op.getRelevantOutputs().get(0).charValue()) {
								int prevValue = operationsCombined.get(i).get(op);
								operationsCombined.get(i).remove(op);
								operationsCombined.get(i).put(op, prevValue+1);
								hasBeenFound = true;
								break;
							}
						}
					}
					if(!hasBeenFound) {
						operationsCombined.get(i).put(instructions.get(i), 1);
					}
				}

			}

		}

		for(Map<CharacterOperation,Integer> characterOperationsAndTheirValues: operationsCombined) {
			int total = 0;
			for(CharacterOperation characterOperation: characterOperationsAndTheirValues.keySet()) {
				total += characterOperationsAndTheirValues.get(characterOperation);
			}

			ArrayList<Double> values = new ArrayList<>();
			ArrayList<CharacterOperation> characterOperations = new ArrayList<>();
			double previous = 0.0;

			for(CharacterOperation characterOperation: characterOperationsAndTheirValues.keySet()) {
				previous += characterOperationsAndTheirValues.get(characterOperation);
				values.add(previous/(double) total);
				characterOperations.add(characterOperation);

			}

			operations.add(new ProbabilitySet(values,characterOperations));

		}


		//StringBuilder tempBuilder = new StringBuilder();
		//this.getParams(tempBuilder);
		//System.out.print(tempBuilder.toString());
	}

	@Override
	public void run(DataStep input, Vector output) {
		String inputText = input.getInputText();
		int minIndex = Math.min(inputText.length(), operations.size());
		stringBuilder.setLength(0);

		double[] randomArray =  random.randomDoublesBetween0and1(minIndex);
		for(int i=0;i<minIndex;i++) {
			operations.get(i).getOperation(randomArray[i]).convertCharacter(inputText.charAt(i), stringBuilder);
		}
		if(inputText.length()>operations.size()) {
			stringBuilder.append(inputText.substring(minIndex));
		}
		output.setData(dataProcessing.stringToDoubleArray(stringBuilder.toString()));

	}

	@Override
	public void runAndDecideImprovements(DataStep input, Vector output, Vector targetOutput) {
		run(input,output);

	}

	@Override
	public void updateModelParameters(double momentum, double beta1, double beta2, double alpha, double OneMinusBeta1,
									  double OneMinusBeta2) {

	}

	@Override
	public void resetState() {

	}

}
