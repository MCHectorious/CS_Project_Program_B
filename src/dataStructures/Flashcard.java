package dataStructures;

public class Flashcard {

	public static final String FRONT_BACK_SEPARATOR = "<F_B_S>";
	public static final String CARD_SENTENCE_SEPARATOR = "<Fc_S_S>";
	
	private final String flashcardFront;
	private final String flashcardBack;
	private String sentence;
	
	public void setSentence(String string) {
		sentence = string;
	}
	
	public String getSentence() {
		return sentence;
	}

	public String getFlashcardFront() {
		return flashcardFront;
	}

	public String getFlashcardBack() {
		return flashcardBack;
	}

	public Flashcard(String combinedFlashcardComponents){
		this.flashcardFront = combinedFlashcardComponents.substring(0, combinedFlashcardComponents.indexOf(FRONT_BACK_SEPARATOR)).trim();
		if(combinedFlashcardComponents.contains(CARD_SENTENCE_SEPARATOR)) {
			this.flashcardBack = combinedFlashcardComponents.substring(combinedFlashcardComponents.indexOf(Flashcard.FRONT_BACK_SEPARATOR)+ FRONT_BACK_SEPARATOR.length(), combinedFlashcardComponents.indexOf(CARD_SENTENCE_SEPARATOR)).trim();
			this.sentence = combinedFlashcardComponents.substring(combinedFlashcardComponents.indexOf(CARD_SENTENCE_SEPARATOR)+ CARD_SENTENCE_SEPARATOR.length()).trim();
		}else {
			this.flashcardBack = combinedFlashcardComponents.substring(combinedFlashcardComponents.indexOf(FRONT_BACK_SEPARATOR) + FRONT_BACK_SEPARATOR.length()).trim();
			this.sentence = "";
		}
		
	}
	
	@Override
	public String toString() {
		if(sentence.equals("")) {
			return flashcardFront +FRONT_BACK_SEPARATOR+ flashcardBack;
		}else {
			return flashcardFront +FRONT_BACK_SEPARATOR+ flashcardBack + CARD_SENTENCE_SEPARATOR + sentence;
		}
		
	}


    static String getFlashcard(String string) {
		return string.substring(0, string.indexOf(CARD_SENTENCE_SEPARATOR));
	}
	
	public static String withSeparator(String f, String b) {
		return f + FRONT_BACK_SEPARATOR + b;
	}
	
	public static String withSeparator(String front, String back, String translation) {
		return front + FRONT_BACK_SEPARATOR + back + CARD_SENTENCE_SEPARATOR + translation;
	}

    static String getSentence(String string) {
		return string.substring(string.indexOf(CARD_SENTENCE_SEPARATOR)+ CARD_SENTENCE_SEPARATOR.length());
	}
	

	
}
