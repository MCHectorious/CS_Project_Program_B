package models;

import matrices.Vector;

public interface Layer extends Model{

	int getWeightCols();

	void backProp(Vector input, Vector output, double[] derivativeOfCostWithRespectToInputFromNextLayer);

	double[] getDerivativeWithRespectToInput();

	void runWithBackProp(Vector input, Vector output);

	void runWithBackProp(Vector input, Vector output, Vector targetOutput);


	void run(Vector input, Vector output);

}
