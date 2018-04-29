package DataStructures;

import LossFunctions.Loss;
import LossFunctions.LossSumOfSquares;
import Models.Model;

import java.util.List;

public interface DataSet {
    Loss trainingLoss = new LossSumOfSquares();// Measures how well the training data and its predictions match

    void displayReport(Model model);//Shows how well the model does on a random piece of testing data

    List<DataStep> getTrainingDataSteps();
    int getTrainingDataStepsSize();
    double getReciprocalOfTrainingSize();//To avoid having to division lots of times which can take some tmie


    List<DataStep> getTestingDataSteps();
    int getTestingDataStepsSize();

}
