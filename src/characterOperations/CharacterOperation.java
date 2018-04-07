package characterOperations;

import generalUtilities.HasDescription;

import java.util.ArrayList;

public interface CharacterOperation extends HasDescription {
    void convertCharacter(char c, StringBuilder builder);

    ArrayList<Character> getInputs();
    ArrayList<Character> getOutputs();
}
