package models;

import matrices.Vector;

public interface Layer extends Model{

	int getWeightColumns();//used to get the out of the  layer

	void backPropagate(Vector input, Vector output, double[] derivativeOfCostWithRespectToInputFromNextLayer);//using the chain rule from calculus and the derivative from the layer after it, it decides what improvements should be made

	double[] getDerivativeWithRespectToInput();//gets the derivative of the cost with respect to the input sot aht it may be used by the prevous layer for backpropagation

	void runWithBackPropagation(Vector input, Vector output);//executes the model while doing some of the work of backpropagation

	void runWithBackPropagation(Vector input, Vector output, Vector targetOutput);// executes the model and decides on the backprogration for the last layer


	void run(Vector input, Vector output);//overloads run so that it canbe done with just vecotrs for faster computation

}
