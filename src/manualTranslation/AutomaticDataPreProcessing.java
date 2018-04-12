package manualTranslation;

import dataStructures.DataStep;
import dataStructures.Flashcard;
import fileManipulation.DataExport;
import fileManipulation.DataImport;
import generalUtilities.Utilities;
import training.DataProcessing;

import java.util.ArrayList;
import java.util.HashSet;

public class AutomaticDataPreProcessing {


    public static ArrayList<DataStep> addCapitalisationVariation(String flashcard, String sentence, DataProcessing dataPrep, double[] output, String outputText) {
		ArrayList<DataStep> steps = new ArrayList<>();//Initialises array
		boolean isFrontANormalWord =checkNormalWord(flashcard, dataPrep), isBackANormalWord=checkNormalWord(sentence, dataPrep);
		if( isFrontANormalWord ) {
			String input = Flashcard.withSeparator(switchCapitalisationOfFirstCharacterInString(flashcard), sentence);//Switches the capilitlisation of the first letter of the front of the flashcard
            steps.add(new DataStep(dataPrep.stringToDoubleArray(input), output, input, outputText));
		}
		if( isBackANormalWord ) {
			String input = Flashcard.withSeparator(flashcard, switchCapitalisationOfFirstCharacterInString(sentence));
			steps.add(new DataStep(dataPrep.stringToDoubleArray(input), output, input, outputText));
		}
		if( isFrontANormalWord && isBackANormalWord) {
			String input = Flashcard.withSeparator(switchCapitalisationOfFirstCharacterInString(flashcard), switchCapitalisationOfFirstCharacterInString(sentence));
			steps.add(new DataStep(dataPrep.stringToDoubleArray(input), output, input, outputText));
		}
		return steps;
	}
	
	private static boolean checkNormalWord(String s, DataProcessing dataProcessing) {
		if(s.length()==1) {
			return false;//Almost all single characters are not words and this assumes they all are
		}else {
            char firstCharacter = s.charAt(0);
            if (Character.isLetter(firstCharacter) && Character.isLowerCase(s.charAt(1))){//Normal words fit into this pattern (examples that wouldn't would be acronyms such as GCSE)
                if (Character.isLowerCase(firstCharacter)){
                    return dataProcessing.getPhrases().contains(Character.toString(Character.toUpperCase(firstCharacter)));//Makes sure that data processing can handle the upper case of this character
                }
                if (Character.isUpperCase(firstCharacter)){
                    return dataProcessing.getPhrases().contains(Character.toString(Character.toLowerCase(firstCharacter)));//Makes sure that data processing can handle the lower case of this character
                }
                return false;//If the first character is a letter but is neither upper case or lower case (such as with Chinese)
            }else {
                return false;
            }

		}
	}
	
	private static String switchCapitalisationOfFirstCharacterInString(String s) {
        String newFirstLetter;
		char oldFirstLetter = s.charAt(0) ;
		if( Character.isLowerCase( oldFirstLetter ) ) {
			newFirstLetter = Character.toString(Character.toUpperCase(oldFirstLetter));
		}else {
			newFirstLetter =  Character.toString(Character.toLowerCase(oldFirstLetter));
        }
		return newFirstLetter + s.substring(1);//Appends the switched character to the rest of the string
	}


