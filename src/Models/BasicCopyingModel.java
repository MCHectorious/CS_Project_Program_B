package Models;

import DataStructures.DataStep;
import Matrices.Vector;

public class BasicCopyingModel implements Model {

    @Override
    public void run(DataStep input, Vector output) {
        for (int i = input.getInputVector().getSize() - 1; i >= 0; i--) {
            output.set(i, input.getInputVector().get(i));
        }

    }

    @Override
    public void runAndDecideImprovements(DataStep input, Vector output, Vector targetOutput) {
        run(input, output);//Improvements will not be made

    }

    @Override
    public void updateModelParameters(double momentum, double beta1, double beta2, double alpha, double OneMinusBeta1,
                                      double OneMinusBeta2) {
        //Parameters don't exist, so they want be changed
    }

    @Override
    public void resetState() {

    }

    @Override
    public String provideDescription() {
        return "Copy Model";
    }

}
