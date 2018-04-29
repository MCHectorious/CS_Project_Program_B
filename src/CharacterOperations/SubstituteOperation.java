package CharacterOperations;

import java.util.ArrayList;

public class SubstituteOperation implements CharacterOperation {

    private char inputCharacter;//The character that will be substituted
    private char outputCharacter;//The substitute character

    public SubstituteOperation(char input, char output) {
        inputCharacter = input;
        outputCharacter = output;
    }

    @Override
    public void convertCharacter(char character, StringBuilder outputTextBuilder) {
        if(character == inputCharacter) {
            outputTextBuilder.append(outputCharacter);//Appends the substitute character to the output
        }else {
            outputTextBuilder.append(character);//If the character isn't the character to be substituted it copies
        }
    }

    @Override
    public ArrayList<Character> getRelevantInputs() {
        ArrayList<Character> relevantInputs = new ArrayList<>();
        relevantInputs.add(inputCharacter);//The input character is the only relevant input
        return relevantInputs;
    }

    @Override
    public ArrayList<Character> getRelevantOutputs() {
        ArrayList<Character> relevantOutputs = new ArrayList<>();
        relevantOutputs.add(outputCharacter);//This is the only relevant output
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
