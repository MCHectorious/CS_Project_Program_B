package matrices;

import generalUtilities.HasDescription;
import generalUtilities.Utilities;

public class GeneralMatrix implements HasDescription {
	protected double[] data;

    public String description() {
        return Utilities.arrayToString(data);
    }

    public void description(StringBuilder builder) {
        builder.append(Utilities.arrayToString(data));
    }
	

    
	public void addToData(int index, double val) {
		data[index] += val;
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
    
    public double set( int index , double val ) {
        return data[index] = val;
    }
    
    
    public void println() {
    	System.out.println(toString());
    }
    
    
    
}
