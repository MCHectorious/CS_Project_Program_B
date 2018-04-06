package dataSplitting;

import java.util.ArrayList;

import dataStructures.DataStep;
import matrices.Vector;
import training.DataPreparation;

public class ContainPhrase implements DataSplitOp{

	private String phrase;
	private DataPreparation dataPrep;
	private double valueOfOp = -1.0;
	
	public ContainPhrase(String Phrase, DataPreparation DataPrep) {
		phrase = Phrase;
		dataPrep = DataPrep;
	}
	
	public ContainPhrase(ArrayList<DataStep> list1,ArrayList<DataStep> list2, DataPreparation dataPreparation) {
		dataPrep = dataPreparation;
		String bestStringSoFar = "";
		for(String string: dataPrep.getPhrases()) {
			int list1LabelledList1=0,list1LabelledList2=0,list2LabelledList1=0,list2LabelledList2=0;
			for(DataStep step:list1) {
				//System.out.println(isInSet(step.getInputText(),string));
				if(isInSet(step.getInputText(),string)) {
					list1LabelledList1++;
				}else {
					list1LabelledList2++;
				}
			}
			
			for(DataStep step:list2) {
				if(isInSet(step.getInputText(),string)) {
					list2LabelledList1++;
				}else {
					list2LabelledList2++;
				}
			}
			
			double value = (double)list1LabelledList1/(double)(list1LabelledList1+list1LabelledList2) + (double)list2LabelledList2/(double)(list2LabelledList1+list2LabelledList2);	
			
			if(value>valueOfOp) {
				valueOfOp = value;
				bestStringSoFar = string;
			}
		}
		phrase = bestStringSoFar;
		
	}
	
	private boolean isInSet(String input, String phraseToUse) {
		for(int i=0;i<input.length()-phraseToUse.length();i++) {
			if( input.substring(i, i+phraseToUse.length()).equals(phraseToUse) ) {
				return true;
			}
		}
		return false;		
	}
	
	@Override
	public boolean isInSet(Vector step) {
		String input = dataPrep.doubleArrayToString(step.getData());
		for(int i=0;i<input.length()-phrase.length();i++) {
			if( input.substring(i, i+phrase.length()).equals(phrase) ) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void toString(StringBuilder builder) {
		builder.append("Contains Phrase: ").append(phrase);
	}

	@Override
	public double getValue() {
		return valueOfOp;
	}

}
