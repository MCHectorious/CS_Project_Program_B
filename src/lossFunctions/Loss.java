package lossFunctions;

import matrix.Vector;

public interface Loss {
	//void backward(Vector actualOutput, Vector targetOutput);
	double measure(Vector actualOutput, Vector targetOutput);
}
