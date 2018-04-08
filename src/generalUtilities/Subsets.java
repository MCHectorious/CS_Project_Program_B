package generalUtilities;

import models.Model;

import java.util.ArrayList;

public class Subsets {

    public static ArrayList<Model[]> getSubsetsGreaterThanSize1(ArrayList<Model> models){
        ArrayList<Model[]> output = new ArrayList<>();

        int modelSize = models.size();

        for (int i = 1; i < (1<<modelSize); i++)
        {
            ArrayList<Model> subset = new ArrayList<>();
            for (int j = 0; j < modelSize; j++) {
                if ((i & (1 << j)) > 0) {
                    subset.add(models.get(j));
                }
            }
            if(subset.size()>=2) {
                output.add(subset.toArray(new Model[0]));
            }

        }

        return output;
    }

}
