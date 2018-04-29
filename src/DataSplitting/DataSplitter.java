package DataSplitting;

import DataStructures.DataStep;
import GeneralUtilities.Utilities;
import LossFunctions.Loss;
import LossFunctions.LossSumOfSquares;
import Matrices.Vector;
import Models.Model;
import Training.DataProcessing;

import java.util.ArrayList;
import java.util.List;

public class DataSplitter {

    public static DataSplitOperation getBestDataSplit(List<DataStep> dataSteps, Model model, DataProcessing dataProcessing) {
		ArrayList<Double> losses = new ArrayList<>();
		
		Loss getLoss = new LossSumOfSquares();
        Vector temporaryOutput = new Vector(DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR);//Used to avoid creating a new object each loop

        for (DataStep step : dataSteps) {//Gets the loss for the model on each data step in the list of data steps
            model.run(step, temporaryOutput);
			double loss = getLoss.measureLoss(temporaryOutput, step.getTargetOutputVector());
			losses.add(loss);
		}

        double upperQuartile = Utilities.getUpperQuartile(losses);// 1/4 of the data steps have worse losses than this
		System.out.println("Upper Quartile: "+upperQuartile);
		
		ArrayList<DataStep> goodLosses = new ArrayList<>();
		ArrayList<DataStep> badLosses = new ArrayList<>();
		
		for(int i=0;i<dataSteps.size();i++) {
			if(losses.get(i)>=upperQuartile) {
				badLosses.add(dataSteps.get(i));//Higher losses are worse
			}else {
				goodLosses.add(dataSteps.get(i));
			}
		}
		
		System.out.println("Good Losses: "+goodLosses.size()+"\t Bad Losses: "+badLosses.size());

		ContainPhrase containsPhrase = new ContainPhrase(goodLosses, badLosses, dataProcessing);
		WithinCertainInterval withinCertainInterval = new WithinCertainInterval(goodLosses, badLosses);
		return (containsPhrase.getValue()>withinCertainInterval.getValue())? containsPhrase : withinCertainInterval;//Returns the data split operation which most accurately splits the data
	}

	public static ArrayList<DataStep> getStepsInSplit(List<DataStep> steps, DataSplitOperation split) {
		ArrayList<DataStep> stepsNotInSplit = new ArrayList<>();

		for(DataStep step: steps) {
			if (split.isInSet(step)) {
				stepsNotInSplit.add(step);
			}
		}

		return stepsNotInSplit;
	}

    public static ArrayList<DataStep> getStepsNotInSplit(List<DataStep> steps, DataSplitOperation split) {
		ArrayList<DataStep> stepsNotInSplit = new ArrayList<>();

		for(DataStep step: steps) {
            if (!split.isInSet(step)) {
				stepsNotInSplit.add(step);
			}
		}

		return stepsNotInSplit;
	}
	
}
