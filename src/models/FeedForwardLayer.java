package models;

import matrix.Matrix;
import matrix.Vector;
import nonlinearity.*;
import util.CustomRandom;
import util.Util;

public class FeedForwardLayer implements Layer, Model{

	public static void main(String[] args) {
		double[] weights = {1,-2,3,-4,5,-6,7,-8,9};
		System.out.println("Weights: "+Util.arrayToString(weights));
		double[] biases = {1,-2,3};
		System.out.println("Biases: "+Util.arrayToString(biases));
		double[] input = {1,2,3};
		System.out.println("Input: "+Util.arrayToString(input));
		FeedForwardLayer layer = new FeedForwardLayer(weights, biases, new RoughTanhUnit());
		Vector output = new Vector(3);
		layer.forward( new Vector(input), output);
		double[] outputArray = output.getData();
		System.out.println("Output: "+Util.arrayToString(outputArray));
	}
	
	private Vector Biases;
	private NonLinearity nonLin;
	//private double[] zDash, costForBiases;
	
	private double[] meanForBiases,varianceForBiases;
	private Matrix Weights;
	//private double[] delta;
	private double[] derivativeOfCostWithRespectToWeight, derivativeOfCostWithRespectToBias, derivativeOfCostWithRespectToOutput;
	private double[] derivativeOfNonLinWithRespectToTotal, derivativeOfCostWithRespectToTotal, derivativeOfCostWithRespectToInput;
	private double[] meanForWeights;
	private double[] varianceForWeights;
	//private double[] derivativeWithRespectToInput;
	private int inputSize;
	private int outputSize;
	
	public FeedForwardLayer(double[] weights, double[] bias, NonLinearity n) {
		Weights = new Matrix(weights, bias.length);
		Biases = new Vector(bias);
		nonLin = n;
		//zDash = new double[bias.length];
		//delta = new double[bias.length];
		
		meanForWeights = new double[weights.length];
		varianceForWeights = new double[weights.length];
		//costForBiases = new double[bias.length];
		meanForBiases = new double[bias.length];
		varianceForBiases = new double[bias.length];
		inputSize = weights.length/bias.length;
		outputSize = bias.length;
		derivativeOfCostWithRespectToOutput = new double[outputSize];
		derivativeOfCostWithRespectToWeight = new double[weights.length];
		derivativeOfCostWithRespectToBias = new double[outputSize];
		derivativeOfNonLinWithRespectToTotal = new double[outputSize];
		derivativeOfCostWithRespectToTotal= new double[outputSize];
		derivativeOfCostWithRespectToInput= new double[inputSize];

	}
	
	public FeedForwardLayer(int inputDimension, int outputDimension, NonLinearity n, CustomRandom util) {
		Weights = Matrix.rand(inputDimension, outputDimension, util);
		//Weights = Matrix.specialIntialisation(inputDimension, outputDimension);
		Biases = Vector.randomVector(outputDimension, util);
		
		//Biases = Vector.randomVector(outputDimension, util);
		nonLin = n;
		//zDash = new double[outputDimension];
		//delta = new double[outputDimension];
		derivativeOfCostWithRespectToWeight = new double[inputDimension*outputDimension];
		meanForWeights = new double[inputDimension*outputDimension];
		varianceForWeights = new double[inputDimension*outputDimension];
		//costForBiases = new double[outputDimension];
		meanForBiases = new double[outputDimension];
		varianceForBiases = new double[outputDimension];
		
		inputSize = inputDimension;
		outputSize = outputDimension;
		//System.out.println(inputDimension+","+outputDimension+","+weightRows+","+weightCols);
		
		derivativeOfCostWithRespectToOutput = new double[outputSize];
		derivativeOfCostWithRespectToWeight = new double[inputSize*outputSize];
		derivativeOfCostWithRespectToBias = new double[outputSize];	
		derivativeOfNonLinWithRespectToTotal = new double[outputSize];
		derivativeOfCostWithRespectToTotal= new double[outputSize];
		derivativeOfCostWithRespectToInput= new double[inputSize];

	}
	
