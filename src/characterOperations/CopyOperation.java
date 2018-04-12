package characterOperations;

import java.util.ArrayList;

public class CopyOperation implements CharacterOperation{


    @Override
    public void convertCharacter(char character, StringBuilder outputTextBuilder) {
        outputTextBuilder.append(character);//Copies the character to the string builder
    }

    @Override
    public ArrayList<Character> getRelevantInputs() {
        return null;//This has no relevant inputs
    }

    @Override
    public ArrayList<Character> getRelevantOutputs() {
        return null;//This has no relevant outputs
    }

    @Override
    public String provideDescription() {
        return "Copy";
    }


}
