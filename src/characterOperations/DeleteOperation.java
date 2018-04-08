package characterOperations;

import java.util.ArrayList;

public class DeleteOperation implements CharacterOperation{


    @Override
    public void convertCharacter(char character, StringBuilder outputTextBuilder) {

    }

    @Override
    public ArrayList<Character> getRelevantInputs() {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Character> getRelevantOutputs() {
        return null;
    }

    @Override
    public String provideDescription() {
        return "Delete";
    }
}
