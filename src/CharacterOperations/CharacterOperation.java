package CharacterOperations;

import GeneralUtilities.HasDescription;

import java.util.ArrayList;

public interface CharacterOperation extends HasDescription {
    void convertCharacter(char character, StringBuilder outputTextBuilder);//Decides what to append to the StringBuilder based on the character

    ArrayList<Character> getRelevantInputs();//Returns any known inputs
    ArrayList<Character> getRelevantOutputs();//Returns any known outputs
}
