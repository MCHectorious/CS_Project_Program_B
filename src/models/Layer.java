package models;

import matrices.Vector;

public interface Layer extends Model{

	int getWeightColumns();

	void backPropagate(Vector input, Vector output, double[] derivativeOfCostWithRespectToInputFromNextLayer);

	double[] getDerivativeWithRespectToInput();

	void runWithBackPropagation(Vector input, Vector output);

	void runWithBackPropagation(Vector input, Vector output, Vector targetOutput);


	void run(Vector input, Vector output);

}
