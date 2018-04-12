package dataSplitting;

import dataStructures.DataStep;
import generalUtilities.HasDescription;

public abstract class DataSplitOperation implements HasDescription {

    double valueOfOperation = -1;//Initialises the value to an impossible value


    public abstract boolean isInSet(DataStep step);

    double getValue() {
        if (valueOfOperation == -1) {
            throw new ExceptionInInitializerError("Value of operation is unknown");
        } else {
            return valueOfOperation;
        }
    }

}
