package FindingBestModel.TemporarySearches;

import DataStructures.FlashcardDataSet;
import FileManipulation.DataExport;
import GeneralUtilities.CustomRandom;
import Models.FeedForwardLayer;
import Models.Model;
import NonlinearityFunctions.RoughTanhUnit;
import Training.DataProcessing;
import Training.ModelTrainer;

public class TempTesting5 {

    public static void main(String[] args){

        //System.out.println("Test");

        //DataExport.overwriteTextFile(1.01, "Models/TempModel1.txt");
        //System.out.println(DataImport.getDoubleFromFile("Models/TempModel1.txt"));

        CustomRandom random = new CustomRandom();

        FlashcardDataSet data = new FlashcardDataSet("DataSets/TranslatedFlashcards.txt", random);

        String savePath = "Models/TempModel5.txt";

        double total,average;
        int attempts = 10;

        int maximumTrainingEpochs = 100000000;
        int displayReportPeriod = 2000000000;
        int showEpochPeriod = 1000;
        int checkMinimumPeriod = 50;

        total = 0.0;
        for(int i=0;i<attempts;i++) {
            Model model = new FeedForwardLayer(DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, new RoughTanhUnit(), random);
            total += (new ModelTrainer("Models/TempLoss5.txt")).train(maximumTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, random);
        }
        average = total/(double) attempts;
        String tempString = "FeedForward Layer Model:\t"+average;
        System.out.println(tempString);
        DataExport.appendToTextFile(tempString, "Models/ParameterTuning.txt");

        System.gc();

    }


}
