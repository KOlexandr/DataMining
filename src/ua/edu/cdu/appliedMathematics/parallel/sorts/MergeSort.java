package ua.edu.cdu.appliedMathematics.parallel.sorts;

public class MergeSort {

    /**
     * Sort array using merge sort
     * @param array - array for sort
     * @return new sorted array
     */
    public static int[] sort(final int[] array){
        if(null == array){
            throw new RuntimeException("Array is null.");
        }
        final int[] sortedArray = new int[array.length];
        System.arraycopy(array, 0, sortedArray, 0, array.length);
        sortMerge(sortedArray, 0, sortedArray.length);
        return sortedArray;
    }

    /**
     * Sort array using merge sort
     * @param array - array for sort
     */
    public static void sortArray(final int[] array){
        if(null == array){
            throw new RuntimeException("Array is null.");
        }
        sortMerge(array, 0, array.length);
    }

    /**
     * merge parts of array
     * @param array - array for sort
     * @param p - left index
     * @param q = (p + r)/2
     * @param r - right index
     */
    private static void merge(final int[] array, final int p, final int q, final int r) {
        final int lengthLeft = q - p;
        final int lengthRight = r - q;
        final int[] left = new int[lengthLeft+1];
        final int[] right = new int[lengthRight+1];

        System.arraycopy(array, p, left, 0, lengthLeft);
        System.arraycopy(array, q, right, 0, lengthRight);
        left[lengthLeft] = Integer.MAX_VALUE;
        right[lengthRight] = Integer.MAX_VALUE;
        int i = 0;
        int j = 0;
        for (int k = p; k < r; k++){
            if (left[i] <= right[j]){
                array[k] = left[i++];
            } else {
                array[k] = right[j++];
            }
        }
    }

    /**
     * Method sorts array of int
     * @param array - array for sort
     * @param p - start index
     * @param r - end index
     */
    private static void sortMerge(final int[] array, final int p, final int r) {
        if (p < r) {
            int q = (p + r) / 2;
            //run sortMerge in his thread
            sortMerge(array, p, q);
            //synchronize
            //run sortMerge in his thread
            sortMerge(array, q+1, r);
            //synchronize
            merge(array, p, q, r);
        }
    }
}
