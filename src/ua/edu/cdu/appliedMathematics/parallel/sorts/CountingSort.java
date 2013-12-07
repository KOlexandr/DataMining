package ua.edu.cdu.appliedMathematics.parallel.sorts;

import ua.edu.cdu.appliedMathematics.parallel.Utils;

public class CountingSort {
    /**
     * sort array using counting sort algorithm and return new sorted array (not change old array)
     * @param array - original unsorted array
     * @return - new sorted array
     */
    public static int[] sort(final int[] array){
        final int[] sortedArray = new int[array.length];
        System.arraycopy(array, 0, sortedArray, 0, array.length);
        countingSort(sortedArray, Utils.getMax(array));
        return sortedArray;
    }

    /**
     * find max value in array and start countingSort, as result sort array
     * run this method if you do not know max element in array
     * @param array - original unsorted array
     */
    public static void sortArray(final int[] array){
        countingSort(array, Utils.getMax(array));
    }

    /**
     * sort array using counting sort algorithm
     * run this method if you know max element in array
     * @param array - original unsorted array
     * @param k - max value in array
     */
    public static void countingSort(final int[] array, final int k){
        final int[] b = new int[array.length];
        final int[] c = new int[k+1];
        for (int i = 0; i < b.length; i++) {
            c[array[i]]++;
        }
        for (int i = 1; i <= k; i++) {
            c[i] += c[i-1];
        }
        for (int i = array.length-1; i >= 0; i--) {
            b[--c[array[i]]] = array[i];
        }
        System.arraycopy(b, 0, array, 0, array.length);
    }
}
