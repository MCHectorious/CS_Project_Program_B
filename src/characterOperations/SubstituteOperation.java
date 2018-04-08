package characterOperations;

import java.util.ArrayList;

public class SubstituteOperation implements CharacterOperation {

    private char inputCharacter;
    private char outputCharacter;

    public SubstituteOperation(char input, char output) {
        inputCharacter = input;
        outputCharacter = output;
    }

    @Override
    public void convertCharacter(char character, StringBuilder outputTextBuilder) {
        if(character == inputCharacter) {
            outputTextBuilder.append(outputCharacter);
        }else {
            outputTextBuilder.append(character);
        }
    }

    @Override
    public ArrayList<Character> getRelevantInputs() {
        ArrayList<Character> relevantInputs = new ArrayList<>();
        relevantInputs.add(inputCharacter);
        return relevantInputs;
    }

    @Override
    public ArrayList<Character> getRelevantOutputs() {
        ArrayList<Character> relevantOutputs = new ArrayList<>();
        relevantOutputs.add(outputCharacter);
        return relevantOutputs;
    }

    @Override
    public String provideDescription() {
        return "Substitute " + inputCharacter + " for " + outputCharacter;
    }

    @Override
    public void provideDescription(StringBuilder stringBuilder) {
        stringBuilder.append("Substitute ").append(inputCharacter).append(" for ").append(outputCharacter);
    }
}
