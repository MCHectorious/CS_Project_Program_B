package characterOperations;

import generalUtilities.Util;

import java.util.ArrayList;

public class SubstitutionSetOperation implements CharacterOperation{



    final public static int ID = 4;

    ArrayList<Character> inputs = new ArrayList<>();
    ArrayList<Character> outputs = new ArrayList<>();

    public SubstitutionSetOperation(ArrayList<Character> i, ArrayList<Character> o) {
        inputs =i;
        outputs=o;
    }

    @Override
    public void convertCharacter(char c, StringBuilder builder) {
        for(int i=0;i<inputs.size();i++) {
            if(c==inputs.get(i).charValue()) {
                builder.append(outputs.get(i));
                break;
            }
        }
    }

    @Override
    public void toString(StringBuilder builder) {
        builder.append("Substituion Set: " + Util.arrayToString(inputs.toArray(new Character[0])) +" for " + Util.arrayToString(outputs.toArray(new Character[0])));
    }

    @Override
    public int getTypeID() {
        return ID;
    }

    public boolean inputContains(char c) {
        return inputs.contains(c);
    }

    @Override
    public ArrayList<Character> getInputs() {
        return inputs;
    }

    @Override
    public ArrayList<Character> getOutputs() {
        return outputs;
    }

}
