package nonlinearityFunctions;

public interface NonLinearity {
	double forward(double x);
	double backward(double x);
}
