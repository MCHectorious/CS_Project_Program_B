package characterOperations;

import generalUtilities.Utilities;

import java.util.ArrayList;

public class SubstitutionSetOperation implements CharacterOperation{


    private ArrayList<Character> inputs;
    private ArrayList<Character> outputs;

    public SubstitutionSetOperation(ArrayList<Character> i, ArrayList<Character> o) {
        inputs =i;
        outputs=o;
    }

    @Override
    public void convertCharacter(char c, StringBuilder builder) {
        for(int i=0;i<inputs.size();i++) {
            if (c == inputs.get(i)) {
                builder.append(outputs.get(i));
                break;
            }
        }
    }


    @Override
    public ArrayList<Character> getInputs() {
        return inputs;
    }

    @Override
    public ArrayList<Character> getOutputs() {
        return outputs;
    }

    @Override
    public String description() {
        return "Substituion Set: " + Utilities.arrayToString(inputs.toArray(new Character[0])) + " for " + Utilities.arrayToString(outputs.toArray(new Character[0]));
    }

    @Override
    public void description(StringBuilder stringBuilder) {
        stringBuilder.append("Substituion Set: ").append(Utilities.arrayToString(inputs.toArray(new Character[0]))).append(" for ").append(Utilities.arrayToString(outputs.toArray(new Character[0])));
    }
}
