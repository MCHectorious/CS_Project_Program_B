package FindingBestModel.BestModel.FirstSplittingSubModel;

public class FirstSplittingModel {

    public static void execute(double[] inputArray, String inputText, double[] output){
        double testingValue = inputArray[55];

        if (testingValue>(0.25) && testingValue< (0.75)){
            LinearLayerInFirstSplit.execute(inputArray,output);
        }else{
            LinearLayerNotInFirstSplit.execute(inputArray,output);
        }
    }

}
