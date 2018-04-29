package DataSplitting;

import DataStructures.DataStep;
import GeneralUtilities.HasDescription;

public abstract class DataSplitOperation implements HasDescription {

    double valueOfOperation = -1;//Initialises the value to an impossible value

    public abstract boolean isInSet(DataStep step);

    double getValue() {
        if (valueOfOperation == -1) {//Checks that the value has been properly initialised
            throw new ExceptionInInitializerError("Value of operation is unknown");//So that this can be handled appropriately
        } else {
            return valueOfOperation;
        }
    }

}
