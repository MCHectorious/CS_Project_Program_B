package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dataStructures.DataStep;
import loss.LossStringDistance;
import matrix.Vector;
import training.DataPreparation;
import util.CustomRandom;
import characterOperations.CharacterOp;
import characterOperations.CopyOp;
import characterOperations.DeleteOp;
import characterOperations.InsertionOp;
import characterOperations.SubstituteOp;
import characterOperations.SubstitutionSetOp;

public class CharacterManipulationFromStringDistance implements Model {

	private class ProbabilitySet{
		
		private ArrayList<Double> keyes = new ArrayList<>();
		public ArrayList<Double> getKeyes() {
			return keyes;
		}

		public ArrayList<CharacterOp> getOps() {
			return ops;
		}

		private ArrayList<CharacterOp> ops = new ArrayList<>();
		
		public ProbabilitySet(ArrayList<Double> k, ArrayList<CharacterOp> o) {
			keyes = k;
			ops = o;
		}
		
		
		
		public CharacterOp getOp(double d) {
			for(int i=0;i<keyes.size();i++) {
				if(d<=keyes.get(i)) {
					return ops.get(i);
				}
			}
			return new CopyOp();
		}
		
	}
	
	
	ArrayList<ProbabilitySet> operations = new ArrayList<>();
	
	DataPreparation dataPrep;
	StringBuilder builder = new StringBuilder();
	CustomRandom random;
	
