package dataSplitting;

import java.util.ArrayList;

import dataStructures.DataStep;
import matrix.Vector;
import training.DataPreparation;

public class WithinCertainInterval implements DataSplitOp {

	private int index;
	private double pivot;
	private double interval;
	private double valueOfOp=-1;
	
	public WithinCertainInterval(ArrayList<DataStep> list1,ArrayList<DataStep> list2) {
		double[] intervalSet = {0.01,0.1,0.25,0.5};
		for(int tempIndex =0;tempIndex < DataPreparation.FIXED_VECTOR_SIZE;tempIndex++) {
			for(int intervalSetIndex = 0; intervalSetIndex<intervalSet.length;intervalSetIndex++) {
				double tempInterval = intervalSet[intervalSetIndex];
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
					
					double value = (double)list1LabelledList1/(double)(list1LabelledList1+list1LabelledList2) + (double)list2LabelledList2/(double)(list2LabelledList1+list2LabelledList2);	
					
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
	public boolean isInSet(Vector step) {
		double testingValue = step.get(index);
		return (testingValue-interval)>pivot && (testingValue+interval)<pivot;
	}

	@Override
	public void toString(StringBuilder builder) {
		builder.append("At index "+index+", within +/- "+interval+" of "+pivot);
	}

	@Override
	public double getValue() {
		return valueOfOp;
	}

}
