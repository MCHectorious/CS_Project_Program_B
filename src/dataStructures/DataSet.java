package dataStructures;

import lossFunctions.Loss;
import lossFunctions.LossSumOfSquares;
import models.Model;

import java.util.List;

public interface DataSet {
    Loss trainingLoss = new LossSumOfSquares();

    void displayReport(Model model);

    List<DataStep> getTestingDataSteps();

    int getTestingDataStepsSize();

    int getTrainingDataStepsSize();

    List<DataStep> getTrainingDataSteps();

    double getReciprocalOfTrainingSize();
}
