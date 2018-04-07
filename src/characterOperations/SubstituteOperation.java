package characterOperations;

import java.util.ArrayList;

public class SubstituteOperation implements CharacterOperation {

    private char input;
    private char output;

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

    @Override
    public String description() {
        return "Substitute " + input + " for " + output;
    }

    @Override
    public void description(StringBuilder stringBuilder) {
        stringBuilder.append("Substitute ").append(input).append(" for ").append(output);
    }
}
