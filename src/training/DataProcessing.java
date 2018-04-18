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
	
	final private static double COUNT_PROPORTION_THRESHOLD = 0.01;// how much of the text as a proportion for a phrase to be considered important
	final private static int MAX_PHRASE_SIZE = 5;//the maximum size of the phrase
	final public static int FIXED_DATA_SIZE_FOR_VECTOR = 100;// the size of the vector representing the string
	private ArrayList<String> sortedPhrases = new ArrayList<>();// the phrases sorted by thee numerical values
	private double[] valuesForSortedPhrases;// the values of the phrases
	private ArrayList<String> phrasesSortedInSize = new ArrayList<>();// the phrases size by the size in descending order
	final private double positionForEmptyString;
	private final int phrasesSize;
	private CustomRandom random = new CustomRandom();
	
	public static void main(String[] args) {// tests that convert to the double array and then converting back to string creates the original string
		CustomRandom random = new CustomRandom();

		FlashcardDataSet data = new FlashcardDataSet("DataSets/TranslatedFlashcards.txt", random);//gets the data set of flashcards and their sentences
		
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
		}//gets input and output

		int total = DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR * fragments.size();// the total possible number of phrases
		
		HashMap<String, Integer> phrasesAndCount = new HashMap<>();

		for(int i = 1; i< MAX_PHRASE_SIZE; i++) {
			for(String fragment:fragments) {
				for(int j=0;j<fragment.length()-i+1;j++) {
					
					String phrase = fragment.substring(j, j+i);

					if(phrasesAndCount.containsKey(phrase)) {
						int previousValue = phrasesAndCount.get(phrase);
						phrasesAndCount.remove(phrase);
						phrasesAndCount.put(phrase, previousValue+1);//Increments the count
					}else {
						phrasesAndCount.put(phrase, 1);//adds the phrase

					}
				}
			}
		}

		Set<String> maxPhrases = phrasesAndCount.keySet();
		ArrayList<String> temporaryPhrases = new ArrayList<>();

		int countThreshold = (int) Math.floor(COUNT_PROPORTION_THRESHOLD *total);// how many occurrences need to occur for a phrase to be considered important

		for(String phrase:maxPhrases) {
			if(phrase.length()==1) {//adds all unique single characters
				temporaryPhrases.add(phrase);
			}else if(phrasesAndCount.get(phrase) >= countThreshold ){
				temporaryPhrases.add(phrase);// adds the phrase if it is considered important
			}
		}

		temporaryPhrases.add(Flashcard.FRONT_BACK_SEPARATOR);//ensures that the front-back separator is a possible phrase

		temporaryPhrases.sort((string1, string2) -> string2.length() - string1.length());// sorts the phrase by size in descending order

		HashMap<String,Integer> finalPhrasesCount = new HashMap<>();

		for(String line:lines) {
			for(int i=0;i<line.length();) {
				for (String phrase : temporaryPhrases) {
					if (i + phrase.length() <= line.length()) {
						if (phrase.equals(line.substring(i, i + phrase.length()))) {
							if (finalPhrasesCount.containsKey(phrase)) {
								int previousValue = finalPhrasesCount.get(phrase);
								finalPhrasesCount.remove(phrase);
								finalPhrasesCount.put(phrase, previousValue + 1);// increments the count of the phrase
							} else {
								finalPhrasesCount.put(phrase, 1);// initialises the count to 1
							}
							i += phrase.length();//searches the next part of the string
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
		for(int i=0;i<sortedPhrases.size();i++) {
			valuesForSortedPhrases[i] = previousValue;
			previousValue += (2*(double) finalPhrasesCount.get(sortedPhrases.get(i)) )/total;
			
		}//gets the value of the phrase based on how frequently it occurs

		

		positionForEmptyString = previousValue;
		valuesForSortedPhrases[sortedPhrases.size()] = positionForEmptyString;

		phrasesSortedInSize.sort((string1, string2) -> string2.length() - string1.length());//sorts the phrases  by size in descending order
		
		phrasesSize = sortedPhrases.size();
		
		StringBuilder phrasesListBuilder = new StringBuilder();
		for(int i = 0;i<phrasesSize;i++) {
			System.out.println(sortedPhrases.get(i)+" - "+valuesForSortedPhrases[i]+" - "+finalPhrasesCount.get( sortedPhrases.get(i) ));
			phrasesListBuilder.append(sortedPhrases.get(i)).append(" - ").append(valuesForSortedPhrases[i]).append("\n");
		}// outputs the phrases and their values
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
				if (i + phrase.length() <= string.length()) {
					if (phrase.equals(string.substring(i, i + phrase.length()))) {
						output[index++] = valuesForSortedPhrases[sortedPhrases.indexOf(phrase)]+0.0000001* random.randomDouble()*(valuesForSortedPhrases[sortedPhrases.indexOf(phrase)+1]-valuesForSortedPhrases[sortedPhrases.indexOf(phrase)]);
						// adds a random amount of variation to avoid over-fitting
						i+= phrase.length();
						break;// searches the next part of the string
					}
				}
			}
			if(index>= FIXED_DATA_SIZE_FOR_VECTOR) {
				break;//stops when all the data is filled
			}
		}
		for(int i = index; i< FIXED_DATA_SIZE_FOR_VECTOR; i++) {
			output[i] = positionForEmptyString+0.0000001* random.randomDouble()*(1.0-positionForEmptyString);
		}//fills the rest of the data
		return output;
	}
	
	public String doubleArrayToString(double[] input) {
		StringBuilder stringBuilder = new StringBuilder();

		for (double value : input) {
			if (value >= positionForEmptyString || value < -1.0) {//if it is outside the range, then it is skipped
				continue;
			}
			//uses binary search (adapted to work between values in the list) to efficient find which phrase the value belongs to
			int low = 0;
			int high = phrasesSize - 1;
			boolean found = false;
			while (!found) {
				int middle = (low + high) >> 1;//the floor of the midpoint between the two indexes
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
	
	public int getNumberOfPhrases() {
		return phrasesSize;
	}
}
