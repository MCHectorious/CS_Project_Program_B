package DataSplitting;

import DataStructures.DataStep;
import Matrices.Vector;
import Training.DataProcessing;

import java.util.ArrayList;

public class WithinCertainInterval extends DataSplitOperation {

	private int index;//The position in the vector which is tested
	private double pivot;//The middle of the range which makes the value in the data split
	private double interval;// the range in the data split = pivot-interval to pivot+interval

	public WithinCertainInterval(int index, double pivot, double interval){
		this.index = index;
		this.pivot = pivot;
		this.interval = interval;
	}//Instantiates this object with pre-determined values

    WithinCertainInterval(ArrayList<DataStep> list1, ArrayList<DataStep> list2) {
		double[] possibleIntervals = {0.01,0.1,0.25,0.5};//Possible values for the interval (not too precise to avoid over-fitting)
        for (int temporaryIndex = 0; temporaryIndex < DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR; temporaryIndex++) {//goes through each possible index

            for (double temporaryInterval : possibleIntervals) {
				for(double temporaryPivot = -1.0;temporaryPivot<=1.0;temporaryPivot=temporaryPivot+0.1) {//The pivot is not too precise to avoid over-fitting
					int inList1AndLabelledList1=0,inList1AndLabelledList2=0,inList2AndLabelledList1=0,inList2AndLabelledList2=0;

					for(DataStep step:list1) {
						if(isInSet(step.getInputVector(),temporaryIndex,temporaryPivot,temporaryInterval)) {
							inList1AndLabelledList1++;
						}else {
							inList1AndLabelledList2++;
						}
					}

					for(DataStep step:list2) {
						if(isInSet(step.getInputVector(),temporaryIndex,temporaryPivot,temporaryInterval)) {
							inList2AndLabelledList1++;
						}else {
							inList2AndLabelledList2++;
						}
					}

                    double temporaryValueOfOperation = (double) inList1AndLabelledList1 / (double) (inList1AndLabelledList1 + inList1AndLabelledList2) + (double) inList2AndLabelledList2 / (double) (inList2AndLabelledList1 + inList2AndLabelledList2);//Measure how accuracy the phrase splits the data to match how it should be. It ranges from 0 to 2

					if(temporaryValueOfOperation> valueOfOperation) {
						valueOfOperation = temporaryValueOfOperation;
						index = temporaryIndex;
						pivot = temporaryPivot;
						interval = temporaryInterval;
					}//Makes sure the best values are saved
				}
			}
		}
	}
		
	private boolean isInSet(Vector step, int temporaryIndex, double temporaryPivot, double temporaryInterval) {//Used in constructor to find the best values
		double testingValue = step.get(temporaryIndex);
		return testingValue>(temporaryPivot-temporaryInterval) && testingValue< (temporaryPivot+temporaryInterval);
	}	
	
	@Override
    public boolean isInSet(DataStep step) {
        double testingValue = step.getInputVector().get(index);
		return testingValue>(pivot-interval) && testingValue< (pivot+interval);//Checks whether testing value is within the interval of the pivot
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
