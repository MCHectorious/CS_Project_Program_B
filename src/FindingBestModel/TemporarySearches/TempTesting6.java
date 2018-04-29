package FindingBestModel.TemporarySearches;

import DataStructures.FlashcardDataSet;
import FileManipulation.DataExport;
import GeneralUtilities.CustomRandom;
import Models.LinearLayer;
import Models.Model;
import Training.DataProcessing;
import Training.ModelTrainer;

public class TempTesting6 {

    public static void main(String[] args){

        //System.out.println("Test");

        //DataExport.overwriteTextFile(1.01, "Models/TempModel1.txt");
        //System.out.println(DataImport.getDoubleFromFile("Models/TempModel1.txt"));

        CustomRandom random = new CustomRandom();

        FlashcardDataSet data = new FlashcardDataSet("DataSets/TranslatedFlashcards.txt", random);

        String savePath = "Models/TempModel6.txt";

        double total,average;
        int attempts = 10;

        int maximumTrainingEpochs = 100000000;
        int displayReportPeriod = 2000000000;
        int showEpochPeriod = 1000;
        int checkMinimumPeriod = 50;

        total = 0.0;
        for(int i=0;i<attempts;i++) {
            Model model = new LinearLayer(DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR, random);
            total += (new ModelTrainer("Models/TempLoss6.txt")).train(maximumTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, random);
        }
        average = total/(double) attempts;
        String tempString = "Linear Layer Model:\t"+average;
        System.out.println(tempString);
        DataExport.appendToTextFile(tempString, "Models/ParameterTuning.txt");
        System.gc();

    }


}