    private static String automaticallyTranslate(String flashcardFront, String flashcardBack) {
        String front = flashcardFront.trim();
        if(front.contains("(") && front.contains(")")) {
            if ( front.substring(front.lastIndexOf("(")+1, front.lastIndexOf(")")).matches("[0-9]+") ){//Removes any numbers in brackets because they normally just represent how many points should be made
                front = front.substring(0,front.length()-3).trim();

            }
        }
        if (front.charAt(front.length()-1)=='?'){
            front = front.substring(0,front.length()-1);//Removes a question mark at the end as some include it and some don't so this way standardises it
        }

        String back = Character.toLowerCase(flashcardBack.charAt(0)) +flashcardBack.trim().substring(1);
        String frontToTest = front.toLowerCase();//Tests with lower case as matching the phrases such not be case sensitive


        if(frontToTest.contains("...")){
            if ( frontToTest.substring( frontToTest.length()-3,frontToTest.length() ).equals("...") ){//If the front of the flashcard ends in an ellipsis then it is assumed that that the back is the continuation from the front
                return front.substring(0,frontToTest.length()-3)+" "+flashcardBack;
            }else{
                return front.substring(0,front.indexOf("..."))+" "+back+" "+front.substring(front.indexOf("...")+3);//If the ellipsis is part way through the front of the flashcard then the back should be inserted where the ellipsis is
            }
        }

		if(!back.contains(" ")) {//i.e. if the back is just one word
			if(!front.contains(" "))  {//i.e. if the fron is just one word
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
				//Checks whether the front is one word with a description of the words gender
				if(!afterSpace.contains(" ")) {
					if( beforeSpace.equals("el")||beforeSpace.equals("le")||beforeSpace.equals("la")||beforeSpace.equals("die")||beforeSpace.equals("der")||beforeSpace.equals("das") ) {//Words for "the" in different languages, often used to show the gender of the word
						return "\"" + front + "\" means \"the " + back + "\"";
					}
					
				}
			}
		}
        
        if(!frontToTest.contains(" ")){
            return front+" means "+back;
        } else if (Utilities.countOfCharacterInString(' ', frontToTest) == 1) {
            return front+" means "+back;
        }//if the fron tis just one word or two, the it assumes that the back means the front
        
        if(back.charAt(0)=='=') {
        	return front+" "+back;//If the back starts with an equals size then it concatenates the front and back
        }

        
        
        if(frontToTest.length()>19) {
            if (frontToTest.substring(0,19).equals("give advantages of ")){//If the front starts with this phrases
                return "Advantages of "+Character.toLowerCase(front.charAt(19))+front.substring(20)+" are that "+back;//Standard format of answer for question of this sort
            }
        }

        if(frontToTest.length()>18) {
            if (frontToTest.substring(0,17).equals("what happens when")){
                return "When "+Character.toLowerCase(front.charAt(18))+front.substring(19)+", "+back;//Standard format of answer for question of this sort
            }
        }

        if(frontToTest.length()>17) {
            if (frontToTest.substring(0,17).equals("give examples of ")){
                return "Examples of "+Character.toLowerCase(front.charAt(17))+front.substring(18)+" are "+back;//Standard format of answer for question of this sort
            }
            if (frontToTest.substring(0,17).equals("what happened at ")){
                return "At "+Character.toLowerCase(front.charAt(17))+front.substring(18)+", "+back;//Standard format of answer for question of this sort
            }
        }
        


        if(frontToTest.length()>16) {
            if (frontToTest.substring(0,16).equals("what happens at ")){
                return "At "+Character.toLowerCase(front.charAt(16))+front.substring(17)+", "+back;//Standard format of answer for question of this sort
            }

            if(frontToTest.substring(frontToTest.length()-16).equals("by which factors") ){
                return "The factors affecting "+Character.toLowerCase(front.charAt(0))+front.substring(1,frontToTest.length()-16)+"are "+back;//Standard format of answer for question of this sort
            }
        }

        if(frontToTest.length()>14) {
            if (frontToTest.substring(0,13).equals("how would you")){
                return "You would "+Character.toLowerCase(front.charAt(14))+front.substring(15)+" "+back;//Standard format of answer for question of this sort
            }
            if (frontToTest.substring(0,13).equals("what happens ")){
                return Character.toUpperCase(front.charAt(14))+front.substring(15)+", "+back;//Standard format of answer for question of this sort
            }
        }
        
        if(frontToTest.length()>13) {
            if (frontToTest.substring(0,12).equals("explain why ")){
                return Character.toUpperCase(front.charAt(13))+front.substring(14)+" because "+back;//Standard format of answer for question of this sort
            }
        }

        if(frontToTest.length()>11) {
            if(frontToTest.substring(frontToTest.length()-11).equals("explain why") ){
                return front.substring(0,frontToTest.length()-11)+"because "+back;//Standard format of answer for question of this sort
            }
            if(frontToTest.substring(frontToTest.length()-11).equals("is given by") ){
                return front+" "+back;//Standard format of answer for question of this sort
            }
        }

        if(frontToTest.length()>10) {
            String start = frontToTest.substring(0,10);
            if (start.equals("how would ")){//If the  front begins with this phrase
                return front.substring(10)+" by "+back;//Standard format of answer for question of this sort
            }

            if(start.equals("what does ")){//If the front begins with this phrase
                if(frontToTest.substring(frontToTest.length()-9,frontToTest.length()).equals("stand for") ){//If the front also ends with this phrase
                    return Character.toUpperCase(front.charAt(10))+front.substring(11,frontToTest.length()-10)+" stands for "+back;
                }

                if(frontToTest.substring(frontToTest.length()-9,frontToTest.length()).equals("represent") ){//If the front also ends with this phrase
                    return Character.toUpperCase(front.charAt(10))+front.substring(11,frontToTest.length()-10)+" represents "+back;
                }

                if(frontToTest.substring(frontToTest.length()-4,frontToTest.length()).equals("mean") ){//If the front also ends with this phrase
                    return Character.toUpperCase(front.charAt(10))+front.substring(11,frontToTest.length()-5)+" means "+back;
                }

                if(frontToTest.substring(frontToTest.length()-7,frontToTest.length()).equals("contain") ){//If the front also ends with this phrase
                    return Character.toUpperCase(front.charAt(10))+front.substring(11,frontToTest.length()-5)+" contains "+back;
                }

            }
            
            if (start.equals("what were ")){
                return Character.toUpperCase(front.charAt(10))+front.substring(11)+" were "+back;//Standard format of answer for question of this sort
            }

        }
        
        
        if(frontToTest.length()>9) {
            String start = frontToTest.substring(0,9);

            if(start.equals("how does ")){
                return Character.toUpperCase(front.charAt(9))+front.substring(10)+"s by "+back;//Standard format of answer for question of this sort
            }

            if (start.equals("what are ") ){
                return Character.toUpperCase(front.charAt(9))+front.substring(10)+" are "+back;//Standard format of answer for question of this sort
            }

            if (start.equals("describe ")){
                return Character.toUpperCase(front.charAt(9))+front.substring(10)+": "+back;//Standard format of answer for question of this sort
            }

            if (start.equals("how does ")){
                return Character.toUpperCase(front.charAt(9))+front.substring(10)+" by "+back;//Standard format of answer for question of this sort
            }

            if (start.equals("where do ")){
                return Character.toUpperCase(front.charAt(9))+front.substring(10)+" in "+back;//Standard format of answer for question of this sort
            }
            if (start.equals("what was ")){
                return Character.toUpperCase(front.charAt(9))+front.substring(10)+" was "+back;//Standard format of answer for question of this sort
            }
        }
        
        
        if(frontToTest.length()>8) {
            String start = frontToTest.substring(0,8);

            if (start.equals("what is ") ){
                return Character.toUpperCase(front.charAt(8))+front.substring(9)+" is "+back;//Standard format of answer for question of this sort
            }

            if(start.equals("what do ")){
                if(frontToTest.substring(frontToTest.length()-3).equals("for") ){//If the front also ends with this phrase
                    return Character.toUpperCase(front.charAt(8))+front.substring(9,frontToTest.length()-4)+" for "+back;
                }
            }

            if (start.equals("how can ")){
                return Character.toUpperCase(front.charAt(8))+front.substring(9)+" by "+back;//Standard format of answer for question of this sort
            }

            if (start.equals("when do ")){
                return Character.toUpperCase(front.charAt(8))+front.substring(9)+" when "+back;//Standard format of answer for question of this sort
            }

            if(start.equals("why are ")){
                return Character.toUpperCase(front.charAt(8))+front.substring(9,front.lastIndexOf(" "))+" are "+front.substring(front.lastIndexOf(" "))+" because "+back;//Standard format of answer for question of this sort
            }
            
            if(start.equals("Def. of ")) {
            	return Character.toUpperCase(front.charAt(8))+front.substring(9)+" means "+back;//Standard format of answer for question of this sort
            }
            
        }
        
        if(frontToTest.length()>7) {
            String start = frontToTest.substring(0,7);

            if(start.equals("define ")){
                return Character.toUpperCase(front.charAt(7))+front.substring(8)+": "+back;//Standard format of answer for question of this sort
            }

            if (start.equals("how do ")){
                return Character.toUpperCase(front.charAt(7))+front.substring(8)+" by "+back;//Standard format of answer for question of this sort
            }

            if(start.equals("why is ")){
                return Character.toUpperCase(front.charAt(7))+front.substring(8,front.lastIndexOf(" "))+" is "+front.substring(front.lastIndexOf(" "))+" because "+back;//Standard format of answer for question of this sort
            }
            
            if(start.equals("why do ")) {
                return Character.toUpperCase(front.charAt(7))+front.substring(8,front.lastIndexOf(" "))+" because "+back;//Standard format of answer for question of this sort
            }
            
        }

        if(frontToTest.length()>6) {
            if (frontToTest.substring(0,6).equals("state ")){
                return Character.toUpperCase(front.charAt(6))+front.substring(7)+": "+back;//Standard format of answer for question of this sort
            }
        }

        if(frontToTest.length()>5) {
            String start = frontToTest.substring(0,5);
            if (start.equals("give ")){
                return Character.toUpperCase(front.charAt(5))+front.substring(6)+": "+back;//Standard format of answer for question of this sort
            }

            if (start.equals("name ")){
                return Character.toUpperCase(front.charAt(5))+front.substring(6)+": "+back;//Standard format of answer for question of this sort
            }

            if (start.equals("list ")){
                return Character.toUpperCase(front.charAt(5))+front.substring(6)+": "+back;//Standard format of answer for question of this sort
            }

            if (start.equals("what ") && front.contains(" is ")){
                return Character.toUpperCase(back.charAt(0))+back.substring(1)+" is the "+front.substring(5,front.indexOf(" is "))+" that is "+front.substring(front.indexOf(" is "));//Standard format of answer for question of this sort
            }
        }
        
        if(frontToTest.length()>1) {
        	if(frontToTest.substring(frontToTest.length()-1).equals("=")) {//If front ends with an equals sign
        		return front+" "+back;//Standard format of answer for question of this sort
        	}
        }
        
        return "";//An impossible result used to show that this flashcard doesn't match any of the known formats
	}
	
