package characterOperations;

import java.util.ArrayList;

public class DeleteOperation implements CharacterOperation{


    @Override
    public void convertCharacter(char character, StringBuilder outputTextBuilder) {
        //It adds nothing to the string builder, which is essentially deleting the character from the output
    }

    @Override
    public ArrayList<Character> getRelevantInputs() {
        return null;//This has no relevant inputs
    }

    @Override
    public ArrayList<Character> getRelevantOutputs() {
        return null;//This has no relevant outputs
    }

    @Override
    public String provideDescription() {
        return "Delete";
    }
}
