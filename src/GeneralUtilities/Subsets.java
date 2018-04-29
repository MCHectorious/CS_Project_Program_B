package GeneralUtilities;

import Models.BasicCopyingModel;
import Models.Model;

import java.util.ArrayList;

public class Subsets {

    public static void main(String[] args){
        ArrayList<Model> models = new ArrayList<>();
        models.add(new BasicCopyingModel());
        models.add(new BasicCopyingModel());
        models.add(new BasicCopyingModel());
        models.add(new BasicCopyingModel());
        models.add(new BasicCopyingModel());
        ArrayList<Model[]> subsets = getSubsetsGreaterThanSize1(models);
        for (Model[] subset:subsets) {
            System.out.println(Utilities.arrayToString(subset));
        }
    }

    public static ArrayList<Model[]> getSubsetsGreaterThanSize1(ArrayList<Model> models){
        ArrayList<Model[]> output = new ArrayList<>();

        int modelSize = models.size();

        for (int i = 2; i < (1<<modelSize); i++) {//Used to get get the numbers between  1 and 2^model size (0 is not included becauseI don't want the empty set)
            ArrayList<Model> subset = new ArrayList<>();
            if( (i & i-1) != 0){//Won't get subsets with just 1 model in them
                for (int j = 0; j < modelSize; j++) {
                    if ( (i & (1 << j)) > 0 ) {//I.e. checks if the model is in the subset by there being a 1 in the binary representation of the number (i)
                        subset.add(models.get(j));//Adds model to the subset
                    }
                }
                output.add(subset.toArray(new Model[0]));//Adds the subset to the output

            }

        }

        return output;
    }

}
