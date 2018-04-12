package generalUtilities;

import models.Model;

import java.util.ArrayList;

public class Subsets {

    public static ArrayList<Model[]> getSubsetsGreaterThanSize1(ArrayList<Model> models){
        ArrayList<Model[]> output = new ArrayList<>();

        int modelSize = models.size();

        for (int i = 1; i < (1<<modelSize); i++) {//Used to get get the numbers between  1 and 2^model size (0 is not included becauseI don't want the empty set)
            ArrayList<Model> subset = new ArrayList<>();
            for (int j = 0; j < modelSize; j++) {
                if ((i & (1 << j)) > 0) {//I.e. checks if the model is in the subset by there being a 1 in the binary representation of the number (i)
                    subset.add(models.get(j));//Adds model to the subset
                }
            }
            if(subset.size()>=2) {//Won't get subsets with just 1 model in them
                output.add(subset.toArray(new Model[0]));//Adds the subset to the output
            }

        }

        return output;
    }

}
