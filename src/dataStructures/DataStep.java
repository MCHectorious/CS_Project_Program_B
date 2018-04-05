package dataStructures;

import matrix.Vector;

public class DataStep {
	private Vector input;
	private Vector targetOutput;
	private String inputText, outputText;
	
	public DataStep(double[] input, double[] targetOutput) {
		this.input = new Vector(input);
		//System.out.println(new Vector(input).toString());
		this.targetOutput = new Vector(targetOutput);
	}
	
	public DataStep(double[] input, double[] targetOutput, String inputText, String outputText) {
		this.input = new Vector(input);
		//System.out.println(new Vector(input).toString());
		this.targetOutput = new Vector(targetOutput);
		this.inputText = inputText;
		this.outputText = outputText;
	}
	
	public String getOutputText() {
		return outputText;
	}
	
	public String getInputText() {
		return inputText;
	}
	
	public double[] getInput() {
		return input.getData();
	}
	
	public Vector getInputVector() {
		return input;
	}
	
	public Vector getTargetOutputVector() {
		return targetOutput;
	}
	
	public double[] getTargetOutput() {
		return targetOutput.getData();
	}
}
