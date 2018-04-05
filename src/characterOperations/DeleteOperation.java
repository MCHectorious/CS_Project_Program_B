package characterOperations;

import java.util.ArrayList;

public class DeleteOperation implements CharacterOperation{

    final public static int ID = 1;

    @Override
    public void convertCharacter(char c, StringBuilder builder) {

    }

    @Override
    public void toString(StringBuilder builder) {
        builder.append("Delete\t");
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
        return null;
    }

}
