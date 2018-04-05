package manualTranslation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import fileIO.DataExport;
import fileIO.DataImport;
import training.DataPreparation;
import util.Util;
import dataStructures.DataStep;
import dataStructures.Flashcard;

public class AutoDataIncrease {

	public static void addCapitalisationVariation(ArrayList<Flashcard> input, String file) {
		for(Flashcard card: input) {
			if( checkNormalWord(card.getFront()) ) { 
				DataExport.appendToTextFile(Flashcard.withSep(reverseFirstChar(card.getFront()), card.getBack(), card.getTranslation()), file);
			}
			
			if( checkNormalWord(card.getBack()) ) {
				DataExport.appendToTextFile(Flashcard.withSep(card.getFront(), reverseFirstChar(card.getBack()), card.getTranslation()), file);
			}
		
			if( checkNormalWord(card.getFront()) && checkNormalWord(card.getBack())) {
				DataExport.appendToTextFile(Flashcard.withSep(reverseFirstChar(card.getFront()), reverseFirstChar(card.getBack()), card.getTranslation()), file);
			}
		}
			
	
		
	}
	
	public static ArrayList<DataStep> addCapitalisationVariation(String front, String back, DataPreparation dataPrep, double[] output,  String outputText) {
		ArrayList<DataStep> steps = new ArrayList<>();
		if( checkNormalWord(front) ) { 
			//System.out.print("*a*");
			//DataExport.appendToTextFile(Flashcard.withSep(reverseFirstChar(card.getFront()), card.getBack(), card.getTranslation()), file);
			String input = Flashcard.withSep(reverseFirstChar(front), back);
			steps.add(new DataStep(dataPrep.stringToDoubleArray(input), output, input, outputText));
		}
		if( checkNormalWord(back) ) {
			//System.out.print("*b*");
			//DataExport.appendToTextFile(Flashcard.withSep(card.getFront(), reverseFirstChar(card.getBack()), card.getTranslation()), file);
			String input = Flashcard.withSep(front, reverseFirstChar(back));
			steps.add(new DataStep(dataPrep.stringToDoubleArray(input), output, input, outputText));
		}
		if( checkNormalWord(front) && checkNormalWord(back)) {	
			String input = Flashcard.withSep(reverseFirstChar(front), reverseFirstChar(back));
			steps.add(new DataStep(dataPrep.stringToDoubleArray(input), output, input, outputText));
	
		}		
		return steps;
	}
	
	private static Boolean checkNormalWord(String s) {
		switch(s.charAt(0)) {
		case 'á': return false;
		case 'â': return false;
		case 'à': return false;
		}
		if(s.length()==1) {
			return true;
		}else {
			//System.out.print("="+s.charAt(0)+"=");
			return Character.isLetter(s.charAt(0)) && Character.isLowerCase(s.charAt(1));
		}
	}
	
	private static String reverseFirstChar(String s) {
		String newFirstLetter;
		char oldFirstLetter = s.charAt(0) ;
		if( Character.isLowerCase( oldFirstLetter ) ) {
			newFirstLetter =  Character.toString(oldFirstLetter).toUpperCase();
		}else {
			newFirstLetter =  Character.toString(oldFirstLetter).toLowerCase();
		}
		return newFirstLetter + s.substring(1);
	}
	
	public static void addOneWordTranslations(ArrayList<Flashcard> input, String file) {
		for(Flashcard card:input) {
			
			if(!card.getBack().contains(" ")) {
				if(!card.getFront().contains(" "))  {
					DataExport.appendToTextFile(Flashcard.withSep(card.getFront(), card.getBack(), "\"" + card.getFront() + "\" means \"" + card.getBack() + "\""), file);
				}else{
					String afterSpace = card.getFront().substring(card.getFront().indexOf(" ")+1);
					String beforeSpace = card.getFront().substring(0, card.getFront().indexOf(" "));
					if(afterSpace.equals("(m)")) {
						DataExport.appendToTextFile(Flashcard.withSep(card.getFront(), card.getBack(), "\"" + beforeSpace + "\" is the male word for \"" + card.getBack() + "\""), file);
					}
					if(afterSpace.equals("(f)")) {
						DataExport.appendToTextFile(Flashcard.withSep(card.getFront(), card.getBack(), "\"" + beforeSpace + "\" is the female word for \"" + card.getBack() + "\""), file);
					}
					if(afterSpace.equals("(n)")) {
						DataExport.appendToTextFile(Flashcard.withSep(card.getFront(), card.getBack(), "\"" + beforeSpace + "\" is the neuter word for \"" + card.getBack() + "\""), file);
					}
					if(!afterSpace.contains(" ")) {
						if( beforeSpace.equals("el")||beforeSpace.equals("le")||beforeSpace.equals("la")||beforeSpace.equals("die")||beforeSpace.equals("der")||beforeSpace.equals("das") ) {
							DataExport.appendToTextFile(Flashcard.withSep(card.getFront(), card.getBack(), "\"" + card.getFront() + "\" means \"the " + card.getBack() + "\""), file);
						}
						
					}
				}
			}
		}
	}
	
