package FindingBestModel.BestModel.ProportionProbabilityForCharacterSubModel;

import java.util.concurrent.ThreadLocalRandom;

public class ProportionalProbability {

    private double[] valuesForProbabilities;//the empirical probabilities of the outputs
    private char[] outputs;//the possible characters

    ProportionalProbability(double[] probabilities, char[] outputs) {

        valuesForProbabilities = new double[probabilities.length];
        double runningTotal = 0;
        for (int i = 0; i < probabilities.length; i++) {
            runningTotal += probabilities[i];
            valuesForProbabilities[i] = runningTotal;
        }
        this.outputs = outputs;

    }

    char execute(ThreadLocalRandom random) {
        double value = random.nextDouble();
        for (int i = 0; i < valuesForProbabilities.length; i++) {
            if (value < valuesForProbabilities[i]) {
                return outputs[i];
            }
        }
        return ' ';//defaults to a space


    }


}
