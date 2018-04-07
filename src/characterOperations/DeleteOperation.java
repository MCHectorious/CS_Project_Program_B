package characterOperations;

import java.util.ArrayList;

public class DeleteOperation implements CharacterOperation{


    @Override
    public void convertCharacter(char c, StringBuilder builder) {

    }

    @Override
    public ArrayList<Character> getInputs() {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Character> getOutputs() {
        return null;
    }

    @Override
    public String description() {
        return "Delete";
    }
}
