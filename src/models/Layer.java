package models;

import matrix.Vector;

public interface Layer extends Model{

	//public double getWeight(int index);
	
	//public double getDelta(int index);
	
	
	public int getWeightCols();
	
	//public int getWeightRows();

	public void backProp(Vector input, Vector output, double[] derivativeOfCostWithRespectToInputFromNextLayer);
	
	//public void backProp(Vector input, Vector output);
	
	public double[] getDerivativeWithRespectToInput();

	public void forwardWithBackProp(Vector input, Vector output);
	
	//public void calculateDelta(Layer layer);

	//public void calculateDerivativeForCost(Vector input);

	

	
}
