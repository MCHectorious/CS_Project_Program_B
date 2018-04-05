package training;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;

import dataStructures.DataStep;
import dataStructures.Flashcard;
import dataStructures.FlashcardDataSet;
import fileIO.DataExport;
import util.CustomRandom;

public class DataPreparation {
	
	final private static int countThreshold = 50000;
	final private static int maxWordPiece = 5;
	final public static int FIXED_VECTOR_SIZE = 100;
	private ArrayList<String> sortedPhrases = new ArrayList<>();
	private double[] valuesForSortedPhrases;
	private ArrayList<String> phrasesSortedInSize = new ArrayList<>();
	final private double positionForEmptyString;
	private final int phrasesSize;
	private CustomRandom rand = new CustomRandom();
	
	public static void main(String[] args) {
		//For testing
		CustomRandom util = new CustomRandom();

		FlashcardDataSet data = new FlashcardDataSet("DataSets/TranslatedFlashcards.txt", util);
		
		for(DataStep step: data.getTrainingDataSteps()) {
			String output = data.getDataPrep().doubleArrayToString(data.getDataPrep().stringToDoubleArray(step.getInputText()));
			if(! output.equals(step.getInputText())    ) {
				System.out.println(step.getInputText()+"|||||"+output);
			}
			
		}
		for(DataStep step: data.getTestingDataSteps()) {
			String output = data.getDataPrep().doubleArrayToString(data.getDataPrep().stringToDoubleArray(step.getInputText()));
			if(! output.equals(step.getInputText())    ) {
				System.out.println(step.getInputText()+"|||||"+output);
			}
		}
		for(DataStep step: data.getTrainingDataSteps()) {
			String output = data.getDataPrep().doubleArrayToString(data.getDataPrep().stringToDoubleArray(step.getOutputText()));
			if(! output.equals(step.getOutputText())    ) {
				System.out.println(step.getOutputText()+"|||||"+output);
			}
			
		}
		
		for(DataStep step: data.getTestingDataSteps()) {
			String output = data.getDataPrep().doubleArrayToString(data.getDataPrep().stringToDoubleArray(step.getOutputText()));
			if(! output.equals(step.getOutputText())    ) {
				System.out.println(step.getOutputText()+"|||||"+output);
			}
			
		}
	}
	
	public ArrayList<String> getPhrases(){
		return sortedPhrases;
	}
	
