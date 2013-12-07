package ua.edu.cdu.appliedMathematics.parallel.oddEvenSort;

public class OddEvenSort {

    /**
     * Sort array using odd-even sort
     * @param array - array for sort
     */
    public static void sort(final int[] array){
        if(null == array){
            throw new RuntimeException("Array is null.");
        }
        final int length = array.length;
        for (int i = 0; i < length; i++) {
            if (i % 2 == 1) {
                for (int j = 0; j < length/2 - 1; j++){
                    compareExchange(array, 2 * j + 1, 2 * j + 2);
                }
                if (length % 2 == 1){
                    compareExchange(array, length - 2, length - 1);
                }
            } else {
                for (int j = 0; j < length/2; j++){
                    compareExchange(array, 2*j, 2*j+1);
                }
            }
        }
    }

    /**
     * verify if item with index i larger then item with index j, ant if its true swam them
     * @param array - array with items
     * @param i - index of first item
     * @param j - index of second item
     */
    private static void compareExchange(final int[] array, final int i, final int j){
        if(array[i] > array[j]){
            int tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
    }
}
