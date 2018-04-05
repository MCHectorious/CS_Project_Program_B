package characterOperations;

import java.util.ArrayList;

public class CopyOperation implements CharacterOperation{

    final public static int ID = 0;

    @Override
    public void convertCharacter(char c, StringBuilder builder) {
        builder.append(c);
    }

    @Override
    public void toString(StringBuilder builder) {
        builder.append("Copy\t");
    }

    @Override
    public int getTypeID() {
        return ID;
    }

    @Override
    public ArrayList<Character> getInputs() {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Character> getOutputs() {
        return null;
    }

}
