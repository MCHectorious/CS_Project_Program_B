package characterOperations;

import java.util.ArrayList;

public class SubstituteOperation implements CharacterOperation {

    final public static int ID = 3;

    char input;
    char output;

    public SubstituteOperation(char i, char o) {
        input = i;
        output = o;
    }

    @Override
    public void convertCharacter(char c, StringBuilder builder) {
        if(c==input) {
            builder.append(output);
        }else {
            builder.append(c);
        }
    }

    @Override
    public void toString(StringBuilder builder) {
        builder.append("Substitute ").append(input).append(" for ").append(output).append("\t");
    }

    @Override
    public int getTypeID() {
        return ID;
    }

    @Override
    public ArrayList<Character> getInputs() {
        ArrayList<Character> output = new ArrayList<>();
        output.add(input);
        return output;
    }

    @Override
    public ArrayList<Character> getOutputs() {
        ArrayList<Character> outputs = new ArrayList<>();
        outputs.add(output);
        return outputs;
    }

}
