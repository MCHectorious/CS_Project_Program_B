package nonlinearityFunctions;

public class RoughTanhUnit implements NonLinearity{

	public static void main(String[] args) {
		RoughTanhUnit nonLin = new RoughTanhUnit();
		
		double[] inputs = {-Double.MAX_VALUE,-10000.0,-10,0,10,10000,Double.MAX_VALUE};
		
		for(Double input: inputs) {
			System.out.println(input+" --> "+nonLin.forward(input));
		}
		
	}
	
	@Override
	public double forward(double x) {
		return x/(1.0+Math.abs(x));
	}

	@Override
	public double backward(double x) {
		double denom = Math.abs(x)+1;
		return 1.0/(denom*denom);
	}

}