	public FeedForwardLayer(Matrix weights, Vector biases, NonLinearity n) {
		Weights = weights;
		Biases = biases;
		nonLin = n;
		//zDash = new double[Biases.getSize()];
		//delta = new double[Biases.getSize()];
		//derivativeOfCostWithRespectToWeight = new double[Weights.getSize()];
		//costForBiases = new double[Biases.getSize()];
		outputSize = Biases.getSize();
		inputSize = Weights.getSize()/Biases.getSize();
		//derivativeWithRespectToInput = new double[outputSize];
		
		derivativeOfCostWithRespectToOutput = new double[outputSize];
		derivativeOfCostWithRespectToWeight = new double[Weights.getSize()];
		derivativeOfCostWithRespectToBias = new double[outputSize];
		derivativeOfNonLinWithRespectToTotal = new double[outputSize];
		derivativeOfCostWithRespectToTotal= new double[outputSize];
		derivativeOfCostWithRespectToInput= new double[inputSize];

	}
	
	@Override
	public void forward(Vector input, Vector Output) {
		//System.out.print("Doing forward pass");
		int indexA = Weights.getSize()-1;
        double total;
        //System.out.println(input.getSize()+"\t"+Output.getSize()+"\t"+weightRows+"\t"+weightCols);

        
        
        for( int i = outputSize-1; i >=0; i-- ) {
            total = Biases.get(i);
            //System.out.print(Biases.get(i)+" ");
            for( int j = inputSize-1; j >=0; j-- ) {
            	
            	//System.out.print(Weights.get(indexA)+"*"+input.get(j)+" ");
                total += Weights.get(indexA--) * input.get(j);
            }
            //System.out.println(total+"-->"+nonLin.forward(total));
            Output.set(i ,  nonLin.forward(total) );
        }
	}
	@Override
	public void forwardWithBackProp(Vector input, Vector Output, Vector targetOutput) {
		//System.out.println(derivativeWithRespectToInput.length+" "+targetOutput.getSize()+" "+weightRows+" "+weightCols);
		
		//System.out.println("\nThis backprop");
		
		/*System.out.println(input.toString());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}*/
		int indexA = Weights.getSize()-1;
        double total;
        double outputValue;
        for( int i = outputSize-1; i >=0; i-- ) {
            total = Biases.get(i);
            //System.out.print(Biases.get(i)+" ");
            for( int j = inputSize-1; j >=0; j-- ) {
            	//if(input.get(j)!=0.0) {
            		//System.out.print(input.get(j));
            	//}
            	//double test = input.get(j);
            	//System.out.println(Weights.get(indexA)+","+input.get(j));
            	
            	//if(Double.isNaN(Weights.get(indexA)*input.get(j) )) {
            		//StringBuilder builder = new StringBuilder();
            		//input.toString(builder);
            		//System.out.println(builder.toString());
    				//System.out.println(Weights.get(indexA--)+" _ "+input.get(j));
    				//System.out.println(1/0);
    			//}
            	//System.out.print(Weights.get(indexA)+"*"+input.get(j)+" ");
                total += Weights.get(indexA--) * input.get(j);
            }
            //System.out.println(total);
            
            //if(Double.isNaN(total)) {
            	//System.out.println("total is NaN");
            	//System.out.println(1/0);
            //}
            
            //System.out.println(total+"-->"+outputValue+" ?= "+targetOutput.get(i));
            //System.out.println(outputValue+","+targetOutput.get(i)+","+nonLin.backward(total));
            //System.out.println((outputValue-targetOutput.get(i))*nonLin.backward(total));
            
            
            //if(Double.isNaN((outputValue-targetOutput.get(i))*nonLin.backward(total))) {
				//System.out.println(outputValue+" "+targetOutput.get(i)+" "+nonLin.backward(total));
				//System.out.println();
				//System.out.println(1/0);
			//}
            
            
            
            
            //derivativeWithRespectToInput[i] = (outputValue-targetOutput.get(i))*nonLin.backward(total);
            //costForBiases[i] += derivativeWithRespectToInput[i];
            //int startingPos = i*weightRows;
            //System.out.println(startingPos);
            //for(int j=weightRows-1; j >=0; j--) {
            	//derivativeOfCostWithRespectToWeight[startingPos+j] += input.get(j)*derivativeWithRespectToInput[i];
            	
            	//System.out.print(costForWeight[startingPos+j]+" "+costForBiases[j]+"\t");
            	
            	//System.out.println(targetOutput.get(i)+" "+outputValue+" "+delta+" "+input.get(j)*delta+" "+total);
            	//System.out.println(outputValue+" "+targetOutput.get(i)+" "+(outputValue-targetOutput.get(i))+" "+total+" "+nonLin.backward(total)+" "+delta);
            //}
            //System.out.println();
            //System.out.println(delta[i]);
            
            //System.out.print(outputValue+" ?= "+targetOutput.get(i)+"\t");
            outputValue = nonLin.forward(total);
            derivativeOfCostWithRespectToOutput[i] = outputValue-targetOutput.get(i);
            derivativeOfNonLinWithRespectToTotal[i] = nonLin.backward(total);
            derivativeOfCostWithRespectToTotal[i] = derivativeOfCostWithRespectToOutput[i]*derivativeOfNonLinWithRespectToTotal[i];
            derivativeOfCostWithRespectToBias[i] += derivativeOfCostWithRespectToTotal[i];
            Output.set(i ,  outputValue );
        }
        
        for(int i=0;i<outputSize;i++) {
        	int startingPos = i*inputSize;
        	for(int j=0;j<inputSize;j++) {
        		derivativeOfCostWithRespectToWeight[startingPos+j] +=  input.get(j)*derivativeOfCostWithRespectToTotal[i];
        		derivativeOfCostWithRespectToInput[j] += Weights.get(startingPos+j)*derivativeOfCostWithRespectToTotal[i];
        	}
        }
        //calculateDerivativeForCost(input);
        //System.out.println();
	}
	
