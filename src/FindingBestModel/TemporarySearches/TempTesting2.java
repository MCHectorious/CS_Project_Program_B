package FindingBestModel.TemporarySearches;

import DataStructures.FlashcardDataSet;
import FileManipulation.DataExport;
import GeneralUtilities.CustomRandom;
import Models.AverageModel;
import Models.Model;
import Training.ModelTrainer;

public class TempTesting2 {

    public static void main(String[] args){

        //System.out.println("Test");

        //DataExport.overwriteTextFile(1.01, "Models/TempModel1.txt");
        //System.out.println(DataImport.getDoubleFromFile("Models/TempModel1.txt"));

        CustomRandom random = new CustomRandom();

        FlashcardDataSet data = new FlashcardDataSet("DataSets/TranslatedFlashcards.txt", random);

        String savePath = "Models/TempModel2.txt";

        double total,average;
        int attempts = 2;

        int maximumTrainingEpochs = 100;
        int displayReportPeriod = 200;
        int showEpochPeriod = 5;
        int checkMinimumPeriod = 20;

        total = 0.0;
        for(int i=0;i<attempts;i++) {
            Model model = new AverageModel(data.getTrainingDataSteps());
            total += (new ModelTrainer("Models/TempLoss2.txt")).train(maximumTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, random);
        }
        average = total/(double) attempts;
        String evaluationOfModel ="Average Model:\t"+average;
        System.out.println(evaluationOfModel);
        DataExport.appendToTextFile(evaluationOfModel, "Models/ParameterTuning.txt");
        System.gc();

    }


}
