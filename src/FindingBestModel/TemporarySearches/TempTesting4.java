package FindingBestModel.TemporarySearches;

import DataStructures.FlashcardDataSet;
import FileManipulation.DataExport;
import GeneralUtilities.CustomRandom;
import Models.CharacterManipulationFromStringDistanceModel;
import Models.Model;
import Training.ModelTrainer;

public class TempTesting4 {

    public static void main(String[] args){

        //System.out.println("Test");

        //DataExport.overwriteTextFile(1.01, "Models/TempModel1.txt");
        //System.out.println(DataImport.getDoubleFromFile("Models/TempModel1.txt"));

        CustomRandom random = new CustomRandom();

        FlashcardDataSet data = new FlashcardDataSet("DataSets/TranslatedFlashcards.txt", random);

        String savePath = "Models/TempModel4.txt";

        double total,average;
        int attempts = 2;

        int maximumTrainingEpochs = 100;
        int displayReportPeriod = 200;
        int showEpochPeriod = 5;
        int checkMinimumPeriod = 20;

        total = 0.0;
        for(int i=0;i<attempts;i++) {
            Model model = new CharacterManipulationFromStringDistanceModel(data.getTrainingDataSteps(), data.getDataProcessing(), random);
            total += (new ModelTrainer("Models/TempLoss4.txt")).train(maximumTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, random);
        }
        average = total/(double) attempts;
        String tempString = "Character Manipulation From String Distance Model:\t"+average;
        System.out.println(tempString);
        DataExport.appendToTextFile(tempString, "Models/ParameterTuning.txt");

        System.gc();

    }


}
