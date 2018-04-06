package models;

import matrices.Vector;

public interface Model {
	void forward(Vector input, Vector output);
	void forwardWithBackProp(Vector input, Vector output, Vector targetOutput);
	//void forwardWithBackProp(Vector input, Vector output);
	
	//void forwardWithBackProp(Vector input, Vector output, BackPropagation backProp);
	//ArrayList<GeneralMatrix> getParameters();
	//void backPropagate();
	//void updateModelParams(double learningRateDividedByTrainingSize);
	void getParams(StringBuilder builder);
	//void updateModelParams(double alpha, double beta1, double beta2, double epsilon);
	//void updateModelParams(double alpha, double beta1, double beta2, double epsilon, double learningRate);
	//void updateModelParams(double momentum, double beta1, double beta2, double alpha);
	void updateModelParams(double momentum, double beta1, double beta2, double alpha, double OneMinusBeta1,	double OneMinusBeta2);
	
	void resetState();

}
