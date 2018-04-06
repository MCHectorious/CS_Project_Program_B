package dataSplitting;

import matrices.Vector;

public interface DataSplitOp {

	boolean isInSet(Vector step);
	
	void toString(StringBuilder builder);

	double getValue();
	
}
