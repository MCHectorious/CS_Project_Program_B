package characterOperations;

import java.util.ArrayList;

public class CopyOperation implements CharacterOperation{


    @Override
    public void convertCharacter(char c, StringBuilder builder) {
        builder.append(c);
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
        return "Copy";
    }


}
