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
		double[] intervalSet = {0.01,0.1,0.25,0.5};
        for (int tempIndex = 0; tempIndex < DataProcessing.FIXED_VECTOR_SIZE; tempIndex++) {

            for (double tempInterval : intervalSet) {
				for(double tempValue = -1.0;tempValue<=1.0;tempValue=tempValue+0.1) {
					int list1LabelledList1=0,list1LabelledList2=0,list2LabelledList1=0,list2LabelledList2=0;
					for(DataStep step:list1) {
						if(isInSet(step.getInputVector(),tempIndex,tempValue,tempInterval)) {
							list1LabelledList1++;
						}else {
							list1LabelledList2++;
						}
					}

					for(DataStep step:list2) {
						if(isInSet(step.getInputVector(),tempIndex,tempValue,tempInterval)) {
							list2LabelledList1++;
						}else {
							list2LabelledList2++;
						}
					}

                    double value = (double) list1LabelledList1 / (double) (list1LabelledList1 + list1LabelledList2) + (double) list2LabelledList2 / (double) (list2LabelledList1 + list2LabelledList2);

					if(value>valueOfOp) {
						valueOfOp = value;
						index = tempIndex;
						pivot = tempValue;
						interval = tempInterval;
					}
				}
			}
		}
	}
		
	private boolean isInSet(Vector step, int tempIndex, double tempPivot, double tempInterval) {
		double testingValue = step.get(tempIndex);
		return (testingValue-tempInterval)>tempPivot && (testingValue+tempInterval)<tempPivot;
	}	
	
	@Override
    public boolean isInSet(DataStep step) {
        double testingValue = step.getInputVector().get(index);
		return (testingValue-interval)>pivot && (testingValue+interval)<pivot;
	}

	@Override
	public double getValue() {
		return valueOfOp;
	}

    @Override
    public String description() {
        return "At index " + index + ", within +/- " + interval + " of " + pivot;
    }

    @Override
    public void description(StringBuilder stringBuilder) {
        stringBuilder.append("At index ").append(index).append(", within +/- ").append(interval).append(" of ").append(pivot);
    }
}
