package generalUtilities;

import java.util.ArrayList;

class QuickSort {

    private ArrayList<Double> doubles;

    @SuppressWarnings("unchecked")
    ArrayList<Double> sort(ArrayList<Double> values) {
        if (values == null || values.size() == 0) {
            return null;
        }
        try {

            this.doubles = (ArrayList<Double>) values.clone();
        } catch (ClassCastException exception) {
            System.out.println("Error trying to cast the ro an ArrayList<Double>");
            System.out.print(Utilities.arrayToString(values.toArray(new Double[0])));
            System.out.println(exception.getMessage());
            return null;
        }
        sort(0, values.size() - 1);
        return doubles;
    }

    private void sort(int lowerIndex, int upperIndex) {
        int i = lowerIndex, j = upperIndex;
        double pivot = doubles.get(lowerIndex + (upperIndex - lowerIndex) / 2);

        while (i <= j) {

            while (doubles.get(i) < pivot) {
                i++;
            }

            while (doubles.get(j) > pivot) {
                j--;
            }

            if (i <= j) {
                exchangeValue(i, j);
                i++;
                j--;
            }
        }
        if (lowerIndex < j)
            sort(lowerIndex, j);
        if (i < upperIndex)
            sort(i, upperIndex);
    }

    private void exchangeValue(int oldIndex, int newIndex) {
        double temp = doubles.get(oldIndex);
        doubles.set(oldIndex, doubles.get(newIndex));
        doubles.set(newIndex, temp);
    }

}
