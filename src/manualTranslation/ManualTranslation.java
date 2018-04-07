package manualTranslation;

import dataStructures.Flashcard;
import fileManipulation.DataExport;
import fileManipulation.DataImport;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class ManualTranslation {

	public static void main(String[] args) {

        ArrayList<Flashcard> Flashcards = DataImport.getFlashcardListFromTextFile("DataSets/RawFlashcards.txt");
        ArrayList<Flashcard> FlashcardsWithTranslations = DataImport.getFlashcardListFromTextFile("DataSets/TranslatedFlashcards.txt");
		ArrayList<Flashcard> AvailableFlashcards = new ArrayList<>();
		
		for(Flashcard cardWithoutTranslation: Flashcards){
			if(!FlashcardsWithTranslations.contains(cardWithoutTranslation)){
				AvailableFlashcards.add(cardWithoutTranslation);}
		}
		
		
		Random rand = new Random();
		Flashcard card;
		String Translation;
		
		Scanner scanner = new Scanner(System.in);
		
		while(AvailableFlashcards.size()>0){		
			int randomCardIndex = rand.nextInt(AvailableFlashcards.size());
			card = AvailableFlashcards.get(randomCardIndex);
            System.out.println("What would be the sentence form of a flashcard with a front of " + card.getFront() + " and a back of " + card.getBack() + "?");
			do{
				Translation = scanner.nextLine();
			}while( Translation.isEmpty() );
			AvailableFlashcards.remove(randomCardIndex);
			card.setTranslation(Translation);					
			DataExport.appendToTextFile(card.toString(), "DataSets/TranslatedFlashcards.txt");
		}
		
		scanner.close();
	
		
	}

}
