package matrices;

public class GeneralMatrix {
	protected double[] data;
	
    public void toString(StringBuilder builder) {
    	for(double d: data) {
    		builder.append(d+"\t");
    	}
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
