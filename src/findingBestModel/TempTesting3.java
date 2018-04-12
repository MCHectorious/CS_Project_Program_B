package findingBestModel;

import dataStructures.FlashcardDataSet;
import fileManipulation.DataExport;
import generalUtilities.CustomRandom;
import models.BasicCopyingModel;
import models.Model;
import models.ProportionProbabilityForCharacterModel;
import training.ModelTrainer;

public class TempTesting3 {

    public static void main(String[] args){

        //System.out.println("Test");

        //DataExport.overwriteTextFile(1.01, "Models/TempModel1.txt");
        //System.out.println(DataImport.getDoubleFromFile("Models/TempModel1.txt"));

        CustomRandom random = new CustomRandom();

        FlashcardDataSet data = new FlashcardDataSet("DataSets/TranslatedFlashcards.txt", random);

        String savePath = "Models/TempModel3.txt";

        double total,average;
        int attempts = 2;

        int maximumTrainingEpochs = 100;
        int displayReportPeriod = 200;
        int showEpochPeriod = 5;
        int checkMinimumPeriod = 20;

        for(int size=0;size<8;size++) {
            total = 0.0;
            for(int i=0;i<attempts;i++) {
                Model model = new ProportionProbabilityForCharacterModel(data.getTrainingDataSteps(), size, data.getDataProcessing(), random);
                total += (new ModelTrainer("Models/TempLoss3.txt")).train(maximumTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, random);
            }
            average = total/(double) attempts;
            String tempString ="Categoric Prob for Character with size of "+size+":\t"+average;
            System.out.println(tempString);
            DataExport.appendToTextFile(tempString, "Models/ParameterTuning.txt");
            System.gc();
        }


    }


}
