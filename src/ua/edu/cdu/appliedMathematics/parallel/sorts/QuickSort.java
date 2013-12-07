package ua.edu.cdu.appliedMathematics.parallel.sorts;

public class QuickSort {
    /**
     * Sort array using quick sort
     * @param array - array for sort
     * @return new sorted array
     */
    public static int[] sort(final int[] array){
        if(null == array){
            throw new RuntimeException("Array is null.");
        }
        final int[] sortedArray = new int[array.length];
        System.arraycopy(array, 0, sortedArray, 0, array.length);
        quickSort(sortedArray, 0, sortedArray.length-1);
        return sortedArray;
    }

    /**
     * sort parts of array using hoare algorithm
     * @param array - array for sort
     * @param p - left index
     * @param r - right index
     */
    private static int hoarPartition(final int[] array, final int p, final int r) {
        int x = array[p];
        int i = p - 1;
        int j = r + 1;
        while(true){
            do {
                j--;
            } while(array[j] <= x);
            do {
                i++;
            } while(array[i] >= x);
            if(i < j){
                int tmp = array[i];
                array[i] = array[j];
                array[j] = tmp;
            } else {
                return j;
            }
        }
    }

    /**
     * sort parts of array
     * @param array - array for sort
     * @param p - left index
     * @param r - right index
     */
    private static int partition(final int[] array, final int p, final int r) {
        int x = array[r];
        int i = p - 1;
        for (int j = p; j < r; j++) {
            if(array[j] <= x){
                int tmp = array[++i];
                array[i] = array[j];
                array[j] = tmp;
            }
        }
        int tmp = array[++i];
        array[i] = array[r];
        array[r] = tmp;
        return i;
    }

    /**
     * Method sorts array of int
     * @param array - array for sort
     * @param p - start index
     * @param r - end index
     */
    private static void quickSort(final int[] array, final int p, final int r) {
        if (p < r) {
            final int q = partition(array, p, r);
            quickSort(array, p, q - 1);
            quickSort(array, q + 1, r);
        }
    }
}