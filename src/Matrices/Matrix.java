package Matrices;

import GeneralUtilities.CustomRandom;

public class Matrix extends GeneralMatrix{

    private int numberOfRows;
    private int numberOfColumns;

	public Matrix(double[] array, int numberOfColumns) {
		data = array;
		this.numberOfColumns = numberOfColumns;
		numberOfRows = array.length/numberOfColumns;
	}

	private Matrix(int rows, int cols) {
    	numberOfRows = rows;
    	numberOfColumns = cols;
    }
    
    public int getSize() {
    	return numberOfRows * numberOfColumns;
    }
    
    public static Matrix random(int rows, int numberOfColumns, CustomRandom random) {//Generates a random matrix of specified dimensions
    	Matrix output = new Matrix(rows,numberOfColumns);
    	output.setData(random.randomDoubleArray(rows*numberOfColumns));
    	return output;
    }

}
