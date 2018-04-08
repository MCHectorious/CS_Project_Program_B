package characterOperations;

import java.util.ArrayList;

public class InsertionOperation implements CharacterOperation {

    private char characterToInsert;

    public InsertionOperation(char charValue) {
        characterToInsert = charValue;
    }

    @Override
    public void convertCharacter(char character, StringBuilder outputTextBuilder) {
        outputTextBuilder.append(characterToInsert);

    }

    @Override
    public ArrayList<Character> getRelevantInputs() {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<Character> getRelevantOutputs() {
        ArrayList<Character> relevantOutputs = new ArrayList<>();
        relevantOutputs.add(characterToInsert);
        return relevantOutputs;
    }


    @Override
    public String provideDescription() {
        return "Insert " + characterToInsert;
    }
}