	@Override
	public void forwardWithBackProp(Vector input, Vector output) {
		//int indexA = Weights.getSize()-1;
        double total;
        //System.out.println(input.getSize()+"\t"+Output.getSize()+"\t"+weightRows+"\t"+weightCols);

        
        
        for( int i = outputSize-1; i >=0; i-- ) {
            total = Biases.get(i);
			int startingPos = i*inputSize;
            //System.out.print(Biases.get(i)+" ");
            for( int j = inputSize-1; j >=0; j-- ) {
            	
            	//System.out.print(Weights.get(indexA)+"*"+input.get(j)+" ");
                total += Weights.get(startingPos+j) * input.get(j);
            }
            derivativeOfNonLinWithRespectToTotal[i] = nonLin.backward(total);
            output.set(i ,  nonLin.forward(total) );
        }
        
        for(int i=0;i<outputSize;i++) {
        	int startingPos = i*inputSize;
        	for(int j=0;j<inputSize;j++) {
        		derivativeOfCostWithRespectToInput[j] += Weights.get(startingPos+j)*derivativeOfCostWithRespectToTotal[i];
        	}
        }
		
	}
	
	/*public void forwardWithBackProp(Vector input, Vector Output) {
		
		
		int indexA = Weights.getSize()-1;
		//System.out.println("a");
        double total;
        //System.out.println(input.getSize()+"\t"+Output.getSize()+"\t"+weightRows+"\t"+weightCols);
        for( int i = weightCols-1; i >=0; i-- ) {
            total = Biases.get(i);
            
            //if(Double.isNaN(total)) {
            	//System.out.println(1/0);
            //}
            
            for( int j = weightRows-1; j >=0; j-- ) {
                //if(input.get(j)!=0.0) {
                	//System.out.println(input.get(j));
                //}
            	
            	//if(Double.isNaN(Weights.get(indexA))) {
                	//System.out.println(1/0);
                //}
            	
            	//if(Double.isNaN(Weights.get(indexA)*input.get(j) )) {
            		//StringBuilder builder = new StringBuilder();
            		//input.toString(builder);
            		//System.out.println(builder.toString());
    				//System.out.println(Weights.get(indexA)+" _ "+input.get(j));
    				//System.out.println(1/0);
    			//}
            	total += Weights.get(indexA--) * input.get(j);
            }
           
            //if(Double.isNaN(total)) {
            //	System.out.println(1/0);
            //}
            
            //if(nonLin.forward(total)!=0.0) {
            	//System.out.println(nonLin.forward(total));
            //}
            
            zDash[i] = nonLin.backward(total);
            
            //System.out.println(nonLin.forward(total));
            Output.set(i ,  nonLin.forward(total) );
        }
	}*/
	
	
	
