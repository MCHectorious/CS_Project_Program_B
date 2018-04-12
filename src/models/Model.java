package models;

import dataStructures.DataStep;
import generalUtilities.HasDescription;
import matrices.Vector;

public interface Model extends HasDescription {
	void run(DataStep input, Vector output);// executes the model

	void runAndDecideImprovements(DataStep input, Vector output, Vector targetOutput);//executes the model and decides on what is the best way to change the model parameters to reduce loss

	void updateModelParameters(double momentum, double beta1, double beta2, double alpha, double OneMinusBeta1, double OneMinusBeta2);// updates the model parameters based on the imporvements suggested
	
	void resetState();// remove the effects of  momentum

}
