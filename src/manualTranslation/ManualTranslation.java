package manualTranslation;

import dataStructures.Flashcard;
import fileManipulation.DataExport;
import fileManipulation.DataImport;
import generalUtilities.CustomRandom;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class ManualTranslation {

	public static void main(String[] args) {

        ArrayList<Flashcard> rawFlashcards = DataImport.getFlashcardListFromTextFile("DataSets/RawFlashcards.txt");
        ArrayList<Flashcard> flashcardsWithTranslations = DataImport.getFlashcardListFromTextFile("DataSets/TranslatedFlashcards.txt");
		ArrayList<Flashcard> availableFlashcards = new ArrayList<>();
		
		for(Flashcard cardWithoutTranslation: rawFlashcards){
			if(!flashcardsWithTranslations.contains(cardWithoutTranslation)){
				availableFlashcards.add(cardWithoutTranslation);}
		}
		
		
		CustomRandom random = new CustomRandom();
		Flashcard flashcard;
		String userTranslation;
		
		Scanner scanner = new Scanner(System.in);
		
		while(availableFlashcards.size()>0){
			int randomFlashcardIndex = random.randomInt(availableFlashcards.size());
			flashcard = availableFlashcards.get(randomFlashcardIndex);
            System.out.println("What would be the sentence form of a flashcard with a front of " + flashcard.getFlashcardFront() + " and a back of " + flashcard.getFlashcardBack() + "?");
			do{
				userTranslation = scanner.nextLine();
			}while( userTranslation.isEmpty() );
			availableFlashcards.remove(randomFlashcardIndex);
			flashcard.setSentence(userTranslation);
			DataExport.appendToTextFile(flashcard.toString(), "DataSets/TranslatedFlashcards.txt");
		}
		
		scanner.close();
	
		
	}

}
