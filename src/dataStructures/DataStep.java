package dataStructures;

import matrices.Vector;

public class DataStep {
	private Vector input;
	private Vector targetOutput;
	private String inputText, targetOutputText;
	
	public DataStep(double[] input, double[] targetOutput) {
		this.input = new Vector(input);
		this.targetOutput = new Vector(targetOutput);
	}

    public DataStep(double[] input) {
        this.input = new Vector(input);
    }

    public DataStep(Vector input) {
        this.input = input;
    }

	public DataStep(double[] input, double[] targetOutput, String inputText, String targetOutputText) {
		this.input = new Vector(input);
		this.targetOutput = new Vector(targetOutput);
		this.inputText = inputText;
		this.targetOutputText = targetOutputText;
	}
	
	public String getTargetOutputText() {
		return targetOutputText;
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
