package matrices;

import generalUtilities.CustomRandom;

public class Matrix extends GeneralMatrix{

    private int numRows;
    private int numCols;
    
    public int getNumRows() {
		return numRows;
	}

	public int getNumCols() {
		return numCols;
	}

	public Matrix(double[] array, int cols) {
		data = array;
		numCols = cols;
		numRows = array.length/cols;
	}
	
	public Matrix(int rows, int cols) {
    	numRows = rows;
    	numCols = cols;
    }
    
    public int getSize() {
    	return numRows*numCols;
    }
    
    public static Matrix rand(int rows, int cols, CustomRandom util) {
    	Matrix output = new Matrix(rows,cols);
    	output.setData(util.randomDoubleArray(rows*cols));
    	return output;
    }
    
    public static Matrix specialIntialisation(int rows, int cols) {
    	double[] values = new double[rows*cols];
    	double difference;
    	for(int i=0;i<values.length;i++) {
    		difference = (double) (i%rows) - (i%cols); 
    		values[i] = Math.exp(-4*difference*difference);
    	}
    	return new Matrix(values, cols);
    }
   /* public String toString() {
    	StringBuilder builder = new StringBuilder();
    	builder.append("[");
    	for(int i = 0; i<data.length;i++) {
    		builder.append(data[i]+"\t");
    		if(i % numCols == 0) {
    			builder.append("\n");
    		}
    	}
    	builder.append("]");    	
    	return builder.toString();
    }*/
    
}
