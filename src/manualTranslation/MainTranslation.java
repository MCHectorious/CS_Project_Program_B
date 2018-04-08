package manualTranslation;

public class MainTranslation {

	public static void main(String[] args) {

		//ArrayList<Flashcard> Flashcards = Flashcard.getFlashcardListFromTextFile("DataSets/TranslatedFlashcards.txt");
		//autoDataIncrease.addCapitalisationVariation(Flashcards, "DataSets/TranslatedFlashcards.txt");
		
		//ArrayList<Flashcard> Flashcards = Flashcard.getFlashcardListFromTextFile("DataSets/RawFlashcards.txt");
		//autoDataIncrease.addOneWordTranslations(Flashcards, "DataSets/TranslatedFlashcards.txt");
		//String s = "â";
		//System.out.println(s.toUpperCase());
		
		
		//ArrayList<Flashcard> Flashcards = Flashcard.getFlashcardListFromTextFile("DataSets/RawFlashcards.txt");

        //AutomaticDataPreProcessing.automaticallyTranslateRawFlashcards(Flashcards, "DataSets/TranslatedFlashcards.txt");

        //AutomaticDataPreProcessing.deleteDuplicateFlashcards("DataSets/TranslatedFlashcards.txt");

        AutomaticDataPreProcessing.removeTranslatedCards("DataSets/RawFlashcards.txt", "DataSets/TranslatedFlashcards.txt");
		
		
		//.removeFlashcardsWithUnknownCharacters("DataSets/RawFlashcards.txt");
		
		//System.out.println("�?");

		
		//String test = "�?�√(8a�?�b³)<F_B_S>a �?�√(8b³)<Fc_S_S>�?�√(8a�?�b³) means a �?�√(8b³)\r\n"; 
				
		//System.out.println(test.contains("�?"));
		
	}

}
