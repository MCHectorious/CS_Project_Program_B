package dataSplitting;

import dataStructures.DataStep;
import generalUtilities.Utilities;
import lossFunctions.Loss;
import lossFunctions.LossSumOfSquares;
import matrices.Vector;
import models.Model;
import training.DataProcessing;

import java.util.ArrayList;
import java.util.List;

public class DataSplitter {

    public static DataSplitOperation getBestDataSplit(List<DataStep> steps, Model model, DataProcessing dataPrep) {
		ArrayList<Double> losses = new ArrayList<>();
		
		Loss getLoss = new LossSumOfSquares();
        Vector temporaryOutput = new Vector(DataProcessing.FIXED_DATA_SIZE_FOR_VECTOR);

        for (DataStep step : steps) {
            model.run(step, temporaryOutput);
			double loss = getLoss.measureLoss(temporaryOutput, step.getTargetOutputVector());
			losses.add(loss);
		}


        double upperQuartile = Utilities.getUpperQuartile(losses);
		System.out.println("Upper Quartile: "+upperQuartile);
		
		ArrayList<DataStep> goodLosses = new ArrayList<>();
		ArrayList<DataStep> badLosses = new ArrayList<>();
		
		for(int i=0;i<steps.size();i++) {
			if(losses.get(i)>=upperQuartile) {
				badLosses.add(steps.get(i));
			}else {
				goodLosses.add(steps.get(i));
			}
		}
		
		System.out.println("Good Losses: "+goodLosses.size()+"\t Bad Losses: "+badLosses.size());

		
		ContainPhrase containsPhrase = new ContainPhrase(goodLosses, badLosses, dataPrep);
		WithinCertainInterval withinCertainInterval = new WithinCertainInterval(goodLosses, badLosses);
		return (containsPhrase.getValue()>withinCertainInterval.getValue())? containsPhrase : withinCertainInterval;
		
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
