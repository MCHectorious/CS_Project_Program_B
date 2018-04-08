package models;

import dataStructures.DataStep;
import matrices.Vector;

public class BasicCopyingModel implements Model {

    @Override
    public void run(DataStep input, Vector output) {
        for (int i = input.getInputVector().getSize() - 1; i >= 0; i--) {
            output.set(i, input.getInputVector().get(i));
        }

    }

    @Override
    public void runAndDecideImprovements(DataStep input, Vector output, Vector targetOutput) {
        run(input, output);

    }

    @Override
    public void updateModelParameters(double momentum, double beta1, double beta2, double alpha, double OneMinusBeta1,
                                      double OneMinusBeta2) {

    }

    @Override
    public void resetState() {

    }

    @Override
    public String provideDescription() {
        return "Copy Model";
    }

}
