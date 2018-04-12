package dataStructures;

import matrices.Vector;

public class DataStep {
	private Vector input;//The numerical form of the input
	private Vector targetOutput;//The numerical form of the target output
	private String inputText, targetOutputText;// The textual form of the input and target out respectively
	
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