	/*public void calculateDelta(Layer nextLayer) {
		int nextLayerRows = nextLayer.getWeightRows();
		int nextLayerCols = nextLayer.getWeightCols();
		//System.out.println(nextLayerCols);
		//System.out.println(nextLayer.getWeightRows());
		//System.out.println();
		double total;
		
		//System.out.println(nextLayerRows+" "+nextLayerCols);
		for(int i =nextLayerRows-1; i>=0;i--) {
			total = 0;
			for(int j=nextLayerCols-1;j>=0;j--) {
				//System.out.println(nextLayer.getWeight(j*nextLayerCols+i)+","+nextLayer.getDelta(j));
				//if(Double.isNaN(nextLayer.getWeight(j*nextLayerRows+i)*nextLayer.getDelta(j))) {
					//System.out.println(nextLayer.getWeight(j*nextLayerRows+i)+" "+nextLayer.getDelta(j));
					//System.out.println(1/0);
				//}
				total += nextLayer.getWeight(j*nextLayerRows+i)*nextLayer.getDelta(j);
			}
			//System.out.println(total+","+zDash[i]);
			//if(Double.isNaN(total*zDash[i])) {
				//System.out.println(total+" "+zDash[i]);
				//System.out.println(1/0);
			//}
			delta[i] = total*zDash[i];
		}
		
	}
	*/
	
	/*public void calculateDerivativeForCost(Vector previousLayerOutput) {
		//System.out.println("Did  this");
		int index = 0;
		double prevOutput;
		for(int i=0;i<weightRows;i++) {
			prevOutput = previousLayerOutput.get(i);
			for(int j =0;j<weightCols;j++) {
				costForWeight[index++]+=prevOutput*delta[j];
			}
		}
		for(int i=0;i<costForBiases.length;i++) {
			costForBiases[i] = delta[i];
		}
	}*/



