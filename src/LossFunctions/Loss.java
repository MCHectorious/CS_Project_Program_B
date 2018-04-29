package LossFunctions;

import Matrices.Vector;

public interface Loss {
	double measureLoss(Vector actualOutput, Vector targetOutput);// measures hiw closes the actual output is to the target output
}
