package dataSplitting;

import dataStructures.DataStep;
import matrices.Vector;
import training.DataProcessing;

import java.util.ArrayList;

public class WithinCertainInterval extends DataSplitOperation {

	private int index;
	private double pivot;
	private double interval;

    WithinCertainInterval(ArrayList<DataStep> list1, ArrayList<DataStep> list2) {
		double[] possibleIntervals = {0.01,0.1,0.25,0.5};
        for (int temporaryIndex = 0; temporaryIndex < DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR; temporaryIndex++) {

            for (double temporaryInterval : possibleIntervals) {
				for(double temporaryPivot = -1.0;temporaryPivot<=1.0;temporaryPivot=temporaryPivot+0.1) {
					int list1LabelledList1=0,list1LabelledList2=0,list2LabelledList1=0,list2LabelledList2=0;
					for(DataStep step:list1) {
						if(isInSet(step.getInputVector(),temporaryIndex,temporaryPivot,temporaryInterval)) {
							list1LabelledList1++;
						}else {
							list1LabelledList2++;
						}
					}

					for(DataStep step:list2) {
						if(isInSet(step.getInputVector(),temporaryIndex,temporaryPivot,temporaryInterval)) {
							list2LabelledList1++;
						}else {
							list2LabelledList2++;
						}
					}

                    double temporaryValueOfOperation = (double) list1LabelledList1 / (double) (list1LabelledList1 + list1LabelledList2) + (double) list2LabelledList2 / (double) (list2LabelledList1 + list2LabelledList2);

					if(temporaryValueOfOperation> valueOfOperation) {
						valueOfOperation = temporaryValueOfOperation;
						index = temporaryIndex;
						pivot = temporaryPivot;
						interval = temporaryInterval;
					}
				}
			}
		}
	}
		
	private boolean isInSet(Vector step, int temporaryIndex, double temporaryPivot, double temporaryInterval) {
		double testingValue = step.get(temporaryIndex);
		return (testingValue-temporaryInterval)>temporaryPivot && (testingValue+temporaryInterval)<temporaryPivot;
	}	
	
	@Override
    public boolean isInSet(DataStep step) {
        double testingValue = step.getInputVector().get(index);
		return (testingValue-interval)>pivot && (testingValue+interval)<pivot;
	}

	@Override
	public double getValue() {
		return valueOfOperation;
	}

    @Override
    public String provideDescription() {
        return "At index " + index + ", within +/- " + interval + " of " + pivot;
    }

    @Override
    public void provideDescription(StringBuilder stringBuilder) {
        stringBuilder.append("At index ").append(index).append(", within +/- ").append(interval).append(" of ").append(pivot);
    }
}