	@Override
	public void updateModelParams(double momentum,double beta1,double beta2, double alpha, double OneMinusBeta1, double OneMinusBeta2) {
		
		double gradient,value;
		for(int i=0;i<derivativeOfCostWithRespectToWeight.length;i++) {
			//System.out.println(learningRateDividedByTrainingSize+","+costForWeight[i]);
			//value = learningRateDividedByTrainingSize*costForWeight[i];
			//System.out.println(costForWeight[i]+","+value);
			//costForWeight[i] = -value*0.5;
			//Weights.addToData(i, -value);
			//System.out.print(costForWeight[i]+" ");
			gradient = derivativeOfCostWithRespectToWeight[i];
			//System.out.print(gradient+"\t");
			
			//System.out.println("Mean For Weight "+i+" = "+beta1+"*"+meanForWeights[i]+"+"+OneMinusBeta1+"*"+gradient);
			
			meanForWeights[i] =  beta1*meanForWeights[i]+OneMinusBeta1*gradient;
			
			//System.out.println("Mean For Weight = "+beta1+"*"+meanForWeights[i]+"+"+OneMinusBeta1+"*"+gradient);

			//System.out.println("varianceForWeights "+i+" = "+ beta2+"*"+varianceForWeights[i]+"+"+OneMinusBeta2+"*"+gradient+"*"+gradient);
			varianceForWeights[i] =  beta2*varianceForWeights[i]+OneMinusBeta2*gradient*gradient ;
			
			value = alpha*meanForWeights[i]/(Math.sqrt(varianceForWeights[i])+0.00000001);
			
			
			Weights.addToData(i, -value);
			derivativeOfCostWithRespectToWeight[i] = momentum*value;
			
		}
		//System.out.println();
		for(int i=0;i<derivativeOfCostWithRespectToBias.length;i++) {
			gradient = derivativeOfCostWithRespectToBias[i];
			
			meanForBiases[i] =  beta1*meanForBiases[i]+OneMinusBeta1*gradient;
			
			varianceForBiases[i] =  beta2*varianceForBiases[i]+OneMinusBeta2*gradient*gradient ;
			
			value = alpha*meanForBiases[i]/(Math.sqrt(varianceForBiases[i])+0.00000001);
			
			
			Biases.addToData(i, -value);
			derivativeOfCostWithRespectToBias[i] = momentum*value;
		}
	}

	@Override
	public void getParams(StringBuilder builder) {
		
		builder.append("Biases: ");
		Biases.toString(builder);
		builder.append("\n");
		builder.append("Weights: ");
		Weights.toString(builder);
		builder.append("\n");
	}

	/*@Override
	public double getWeight(int index) {
		return Weights.get(index);
	}*/

	/*@Override
	public double getDelta(int index) {
		return delta[index];
	}*/

	@Override
	public int getWeightCols() {
		return outputSize;
	}

	/*@Override
	public int getWeightRows() {
		return weightRows;
	}*/

	@Override
	public void resetState() {
		for(int i=0;i<derivativeOfCostWithRespectToWeight.length;i++) {
			derivativeOfCostWithRespectToWeight[i] = 0;
		}
		for(int i=0;i<derivativeOfCostWithRespectToBias.length;i++) {
			derivativeOfCostWithRespectToBias[i] = 0;
		}
		
	}
	
	
	
	

	@Override
	public void backProp(Vector input, Vector output, double[] derivativeOfCostWithRespectToInputFromNextLayer) {
		//System.out.println(input.getSize()+"\t"+output.getSize()+"\t"+outputSize+"\t"+inputSize+"\t"+derivativeOfCostWithRespectToInputFromNextLayer.length);
		for(int i=0;i<outputSize;i++) {
			double total =0;
        	int startingPos = i*inputSize;

			for(int j=0;j<inputSize;j++) {
				//System.out.println(startingPos+j);
				total+= Weights.get(startingPos+j)*derivativeOfCostWithRespectToInputFromNextLayer[i];
			}
			derivativeOfCostWithRespectToOutput[i] = total;
			
			derivativeOfCostWithRespectToTotal[i] = total*derivativeOfNonLinWithRespectToTotal[i];
			
			
			derivativeOfCostWithRespectToBias[i] += derivativeOfCostWithRespectToTotal[i];
		}
		
		for(int i=0;i<outputSize;i++) {
			int startingPos = i*inputSize;
			for(int j=0;j<inputSize;j++) {
				derivativeOfCostWithRespectToWeight[startingPos+j] += input.get(j)*derivativeOfCostWithRespectToTotal[i];
				
			}
		}
		//System.out.println();
		
		//System.out.println(Util.arrayToString(derivativeOfCostWithRespectToWeight));
		
	}


	/*@Override
	public double[] getDerivativeWithRespectToInput() {
		return derivativeWithRespectToInput;
	}*/

	@Override
	public double[] getDerivativeWithRespectToInput() {
		return derivativeOfCostWithRespectToInput;
	}



	
}
