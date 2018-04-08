package characterOperations;

import generalUtilities.HasDescription;

import java.util.ArrayList;

public interface CharacterOperation extends HasDescription {
    void convertCharacter(char character, StringBuilder outputTextBuilder);

    ArrayList<Character> getRelevantInputs();
    ArrayList<Character> getRelevantOutputs();
}
