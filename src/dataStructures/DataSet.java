package dataStructures;

import lossFunctions.Loss;
import lossFunctions.LossSumOfSquares;
import models.Model;

import java.util.List;

public interface DataSet {
    Loss lossTraining = new LossSumOfSquares();

    void DisplayReport(Model model);

    List<DataStep> getTestingDataSteps();

    int getTestingSize();

    int getTrainingSize();

    List<DataStep> getTrainingDataSteps();

    double getReciprocalOfTrainingSize();
}
