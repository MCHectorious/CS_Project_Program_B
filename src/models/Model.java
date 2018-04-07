package models;

import dataStructures.DataStep;
import generalUtilities.HasDescription;
import matrices.Vector;

public interface Model extends HasDescription {
	void run(DataStep input, Vector output);

	void runAndDecideImprovements(DataStep input, Vector output, Vector targetOutput);

	void updateModelParameters(double momentum, double beta1, double beta2, double alpha, double OneMinusBeta1, double OneMinusBeta2);
	
	void resetState();

}
