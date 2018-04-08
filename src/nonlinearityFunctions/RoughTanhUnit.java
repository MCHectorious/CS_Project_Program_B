package nonlinearityFunctions;

public class RoughTanhUnit implements NonLinearity{

	public static void main(String[] args) {
		RoughTanhUnit roughTanhUnit = new RoughTanhUnit();
		
		double[] inputs = {-Double.MAX_VALUE,-10000.0,-10,0,10,10000,Double.MAX_VALUE};
		
		for(Double input: inputs) {
			System.out.println(input+" --> "+roughTanhUnit.evaluate(input));
		}
		
	}
	
	@Override
	public double evaluate(double x) {
		return x/(1.0+Math.abs(x));
	}

	@Override
	public double evaluateDerivative(double x) {
		double squareRootOfDenominator = Math.abs(x)+1;
		return 1.0/(squareRootOfDenominator*squareRootOfDenominator);
	}

}
