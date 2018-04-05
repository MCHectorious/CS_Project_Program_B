package characterOperations;

import java.util.ArrayList;

public class InsertionOperation implements CharacterOperation {

    final public static int ID = 2;

    char character;

    public InsertionOperation(char charValue) {
        character = charValue;
    }

    @Override
    public void convertCharacter(char c, StringBuilder builder) {
        builder.append(character);

    }

    @Override
    public void toString(StringBuilder builder) {
        builder.append("Insert ").append(character);
    }

    @Override
    public int getTypeID() {
        return ID;
    }

    @Override
    public ArrayList<Character> getInputs() {
        return new ArrayList<Character>();
    }

    @Override
    public ArrayList<Character> getOutputs() {
        ArrayList<Character> output = new ArrayList<>();
        output.add(character);
        return output;
    }



}
