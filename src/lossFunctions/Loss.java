package lossFunctions;

import matrices.Vector;

public interface Loss {
	double measureLoss(Vector actualOutput, Vector targetOutput);
}
