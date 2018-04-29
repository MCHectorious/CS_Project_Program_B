package ManualTranslation;

import DataStructures.Flashcard;
import FileManipulation.DataImport;

import java.util.ArrayList;

public class MainTranslation {

	public static void main(String[] args) {
        AutomaticDataPreProcessing.removeFlashcardsWithUnknownCharacters("DataSets/RawFlashcards.txt");//Removes flashcards which include characters which can't represented in UTF-8 correctly (this could include characters from language for example)

		ArrayList<Flashcard> flashcards = DataImport.getFlashcardListFromTextFile("DataSets/RawFlashcards.txt");//Gets the raw flashcards

        AutomaticDataPreProcessing.automaticallyTranslateRawFlashcards(flashcards, "DataSets/TranslatedFlashcards.txt");//If possible generate the correct sentence

        AutomaticDataPreProcessing.removeTranslatedCards("DataSets/RawFlashcards.txt", "DataSets/TranslatedFlashcards.txt");//Because the flashcards have been  translated they can be removed

		AutomaticDataPreProcessing.deleteDuplicateFlashcards("DataSets/TranslatedFlashcards.txt");//To avoiid the model being biased to certain data steps

	}

}
