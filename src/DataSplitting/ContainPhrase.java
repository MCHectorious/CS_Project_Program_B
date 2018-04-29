package DataSplitting;

import DataStructures.DataStep;
import Training.DataProcessing;

import java.util.ArrayList;

public class ContainPhrase extends DataSplitOperation {

	private String phrase = "";//The phrase which determines whether it is in the set or not

    ContainPhrase(ArrayList<DataStep> list1, ArrayList<DataStep> list2, DataProcessing dataProcessing) {
        for (String temporaryPhrase : dataProcessing.getPhrases()) {

        	int inList1AndLabelledList1=0,inList1AndLabelledList2=0,inList2AndLabelledList1=0,inList2AndLabelledList2=0;

			for(DataStep step: list1) {
				if(isInSet(step.getInputText(),temporaryPhrase)) {
					inList1AndLabelledList1++;
				}else {
					inList1AndLabelledList2++;
				}
			}
			
			for(DataStep step: list2) {
				if(isInSet(step.getInputText(),temporaryPhrase)) {
					inList2AndLabelledList1++;
				}else {
					inList2AndLabelledList2++;
				}
			}
			
			double value = (double) inList1AndLabelledList1/ (double)(inList1AndLabelledList1+inList1AndLabelledList2) + (double) inList2AndLabelledList2/ (double) (inList2AndLabelledList1+inList2AndLabelledList2);//Measure how accuracy the phrase splits the data to match how it should be. It ranges from 0 to 2
			//No error handling is needed for this casting as casting from an integer to a double is safe

			if(value> valueOfOperation) {
				valueOfOperation = value;
				phrase = temporaryPhrase;
			}//Updates to get the best phrase and corresponding value of operation
		}
	}
	
	private boolean isInSet(String input, String phraseToUse) {//Used to determine which phrase to use in constructor
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
