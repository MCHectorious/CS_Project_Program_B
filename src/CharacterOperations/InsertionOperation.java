package CharacterOperations;

import java.util.ArrayList;

public class InsertionOperation implements CharacterOperation {

    private char characterToInsert;//The character that will be inserted

    public InsertionOperation(char charValue) {
        characterToInsert = charValue;
    }

    @Override
    public void convertCharacter(char character, StringBuilder outputTextBuilder) {
        outputTextBuilder.append(characterToInsert);//Inserts the character to the output

    }

    @Override
    public ArrayList<Character> getRelevantInputs() {
        return null;//This has no relevant inputs
    }

    @Override
    public ArrayList<Character> getRelevantOutputs() {
        ArrayList<Character> relevantOutputs = new ArrayList<>();
        relevantOutputs.add(characterToInsert);//The only output is the character which is inserted
        return relevantOutputs;
    }


    @Override
    public String provideDescription() {
        return "Insert " + characterToInsert;
    }
}
