package dataStructures;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;



public class Flashcard {

	public static final String FRONT_BACK_SEPARATOR = "<F_B_S>";
	public static final String CARD_SENTENCE_SEPERATOR = "<Fc_S_S>";
	
	private final String front;
	private final String back;
	private String translation;
	
	public void setTranslation(String string) {
		translation = string;
	}
	
	public String getTranslation() {
		return translation;
	}

	public String getFront() {
		return front;
	}

	public String getBack() {
		return back;
	}

	public Flashcard(String combined){
		this.front = combined.substring(0, combined.indexOf(FRONT_BACK_SEPARATOR)).trim();
		if(combined.contains(CARD_SENTENCE_SEPERATOR)) {
			this.back = combined.substring(combined.indexOf(Flashcard.FRONT_BACK_SEPARATOR)+ FRONT_BACK_SEPARATOR.length(), combined.indexOf(CARD_SENTENCE_SEPERATOR)).trim();
			this.translation = combined.substring(combined.indexOf(CARD_SENTENCE_SEPERATOR)+ CARD_SENTENCE_SEPERATOR.length()).trim();
		}else {
			this.back= combined.substring(combined.indexOf(FRONT_BACK_SEPARATOR) + FRONT_BACK_SEPARATOR.length()).trim();
			this.translation = "";
		}
		
	}
	
	@Override
	public String toString() {
		if(translation.equals("")) {
			return front+FRONT_BACK_SEPARATOR+back;	
		}else {
			return front+FRONT_BACK_SEPARATOR+back+CARD_SENTENCE_SEPERATOR+translation;
		}
		
	}
	
	
	public static String getFlashcard(String string) {
		return string.substring(0, string.indexOf(CARD_SENTENCE_SEPERATOR));
	}
	
	public static String withSep(String f,String b) {
		return f + FRONT_BACK_SEPARATOR + b;
	}
	
	public static String withSep(String front, String back, String translation) {
		return front + FRONT_BACK_SEPARATOR + back + CARD_SENTENCE_SEPERATOR + translation;
	}
	
	public static String getFront(String string) {
		return string.substring(0, string.indexOf(FRONT_BACK_SEPARATOR));
	}
	
	public static String getBack(String string) {
		return string.substring(string.indexOf(Flashcard.FRONT_BACK_SEPARATOR)+ FRONT_BACK_SEPARATOR.length(), string.indexOf(CARD_SENTENCE_SEPERATOR));
	}
	
	public static String getTranslation(String string) {
		return string.substring(string.indexOf(CARD_SENTENCE_SEPERATOR)+ CARD_SENTENCE_SEPERATOR.length());
	}
	
	public static ArrayList<Flashcard> getFlashcardListFromTextFile(String file){
		ArrayList<Flashcard> output = new ArrayList<>();
		
		try{
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
		    while ((line = br.readLine()) != null){
	    		output.add(new Flashcard(line));
		    }
		    br.close();
		}catch(Exception e){
			e.printStackTrace();
		}

		
		return output;
	}
	
}
