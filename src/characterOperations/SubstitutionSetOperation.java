package characterOperations;

import generalUtilities.Utilities;

import java.util.ArrayList;

public class SubstitutionSetOperation implements CharacterOperation{


    private ArrayList<Character> inputs;
    private ArrayList<Character> outputs;

    public SubstitutionSetOperation(ArrayList<Character> inputs, ArrayList<Character> outputs) {
        this.inputs =inputs;
        this.outputs =outputs;
    }

    @Override
    public void convertCharacter(char character, StringBuilder outputTextBuilder) {
        for(int i=0;i<inputs.size();i++) {
            if (character == inputs.get(i)) {
                outputTextBuilder.append(outputs.get(i));
                break;
            }
        }
    }


    @Override
    public ArrayList<Character> getRelevantInputs() {
        return inputs;
    }

    @Override
    public ArrayList<Character> getRelevantOutputs() {
        return outputs;
    }

    @Override
    public String provideDescription() {
        return "Substitution Set: " + Utilities.arrayToString(inputs.toArray(new Character[0])) + " for " + Utilities.arrayToString(outputs.toArray(new Character[0]));
    }

    @Override
    public void provideDescription(StringBuilder stringBuilder) {
        stringBuilder.append("Substitution Set: ").append(Utilities.arrayToString(inputs.toArray(new Character[0]))).append(" for ").append(Utilities.arrayToString(outputs.toArray(new Character[0])));
    }
}
