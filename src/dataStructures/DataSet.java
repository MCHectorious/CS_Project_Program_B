package dataStructures;

import lossFunctions.Loss;
import lossFunctions.LossSumOfSquares;
import models.Model;

import java.util.List;

public interface DataSet {
    Loss trainingLoss = new LossSumOfSquares();// Measures how well the training data and its predictions match

    void displayReport(Model model);//Shows how well the model does on a random piece of testing data

    List<DataStep> getTestingDataSteps();

    int getTestingDataStepsSize();

    int getTrainingDataStepsSize();

    List<DataStep> getTrainingDataSteps();

    double getReciprocalOfTrainingSize();//To avoid having to division lots of times which can take some tmie
}
