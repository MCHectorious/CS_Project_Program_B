package findingBestModel;

import dataStructures.FlashcardDataSet;
import fileManipulation.DataExport;
import generalUtilities.CustomRandom;
import generalUtilities.Utilities;
import models.BasicCopyingModel;
import models.Model;
import models.NeuralNetworkModel;
import training.DataProcessing;
import training.ModelTrainer;

public class TempTesting8 {

    public static void main(String[] args){

        //System.out.println("Test");

        //DataExport.overwriteTextFile(1.01, "Models/TempModel1.txt");
        //System.out.println(DataImport.getDoubleFromFile("Models/TempModel1.txt"));

        CustomRandom random = new CustomRandom();

        FlashcardDataSet data = new FlashcardDataSet("DataSets/TranslatedFlashcards.txt", random);

        String savePath = "Models/TempModel8.txt";

        double total,average;
        int attempts = 3;

        int maximumTrainingEpochs = 100000000;
        int displayReportPeriod = 2000000000;
        int showEpochPeriod = 1000;
        int checkMinimumPeriod = 50;

        int [] layerTypeSet = {0,1};
        int [] hiddenDimensionSet = {1,2,3,5,10,20,50,75};
        int maxArraySize = 5;

        String tempString;

        for(int j=3;j<=maxArraySize;j++) {
            int[][] layerTypes = new int[(int) Math.pow(layerTypeSet.length, j)][j];
            for(int i=0;i<=Math.pow(layerTypeSet.length, j)-1;i++) {
                for(int k=0;k<j;k++) {
                    layerTypes[i][k] =  layerTypeSet[ (int) Math.floor((i)/(int) Math.floor(Math.pow(layerTypeSet.length, k)) ) % layerTypeSet.length ];
                }
            }
            int[][] hiddenDims = new int[(int) Math.pow(hiddenDimensionSet.length, j-1)][j-1];
            for(int i=0;i<=Math.pow(hiddenDimensionSet.length, j-1)-1;i++) {
                for(int k=0;k<j-1;k++) {
                    hiddenDims[i][k] =  hiddenDimensionSet[ (int) Math.floor((i)/(int) Math.floor(Math.pow(hiddenDimensionSet.length, k)) ) % hiddenDimensionSet.length ];
                }
            }
            for(int a=0;a<layerTypes.length;a++) {
                if(layerTypes[a][layerTypes[a].length-1]==1) {
                    continue;
                }
                for(int b=0;b<hiddenDims.length;b++) {
                    total = 0.0;

                    //.out.println(Utilities.arrayToString(hiddenDims[b]));
                    for(int i=0;i<attempts;i++) {
                        Model model = new NeuralNetworkModel(layerTypes[a],DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR,hiddenDims[b],DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR,random);
                        total += (new ModelTrainer("Models/TempLoss8.txt")).train(maximumTrainingEpochs, model, data, displayReportPeriod, showEpochPeriod, checkMinimumPeriod, savePath, random);
                    }
                    average = total/(double) attempts;
                    tempString = "Neural Network with layers "+Utilities.arrayToString(layerTypes[a])+"and hidden dimensions "+Utilities.arrayToString(hiddenDims[b])+"  Model:\t"+average;
                    System.out.println(tempString);
                    DataExport.appendToTextFile(tempString, "Models/ParameterTuning.txt");
                    System.gc();
                }
            }
        }


    }


}
