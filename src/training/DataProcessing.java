package training;

import dataStructures.DataStep;
import dataStructures.Flashcard;
import dataStructures.FlashcardDataSet;
import fileManipulation.DataExport;
import generalUtilities.CustomRandom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class DataProcessing {
	
	final private static int countThreshold = 50000;
	final private static int maxPhraseSize = 5;
	final public static int FIXED_DATA_SIZE_FOR_VECTOR = 100;
	private ArrayList<String> sortedPhrases = new ArrayList<>();
	private double[] valuesForSortedPhrases;
	private ArrayList<String> phrasesSortedInSize = new ArrayList<>();
	final private double positionForEmptyString;
	private final int phrasesSize;
	private CustomRandom random = new CustomRandom();
	
	public static void main(String[] args) {
		CustomRandom random = new CustomRandom();

		FlashcardDataSet data = new FlashcardDataSet("DataSets/TranslatedFlashcards.txt", random);
		
		for(DataStep step: data.getTrainingDataSteps()) {
			String output = data.getDataProcessing().doubleArrayToString(data.getDataProcessing().stringToDoubleArray(step.getInputText()));
			if(! output.equals(step.getInputText())    ) {
				System.out.println(step.getInputText()+"|||||"+output);
			}
			
		}
		for(DataStep step: data.getTestingDataSteps()) {
			String output = data.getDataProcessing().doubleArrayToString(data.getDataProcessing().stringToDoubleArray(step.getInputText()));
			if(! output.equals(step.getInputText())    ) {
				System.out.println(step.getInputText()+"|||||"+output);
			}
		}
		for(DataStep step: data.getTrainingDataSteps()) {
			String output = data.getDataProcessing().doubleArrayToString(data.getDataProcessing().stringToDoubleArray(step.getTargetOutputText()));
			if(! output.equals(step.getTargetOutputText())    ) {
				System.out.println(step.getTargetOutputText()+"|||||"+output);
			}
			
		}
		
		for(DataStep step: data.getTestingDataSteps()) {
			String output = data.getDataProcessing().doubleArrayToString(data.getDataProcessing().stringToDoubleArray(step.getTargetOutputText()));
			if(! output.equals(step.getTargetOutputText())    ) {
				System.out.println(step.getTargetOutputText()+"|||||"+output);
			}
			
		}
	}
	
	public ArrayList<String> getPhrases(){
		return sortedPhrases;
	}

	public DataProcessing(ArrayList<String> lines) {
		ArrayList<String> fragments = new ArrayList<>();
		
		for(String line: lines) {
			String[] tempArray = line.split(Flashcard.CARD_SENTENCE_SEPARATOR);
			fragments.add(tempArray[0]);

			fragments.add(tempArray[1]);

		}

		int total = DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR * fragments.size();
		
		HashMap<String, Integer> phrasesAndCount = new HashMap<>();

		for(int i = 1; i< maxPhraseSize; i++) {
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
		ArrayList<String> temporaryPhrases = new ArrayList<>();
		for(String phrase:maxPhrases) {
			if(phrase.length()==1 || phrasesAndCount.get(phrase) >= countThreshold ) {
				temporaryPhrases.add(phrase);
			}
		}
		
		temporaryPhrases.add(Flashcard.FRONT_BACK_SEPARATOR);

		temporaryPhrases.sort((string1, string2) -> string2.length() - string1.length());

		HashMap<String,Integer> finalPhrasesCount = new HashMap<>();
		
		for(String line:lines) {
			for(int i=0;i<line.length();) {
				for (String phrase : temporaryPhrases) {
					if (i + phrase.length() <= line.length()) {
						if (phrase.equals(line.substring(i, i + phrase.length()))) {
							if (finalPhrasesCount.containsKey(phrase)) {
								int previousValue = finalPhrasesCount.get(phrase);
								finalPhrasesCount.remove(phrase);
								finalPhrasesCount.put(phrase, previousValue + 1);
							} else {
								finalPhrasesCount.put(phrase, 1);
							}
							i += phrase.length();
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

		phrasesSortedInSize.sort((string1, string2) -> string2.length() - string1.length());
		
		phrasesSize = sortedPhrases.size();
		
		StringBuilder phrasesListBuilder = new StringBuilder();
		for(int i = 0;i<phrasesSize;i++) {
			System.out.println(sortedPhrases.get(i)+" - "+valuesForSortedPhrases[i]+" - "+finalPhrasesCount.get( sortedPhrases.get(i) ));
			phrasesListBuilder.append(sortedPhrases.get(i)).append(" - ").append(valuesForSortedPhrases[i]).append("\n");
		}
		DataExport.overwriteTextFile(phrasesListBuilder, "Models/Phrases and Values.txt");
		System.out.println("Position For Empty String - "+positionForEmptyString);
		
		System.out.println("Finished Data Preparation");
	}


	
	public double[] stringToDoubleArray(String string) {
		double[] output = new double[FIXED_DATA_SIZE_FOR_VECTOR];
		int index =0;
		for (int i = 0; i < string.length(); ) {
			for(int j=0;j<phrasesSize;j++) {
				String phrase = phrasesSortedInSize.get(j);
				//System.out.println(phrase + "\t" + message.substring(i, i+phrase.length()));
				if (i + phrase.length() <= string.length()) {
					if (phrase.equals(string.substring(i, i + phrase.length()))) {
						//System.out.println(phrase);
						output[index++] = valuesForSortedPhrases[sortedPhrases.indexOf(phrase)]+0.0000001* random.randomDouble()*(valuesForSortedPhrases[sortedPhrases.indexOf(phrase)+1]-valuesForSortedPhrases[sortedPhrases.indexOf(phrase)]);
						
						i+= phrase.length();
						//System.out.print(output[index-1]+" - "+phrase+" "+i+" "+"\t");
						
						break;
					}
				}
				
				
			}
			if(index>= FIXED_DATA_SIZE_FOR_VECTOR) {
				break;
			}
		}
		for(int i = index; i< FIXED_DATA_SIZE_FOR_VECTOR; i++) {
			output[i] = positionForEmptyString+0.0000001* random.randomDouble()*(1.0-positionForEmptyString);
		}
		
		//System.out.println();
		//System.out.print(1/0);
		return output;
	}
	
	public String doubleArrayToString(double[] input) {
		StringBuilder stringBuilder = new StringBuilder();

		for (double value : input) {
			if (value >= positionForEmptyString || value < -1.0) {
				continue;
			}
			int low = 0;
			int high = phrasesSize - 1;
			boolean found = false;
			while (!found) {
				int middle = (low + high) / 2;
				if (value >= valuesForSortedPhrases[middle] && value < valuesForSortedPhrases[middle + 1]) {
					stringBuilder.append(sortedPhrases.get(middle));
					found = true;
				} else if (value < valuesForSortedPhrases[middle]) {
					high = middle - 1;
				} else {
					low = middle + 1;
				}
			}

		}
		return stringBuilder.toString();
	}
	
	public int getNumOfPhrases() {
		return phrasesSize;
	}
}
