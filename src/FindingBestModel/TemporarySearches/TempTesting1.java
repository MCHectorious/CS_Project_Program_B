package FindingBestModel.TemporarySearches;

import DataStructures.FlashcardDataSet;
import FileManipulation.DataExport;
import GeneralUtilities.CustomRandom;
import Models.AdvancedCopyingModel;
import Models.Model;
import Training.DataProcessing;
import Training.ModelTrainer;

public class TempTesting1 {

    public static void main(String[] args){

        //System.out.println("Test");

        //DataExport.overwriteTextFile(1.01, "Models/TempModel1.txt");
        //System.out.println(DataImport.getDoubleFromFile("Models/TempModel1.txt"));

        CustomRandom random = new CustomRandom();

        FlashcardDataSet data = new FlashcardDataSet("DataSets/TranslatedFlashcards.txt", random);

        String savePath = "Models/TempModel1.txt";

        double total,average;
        int attempts=5;

        int maximumTrainingEpochs = 1000;
        int displayReportPeriod = 2000;
        int showEpochPeriod = 5;
        int checkMinimumPeriod = 10;

        for(int size=0;size<5;size++) {
            total = 0.0;
            for(int i=0;i<attempts;i++) {
                Model model = new AdvancedCopyingModel(size, DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR);
                total += (new ModelTrainer("Models/TempLoss1.txt")).train(maximumTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, random);
            }
            average = total/(double) attempts;
            String tempString = "Advanced Copying with size of "+size+":\t"+average;
            System.out.println(tempString);
            DataExport.appendToTextFile(tempString, "Models/ParameterTuning.txt");
        }


    }


}
