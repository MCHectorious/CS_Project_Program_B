package matrices;

import java.util.ArrayList;

import generalUtilities.CustomRandom;

public class Vector extends GeneralMatrix{

	public Vector(double[] array) {
		data = array;
	}
	
	public Vector(int length) {
		data = new double[length];
	}
	
	public int getSize() {
		return data.length;
	}


	public static Vector randomVector(int length, CustomRandom rand) {//generates a random vector of a specified length
		return new Vector(rand.randomDoubleArray(length));
	}
	
	public static void concatenateVector(ArrayList<Vector> vectors, Vector output) {//sets the vales of the out vector to the list of vectors
		int index=0;
		for(Vector vector:vectors) {
			for(int i=0;i<vector.getSize();i++) {
				output.set(index++, vector.get(i));
			}
		}
	}
	
	
}