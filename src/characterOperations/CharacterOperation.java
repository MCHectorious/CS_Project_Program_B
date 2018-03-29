package characterOperations;

import java.util.ArrayList;

public interface CharacterOperation {
    void convertCharacter(char c, StringBuilder builder);

    void toString(StringBuilder builder);

    int getTypeID();


    ArrayList<Character> getInputs();
    ArrayList<Character> getOutputs();
}