	public static String autoTranslate(String flashcardFront, String flashcardBack) {
        String front = flashcardFront.trim();
        if(front.contains("(") && front.contains(")")) {
            if ( front.substring(front.lastIndexOf("(")+1, front.lastIndexOf(")")).matches("[0-9]+") ){
                front = front.substring(0,front.length()-3).trim();

            }
        }
        if (front.charAt(front.length()-1)=='?'){
            front = front.substring(0,front.length()-1);
        }

        String back = Character.toLowerCase(flashcardBack.charAt(0)) +flashcardBack.trim().substring(1);
        String frontToTest = front.toLowerCase();


        if(frontToTest.contains("...")){
            if ( frontToTest.substring( frontToTest.length()-3,frontToTest.length() ).equals("...") ){
                return front.substring(0,frontToTest.length()-3)+" "+flashcardBack;
            }else{
                return front.substring(0,front.indexOf("..."))+" "+back+" "+front.substring(front.indexOf("...")+3);
            }
        }

		if(!back.contains(" ")) {
			if(!front.contains(" "))  {
				return "\"" + front + "\" means \"" + back + "\"";
			}else{
				String afterSpace = front.substring(front.indexOf(" ")+1);
				String beforeSpace = front.substring(0, front.indexOf(" "));
				if(afterSpace.equals("(m)")) {
					return "\"" + beforeSpace + "\" is the male word for \"" + back + "\"";
				}
				if(afterSpace.equals("(f)")) {
					return "\"" + beforeSpace + "\" is the female word for \"" + back + "\"";
				}
				if(afterSpace.equals("(n)")) {
					return "\"" + beforeSpace + "\" is the neuter word for \"" + back + "\"";
				}
				if(!afterSpace.contains(" ")) {
					if( beforeSpace.equals("el")||beforeSpace.equals("le")||beforeSpace.equals("la")||beforeSpace.equals("die")||beforeSpace.equals("der")||beforeSpace.equals("das") ) {
						return "\"" + front + "\" means \"the " + back + "\"";
					}
					
				}
			}
		}
        
        if(!frontToTest.contains(" ")){
            return front+" means "+back;
        }else if(Util.countOfCharInString(' ',frontToTest)==1){
            return front+" means "+back;
        }
        
        if(back.charAt(0)=='=') {
        	return front+" "+back;
        }

        
        
        if(frontToTest.length()>19) {
            if (frontToTest.substring(0,19).equals("give advantages of ")){
                return "Advantages of "+Character.toLowerCase(front.charAt(19))+front.substring(20)+" are that "+back;
            }
        }

        if(frontToTest.length()>18) {
            if (frontToTest.substring(0,17).equals("what happens when")){
                return "When "+Character.toLowerCase(front.charAt(18))+front.substring(19)+", "+back;
            }
        }

        if(frontToTest.length()>17) {
            if (frontToTest.substring(0,17).equals("give examples of ")){
                return "Examples of "+Character.toLowerCase(front.charAt(17))+front.substring(18)+" are "+back;
            }
            if (frontToTest.substring(0,17).equals("what happened at ")){
                return "At "+Character.toLowerCase(front.charAt(17))+front.substring(18)+", "+back;
            }
        }
        


        if(frontToTest.length()>16) {
            if (frontToTest.substring(0,16).equals("what happens at ")){
                return "At "+Character.toLowerCase(front.charAt(16))+front.substring(17)+", "+back;
            }

            if(frontToTest.substring(frontToTest.length()-16).equals("by which factors") ){
                return "The factors affecting "+Character.toLowerCase(front.charAt(0))+front.substring(1,frontToTest.length()-16)+"are "+back;
            }
        }

        if(frontToTest.length()>14) {
            if (frontToTest.substring(0,13).equals("how would you")){
                return "You would "+Character.toLowerCase(front.charAt(14))+front.substring(15)+" "+back;
            }
            if (frontToTest.substring(0,13).equals("what happens ")){
                return Character.toUpperCase(front.charAt(14))+front.substring(15)+", "+back;
            }
        }
        
        if(frontToTest.length()>13) {
            if (frontToTest.substring(0,12).equals("explain why ")){
                return Character.toUpperCase(front.charAt(13))+front.substring(14)+" because "+back;
            }
        }

        if(frontToTest.length()>11) {
            if(frontToTest.substring(frontToTest.length()-11).equals("explain why") ){
                return front.substring(0,frontToTest.length()-11)+"because "+back;
            }
            if(frontToTest.substring(frontToTest.length()-11).equals("is given by") ){
                return front+" "+back;
            }
        }

        if(frontToTest.length()>10) {
            String start = frontToTest.substring(0,10);
            if (start.equals("how would ")){
                return front.substring(10)+" by "+back;
            }

            if(start.equals("what does ")){
                if(frontToTest.substring(frontToTest.length()-9,frontToTest.length()).equals("stand for") ){
                    return Character.toUpperCase(front.charAt(10))+front.substring(11,frontToTest.length()-10)+" stands for "+back;
                }

                if(frontToTest.substring(frontToTest.length()-9,frontToTest.length()).equals("represent") ){
                    return Character.toUpperCase(front.charAt(10))+front.substring(11,frontToTest.length()-10)+" represents "+back;
                }

                if(frontToTest.substring(frontToTest.length()-4,frontToTest.length()).equals("mean") ){
                    return Character.toUpperCase(front.charAt(10))+front.substring(11,frontToTest.length()-5)+" means "+back;
                }

                if(frontToTest.substring(frontToTest.length()-7,frontToTest.length()).equals("contain") ){
                    return Character.toUpperCase(front.charAt(10))+front.substring(11,frontToTest.length()-5)+" contains "+back;
                }

            }
            
            if (start.equals("what were ")){
                return Character.toUpperCase(front.charAt(10))+front.substring(11)+" were "+back;
            }

        }
        
        
        if(frontToTest.length()>9) {
            String start = frontToTest.substring(0,9);

            if(start.equals("how does ")){
                return Character.toUpperCase(front.charAt(9))+front.substring(10)+"s by "+back;
            }

            if (start.equals("what are ") ){
                return Character.toUpperCase(front.charAt(9))+front.substring(10)+" are "+back;
            }

            if (start.equals("describe ")){
                return Character.toUpperCase(front.charAt(9))+front.substring(10)+": "+back;
            }

            if (start.equals("how does ")){
                return Character.toUpperCase(front.charAt(9))+front.substring(10)+" by "+back;
            }

            if (start.equals("where do ")){
                return Character.toUpperCase(front.charAt(9))+front.substring(10)+" in "+back;
            }
            if (start.equals("what was ")){
                return Character.toUpperCase(front.charAt(9))+front.substring(10)+" was "+back;
            }
        }
        
        
        if(frontToTest.length()>8) {
            String start = frontToTest.substring(0,8);

            if (start.equals("what is ") ){
                return Character.toUpperCase(front.charAt(8))+front.substring(9)+" is "+back;
            }

            if(start.equals("what do ")){
                if(frontToTest.substring(frontToTest.length()-3).equals("for") ){
                    return Character.toUpperCase(front.charAt(8))+front.substring(9,frontToTest.length()-4)+" for "+back;
                }
            }

            if (start.equals("how can ")){
                return Character.toUpperCase(front.charAt(8))+front.substring(9)+" by "+back;
            }

            if (start.equals("when do ")){
                return Character.toUpperCase(front.charAt(8))+front.substring(9)+" when "+back;
            }

            if(start.equals("why are ")){
                return Character.toUpperCase(front.charAt(8))+front.substring(9,front.lastIndexOf(" "))+" are "+front.substring(front.lastIndexOf(" "))+" because "+back;
            }
            
            if(start.equals("Def. of ")) {
            	return Character.toUpperCase(front.charAt(8))+front.substring(9)+" means "+back;
            }
            
        }
        
        if(frontToTest.length()>7) {
            String start = frontToTest.substring(0,7);

            if(start.equals("define ")){
                return Character.toUpperCase(front.charAt(7))+front.substring(8)+": "+back;
            }

            if (start.equals("how do ")){
                return Character.toUpperCase(front.charAt(7))+front.substring(8)+" by "+back;
            }

            if(start.equals("why is ")){
                return Character.toUpperCase(front.charAt(7))+front.substring(8,front.lastIndexOf(" "))+" is "+front.substring(front.lastIndexOf(" "))+" because "+back;
            }
            
            if(start.equals("why do ")) {
                return Character.toUpperCase(front.charAt(7))+front.substring(8,front.lastIndexOf(" "))+" because "+back;
            }
            
        }

        if(frontToTest.length()>6) {
            if (frontToTest.substring(0,6).equals("state ")){
                return Character.toUpperCase(front.charAt(6))+front.substring(7)+": "+back;
            }
        }

        if(frontToTest.length()>5) {
            String start = frontToTest.substring(0,5);
            if (start.equals("give ")){
                return Character.toUpperCase(front.charAt(5))+front.substring(6)+": "+back;
            }

            if (start.equals("name ")){
                return Character.toUpperCase(front.charAt(5))+front.substring(6)+": "+back;
            }

            if (start.equals("list ")){
                return Character.toUpperCase(front.charAt(5))+front.substring(6)+": "+back;
            }

            if (start.equals("what ") && front.contains(" is ")){
                return Character.toUpperCase(back.charAt(0))+back.substring(1)+" is the "+front.substring(5,front.indexOf(" is "))+" that is "+front.substring(front.indexOf(" is "));
            }
        }
        
        if(frontToTest.length()>1) {
        	if(frontToTest.substring(frontToTest.length()-1).equals("=")) {
        		return front+" "+back;
        	}
        }
        
        return "";
	}
	
