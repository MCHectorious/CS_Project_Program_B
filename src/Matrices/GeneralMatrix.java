package Matrices;

import GeneralUtilities.HasDescription;
import GeneralUtilities.Utilities;

public class GeneralMatrix implements HasDescription {
	protected double[] data;//the infomration stored in the matrix

    public String provideDescription() {
        return Utilities.arrayToString(data);//The description is just the data
    }

    public void provideDescription(StringBuilder builder) {
        builder.append(Utilities.arrayToString(data));
    }
	

    
	public void addToData(int index, double value) {
		data[index] += value;
	}
	

	
    public double[] getData() {
        return data;
    }
    
    public int getSize() {
    	return data.length;
    }
    
  
	public void setData( double[] data ) {
		this.data = data;
	}
	
    public double get( int index ) {
        return data[index];
    }
    
    public void set( int index , double value ) {
        data[index] = value;
    }

}
