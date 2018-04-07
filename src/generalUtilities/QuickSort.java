package generalUtilities;

import java.util.ArrayList;

class QuickSort {

    private ArrayList<Double> numbers;

    @SuppressWarnings("unchecked")
    ArrayList<Double> sort(ArrayList<Double> values) {
        if (values == null || values.size() == 0) {
            return null;
        }
        try {

            this.numbers = (ArrayList<Double>) values.clone();
        } catch (ClassCastException exception) {
            System.out.println("Error trying to cast the ro an ArrayList<Double>");
            System.out.print(Utilities.arrayToString(values.toArray(new Double[0])));
            System.out.println(exception.getMessage());
            return null;
        }
        quickSort(0, values.size() - 1);
        return numbers;
    }

    private void quickSort(int low, int high) {
        int i = low, j = high;
        double pivot = numbers.get(low + (high - low) / 2);

        while (i <= j) {

            while (numbers.get(i) < pivot) {
                i++;
            }

            while (numbers.get(j) > pivot) {
                j--;
            }

            if (i <= j) {
                exchange(i, j);
                i++;
                j--;
            }
        }
        if (low < j)
            quickSort(low, j);
        if (i < high)
            quickSort(i, high);
    }

    private void exchange(int i, int j) {
        double temp = numbers.get(i);
        numbers.set(i, numbers.get(j));
        numbers.set(j, temp);
    }

}
