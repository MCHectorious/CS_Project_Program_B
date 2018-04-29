package Models;

import CharacterOperations.*;
import DataStructures.DataStep;
import GeneralUtilities.CustomRandom;
import LossFunctions.LossStringDistance;
import Matrices.Vector;
import Training.DataProcessing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CharacterManipulationFromStringDistanceModel implements Model {

    private class ProbabilitySet{

		private ArrayList<Double> keys;//Represents the probability of choosing each operation
		private ArrayList<CharacterOperation> operations;//Represents the possible character operations

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
			return new CopyOperation();//Defaults to just copying
		}

	}

	@Override
	public String provideDescription() {
		StringBuilder stringBuilder = new StringBuilder();
		provideDescription(stringBuilder);
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




	private ArrayList<ProbabilitySet> operations = new ArrayList<>();

	private DataProcessing dataProcessing;//Used to convert the numerical data to text and vice versa
	private StringBuilder stringBuilder = new StringBuilder();//Created here to avoid creating objects in a loop
	private CustomRandom random;

	public CharacterManipulationFromStringDistanceModel(List<DataStep> trainingData, DataProcessing dataProcessing, CustomRandom r) {
		this.dataProcessing = dataProcessing;
		random = r;
		ArrayList<Map<CharacterOperation, Integer>> operationsCombined = new ArrayList<>();

		for(DataStep step: trainingData) {


			ArrayList<CharacterOperation> instructions = LossStringDistance.generateCharacterOperations(step.getInputText(), step.getTargetOutputText());//Generates the character operations for the data step
			for(int i=0;i<instructions.size();i++) {
				if(operationsCombined.size()==i) {
					operationsCombined.add(new HashMap<>());
				}
				boolean found = false;//Checks whether the operation has already occurred
				CharacterOperation instruction = instructions.get(i);

				if (instruction instanceof CopyOperation){
					for(CharacterOperation characterOperation : operationsCombined.get(i).keySet()) {
						if(characterOperation instanceof CopyOperation) {//If there is already a copy operation
							found = true;
							int previousValue = operationsCombined.get(i).get(characterOperation);
							operationsCombined.get(i).remove(characterOperation);
							operationsCombined.get(i).put(characterOperation, previousValue+1);//Increments the count for the copy operation
							break;//Can stop looking
						}
					}
					if(!found) {
						operationsCombined.get(i).put(new CopyOperation(), 1);//adds the copy operation if it  doesn't exist
					}
				}else if (instruction instanceof DeleteOperation){
					for(CharacterOperation characterOperation : operationsCombined.get(i).keySet()) {
						if(characterOperation instanceof DeleteOperation) {//If there is already a delete operation
							found = true;
							int prevValue = operationsCombined.get(i).get(characterOperation);
							operationsCombined.get(i).remove(characterOperation);
							operationsCombined.get(i).put(characterOperation, prevValue+1);//Increments the count of the operation
							break;
						}
					}
					if(!found) {
						operationsCombined.get(i).put(new DeleteOperation(), 1);//Adds a  new delete operation
					}
				}else if (instruction instanceof SubstituteOperation){
					char input = instructions.get(i).getRelevantInputs().get(0);//A substitute operation only has one input
					char output = instructions.get(i).getRelevantOutputs().get(0);//A substitute operation only has one output
					boolean needsToBeAdded = true;
					for(CharacterOperation op: operationsCombined.get(i).keySet()) {
						if(op instanceof SubstitutionSetOperation) {
							boolean inputExists = false;//Checks whether the input of the substitution operation is included in the substitution set operation
							for(int j = 0; j<op.getRelevantInputs().size(); j++) {
								char character = op.getRelevantInputs().get(j);

								if(character==input) {
									inputExists = true;

									if(output== op.getRelevantOutputs().get(j)) {
										int prevValue = operationsCombined.get(i).get(op);
										operationsCombined.get(i).remove(op);
										operationsCombined.get(i).put(op, prevValue+1);//Increment the count for the substitution set operation
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
								operationsCombined.get(i).put(new SubstitutionSetOperation(prevInputs,prevOutputs), prevValue+1);//Adds the input-output pair to the substitution set and increment the count for the substitution set
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
						operationsCombined.get(i).put(new SubstitutionSetOperation(Inputs,Outputs), 1);// Creates a substitution set, starting with the input-output pair of the substituion
					}
				}else if (instruction instanceof InsertionOperation){
					boolean hasBeenFound = false;
					for(CharacterOperation op: operationsCombined.get(i).keySet()) {
						if(op instanceof InsertionOperation) {//If another insertion operation exists
							if(instructions.get(i).getRelevantOutputs().get(0).charValue()==op.getRelevantOutputs().get(0).charValue()) {
								int prevValue = operationsCombined.get(i).get(op);
								operationsCombined.get(i).remove(op);
								operationsCombined.get(i).put(op, prevValue+1);//Increments the count for the insertion operation
								hasBeenFound = true;
								break;
							}
						}
					}
					if(!hasBeenFound) {
						operationsCombined.get(i).put(instructions.get(i), 1);//Adds the insertion operation
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

			}//Decides the probability of choosing each character operation

			operations.add(new ProbabilitySet(values,characterOperations));//Adds the set of operations and their probabilities

		}

	}

	@Override
	public void run(DataStep input, Vector output) {
		String inputText = input.getInputText();
		int minIndex = Math.min(inputText.length(), operations.size());
		stringBuilder.setLength(0);//Resets the string builder

		double[] randomArray =  random.randomDoublesBetween0and1(minIndex);//Decides which operation will be chosen  for each index
		for(int i=0;i<minIndex;i++) {
			operations.get(i).getOperation(randomArray[i]).convertCharacter(inputText.charAt(i), stringBuilder);//Performs the character operation
		}
		if(inputText.length()>operations.size()) {
			stringBuilder.append(inputText.substring(minIndex));
		}
		output.setData(dataProcessing.stringToDoubleArray(stringBuilder.toString()));//Output the answer numerically

	}

	@Override
	public void runAndDecideImprovements(DataStep input, Vector output, Vector targetOutput) {
		run(input,output);//No improvements will be made

	}

	@Override
	public void updateModelParameters(double momentum, double beta1, double beta2, double alpha, double OneMinusBeta1,
									  double OneMinusBeta2) {
        //Parameters won't be changed
	}

	@Override
	public void resetState() {

	}

}