	static void automaticallyTranslateRawFlashcards(ArrayList<Flashcard> inputs, String file) {
		for(Flashcard card: inputs) {
			String sentence;
			try {
				sentence = automaticallyTranslate(card.getFlashcardFront(),card.getFlashcardBack());

			}catch(IndexOutOfBoundsException exception) {
				System.out.println(exception.getMessage());//To help find the cause of the issue
				continue;//Skips to the next flashcard to avoid any further issues
			}
			
			if(sentence.equals("")) {//If the flashcard didn't match  any known formats
				
				try {
					sentence = automaticallyTranslate(card.getFlashcardBack(),card.getFlashcardFront());//Tries to generate a sentence if the front and back swapped because  the issue may be that the flashcard is the wrong way around

				}catch(IndexOutOfBoundsException exception) {
					System.out.println(exception.getMessage());//To help find the cause of the issue
					continue;//Skips to the next flashcard to avoid any further issues
				}
				
				if(!sentence.equals("")) {
					String line = Flashcard.withSeparator(card.getFlashcardBack(), card.getFlashcardFront(), sentence);
					DataExport.appendToTextFile(line, file);//Appends the translated flashcard to the data set included in the text file
				}

			}else {
				String line = Flashcard.withSeparator(card.getFlashcardFront(), card.getFlashcardBack(), sentence);
				DataExport.appendToTextFile(line, file);//Appends the translated flashcard to the data set included in the text file
			}
		}
	}
	
	
	