	public CharacterManipulationFromStringDistance(List<DataStep> trainingData, DataPreparation dataPreparation, CustomRandom r) {
		dataPrep = dataPreparation;
		random = r;
		ArrayList<Map<CharacterOp, Integer>> operationsCombined = new ArrayList<>();
		
		for(DataStep step: trainingData) {
			
			
			ArrayList<CharacterOp> instructions = LossStringDistance.generateCharacterOperations(step.getInputText(), step.getOutputText());
			
			/*for(CharacterOp charOp:instructions) {
				StringBuilder tempBuilder = new StringBuilder();
				charOp.toString(tempBuilder);
				System.out.print(tempBuilder.toString()+" ");
			}
			System.out.println();
			*/
			for(int i=0;i<instructions.size();i++) {
				if(operationsCombined.size()==i) {
					operationsCombined.add(new HashMap<CharacterOp,Integer>());
				}
				boolean found = false;
				switch(instructions.get(i).typeID()) {
					case CopyOp.ID:
						
						for(CharacterOp op : operationsCombined.get(i).keySet()) {
							if(op.typeID()==CopyOp.ID) {
								found = true;
								int prevValue = operationsCombined.get(i).get(op);
								operationsCombined.get(i).remove(op);
								operationsCombined.get(i).put(op, prevValue+1);
								break;
							}
						}
						if(!found) {
							operationsCombined.get(i).put(new CopyOp(), 1);	
						}
						break;
					case DeleteOp.ID:
						for(CharacterOp op : operationsCombined.get(i).keySet()) {
							if(op.typeID()==DeleteOp.ID) {
								found = true;
								int prevValue = operationsCombined.get(i).get(op);
								operationsCombined.get(i).remove(op);
								operationsCombined.get(i).put(op, prevValue+1);
								break;
							}
						}
						if(!found) {
							operationsCombined.get(i).put(new DeleteOp(), 1);
						}
						break;
					case SubstituteOp.ID:
						
						//System.out.println("Yes");
						char input = instructions.get(i).getInputs().get(0).charValue();
						char output = instructions.get(i).getOutputs().get(0).charValue();
						boolean needsToBeAdded = true;
						for(CharacterOp op: operationsCombined.get(i).keySet()) {
							if(op.typeID()==SubstitutionSetOp.ID) {
								boolean inputExists = false;
								for(int j=0;j<op.getInputs().size();j++) {
									char character = op.getInputs().get(j).charValue();
									
									//System.out.println(character+" "+input+" "+(character==input));
									if(character==input) {
										inputExists = true;
										 
										if(output==op.getOutputs().get(j).charValue()) {
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
									operationsCombined.get(i).put(new SubstitutionSetOp(prevInputs,prevOutputs), prevValue+1);
									needsToBeAdded = false;
									break;
								}
								if(inputExists && !needsToBeAdded) {
									break;
								}
								
							}
						}
						if(needsToBeAdded) {
							ArrayList<Character> Inputs = new ArrayList<>();
							ArrayList<Character> Outputs = new ArrayList<>();
							Inputs.add(input);
							Outputs.add(output);
							operationsCombined.get(i).put(new SubstitutionSetOp(Inputs,Outputs), 1);
						}
						break;
						/*char input = instructions.get(i).getInputs().get(0).charValue();
						boolean haveBeenAdded = false;
						boolean needsToBeOverriden = false;
						for(CharacterOp op: operationsCombined.get(i).keySet()) {
							if(op.typeID()==SubstitutionSetOp.ID) {
								for(Character c: op.getInputs()) {
									needsToBeOverriden = false;
									if(c.equals(input)) {
										StringBuilder outputForOp = new StringBuilder();
										op.convert(input, outputForOp);
										StringBuilder outputForInstruction = new StringBuilder();
										instructions.get(i).convert(input, outputForInstruction);
										if(outputForOp.toString().equals(outputForInstruction.toString())) {
											int prevValue = operationsCombined.get(i).get(op);
											operationsCombined.get(i).remove(op);
											operationsCombined.get(i).put(op, prevValue+1);
											haveBeenAdded = true;
											break;
										}else {
											needsToBeOverriden = true;
											break;
										}
									}
								}
								
								if(!needsToBeOverriden) {
									if(haveBeenAdded) {
										break;
									}else {
										ArrayList<Character> inputs = op.getInputs();
										ArrayList<Character> outputs = op.getOutputs();
										inputs.add(instructions.get(i).getInputs().get(0).charValue());
										outputs.add(instructions.get(i).getOutputs().get(0).charValue());
										int prevValue = operationsCombined.get(i).get(op);
										operationsCombined.get(i).remove(op);
										operationsCombined.get(i).put(new SubstitutionSetOp(inputs,outputs), prevValue+1);
										break;
									}
								}
								
							}
						}
						
						if(needsToBeOverriden) {
							ArrayList<Character> inputs = new ArrayList<>();
							ArrayList<Character> outputs = new ArrayList<>();
							inputs.add(instructions.get(i).getInputs().get(0).charValue());
							outputs.add(instructions.get(i).getOutputs().get(0).charValue());
							operationsCombined.get(i).put(new SubstitutionSetOp(inputs,outputs), 1);
							
						}
						break;
						*/
					case InsertionOp.ID:
						boolean hasBeenFound = false;
						for(CharacterOp op: operationsCombined.get(i).keySet()) {
							if(op.typeID()==InsertionOp.ID) {
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
						break;
				}
				
			}
			
		}
		
		for(Map<CharacterOp,Integer> map: operationsCombined) {
			int total = 0;
			for(CharacterOp op: map.keySet()) {
				total += map.get(op);
			}
			
			//Map<Double, CharacterOp> tempMap = new HashMap<>();
			ArrayList<Double> doubles = new ArrayList<>();
			ArrayList<CharacterOp> charOps = new ArrayList<>();
			double previous = 0.0;

			for(CharacterOp op: map.keySet()) {
				previous += map.get(op);
				doubles.add((double)previous/(double) total);
				charOps.add(op);
			
			}
			
			operations.add(new ProbabilitySet(doubles,charOps));

		}
		
		
		//StringBuilder tempBuilder = new StringBuilder();
		//this.getParams(tempBuilder);
		//System.out.print(tempBuilder.toString());
	}
	
	@Override
	public void forward(Vector input, Vector output) {
		String string = dataPrep.doubleArrayToString(input.getData());
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
			
			operations.get(i).getOp(randomArray[i]).convert(string.charAt(i), builder);
			
		}
		if(string.length()>operations.size()) {
			builder.append(string.substring(minIndex));
		}
		output.setData(dataPrep.stringToDoubleArray(builder.toString()));
		
	}

	@Override
	public void forwardWithBackProp(Vector input, Vector output, Vector targetOutput) {
		forward(input,output);

	}

	@Override
	public void getParams(StringBuilder builder) {
		int i=0;
		for(ProbabilitySet set: operations) {
			builder.append(i++).append(": ");
			for(int j=0;j<set.getKeyes().size();j++) {
				builder.append(set.getKeyes().get(j)).append(" - ");
				set.getOps().get(j).toString(builder);
				builder.append("\t");
			}
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
