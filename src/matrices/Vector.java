package matrices;

import java.util.ArrayList;

import util.CustomRandom;

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


	public static Vector randomVector(int length, CustomRandom rand) {
		return new Vector(rand.randomDoubleArray(length));
	}
	
	public static void concatenateVector(ArrayList<Vector> vectors, Vector output) {
		int index=0;
		for(Vector vector:vectors) {
			for(int i=0;i<vector.getSize();i++) {
				output.set(index++, vector.get(i));
			}
		}
	}
	
	
}