package dataSplitting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dataStructures.DataStep;
import loss.Loss;
import loss.LossSumOfSquares;
import matrix.Vector;
import model.Model;
import training.DataPreparation;
import util.Util;

public class Splitter {

	public static DataSplitOp getBestDataSplit(List<DataStep> steps, Model model, DataPreparation dataPrep) {
		//Map<DataStep, Double> map = new HashMap<>();
		ArrayList<Double> losses = new ArrayList<>();
		
		Loss getLoss = new LossSumOfSquares();
		Vector tempOutput = new Vector(DataPreparation.FIXED_VECTOR_SIZE);
		for(int i=0;i<steps.size();i++) {
			DataStep step = steps.get(i);
			model.forward(step.getInputVector(), tempOutput);
			double loss = getLoss.measure(tempOutput, step.getTargetOutputVector());
			losses.add(loss);
		}
		
		double upperQuartile = Util.getUpperQuartile(losses);
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
	
	public static ArrayList<DataStep> getStepsInSplit(List<DataStep> list, DataSplitOp split) {
		ArrayList<DataStep> output = new ArrayList<>();
		for(DataStep step: list) {
			if(split.isInSet(step.getInputVector())) {
				output.add(step);
			}
		}
		return output;
	}

	public static ArrayList<DataStep> getStepsNotInSplit(List<DataStep> steps, DataSplitOp split) {
		ArrayList<DataStep> output = new ArrayList<>();
		for(DataStep step: steps) {
			if(!split.isInSet(step.getInputVector())) {
				output.add(step);
			}
		}
		return output;
	}
	
}