	public static void autoTranslateRawFlashcards(ArrayList<Flashcard> inputs, String file) {
		for(Flashcard card: inputs) {
			String sentence;
			try {
				sentence = autoTranslate(card.getFront(),card.getBack());

			}catch(Exception e) {
				System.out.println(e.getMessage());
				continue;
			}
			
			if(sentence.equals("")) {
				
				try {
					sentence = autoTranslate(card.getBack(),card.getFront());

				}catch(Exception e) {
					System.out.println(e.getMessage());
					continue;
				}
				
				if(!sentence.equals("")) {
					String line = Flashcard.withSep(card.getBack(), card.getFront(), sentence);
					DataExport.appendToTextFile(line, file);
				}

			}else {
				String line = Flashcard.withSep(card.getFront(), card.getBack(), sentence);
				DataExport.appendToTextFile(line, file);
			}
		}
	}
	
	
	
	public static void deleteDuplicates(String file) {
		HashSet<String> uniqueCards = DataImport.getUniqueLines(file);
		HashSet<String> finalCards = uniqueCards;
		
		ArrayList<String> cards = new ArrayList<>();
		
		for(String s: uniqueCards) {
			cards.add(s);
		}
		
		for(int i =0;i<cards.size();i++) {
			for(int j =i+1;j<cards.size();j++) {
				if(cards.get(i).toLowerCase().equals(cards.get(j).toLowerCase())) {
					finalCards.remove(cards.get(j));
				}
			}
		}
		
		DataExport.overwriteToTextFile(finalCards, file);
	}
	
	public static void removeTranslatedCards(String rawFile, String translatedFile) {
		HashSet<String> rawCards = DataImport.getUniqueLines(rawFile);
		HashSet<String> translatedCards = DataImport.getUniqueLines(translatedFile);
		HashSet<String> translatedCardsNoTranslation = new HashSet<>();
		for(String card:translatedCards) {
			translatedCardsNoTranslation.add(card.substring(0, card.indexOf(Flashcard.CARD_SENTENCE_SEPERATOR)));
		}
		
		HashSet<String> output = new HashSet<>();
		
		for(String line : rawCards) {
			
			if(!translatedCardsNoTranslation.contains(line)) {
				output.add(line);
			}
		}
		
		DataExport.overwriteToTextFile(output, rawFile);
		
	}
	
	public static void removeFlashcardsWithUnknownCharacters(String file) {
		ArrayList<String> lines = DataImport.getLines(file);
		StringBuilder builder = new StringBuilder();
		for(String line: lines) {
			if(!line.contains("�")) {
				builder.append(line).append("\n");
			}
		}
		
		DataExport.overwriteToTextFile(builder, file);
		
		
	}
}
