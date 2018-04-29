package ManualTranslation;

import DataStructures.Flashcard;
import FileManipulation.DataExport;
import FileManipulation.DataImport;
import GeneralUtilities.CustomRandom;

import java.util.ArrayList;
import java.util.Scanner;

public class ManualTranslation {

	public static void main(String[] args) {

        ArrayList<Flashcard> rawFlashcards = DataImport.getFlashcardListFromTextFile("DataSets/RawFlashcards.txt");//Gets the flascards without a translation
        ArrayList<Flashcard> flashcardsWithTranslations = DataImport.getFlashcardListFromTextFile("DataSets/TranslatedFlashcards.txt");//Gets the flashcards with a translation
		ArrayList<Flashcard> availableFlashcards = new ArrayList<>();
		
		for(Flashcard flashcardWithoutTranslation: rawFlashcards){
			if(!flashcardsWithTranslations.contains(flashcardWithoutTranslation)){
				availableFlashcards.add(flashcardWithoutTranslation);
			}//Only show flashcards which haven't already been translated
		}
		
		
		CustomRandom random = new CustomRandom();
		Flashcard flashcard;//To store the flashcard which the user is going to translate
		String userTranslation;
		
		Scanner scanner = new Scanner(System.in);//Used to get the user input
		
		while(availableFlashcards.size()>0){//While there are still flashcards to translate
			int randomFlashcardIndex = random.randomInt(availableFlashcards.size());
			flashcard = availableFlashcards.get(randomFlashcardIndex);//Picks a random flashcard
            System.out.println("What would be the sentence form of a flashcard with a front of " + flashcard.getFlashcardFront() + " and a back of " + flashcard.getFlashcardBack() + "?");//Explains to the user what to do
			do{
				userTranslation = scanner.nextLine();//Gets the user input
			}while( userTranslation.isEmpty() );//Keeps asking the user question until they given a response which isn't an empty line
			availableFlashcards.remove(randomFlashcardIndex);//Removes it from the list to avoid asking the user to translate it again
			flashcard.setSentence(userTranslation);//Updates the flashcard
			DataExport.appendToTextFile(flashcard.toString(), "DataSets/TranslatedFlashcards.txt");//Adds the translated flashcard to the te text file
		}
		
		scanner.close();//Closes the scanner to avoid memory leaks
	
		
	}

}
