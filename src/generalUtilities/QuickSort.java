package generalUtilities;

import java.util.ArrayList;

class QuickSort {

    private ArrayList<Double> doubles;//To values being sorted

    @SuppressWarnings("unchecked")//This is ignored as I have handed it but the IDe sutill gives an incorrect warning
    ArrayList<Double> sort(ArrayList<Double> values) {
        if (values == null || values.size() == 0) {//checks that there are actually values to sort
            return null;
        }
        try {

            this.doubles = (ArrayList<Double>) values.clone();
        } catch (ClassCastException exception) {
            System.out.println("Error trying to cast to an ArrayList<Double>");
            System.out.print(Utilities.arrayToString(values.toArray(new Double[0])));//In order to determine what may have caused the error
            System.out.println(exception.getMessage());//To help find the cause of the error
            return null;//stops trying to sort because it may lead to further errors
        }
        sort(0, values.size() - 1);
        return doubles;
    }

    private void sort(int lowerIndex, int upperIndex) {
        int i = lowerIndex, j = upperIndex;
        double pivot = doubles.get(lowerIndex + (upperIndex - lowerIndex) / 2);//Value used to check whether vaues should be in the lower or upper half of the array

        while (i <= j) {

            while (doubles.get(i) < pivot) {
                i++;
            }

            while (doubles.get(j) > pivot) {
                j--;
            }

            if (i <= j) {
                exchangeValue(i, j);//If the values are in the wrong order, swap them
                i++;
                j--;
            }
        }
        if (lowerIndex < j)
            sort(lowerIndex, j);//Recursively calls itself to sort the lower subset
        if (i < upperIndex)
            sort(i, upperIndex);//Recursively calls itself to sort the upper subset
    }

    private void exchangeValue(int oldIndex, int newIndex) {
        double temp = doubles.get(oldIndex);
        doubles.set(oldIndex, doubles.get(newIndex));
        doubles.set(newIndex, temp);
    }

}
