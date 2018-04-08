package dataSplitting;

import dataStructures.DataStep;
import training.DataProcessing;

import java.util.ArrayList;

public class ContainPhrase extends DataSplitOperation {

	private String phrase;


    ContainPhrase(ArrayList<DataStep> list1, ArrayList<DataStep> list2, DataProcessing dataProcessing) {
		String bestStringSoFar = "";
        for (String phrase : dataProcessing.getPhrases()) {
			int list1LabelledList1=0,list1LabelledList2=0,list2LabelledList1=0,list2LabelledList2=0;
			for(DataStep step:list1) {
				//System.out.println(isInSet(step.getInputText(),phrase));
				if(isInSet(step.getInputText(),phrase)) {
					list1LabelledList1++;
				}else {
					list1LabelledList2++;
				}
			}
			
			for(DataStep step:list2) {
				if(isInSet(step.getInputText(),phrase)) {
					list2LabelledList1++;
				}else {
					list2LabelledList2++;
				}
			}
			
			double value = (double)list1LabelledList1/(double)(list1LabelledList1+list1LabelledList2) + (double)list2LabelledList2/(double)(list2LabelledList1+list2LabelledList2);	
			
			if(value> valueOfOperation) {
				valueOfOperation = value;
				bestStringSoFar = phrase;
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
    public boolean isInSet(DataStep step) {
        String input = step.getInputText();
		for(int i=0;i<input.length()-phrase.length();i++) {
			if( input.substring(i, i+phrase.length()).equals(phrase) ) {
				return true;
			}
		}
		
		return false;
	}

	@Override
    public String provideDescription() {
        return "Contains Phrase: " + phrase;
	}

	@Override
    public void provideDescription(StringBuilder stringBuilder) {
        stringBuilder.append("Contains Phrase: ").append(phrase);
	}
}
