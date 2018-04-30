package FindingBestModel.BestModel.SecondSplittingSubModel;

public class SecondSplittingModel {

    public static void execute(double[] inputArray, String inputText, double[] output){
        double testingValue = inputArray[60];

        if (testingValue>(0.25) && testingValue< (0.75)){
            FeedForwardLayerInSecondSplit.execute(inputArray, output);
        }else{
            LinearLayerNotInSecondSplit.execute(inputArray,output);
        }
    }

}