	public DataPreparation(ArrayList<String> lines) {
		ArrayList<String> fragments = new ArrayList<>();
		
		for(String line: lines) {
			//System.out.println("Combined: "+line);
			String[] tempArray = line.split(Flashcard.CARD_SENTENCE_SEPERATOR);
			fragments.add(tempArray[0]);
			//System.out.println("Input: "+tempArray[0]);

			fragments.add(tempArray[1]);
			//System.out.println("Expected Output: "+tempArray[1]);

		}
		
		int total = DataPreparation.FIXED_VECTOR_SIZE*fragments.size();
		
		HashMap<String, Integer> phrasesAndCount = new HashMap<>();

		for(int i=1;i<maxWordPiece;i++) {
			for(String fragment:fragments) {
				for(int j=0;j<fragment.length()-i;j++) {
					
						String character = fragment.substring(j, j+i);
						if(phrasesAndCount.containsKey(character)) {
							int previousValue = phrasesAndCount.get(character);
							phrasesAndCount.remove(character);
							phrasesAndCount.put(character, previousValue+1);
						}else {
							phrasesAndCount.put(character, 1);

						}
					
					
					
				}
			}
		}

		Set<String> maxPhrases = phrasesAndCount.keySet();
		ArrayList<String> tempPhrases = new ArrayList<>();
		for(String phrase:maxPhrases) {
			if(phrase.length()==1 || phrasesAndCount.get(phrase) >= countThreshold ) {
				tempPhrases.add(phrase);
			}
		}
		
		tempPhrases.add(Flashcard.FRONT_BACK_SEPARATOR);
		
		tempPhrases.sort( new Comparator<String>() {
			@Override
			public int compare(String string1, String string2) {
				return string2.length()-string1.length();
			}
			
		});
		
		//ArrayList<String> phrasesUnsorted = new ArrayList<>();
		
		HashMap<String,Integer> finalPhrasesCount = new HashMap<>();
		
		for(String line:lines) {
			for(int i=0;i<line.length();) {
				for(int j=0;j<tempPhrases.size();j++) {
					String phrase = tempPhrases.get(j);
					
					if(i+phrase.length()<=line.length()) {
						if(phrase.equals( line.substring(i, i+phrase.length()) )) {
							if(finalPhrasesCount.containsKey(phrase)) {
								int previousValue = finalPhrasesCount.get(phrase);
								finalPhrasesCount.remove(phrase);
								finalPhrasesCount.put(phrase, previousValue+1);
							}else {
								finalPhrasesCount.put(phrase, 1);
							}
							i+= phrase.length();
							break;
						}
					}
				}
			}
		}
		
		Set<String> finalPhrases = finalPhrasesCount.keySet();
		
		for(String phrase : finalPhrases) {
			phrasesSortedInSize.add(phrase);
			sortedPhrases.add(phrase);
		}
		
		int totalCounts = 0;
		for(String phrase: sortedPhrases) {
			totalCounts += finalPhrasesCount.get(phrase);
		}
		
		valuesForSortedPhrases = new double[sortedPhrases.size()+1];
		double previousValue = -1.0;
		//valuesForSortedPhrases[0] = previousValue;
		for(int i=0;i<sortedPhrases.size();i++) {
			valuesForSortedPhrases[i] = previousValue;
			previousValue += (2*(double) finalPhrasesCount.get(sortedPhrases.get(i)) )/total;
			
		}
		
		//System.out.println(totalCounts+"/"+total);
		
		positionForEmptyString = previousValue;
		valuesForSortedPhrases[sortedPhrases.size()] = positionForEmptyString;
		
		phrasesSortedInSize.sort( new Comparator<String>() {
			@Override
			public int compare(String string1, String string2) {
				return string2.length()-string1.length();
			}
			
		});
		
		phrasesSize = sortedPhrases.size();
		
		StringBuilder tempBuilder = new StringBuilder();
		for(int i = 0;i<phrasesSize;i++) {
			System.out.println(sortedPhrases.get(i)+" - "+valuesForSortedPhrases[i]+" - "+finalPhrasesCount.get( sortedPhrases.get(i) ));
			tempBuilder.append(sortedPhrases.get(i)+" - "+valuesForSortedPhrases[i]+"\n");
		}
		DataExport.overwriteToTextFile(tempBuilder, "Models/Phrases and Values.txt");
		System.out.println("Position For Empty String - "+positionForEmptyString);
		
		System.out.println("Finished Data Preparation");
	}


	
	public double[] stringToDoubleArray(String string) {
		double[] output = new double[FIXED_VECTOR_SIZE];
		String message = string;
		int index =0;
		//System.out.println(message + message.length());
		for(int i=0;i<message.length();) {
			for(int j=0;j<phrasesSize;j++) {
				String phrase = phrasesSortedInSize.get(j);
				//System.out.println(phrase + "\t" + message.substring(i, i+phrase.length()));
				if(i+phrase.length()<=message.length()) {
					if(phrase.equals(message.substring(i, i+phrase.length()))) {
						//System.out.println(phrase);
						output[index++] = valuesForSortedPhrases[sortedPhrases.indexOf(phrase)]+0.0000001*rand.randomDouble()*(valuesForSortedPhrases[sortedPhrases.indexOf(phrase)+1]-valuesForSortedPhrases[sortedPhrases.indexOf(phrase)]);
						
						i+= phrase.length();
						//System.out.print(output[index-1]+" - "+phrase+" "+i+" "+"\t");
						
						break;
					}
				}
				
				
			}
			if(index>=FIXED_VECTOR_SIZE) {
				break;
			}
		}
		for(int i = index;i<FIXED_VECTOR_SIZE;i++) {
			output[i] = positionForEmptyString+0.0000001*rand.randomDouble()*(1.0-positionForEmptyString);
		}
		
		//System.out.println();
		//System.out.print(1/0);
		return output;
	}
	
	public String doubleArrayToString(double[] input) {
		StringBuilder builder = new StringBuilder();
		//for(int i=0;i<input.length;i++) {
			//System.out.print(input[i]+"\t");
		//}
		//System.out.println();
		for(int i=0;i<input.length;i++) {
			//System.out.print(input[i]+" ");
			if(input[i]>=positionForEmptyString || input[i]<-1.0) {
				continue;
			}
			double value = input[i];
			int low = 0;
			int high = phrasesSize-1;
			boolean found = false;
			while(!found) {
				int middle = (low+high)/2;
				if(value>=valuesForSortedPhrases[middle] && value<valuesForSortedPhrases[middle+1]) {
					builder.append(sortedPhrases.get(middle));
					//System.out.println(sortedPhrases.get(middle));
					found = true;
				} else if(value < valuesForSortedPhrases[middle]) {
					high = middle -1;
				}else {
					low = middle+1;
				}
			}
			
		}
		return builder.toString();
	}
	
	public int getNumOfPhrases() {
		return phrasesSize;
	}
}
