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
	public String description() {
		StringBuilder stringBuilder = new StringBuilder();
		int i=0;
		for(ProbabilitySet set: operations) {
			stringBuilder.append(i++).append(": ");
			for(int j=0;j<set.getKeyes().size();j++) {
				stringBuilder.append(set.getKeyes().get(j)).append(" - ");
				set.getOps().get(j).description(stringBuilder);
				stringBuilder.append("\t");
			}
			stringBuilder.append("\n");

		}
		return stringBuilder.toString();
	}

	@Override
	public void description(StringBuilder stringBuilder) {
		int i=0;
		for(ProbabilitySet set: operations) {
			stringBuilder.append(i++).append(": ");
			for(int j=0;j<set.getKeyes().size();j++) {
				stringBuilder.append(set.getKeyes().get(j)).append(" - ");
				set.getOps().get(j).description(stringBuilder);
				stringBuilder.append("\t");
			}
			stringBuilder.append("\n");

		}
	}

	private class ProbabilitySet{

		private ArrayList<Double> keyes;
		ArrayList<Double> getKeyes() {
			return keyes;
		}

		ArrayList<CharacterOperation> getOps() {
			return ops;
		}

		private ArrayList<CharacterOperation> ops;

		ProbabilitySet(ArrayList<Double> k, ArrayList<CharacterOperation> o) {
			keyes = k;
			ops = o;
		}



		CharacterOperation getOp(double d) {
			for(int i=0;i<keyes.size();i++) {
				if(d<=keyes.get(i)) {
					return ops.get(i);
				}
			}
			return new CopyOperation();
		}

	}


	private ArrayList<ProbabilitySet> operations = new ArrayList<>();

	private DataProcessing dataPrep;
	private StringBuilder builder = new StringBuilder();
	private CustomRandom random;

	public CharacterManipulationFromStringDistanceModel(List<DataStep> trainingData, DataProcessing dataProcessing, CustomRandom r) {
		dataPrep = dataProcessing;
		random = r;
		ArrayList<Map<CharacterOperation, Integer>> operationsCombined = new ArrayList<>();

		for(DataStep step: trainingData) {


			ArrayList<CharacterOperation> instructions = LossStringDistance.generateCharacterOperations(step.getInputText(), step.getOutputText());

			/*for(CharacterOp charOp:instructions) {
				StringBuilder tempBuilder = new StringBuilder();
				charOp.toString(tempBuilder);
				System.out.print(tempBuilder.toString()+" ");
			}
			System.out.println();
			*/
			for(int i=0;i<instructions.size();i++) {
				if(operationsCombined.size()==i) {
					operationsCombined.add(new HashMap<>());
				}
				boolean found = false;
				CharacterOperation instruction = instructions.get(i);

				if (instruction instanceof CopyOperation){
					for(CharacterOperation op : operationsCombined.get(i).keySet()) {
						if(op instanceof CopyOperation) {
							found = true;
							int prevValue = operationsCombined.get(i).get(op);
							operationsCombined.get(i).remove(op);
							operationsCombined.get(i).put(op, prevValue+1);
							break;
						}
					}
					if(!found) {
						operationsCombined.get(i).put(new CopyOperation(), 1);
					}
				}else if (instruction instanceof DeleteOperation){
					for(CharacterOperation op : operationsCombined.get(i).keySet()) {
						if(op instanceof DeleteOperation) {
							found = true;
							int prevValue = operationsCombined.get(i).get(op);
							operationsCombined.get(i).remove(op);
							operationsCombined.get(i).put(op, prevValue+1);
							break;
						}
					}
					if(!found) {
						operationsCombined.get(i).put(new DeleteOperation(), 1);
					}
				}else if (instruction instanceof SubstituteOperation){
					char input = instructions.get(i).getInputs().get(0);
					char output = instructions.get(i).getOutputs().get(0);
					boolean needsToBeAdded = true;
					for(CharacterOperation op: operationsCombined.get(i).keySet()) {
						if(op instanceof SubstitutionSetOperation) {
							boolean inputExists = false;
							for(int j=0;j<op.getInputs().size();j++) {
								char character = op.getInputs().get(j);

								//System.out.println(character+" "+input+" "+(character==input));
								if(character==input) {
									inputExists = true;

									if(output== op.getOutputs().get(j)) {
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
								ArrayList<Character> prevInputs = op.getInputs();
								ArrayList<Character> prevOutputs = op.getOutputs();
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
							if(instructions.get(i).getOutputs().get(0).charValue()==op.getOutputs().get(0).charValue()) {
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

		for(Map<CharacterOperation,Integer> map: operationsCombined) {
			int total = 0;
			for(CharacterOperation op: map.keySet()) {
				total += map.get(op);
			}

			//Map<Double, CharacterOp> tempMap = new HashMap<>();
			ArrayList<Double> doubles = new ArrayList<>();
			ArrayList<CharacterOperation> charOps = new ArrayList<>();
			double previous = 0.0;

			for(CharacterOperation op: map.keySet()) {
				previous += map.get(op);
				doubles.add(previous/(double) total);
				charOps.add(op);

			}

			operations.add(new ProbabilitySet(doubles,charOps));

		}


		//StringBuilder tempBuilder = new StringBuilder();
		//this.getParams(tempBuilder);
		//System.out.print(tempBuilder.toString());
	}

	@Override
	public void run(DataStep input, Vector output) {
		String string = input.getInputText();
		int minIndex = Math.min(string.length(), operations.size());
		builder.setLength(0);

		double[] randomArray =  random.randomDoublesBetween0and1(minIndex);
		for(int i=0;i<minIndex;i++) {
			/*for(Double d :operations.get(i).keySet()) {
				if(randomArray[i]<=d) {
					operations.get(i).get(d).convert(string.charAt(i), builder);
					break;
				}
			}*/

			operations.get(i).getOp(randomArray[i]).convertCharacter(string.charAt(i), builder);

		}
		if(string.length()>operations.size()) {
			builder.append(string.substring(minIndex));
		}
		output.setData(dataPrep.stringToDoubleArray(builder.toString()));

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
