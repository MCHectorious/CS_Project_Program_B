package dataSplitting;

import dataStructures.DataStep;
import generalUtilities.HasDescription;

public abstract class DataSplitOperation implements HasDescription {

    double valueOfOp = -1;


    public abstract boolean isInSet(DataStep step);

    double getValue() {
        if (valueOfOp == -1) {
            throw new ExceptionInInitializerError("Value of operation is unknown");
        } else {
            return valueOfOp;
        }
    }

}
