package dataStructures;

import java.util.List;

import loss.Loss;
import loss.LossSumOfSquares;
import model.Model;
import nonlinearity.NonLinearity;
import training.DataPreparation;

public interface DataSet {
	public int inputDimension = DataPreparation.FIXED_VECTOR_SIZE;
	public int outputDimension = DataPreparation.FIXED_VECTOR_SIZE;
	public Loss lossTraining = new LossSumOfSquares();
	public Loss lossReporting = new LossSumOfSquares();

	public  void DisplayReport(Model model);
	public  NonLinearity getDataSetNonLinearity();
	public  List<DataStep> getTestingDataSteps();
	public int getTestingSize();
	public int getTrainingSize();
	public List<DataStep> getTrainingDataSteps();
	public double getReciprocalOfTrainingSize();
}
