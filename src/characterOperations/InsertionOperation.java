package characterOperations;

import java.util.ArrayList;

public class InsertionOperation implements CharacterOperation {

    private char character;

    public InsertionOperation(char charValue) {
        character = charValue;
    }

    @Override
    public void convertCharacter(char c, StringBuilder builder) {
        builder.append(character);

    }

    @Override
    public ArrayList<Character> getInputs() {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Character> getOutputs() {
        ArrayList<Character> output = new ArrayList<>();
        output.add(character);
        return output;
    }


    @Override
    public String description() {
        return "Insert " + character;
    }
}