	 static void deleteDuplicateFlashcards(String filePath) {
		HashSet<String> uniqueFlashcards = DataImport.getUniqueLinesFromTextFile(filePath);//gets the unique lines  (case sensitive)

        ArrayList<String> flashcards = new ArrayList<>(uniqueFlashcards);
		
		for(int i =0;i<flashcards.size();i++) {
			for(int j =i+1;j<flashcards.size();j++) {
				if(flashcards.get(i).toLowerCase().equals(flashcards.get(j).toLowerCase())) {
                    uniqueFlashcards.remove(flashcards.get(j));//makes it so that the uniqueness is not case sensitive
				}
			}
		}

        DataExport.overwriteTextFile(uniqueFlashcards, filePath);///essentially removes the duplicate flashcards from the text file
    }

    static void removeTranslatedCards(String rawFlashcardsFilePath, String translatedFlashcardsFilePath) {
		HashSet<String> rawFlashcards = DataImport.getUniqueLinesFromTextFile(rawFlashcardsFilePath);
		HashSet<String> translatedFlashcards = DataImport.getUniqueLinesFromTextFile(translatedFlashcardsFilePath);
		HashSet<String> translatedFlashcardsNoTranslation = new HashSet<>();
		for(String flashcard:translatedFlashcards) {
			translatedFlashcardsNoTranslation.add(flashcard.substring(0, flashcard.indexOf(Flashcard.CARD_SENTENCE_SEPARATOR)));//Gets the flashcard part of the datestep without getting the sentece part
		}
		
		HashSet<String> output = new HashSet<>();
		
		for(String rawFlashcard : rawFlashcards) {
			
			if(!translatedFlashcardsNoTranslation.contains(rawFlashcard)) {
				output.add(rawFlashcard);
			}
		}
		
		DataExport.overwriteTextFile(output, rawFlashcardsFilePath);
		
	}
	
	 static void removeFlashcardsWithUnknownCharacters(String file) {
		ArrayList<String> lines = DataImport.getLinesFromTextFile(file);
		StringBuilder stringBuilder = new StringBuilder();
		for(String line: lines) {
			if(!line.contains("�")) {
				stringBuilder.append(line).append("\n");
			}
		}
		
		DataExport.overwriteTextFile(stringBuilder, file);
		
		
	}
}
